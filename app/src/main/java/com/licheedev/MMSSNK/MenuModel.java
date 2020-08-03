package com.licheedev.MMSSNK;


public class MenuModel {

    public String menuName, url;
    public boolean hasChildren, isGroup;
    public int iConItem;

    public MenuModel(int iConItem, String menuName, boolean isGroup, boolean hasChildren, String url) {
        this.iConItem = iConItem;
        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
