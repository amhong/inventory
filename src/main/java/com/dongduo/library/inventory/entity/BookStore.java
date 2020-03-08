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

    @Column
    private String banid;

    @Column(name = "rfid_sn")
    private String rfidSn;

    @JoinColumn(name = "bk_id", referencedColumnName = "id")
    @ManyToOne()
    private BookInfo bookInfo;

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

    public String getBanid() {
        return banid;
    }

    public void setBanid(String banid) {
        this.banid = banid;
    }

    public String getRfidSn() {
        return rfidSn;
    }

    public void setRfidSn(String rfidSn) {
        this.rfidSn = rfidSn;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}
