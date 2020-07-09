package com.ky.ykt.comparator;

import com.ky.ykt.entity.MenuEntity;

import java.util.Comparator;

public class MenuComparator implements Comparator<MenuEntity> {
    @Override
    public int compare(MenuEntity o1, MenuEntity o2) {
        if (o1.getMenuSort() > o2.getMenuSort())
            return 1;
        else return -1;
    }
}
