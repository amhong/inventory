package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.repository.BookRepository;
import com.dongduo.library.inventory.service.InventoryThread;
import com.dongduo.library.inventory.service.RPanUHF;
import com.dongduo.library.inventory.util.EpcCode;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

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
    private RPanUHF rPanUHF;

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
        progressBar.setProgress(0.05D);

        if (rPanUHF.connect()) {
            Set<EpcCode> epcStrs = rPanUHF.getRecordEpc();
            rPanUHF.disconnect();
            if (!CollectionUtils.isEmpty(epcStrs)) {
                InventoryThread inventoryThread = new InventoryThread(epcStrs, bookRepository, bookVos);
                inventoryThread.start();

                int totalSize = epcStrs.size();
                while (inventoryThread.isAlive()) {
                    progressBar.setProgress(bookVos.size() / totalSize * 0.9);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                progressBar.setProgress(1);
            } else {
                // 弹出提示框，从手持盘点仪读取到的数据为空
            }
        } else {
            // 弹出提示框
        }

        progressBar.setVisible(false);
        progressBar.setProgress(0.0D);
        place.setDisable(false);
        start.setVisible(true);
        cancel.setVisible(false);
    }
}
