package com.levi.community.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {
    private int current = 1;
    private int limit = 10;
    private int rows;
    private String path;

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public int getOffset() {
        // currentPage * limit - limit
        return (current - 1) * limit;
    }

    public int getTotal() {
        // rows / limit [+1]
        return rows % limit == 0 ? rows / limit : rows / limit + 1;
    }

    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
