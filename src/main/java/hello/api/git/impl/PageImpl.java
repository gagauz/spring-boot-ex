package hello.api.git.impl;

import java.util.Map;

import hello.api.git.Page;

public class PageImpl implements Page {
    private String startCursor;
    private String endCursor;
    private int totalCount;
    private boolean hasNext;

    @Override
    public String getStartCursor() {
        return startCursor;
    }

    public void setStartCursor(String startCursor) {
        this.startCursor = startCursor;
    }

    @Override
    public String getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public static PageImpl create(Map<String, ?> map) {
        PageImpl page = new PageImpl();
        page.startCursor = (String) map.get("startCursor");
        page.endCursor = (String) map.get("endCursor");
        page.hasNext = Boolean.getBoolean((String) map.get("hasNext"));
        return page;
    }
}
