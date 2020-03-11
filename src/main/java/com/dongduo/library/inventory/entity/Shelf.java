package com.dongduo.library.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 书架
 */
@Entity
@Table(name = "bk_shelf")
public class Shelf {
    @Id
    @Column
    private String id;

    /**
     * 书库id
     */
    @Column(name = "parent_id")
    private String placeId;

    /**
     * 名称
     */
    @Column
    private String name;

    public Shelf() {
    }

    public Shelf(String id, String placeId, String name) {
        this.id = id;
        this.placeId = placeId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
