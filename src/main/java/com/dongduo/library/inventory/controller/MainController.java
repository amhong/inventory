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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ChoiceBox<Place> inventory_place;

    @FXML
    private ChoiceBox<Shelf> inventory_shelf;

    @FXML
    private Button inventory_start;

    @FXML
    private TableView<BookVo> inventory_tableView;

    @FXML
    private TableColumn<BookVo, String> inventory_tableView_bookname;

    @FXML
    private TableColumn<BookVo, String> inventory_tableView_banid;

    @FXML
    private TableColumn<BookVo, String> inventory_tableView_isbn;

    @FXML
    private TableColumn<BookVo, String> inventory_tableView_author;

    @FXML
    private TableColumn<BookVo, BookVo.Status> inventory_tableView_status;

    @FXML
    private ProgressBar inventory_progressBar;


    @FXML
    private Button putaway_read;

    @FXML
    private Button putaway_start;

    @FXML
    private ChoiceBox<Place> putaway_place;

    @FXML
    private ChoiceBox<Shelf> putaway_shelf;

    @FXML
    private TableView<BookVo> putaway_tableView;

    @FXML
    private TableColumn<BookVo, Boolean> putaway_tableView_select;

    @FXML
    private TableColumn<BookVo, String> putaway_tableView_bookname;

    @FXML
    private TableColumn<BookVo, String> putaway_tableView_banid;

    @FXML
    private TableColumn<BookVo, String> putaway_tableView_isbn;

    @FXML
    private TableColumn<BookVo, String> putaway_tableView_author;

    @FXML
    private TableColumn<BookVo, BookVo.Status> putaway_tableView_status;

    @FXML
    private ProgressBar putaway_progressBar;

    private IRPanUHF rPanUHF;

    private BookRepository bookRepository;

    private PlaceRepository placeRepository;

    private ShelfRepository shelfRepository;

    @Value("${inventory.page.size}")
    private int pageSize;

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
        initPlaceAndShelf(inventory_place, inventory_shelf);
        initPlaceAndShelf(putaway_place, putaway_shelf);
        inventory_start.setOnMouseClicked(this::handleInventoryStart);
        putaway_read.setOnMouseClicked(this::handlePutawayRead);
        putaway_start.setOnMouseClicked(this::handlePutawayStart);
        initInventoryTableView();
        initPutawayTableView();
    }

    /**
     * 初始化书库、书架下拉菜单
     */
    private void initPlaceAndShelf(ChoiceBox<Place> place, ChoiceBox<Shelf> shelf) {
        // 初始化书库下拉菜单
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
        place.getSelectionModel().selectedItemProperty().addListener(new PlaceChangeListener(shelf, shelfRepository));

        // 初始化书架下拉菜单
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
                .addListener((observable, oldValue, newValue) -> inventory_start.setDisable(newValue.getId() == null));
    }

    /**
     * 选择书库的监听器
     */
    public static class PlaceChangeListener implements ChangeListener<Place> {
        private final ChoiceBox<Shelf> shelf;
        private final ShelfRepository shelfRepository;

        public PlaceChangeListener(ChoiceBox<Shelf> shelf, ShelfRepository shelfRepository) {
            this.shelf = shelf;
            this.shelfRepository = shelfRepository;
        }

        public void changed(ObservableValue<? extends Place> observable, Place oldValue, Place newValue) {
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
    }

    /**
     * 初始化图书盘点列表
     */
    private void initInventoryTableView() {
        inventory_tableView_bookname.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        inventory_tableView_banid.setCellValueFactory(new PropertyValueFactory<>("banid"));
        inventory_tableView_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        inventory_tableView_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        inventory_tableView_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_tableView_status.setCellFactory(new Callback<TableColumn<BookVo, BookVo.Status>, TableCell<BookVo, BookVo.Status>>() {
            public TableCell<BookVo, BookVo.Status> call(TableColumn param) {
                return new TableCell<BookVo, BookVo.Status>() {
                    @Override
                    protected void updateItem(BookVo.Status item, boolean empty) {
                        if (!empty) {
                            switch (item) {
                                case 图书缺失:
                                    this.setTextFill(Color.RED);
                                    break;
                                case 异常在架:
                                    this.setTextFill(Color.ORANGE);
                                    break;
                                case 架位错误:
                                    this.setTextFill(Color.YELLOW);
                                    break;
                                case 正常借出:
                                    this.setTextFill(Color.DEEPSKYBLUE);
                                    break;
                                case 正常在架:
                                    this.setTextFill(Color.MEDIUMSEAGREEN);
                                    break;
                            }
                            this.setAlignment(Pos.CENTER);
                            this.setText(item.name());
                        }
                    }
                };
            }
        });
    }

    /**
     * 初始化图书上架列表
     */
    private void initPutawayTableView() {
        putaway_tableView.setEditable(true);
        putaway_tableView_select.setCellFactory(CheckBoxTableCell.forTableColumn(putaway_tableView_select));
        putaway_tableView_select.setCellValueFactory(new PropertyValueFactory<>("selected"));
        putaway_tableView_bookname.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        putaway_tableView_banid.setCellValueFactory(new PropertyValueFactory<>("banid"));
        putaway_tableView_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        putaway_tableView_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        putaway_tableView_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        putaway_tableView_status.setCellFactory(new Callback<TableColumn<BookVo, BookVo.Status>, TableCell<BookVo, BookVo.Status>>() {
            public TableCell<BookVo, BookVo.Status> call(TableColumn param) {
                return new TableCell<BookVo, BookVo.Status>() {
                    @Override
                    protected void updateItem(BookVo.Status item, boolean empty) {
                        if (!empty) {
                            switch (item) {
                                case 图书缺失:
                                    this.setTextFill(Color.RED);
                                    break;
                                case 异常在架:
                                    this.setTextFill(Color.ORANGE);
                                    break;
                                case 架位错误:
                                    this.setTextFill(Color.YELLOW);
                                    break;
                                case 正常借出:
                                    this.setTextFill(Color.DEEPSKYBLUE);
                                    break;
                                case 正常在架:
                                    this.setTextFill(Color.MEDIUMSEAGREEN);
                                    break;
                            }
                            this.setAlignment(Pos.CENTER);
                            this.setText(item.name());
                        }
                    }
                };
            }
        });
    }

    private void handleInventoryStart(MouseEvent event) {
        inventory_tableView.getItems().clear();
        inventory_start.setDisable(true);
        inventory_place.setDisable(true);
        inventory_shelf.setDisable(true);
        inventory_progressBar.setVisible(true);

        if (rPanUHF.connect()) {
            Task<Void> task = new InventoryRunTask();
            inventory_progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("手持盘点仪连接失败！");
            alert.setContentText("请检查手持盘点仪是否已正确连接。");
            alert.setOnCloseRequest(e -> renewInventory());
            alert.showAndWait();
        }
    }

    private class InventoryRunTask extends Task<Void> {
        @Override
        protected Void call() {
            updateProgress(0.03D, 1D);
            Set<EpcCode> epcStrs = rPanUHF.getRecordEpc();
            rPanUHF.disconnect();
            updateProgress(0.07D, 1D);
            if (!CollectionUtils.isEmpty(epcStrs)) {
                ObservableList<BookVo> bookVos = inventory_tableView.getItems();
                Map<String, EpcCode> epcMap = epcStrs.stream().collect(Collectors.toMap(EpcCode::getItemId, e -> e));
                Specification<BookStore> criteria = (root, query, cb) -> cb.equal(root.get("shelfId"), inventory_shelf.getValue().getId());
                Pageable pageable = PageRequest.of(0, pageSize, Sort.by("bookname"));
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
                // TODO 实现方式需要优化
                this.failed();
            }

            return null;
        }

        @Override
        protected void succeeded() {
            updateProgress(1D, 1D);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            renewInventory();
        }

        @Override
        protected void failed() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("没有从手持盘点仪读取到数据！");
            alert.setContentText("请确认是否已完成书架扫描。");
            alert.setOnCloseRequest(e -> renewInventory());
            alert.showAndWait();
            renewInventory();
        }

        @Override
        protected void cancelled() {
            renewInventory();
        }

        private BookVo.Status obtainStatus(Map<String, EpcCode> epcMap, BookStore bs) {
            if (epcMap.remove(bs.getBanId()) != null) { // 移除成功表示在架
                return bs.getLeftCount() != 0 ? BookVo.Status.正常在架 : BookVo.Status.异常在架;
            } else { // 移除失败表示不在架
                return bs.getLeftCount() != 0 ? BookVo.Status.图书缺失 : BookVo.Status.正常借出;
            }
        }
    }

    private void renewInventory() {
        inventory_progressBar.setVisible(false);
        inventory_shelf.setDisable(false);
        inventory_place.setDisable(false);
        inventory_start.setDisable(false);
    }

    private void handlePutawayRead(MouseEvent event) {
        putaway_tableView.getItems().clear();
        putaway_progressBar.setVisible(true);

        if (rPanUHF.connect()) {
            Task<Void> task = new PutawayReadRunTask();
            putaway_progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("手持盘点仪连接失败！");
            alert.setContentText("请检查手持盘点仪是否已正确连接。");
            alert.setOnCloseRequest(e -> renewInventory());
            alert.showAndWait();
        }
    }

    private void handlePutawayStart(MouseEvent event) {
        ObservableList<BookVo> bookVos = putaway_tableView.getItems();
        bookVos.stream().forEach(System.out::println);
    }

    private class PutawayReadRunTask extends Task<Void> {
        @Override
        protected Void call() {
            updateProgress(0.03D, 1D);
            Set<EpcCode> epcStrs = rPanUHF.getRecordEpc();
            rPanUHF.disconnect();
            updateProgress(0.07D, 1D);
            if (!CollectionUtils.isEmpty(epcStrs)) {
                ObservableList<BookVo> bookVos = putaway_tableView.getItems();
                epcStrs.stream().forEach(epcCode -> {
                    BookStore bookStore = bookRepository.findByBanId(epcCode.getItemId());
                    if (bookStore != null) {
                        bookVos.add(new BookVo(bookStore, BookVo.Status.正常在架));
                    } else {
                        bookVos.add(new BookVo(epcCode.getItemId(), BookVo.Status.图书缺失));
                    }
                    updateProgress(bookVos.size() / epcStrs.size() * 0.9D + 0.07D, 1D);
                });
            } else {
                // TODO 实现方式需要优化
                this.failed();
            }

            return null;
        }

        @Override
        protected void succeeded() {
            updateProgress(1D, 1D);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            renewInventory();
        }

        @Override
        protected void failed() {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("没有从手持盘点仪读取到数据！");
            alert.setContentText("请确认是否已完成书架扫描。");
            alert.setOnCloseRequest(e -> renewInventory());
            alert.showAndWait();
            renewInventory();
        }

        @Override
        protected void cancelled() {
            renewInventory();
        }

        private BookVo.Status obtainStatus(Map<String, EpcCode> epcMap, BookStore bs) {
            if (epcMap.remove(bs.getBanId()) != null) { // 移除成功表示在架
                return bs.getLeftCount() != 0 ? BookVo.Status.正常在架 : BookVo.Status.异常在架;
            } else { // 移除失败表示不在架
                return bs.getLeftCount() != 0 ? BookVo.Status.图书缺失 : BookVo.Status.正常借出;
            }
        }
    }
}
