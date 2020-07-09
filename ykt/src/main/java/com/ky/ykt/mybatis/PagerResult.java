package com.ky.ykt.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 170181 on 2017/1/6.
 */
public class PagerResult {
    private long pageNumber, pageSize, pagesCount, totalItemsCount;
    private List items;

    public PagerResult(List items, long totalItemsCount, long currentPage, long pageSize) {
        this.items = (items == null ? new ArrayList() : items);
        this.totalItemsCount = (totalItemsCount <= 0 ? 0 : totalItemsCount);
        this.pageNumber = (currentPage) <= 0 ? 0 : (currentPage);
        this.pageSize = (pageSize <= 0 ? 10 : pageSize);
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getPagesCount() {
        return totalItemsCount % pageSize == 0 ? totalItemsCount / pageSize : totalItemsCount / pageSize + 1;
    }

    public long getTotalItemsCount() {
        return totalItemsCount;
    }

    public List getItems() {
        return items;
    }
}
