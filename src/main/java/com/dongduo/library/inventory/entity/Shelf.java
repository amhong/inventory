package com.dongduo.library.inventory.entity;

import javax.persistence.*;

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
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Place place;

    /**
     * 名称
     */
    @Column
    private String name;

    public Shelf() {
    }

    public Shelf(String id, Place place, String name) {
        this.id = id;
        this.place = place;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
