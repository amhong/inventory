package com.dongduo.library.inventory.entity;

import javax.persistence.*;

/**
 * 书籍典藏信息
 */
@Entity
@Table(name = "bk_bookstore")
public class BookStore {
    @Id
    @Column
    private long id;

    @Column
    private String bookname;

    @Column(name = "banid")
    private String banId;

    @Column(name = "rfid_sn")
    private String rfidSn;

    @Column(name = "left_count")
    private int leftCount;

    @JoinColumn(name = "bk_id", referencedColumnName = "id")
    @ManyToOne()
    private BookInfo bookInfo;

    public BookStore() {
    }

    public BookStore(long id, String bookname, String banId, String rfidSn, int leftCount, BookInfo bookInfo) {
        this.id = id;
        this.bookname = bookname;
        this.banId = banId;
        this.rfidSn = rfidSn;
        this.leftCount = leftCount;
        this.bookInfo = bookInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBanId() {
        return banId;
    }

    public void setBanId(String banId) {
        this.banId = banId;
    }

    public String getRfidSn() {
        return rfidSn;
    }

    public void setRfidSn(String rfidSn) {
        this.rfidSn = rfidSn;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}
