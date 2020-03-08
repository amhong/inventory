package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.entity.BookStore;
import com.dongduo.library.inventory.service.MainFrm;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MainController implements Initializable {
    @FXML
    private ChoiceBox place;

    @FXML
    private Button start;

    @FXML
    private Button cancel;

    @FXML
    private TableView<BookStore> tableView;

    @FXML
    private TableColumn<BookStore, String> bookname;

    @FXML
    private TableColumn<BookStore, String> banid;

    @FXML
    private TableColumn<BookStore, String> isbn;

    @FXML
    private TableColumn<BookStore, String> author;

    @FXML
    private TableColumn<BookStore, String> status;

    @FXML
    private ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start.setOnMouseClicked(this::handleStart);
        bookname.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        banid.setCellValueFactory(new PropertyValueFactory<>("banid"));
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void handleStart(MouseEvent event) {
        cancel.setVisible(true);
        start.setVisible(false);
        place.setDisable(true);
        progressBar.setVisible(true);
//        Thread a = new Thread(() -> {
//            try {
//                progressBar.setProgress(0.1D);
//                Thread.sleep(2000L);
//                progressBar.setProgress(0.2D);
//                Thread.sleep(2000L);
//                progressBar.setProgress(0.6D);
//                Thread.sleep(2000L);
//                ObservableList<BookStore> bookStores = FXCollections.observableArrayList();
//                tableView.setItems(bookStores);
//                bookStores.add(new BookStore("黑客与画家", "9787115249494", "978-7-115-249494-4", "[美]Paul Graham著 阮一峰译", "正常在架"));
//                progressBar.setProgress(1.0D);
//                bookStores.add(new BookStore("黑客与画家", "9787115249494", "978-7-115-249494-4", "[美]Paul Graham著 阮一峰译", "正常在架"));
//                Thread.sleep(1000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                progressBar.setVisible(false);
//                progressBar.setProgress(0.0D);
//                place.setDisable(false);
//                start.setVisible(true);
//                cancel.setVisible(false);
//            }
//        });
//        a.start();
        MainFrm mainFrm = new MainFrm();
        mainFrm.connect();
        mainFrm.getRecordPro();
    }
}
