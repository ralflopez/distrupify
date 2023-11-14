package com.distrupify.utils;

import io.quarkus.panache.common.Page;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pageable {
    private int page;
    private int size;

    public static Pageable of(Page page) {
        return new Pageable(page.index + 1, page.size);
    }

    public static Pageable of(int pageNumber, int size) {
        return new Pageable(pageNumber, size);
    }

    public int offset() {
        if (page < 1) return 0;

        return (page - 1) * size;
    }

    public int limit() {
        return size;
    }
}
