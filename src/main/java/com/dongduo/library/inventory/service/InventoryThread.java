package com.dongduo.library.inventory.service;

import com.dongduo.library.inventory.controller.BookVo;
import com.dongduo.library.inventory.entity.BookStore;
import com.dongduo.library.inventory.repository.BookRepository;
import com.dongduo.library.inventory.util.EpcCode;
import javafx.collections.ObservableList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InventoryThread extends Thread {
    private Set<EpcCode> epcStrs;
    private BookRepository bookRepository;
    private ObservableList<BookVo> bookVos;

    public InventoryThread(Set<EpcCode> epcStrs, BookRepository bookRepository, ObservableList<BookVo> bookVos) {
        this.epcStrs = epcStrs;
        this.bookRepository = bookRepository;
        this.bookVos = bookVos;
    }

    @Override
    public void run() {
        Map<String, EpcCode> epcMap = epcStrs.stream().collect(Collectors.toMap(EpcCode::getItemId, e -> e));
        Specification<BookStore> criteria = (root, query, cb) -> {
            return null;
        };
        Pageable pageable = PageRequest.of(1, 1000, Sort.by("bookname"));
        Page<BookStore> page;

        do {
            page = bookRepository.findAll(criteria, pageable);
            Set<BookVo> bookVosOfPage = page.get().map(bs -> {
                BookVo.Status status;
                if (epcMap.remove(bs.getBanId()) != null) { // 移除成功表示从在架
                    status = bs.getLeftCount() != 0 ? BookVo.Status.正常在架 : BookVo.Status.图书缺失;
                } else { // 移除失败表示不在架
                    status = bs.getLeftCount() != 0 ? BookVo.Status.异常在架 : BookVo.Status.正常借出;
                }
                return new BookVo(bs, status);
            }).collect(Collectors.toSet());
            bookVos.addAll(bookVosOfPage);
            pageable = pageable.next();
        } while (page != null && page.hasNext());

        if (!epcMap.isEmpty()) { // 处理不应放置在该架上的图书
            for (String banId : epcMap.keySet()) {
                BookStore bookStore = bookRepository.findByBanId(banId);
                if (bookStore != null) {
                    bookVos.addAll(new BookVo(bookStore, BookVo.Status.架位错误));
                }
            }
        }

        bookVos.sorted();
    }
}
