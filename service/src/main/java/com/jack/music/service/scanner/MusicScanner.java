package com.jack.music.service.scanner;

import android.annotation.TargetApi;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.jack.aidl.JCMusic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 16/9/11.
 */
public class MusicScanner {
    private static final String TAG = "DUG_MusicScanner";
    private static final int DEFAULT_SIZE = 1024;
    private static final String EXTERNAL_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();/*"/sdcard/"*/
    private File EXTERNAL_ROOT = null;//Environment.getExternalStorageDirectory();
    private static MusicScanner mInstance;
    private List<JCMusic> allMusicList;
    private MediaMetadataRetriever mediaRetriever;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private MusicScanner() {
        allMusicList = new ArrayList<JCMusic>();
        Log.d(TAG, "MusicScanner : Root path :" + EXTERNAL_ROOT_PATH);
        EXTERNAL_ROOT = new File(EXTERNAL_ROOT_PATH);
        mediaRetriever = new MediaMetadataRetriever();

    }

    public static MusicScanner getInstance() {
        if (mInstance == null) {
            mInstance = new MusicScanner();
        }
        return mInstance;
    }

    public MusicScanner traverseRoot() {
        if (EXTERNAL_ROOT == null || !EXTERNAL_ROOT.exists() /*|| !EXTERNAL_ROOT.canRead()*/ || !EXTERNAL_ROOT.isDirectory()) {
            return this;
        }
        File[] files = EXTERNAL_ROOT.listFiles();
        if (files == null || files.length <= 0) {
            return this;
        }

        traceParent(EXTERNAL_ROOT);
        return this;
    }

    private synchronized void traceParent(File parent) {
        Log.d(TAG, "traceParent() begin");
        File[] stack = new File[DEFAULT_SIZE];
        int top = 0;
        stack[top] = parent;
        top++;
        File file;

        while (top > 0) {
            file = stack[top - 1];
            top--;
            if (!file.isDirectory()) {
                dealFile(file);
            } else {
                File[] listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++) {
                    File tmp = listFile[i];
                    if (!tmp.isDirectory()) {
                        dealFile(tmp);
                    } else {
                        stack[top] = listFile[i];
                        top++;
                    }
                }
            }


        }
        Log.d(TAG, "traceParent() end");

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dealFile(File file) {
        String path = file.getAbsolutePath();
        if (isFileMediaType(path)) {

            mediaRetriever.setDataSource(path);
            JCMusic music = new JCMusic();
            String album = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            music.setAlbumName(album);
            String artist = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            music.setArtistName(artist);
            String musicName = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            music.setMusicName(musicName);
            String totalTime = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = 0;
            if (!TextUtils.isEmpty(totalTime)) {
                duration = Long.parseLong(totalTime);
                music.setDuration(duration);

            }

            Log.d(TAG, path + " accepted as media file .\n name :" + musicName + " \n artist :" + artist + " \n album :" + album + " \n duration :" + duration + " \n totalTime:" + totalTime);

            music.setFilePath(path);
            //由于数据库中歌曲的id非空，故需要对歌曲的id进行取值
            music.setMusicId("music_" + path.hashCode() + "");
            allMusicList.add(music);
        }
    }

    private boolean isFileMediaType(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        boolean result = false;
        String dealtPath = path.toLowerCase();
        if (dealtPath.endsWith(MUSIC_TYPE.CDA) ||
                dealtPath.endsWith(MUSIC_TYPE.MP3) ||
                dealtPath.endsWith(MUSIC_TYPE.MIDI) ||
                dealtPath.endsWith(MUSIC_TYPE.APE) ||
                dealtPath.endsWith(MUSIC_TYPE.AAC) ||
                dealtPath.endsWith(MUSIC_TYPE.WAV) ||
                dealtPath.endsWith(MUSIC_TYPE.WMA) ||
                dealtPath.endsWith(MUSIC_TYPE.OGG) ||
                dealtPath.endsWith(MUSIC_TYPE.FLAC) ||
                dealtPath.endsWith(MUSIC_TYPE.MOD)) {
            result = true;
        }

        return result;
    }

    public List<JCMusic> getAllMusic() {
        return allMusicList;
    }


    public interface MUSIC_TYPE {
        public static final String CDA = ".cda";
        public static final String WAV = ".wav";
        public static final String MP3 = ".mp3";
        public static final String WMA = ".wma";
        public static final String MIDI = ".mid";
        public static final String OGG = ".ogg";
        public static final String APE = ".ape";
        public static final String FLAC = ".flac";
        public static final String AAC = ".aac";
        public static final String MOD = ".mod";
    }
}
