package com.luooh.gallery.bean;

import java.io.Serializable;

/**
 * Created by Luooh on 2017/8/3.
 */

public class ImageInfo implements Serializable {

    private String filePath;  //图片路径
    private boolean isCheck;  //是否选中
    private long createTime;  //创建时间，可能会具体到分秒的时间，与display的时间不同
    private long fileSize;    //图片大小
    private long displaytime; //显示的时间，这个时间主要是格式化到年月日用的

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDisplaytime() {
        return displaytime;
    }

    public void setDisplaytime(long displaytime) {
        this.displaytime = displaytime;
    }
}
