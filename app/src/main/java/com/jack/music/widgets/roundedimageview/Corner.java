package com.jack.music.widgets.roundedimageview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by liuyang on 16/11/5.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        Corner.TOP_LEFT,
        Corner.TOP_RIGHT,
        Corner.BOTTOM_LEFT,
        Corner.BOTTOM_RIGHT
})

public @interface Corner {
    int TOP_LEFT = 0;
    int TOP_RIGHT = 1;
    int BOTTOM_RIGHT = 2;
    int BOTTOM_LEFT = 3;
}
