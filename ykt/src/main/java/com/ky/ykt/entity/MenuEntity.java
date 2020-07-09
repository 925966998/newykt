package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

import java.util.List;

public class MenuEntity extends BaseEntity {

    private String menuName;
    private String parentId;
    private String menuIcon;
    private String menuClass;
    private String menuUrl;
    private int isFirstMenu;
    private int menuSort;
    private List<MenuEntity> menuChildren;

    public List<MenuEntity> getMenuChildren() {
        return menuChildren;
    }

    public void setMenuChildren(List<MenuEntity> menuChildren) {
        this.menuChildren = menuChildren;
    }

    public int getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(int menuSort) {
        this.menuSort = menuSort;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuClass() {
        return menuClass;
    }

    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public int getIsFirstMenu() {
        return isFirstMenu;
    }

    public void setIsFirstMenu(int isFirstMenu) {
        this.isFirstMenu = isFirstMenu;
    }
}
