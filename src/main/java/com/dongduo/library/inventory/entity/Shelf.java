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
    private long id;

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
}
