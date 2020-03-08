package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.entity.BookStore;
import com.dongduo.library.inventory.repository.BookRepository;
import com.dongduo.library.inventory.service.ConnectFailedException;
import com.dongduo.library.inventory.service.RPanUHF;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        try {
            rPanUHF.connect();
        } catch (ConnectFailedException e) {
            logger.error(e.getMessage());
            // TODO 弹出提示框
            return;
        }

        cancel.setVisible(true);
        start.setVisible(false);
        place.setDisable(true);
        progressBar.setVisible(true);
        progressBar.setProgress(0.05D);

        Thread a = new Thread(() -> {
            Set<String> epcSet = rPanUHF.getRecordEpc();
            if (CollectionUtils.isEmpty(epcSet)) {
                // TODO 弹出提示框，从手持盘点仪读取到的数据为空
            } else {
                ObservableList<BookVo> bookStores = FXCollections.observableArrayList();
                Specification<BookStore> criteria = (root, query, cb) -> {return null};
                Pageable pageable = PageRequest.of(1, 1000, Sort.by(""));
                Page<BookStore> page;
                do {
                    page = bookRepository.findAll(criteria, pageable);
                    epcSet.remove("");
                    page.get().map(bs -> {
                        //if (bs)
                        return new BookVo(bs.getBookname(), bs.getBanid(), bs.getBookInfo().getIsbn(), bs.getBookInfo().getAuthor(), "");
                    });
                    pageable = pageable.next();
                } while (page != null && page.hasNext());
            }
            try {
                progressBar.setProgress(0.1D);
                Thread.sleep(2000L);
                progressBar.setProgress(0.2D);
                Thread.sleep(2000L);
                progressBar.setProgress(0.6D);
                Thread.sleep(2000L);
                ObservableList<BookVo> bookStores = FXCollections.observableArrayList();
                tableView.setItems(bookStores);
                bookStores.add(new BookVo("黑客与画家", "9787115249494", "978-7-115-249494-4", "[美]Paul Graham著 阮一峰译", "正常在架"));
                progressBar.setProgress(1.0D);
                bookStores.add(new BookVo("黑客与画家", "9787115249494", "978-7-115-249494-4", "[美]Paul Graham著 阮一峰译", "正常在架"));
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

            }
        });
        a.start();

        rPanUHF.disconnect();
        progressBar.setVisible(false);
        progressBar.setProgress(0.0D);
        place.setDisable(false);
        start.setVisible(true);
        cancel.setVisible(false);
    }
}
