package com.dongduo.library.inventory.entity;

public class BookStore {
    private String bookname;

    private String banid;

    private String isbn;

    private String author;

    private String status;

    public BookStore() {
    }

    public BookStore(String bookname, String banid, String isbn, String author, String status) {
        this.bookname = bookname;
        this.banid = banid;
        this.isbn = isbn;
        this.author = author;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
