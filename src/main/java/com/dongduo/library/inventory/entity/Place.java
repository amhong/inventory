package com.dongduo.library.inventory.entity;

import javax.persistence.*;

/**
 * 书库
 */
@Entity
@Table(name = "bk_place")
public class Place {
    @Id
    @Column
    private String id;

    @Column(name = "gcd_name")
    private String gcdName;

    @Column(name = "gcd_code")
    private String gcdCode;

    @Column(name = "del_flag")
    private char delFlag;

    public Place() {
    }

    public Place(String id, String gcdName, String gcdCode) {
        this.id = id;
        this.gcdName = gcdName;
        this.gcdCode = gcdCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public char getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(char delFlag) {
        this.delFlag = delFlag;
    }
}
