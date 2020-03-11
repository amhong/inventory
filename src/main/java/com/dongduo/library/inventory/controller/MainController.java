package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.entity.BookStore;
import com.dongduo.library.inventory.entity.Place;
import com.dongduo.library.inventory.entity.Shelf;
import com.dongduo.library.inventory.repository.BookRepository;
import com.dongduo.library.inventory.repository.PlaceRepository;
import com.dongduo.library.inventory.repository.ShelfRepository;
import com.dongduo.library.inventory.service.IRPanUHF;
import com.dongduo.library.inventory.util.EpcCode;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@FXMLController
public class MainController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private ChoiceBox<Place> place;

    @FXML
    private ChoiceBox<Shelf> shelf;

    @FXML
    private Button start;

    @FXML
    private Button cancel;

    @FXML
    private TableView<BookVo> tableView;

    @FXML
    private TableColumn<BookVo, String> bookname;

    @FXML
    private TableColumn<BookVo, String> banid;

    @FXML
    private TableColumn<BookVo, String> isbn;

    @FXML
    private TableColumn<BookVo, String> author;

    @FXML
    private TableColumn<BookVo, String> status;

    @FXML
    private ProgressBar progressBar;

    private IRPanUHF rPanUHF;

    private BookRepository bookRepository;

    private PlaceRepository placeRepository;

    private ShelfRepository shelfRepository;

    private ObservableList<BookVo> bookVos = FXCollections.observableArrayList();

    @Autowired
    public MainController(IRPanUHF rPanUHF, BookRepository bookRepository, PlaceRepository placeRepository,
                          ShelfRepository shelfRepository) {
        this.rPanUHF = rPanUHF;
        this.bookRepository = bookRepository;
        this.placeRepository = placeRepository;
        this.shelfRepository = shelfRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPlace();
        initShelf();
        start.setDisable(true);
        start.setOnMouseClicked(this::handleStart);
        initTableView();
    }

    /**
     * 初始化书库下拉菜单
     */
    private void initPlace() {
        place.converterProperty().set(new StringConverter<Place>() {
            @Override
            public String toString(Place object) {
                return object.getGcdName();
            }

            @Override
            public Place fromString(String string) {
                return null;
            }
        });
        place.getItems().add(new Place(0L, "----请选择----", null));
        List<Place> placeEntityList =  placeRepository.findAll();
        if (!CollectionUtils.isEmpty(placeEntityList)) {
            place.getItems().addAll(placeEntityList);
        }
        place.getSelectionModel().selectFirst();
        place.getSelectionModel().selectedItemProperty().addListener(this::handlePlaceChanged);
    }

    /**
     * 初始化书架下拉菜单
     */
    private void initShelf() {
        shelf.setDisable(true);
        shelf.converterProperty().set(new StringConverter<Shelf>() {
            @Override
            public String toString(Shelf object) {
                return object.getName();
            }

            @Override
            public Shelf fromString(String string) {
                return null;
            }
        });
        shelf.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> start.setDisable(newValue.getId() == null));
    }

    /**
     * 初始化列表
     */
    private void initTableView() {
        bookname.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        banid.setCellValueFactory(new PropertyValueFactory<>("banid"));
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(bookVos);
    }

    /**
     * 选择书库的处理方法
     */
    public void handlePlaceChanged(ObservableValue observable, Place oldValue, Place newValue) {
        long selectedId = newValue.getId();
        if (oldValue.getId() != selectedId) {
            if (selectedId == 0L) {
                shelf.getSelectionModel().selectFirst();
                shelf.setDisable(true);
            } else {
                List<Shelf> shelfList = shelfRepository.findByPlaceId(String.valueOf(selectedId));
                if (!CollectionUtils.isEmpty(shelfList)) {
                    shelf.getItems().setAll(shelfList);
                    shelf.setDisable(false);
                }
                shelf.getItems().add(0, new Shelf(null, null, "----请选择----"));
                shelf.getSelectionModel().selectFirst();
            }
        }
    }

    private void handleStart(MouseEvent event) {
        bookVos.clear();
        cancel.setVisible(true);
        start.setVisible(false);
        place.setDisable(true);
        shelf.setDisable(true);
        progressBar.setVisible(true);

        if (rPanUHF.connect()) {
            Task<Void> task = new ReallyRunTask();
            progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        } else {
            // TODO 弹出提示框
        }
    }

    private class ReallyRunTask extends Task<Void> {
        @Override
        protected Void call() {
            updateProgress(0.03D, 1D);
            Set<EpcCode> epcStrs = rPanUHF.getRecordEpc();
            rPanUHF.disconnect();
            updateProgress(0.07D, 1D);
            if (!CollectionUtils.isEmpty(epcStrs)) {
                Map<String, EpcCode> epcMap = epcStrs.stream().collect(Collectors.toMap(EpcCode::getItemId, e -> e));
                Specification<BookStore> criteria = (root, query, cb) -> cb.equal(root.get("shelfId"), shelf.getValue().getId());
                Pageable pageable = PageRequest.of(0, 2, Sort.by("bookname"));
                Page<BookStore> page;
                double totalSize = epcStrs.size();
                do {
                    page = bookRepository.findAll(criteria, pageable);
                    page.get().map(bs -> new BookVo(bs, obtainStatus(epcMap, bs)))
                            .forEach(bv -> {
                                bookVos.add(bv);
                                updateProgress(bookVos.size() / totalSize * 0.9D + 0.07D, 1D);
                            });
                    pageable = pageable.next();
                } while (page.hasNext());

                if (!epcMap.isEmpty()) { // 处理不应放置在该架上的图书
                    epcMap.forEach((banId, bv) -> {
                        BookStore bookStore = bookRepository.findByBanId(banId);
                        if (bookStore != null) {
                            bookVos.add(new BookVo(bookStore, BookVo.Status.架位错误));
                            updateProgress(bookVos.size() / totalSize * 0.9D + 0.07D, 1D);
                        }
                    });
                }

                bookVos.sorted();
            } else {
                // TODO 弹出提示框，从手持盘点仪读取到的数据为空
            }

            return null;
        }

        @Override
        protected void succeeded() {
            updateProgress(1D, 1D);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            renew();
        }

        @Override
        protected void failed() {
            renew();
        }

        @Override
        protected void cancelled() {
            renew();
        }
    }

    private BookVo.Status obtainStatus(Map<String, EpcCode> epcMap, BookStore bs) {
        if (epcMap.remove(bs.getBanId()) != null) { // 移除成功表示在架
            return bs.getLeftCount() != 0 ? BookVo.Status.正常在架 : BookVo.Status.异常在架;
        } else { // 移除失败表示不在架
            return bs.getLeftCount() != 0 ? BookVo.Status.图书缺失 : BookVo.Status.正常借出;
        }
    }

    private void renew() {
        progressBar.setVisible(false);
        shelf.setDisable(false);
        place.setDisable(false);
        start.setVisible(true);
        cancel.setVisible(false);
    }
}
