package com.dongduo.library.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 图书信息
 */
@Entity
@Table(name = "bk_bookinfo")
public class BookInfo {
    @Id
    @Column
    private long id;

    @Column(name = "ISBN")
    private String isbn;

    @Column
    private String author;

    public long getId() {
        return id;
    }

    public BookInfo() {
    }

    public BookInfo(long id, String isbn, String author) {
        this.id = id;
        this.isbn = isbn;
        this.author = author;
    }

    public void setId(long id) {
        this.id = id;
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
}
