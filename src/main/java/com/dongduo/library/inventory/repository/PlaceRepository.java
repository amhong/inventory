package com.dongduo.library.inventory.repository;

import com.dongduo.library.inventory.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByDelFlag(char delFlag);
}
