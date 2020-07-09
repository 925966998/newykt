package com.ky.ykt.entity;

import java.util.List;

public class TreeNode {

    private String id; // 要显示的子节点的ID
    private String text; // 要显示的子节点的 Text
    private String iconCls; // 节点的图标
    private String parentId; // 父节点的ID
    private boolean checked; // 是否选中
    private List<TreeNode> children; // 孩子节点的List

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
