package com.dongduo.library.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 书库
 */
@Entity
@Table(name = "bk_place")
public class Place {
    @Id
    @Column
    private long id;

    @Column(name = "gcd_name")
    private String gcdName;

    @Column(name = "gcd_code")
    private String gcdCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGcdName() {
        return gcdName;
    }

    public void setGcdName(String gcdName) {
        this.gcdName = gcdName;
    }

    public String getGcdCode() {
        return gcdCode;
    }

    public void setGcdCode(String gcdCode) {
        this.gcdCode = gcdCode;
    }
}
