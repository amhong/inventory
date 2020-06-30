package com.dongduo.library.inventory.repository;

import com.dongduo.library.inventory.entity.BookStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<BookStore, Long>, JpaSpecificationExecutor<BookStore> {
    BookStore findByBanId(String banId);
    Page<BookStore> findByShelfId(String shelfId, Pageable pageable);

    @Modifying
    @Query("update BookStore set shelf.id = ?2 where banId = ?1")
    @Transactional
    int updateShelf(String banId, String shelfId);
}
