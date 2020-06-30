package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.entity.BookStore;
import com.dongduo.library.inventory.entity.Shelf;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.StringJoiner;

public class BookVo {
    public enum Status {
        正常在架,
        正常借出,
        异常在架,
        图书缺失,
        架位错误,
        已上架,
        未上架,
        未入库
    }

    private String bookname;

    private String banid;

    private String isbn;

    private String author;

    private String place;

    private String shelf;

    private Status status;

    private SimpleBooleanProperty selected;

    public BookVo() {
    }

    public BookVo(String banid, Status status, boolean selected) {
        this.banid = banid;
        this.status = status;
        this.selected = new SimpleBooleanProperty(selected);
    }

    public BookVo(BookStore bookStore, Status status, boolean selected) {
        this.bookname = bookStore.getBookname();
        this.banid = bookStore.getBanId();
        this.isbn = bookStore.getBookInfo().getIsbn();
        this.author = bookStore.getBookInfo().getAuthor();
        Shelf shelf = bookStore.getShelf();
        if (shelf != null) {
            this.place = shelf.getPlace().getGcdName();
            this.shelf = shelf.getName();
        }
        this.status = status;
        this.selected = new SimpleBooleanProperty(selected);
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBanid() {
        return banid;
    }

    public void setBanid(String banid) {
        this.banid = banid;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BookVo.class.getSimpleName() + "[", "]")
                .add("bookname='" + bookname + "'")
                .add("banid='" + banid + "'")
                .add("isbn='" + isbn + "'")
                .add("author='" + author + "'")
                .add("status=" + status)
                .add("selected=" + selected.get())
                .toString();
    }
}
