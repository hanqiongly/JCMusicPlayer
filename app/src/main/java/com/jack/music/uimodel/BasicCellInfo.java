package com.jack.music.uimodel;

import android.graphics.drawable.Drawable;

/**
 * Created by liuyang on 2016/12/23.
 */

public class BasicCellInfo {
    private String cellInfo;
    private Drawable cellImg;

    public void setCellInfo(String info) {
        cellInfo = info;
    }

    public void setCellImg(Drawable drawable) {
        cellImg = drawable;
    }

    public String getCellInfo() {
        return cellInfo;
    }

    public Drawable getCellImg() {
        return cellImg;
    }
}
