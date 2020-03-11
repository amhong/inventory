package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.entity.BookStore;
import com.dongduo.library.inventory.repository.BookRepository;
import com.dongduo.library.inventory.service.IRPanUHF;
import com.dongduo.library.inventory.util.EpcCode;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FXMLController
public class MainController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private ChoiceBox place;

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

    @Autowired
    private IRPanUHF rPanUHF;

    @Autowired
    private BookRepository bookRepository;

    private ObservableList<BookVo> bookVos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start.setOnMouseClicked(this::handleStart);
        bookname.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        banid.setCellValueFactory(new PropertyValueFactory<>("banid"));
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(bookVos);
    }

    private void handleStart(MouseEvent event) {
        bookVos.clear();
        cancel.setVisible(true);
        start.setVisible(false);
        place.setDisable(true);
        progressBar.setVisible(true);

        if (rPanUHF.connect()) {
            Task<Void> task = new SimulatedTask();
            progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        } else {
            // 弹出提示框
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
                Specification<BookStore> criteria = (root, query, cb) -> {
                    // TODO
                    return null;
                };
                Pageable pageable = PageRequest.of(1, 1000, Sort.by("bookname"));
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
                } while (page != null && page.hasNext());

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
                // 弹出提示框，从手持盘点仪读取到的数据为空
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
    };

    private class SimulatedTask extends Task<Void> {
        @Override
        protected Void call() {
            updateProgress(0.03D, 1D);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            updateProgress(0.07D, 1D);
            Stream.iterate(1, n -> n+1)
                    .limit(100000)
                    .forEach(n -> {
                        bookVos.add(new BookVo("黑客与画家", n.toString(), String.valueOf(bookVos.size() / 100000D), "[美]Paul Graham著 阮一峰译", BookVo.Status.正常在架));
                        updateProgress(bookVos.size() / 100000D * 0.9D + 0.07D, 1D);
                    });
            //bookVos.sorted();
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
    };

    private BookVo.Status obtainStatus(Map<String, EpcCode> epcMap, BookStore bs) {
        if (epcMap.remove(bs.getBanId()) != null) { // 移除成功表示从在架
            return bs.getLeftCount() != 0 ? BookVo.Status.正常在架 : BookVo.Status.图书缺失;
        } else { // 移除失败表示不在架
            return bs.getLeftCount() != 0 ? BookVo.Status.异常在架 : BookVo.Status.正常借出;
        }
    }

    private void renew() {
        progressBar.setVisible(false);
        place.setDisable(false);
        start.setVisible(true);
        cancel.setVisible(false);
    }
}
