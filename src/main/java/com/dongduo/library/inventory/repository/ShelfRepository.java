package com.dongduo.library.inventory.repository;

import com.dongduo.library.inventory.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findByPlaceIdAndDelFlag(String placeId, char delFlag);
}
