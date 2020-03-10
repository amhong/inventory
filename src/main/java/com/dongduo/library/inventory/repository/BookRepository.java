package com.dongduo.library.inventory.repository;

import com.dongduo.library.inventory.entity.BookStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<BookStore, Long>, JpaSpecificationExecutor<BookStore> {
    BookStore findByBanId(String banId);
}
