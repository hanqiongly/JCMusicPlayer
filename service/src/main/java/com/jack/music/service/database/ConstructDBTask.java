package com.jack.music.service.database;

import android.content.Context;

import com.jack.music.service.scanner.MusicScanner;

/**
 * Created by liuyang on 16/10/21.
 */


public class ConstructDBTask {
    private static ConstructDBTask MINSTANCE;
    private MusicScanner musicScanner;
    private MusicDataBaseHelper dataBaseHelper;

    private ConstructDBTask(Context context) {
        musicScanner = MusicScanner.getInstance();
        dataBaseHelper = MusicDataBaseHelper.getInstance(context);
    }

    public static ConstructDBTask getInstance(Context context) {
        if (MINSTANCE == null) {
            MINSTANCE = new ConstructDBTask(context);
        }
        return MINSTANCE;
    }

    public void constructDB() {
        new Thread() {
            public void run() {
                if (musicScanner != null) {
                    musicScanner.traverseRoot();
                    if (dataBaseHelper != null) {
                        dataBaseHelper.addAllMusicTable(musicScanner.getAllMusic());
                    }
                }
            }
        }.start();
    }

}
