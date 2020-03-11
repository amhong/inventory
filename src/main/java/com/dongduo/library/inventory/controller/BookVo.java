package com.dongduo.library.inventory.controller;

import com.dongduo.library.inventory.entity.BookStore;

public class BookVo {
    public enum Status {
        正常在架,
        正常借出,
        异常在架,
        图书缺失,
        架位错误
    }

    private String bookname;

    private String banid;

    private String isbn;

    private String author;

    private Status status;

    public BookVo() {
    }

    public BookVo(String bookname, String banid, String isbn, String author, Status status) {
        this.bookname = bookname;
        this.banid = banid;
        this.isbn = isbn;
        this.author = author;
        this.status = status;
    }

    public BookVo(BookStore bookStore, Status status) {
        this.bookname = bookStore.getBookname();
        this.banid = bookStore.getBanId();
        this.isbn = bookStore.getBookInfo().getIsbn();
        this.author = bookStore.getBookInfo().getAuthor();
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
