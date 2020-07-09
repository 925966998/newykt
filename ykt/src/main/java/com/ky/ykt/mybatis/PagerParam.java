package com.ky.ykt.mybatis;

import java.util.Map;

/**
 * Created by Linan
 */
public class PagerParam {
    private long currentPage = 1, pageSize = 10;
    private Map<String, String> condition;

    public PagerParam(long currentPage, long pageSize, Map<String, String> condition) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.condition = condition;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, String> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, String> condition) {
        this.condition = condition;
    }
}
