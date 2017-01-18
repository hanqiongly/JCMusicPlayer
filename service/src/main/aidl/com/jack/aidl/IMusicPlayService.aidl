// IMusicPlayService.aidl
package com.jack.aidl;

import com.jack.aidl.JCMusic;
import com.jack.aidl.IPlayStatusCallback;

interface IMusicPlayService {
    void play(in String path);
    void pause();
    void playAll();
    void next();
    void prev();
    long duration();
    long position();
    void seek(in int pos);
    String getMusicName();
    String getAlbumName();
    String getArtistId();
    void stop();
    JCMusic getCurrentMusic();
    void playList(in List<JCMusic> musicList);
    boolean isPlaying();
    void resumePlay();
    void registerPlayStatusCallback(IPlayStatusCallback callback);
    void unRegisterPlayStatusCallback(IPlayStatusCallback callback);
//    JCMusicImplAidl getCurrentMusic();
    //JCMusic getMusic();
}
