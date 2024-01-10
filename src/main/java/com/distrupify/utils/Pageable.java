package com.distrupify.utils;

import io.quarkus.panache.common.Page;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Pageable {
    public static int DEFAULT_PAGE_SIZE = 2;

    private Integer page;
    private Integer size;

    public static Pageable of(Page page) {
        return new Pageable(page.index + 1, page.size);
    }

    public static Pageable all() {
        return new Pageable(null, null);
    }

    public static Pageable of(Integer pageNumber, Integer size) {
        if (pageNumber == null && size != null) {
            return new Pageable(1, size);
        }

        return new Pageable(pageNumber, size);
    }

    public int getPage() {
        checkPageAndSize();
        return page;
    }

    public int getPageSize() {
        checkPageAndSize();
        return size;
    }

    public boolean isAll() {
        return size == null || page == null;
    }

    public int offset() {
        checkPageAndSize();
        if (page < 1) return 0;

        return (page - 1) * size;
    }

    public int limit() {
        checkPageAndSize();
        return size;
    }

    private void checkPageAndSize() {
        if (size == null) {
            throw new IllegalStateException("Size is null");
        }

        if (page == null) {
            throw new IllegalStateException("Page is null");
        }
    }
}
