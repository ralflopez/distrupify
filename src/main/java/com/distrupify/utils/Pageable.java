package com.distrupify.utils;

import io.quarkus.panache.common.Page;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pageable {
    public static int DEFAULT_PAGE_SIZE = 30;

    private int page;
    private int size;

    public static Pageable of(Page page) {
        return new Pageable(page.index + 1, page.size);
    }

    public static Pageable of(int pageNumber, int size) {
        return new Pageable(pageNumber, size);
    }

    public static Pageable of(Integer pageNumber, Integer size) {
        return new Pageable(pageNumber == null ? 1 : pageNumber, size == null ? DEFAULT_PAGE_SIZE : size);
    }

    public int offset() {
        if (page < 1) return 0;

        return (page - 1) * size;
    }

    public int limit() {
        return size;
    }
}
