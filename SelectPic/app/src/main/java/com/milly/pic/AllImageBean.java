package com.milly.pic;


public class AllImageBean {
    //列表显示集合的第一张图片路劲
    private String showFirstImage;
    //显示的文件夹名称
    private String showName;
    //路径集合图片的数量
    private int count;

    public String getShowFirstImage() {
        return showFirstImage;
    }

    public void setShowFirstImage(String showFirstImage) {
        this.showFirstImage = showFirstImage;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
