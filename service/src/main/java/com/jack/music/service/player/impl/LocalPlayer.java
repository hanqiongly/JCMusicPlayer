package com.jack.music.service.player.impl;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import com.jack.aidl.JCMusic;
import com.jack.music.service.player.Player;
import com.jack.music.service.scanner.MusicScanner;
import com.jack.music.service.utils.PlayConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 16/8/27.
 */
public class LocalPlayer implements Player, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    //当前播放核心引擎
    private MediaPlayer mediaPlayer;
    //当前的播放引擎是否有播放实体对象
    private JCMusic music;
    //当前的播放引擎是否已经设置了播放源
    private boolean isPrepared;
    //当前播放引擎是否能够播放
    private boolean isPlayerLocked;
    //当前音乐播放列表
    private List<JCMusic> musicList;
    private int curPosition = 0;

    public LocalPlayer() {
        initPlayer();
        musicList = new ArrayList<JCMusic>();
        initMusicList();
    }

    //For debug test
    private void initMusicList() {
        musicList = MusicScanner.getInstance().traverseRoot().getAllMusic();
    }

    public void initPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        isPrepared = false;
        isPlayerLocked = false;
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void adjustVolume(int deltaVol) {

    }

    @Override
    public String getCurrentAlbumId() {
        if (music != null) {
            return music.getAlbumId();
        }
        return "";
    }

    @Override
    public String getCurrentAudioId() {
        if (music != null) {
            return music.getMusicId();
        }
        return "";
    }

    @Override
    public boolean isMusicLoaded() {
        return musicList != null && musicList.size() > 0;
    }

    @Override
    public boolean play(String absPath) {
        boolean res = prepare(absPath);
        if (res)
            play();
        return res;
    }

    private boolean play(int position) {
        boolean res = false;
        if (musicList == null || musicList.size() <= 0) {
            return res;
        }
        if (position < 0)
            position = 0;
        if (position >= musicList.size()) {
            position = musicList.size() - 1;
        }

        music = musicList.get(position);
        String filePath = music.getFilePath();
        if (!TextUtils.isEmpty(filePath)) {
            if (prepare(filePath)) {
                play();
                res = true;
            }
        }
        curPosition = position;
        return res;
    }

    private synchronized boolean prepare(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        isPrepared = false;
        if (mediaPlayer != null) {
            try {
                //如果要重新设置数据源，就要重置当前播放引擎的状态
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                isPrepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isPrepared;
    }

    @Override
    public boolean playAll(List<JCMusic> songs) {
        boolean res = false;
        if (songs != null && songs.size() > 0) {
            musicList.addAll(songs);
            curPosition = 0;
            res = play(curPosition);
        } else {
            if (musicList != null && musicList.size() > 0) {
                curPosition = 0;
                res = play(curPosition);
            }
        }
        return res;
    }

    @Override
    public List<JCMusic> getAllSongs() {
        return musicList;
    }

    @Override
    public void clearPlayList() {
        if (musicList != null)
            musicList.clear();
    }

    @Override
    public void addToCurrentPlayList(List<JCMusic> list) {
        if (list != null && list.size() > 0)
            musicList.addAll(list);
    }

    @Override
    public JCMusic getMusic(int position) {
        if (musicList != null && musicList.size() > 0) {
            if (position < 0 || position >= musicList.size()) {
                return null;
            }

            return musicList.get(position);
        }
        return null;
    }

    @Override
    public int getQueuePosition() {
        return curPosition;
    }

    @Override
    public boolean isPlaying() {
        if (isPrepared)
            return mediaPlayer.isPlaying();
        else
            return false;
    }

    @Override
    public boolean stop() {
        if (isPrepared) {
            mediaPlayer.stop();
            return true;
        }
        return false;
    }

    @Override
    public boolean pause() {
        if (isPrepared) {
            mediaPlayer.pause();
            return true;
        }
        return false;
    }

    public int playCurrent() {
        int status = PlayConstants.PLAY_ERROR;
        if (isPrepared) {
            play();
            status = PlayConstants.RESUME_PLAY;
        } else {
            if (curPosition < 0) {
                curPosition = 0;
            }
            if (play(curPosition))
                status = PlayConstants.NEW_PLAY;
        }
        return status;
    }

    private void play() throws IllegalStateException {
        if (!isPrepared)
            throw new IllegalStateException();
        if (isPlayerLocked)
            throw new IllegalStateException();
        if (isPrepared) {
            mediaPlayer.start();
        }
    }

    @Override
    public boolean prev() {
        curPosition--;
        return play(curPosition);
    }

    @Override
    public boolean next() {
        curPosition++;
        return play(curPosition);
    }

    @Override
    public long duration() {
        if (isPrepared) {
            return mediaPlayer.getDuration();
        } else {
            if (music != null) {
                return music.getDuration();
            }
        }
        return 0;
    }

    @Override
    public long position() {
        if (isPrepared) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seek(int pos) {
        if (isPrepared) {
            mediaPlayer.seekTo(pos);
        }
    }

    @Override
    public String getTrackName() {
        if (music != null) {
            return music.getName();
        }
        return null;
    }

    @Override
    public String getAlbumName() {
        if (music != null) {
            return music.getAlbumName();
        }
        return null;
    }

    @Override
    public String getAlbumId() {
        if (music != null) {
            return music.getAlbumId();
        }
        return "";
    }

    @Override
    public String getArtistName() {
        if (music != null) {
            return music.getArtistName();
        }
        return null;
    }

    @Override
    public String getArtistId() {
        if (music != null) {
            return music.getArtistId();
        }
        return "";
    }

    @Override
    public void setQueuePosition(int index) {
        if (musicList == null || musicList.size() == 0) {
            curPosition = -1;
            return;
        }
        if (index < 0)
            curPosition = 0;
        if (index >= musicList.size())
            curPosition = musicList.size() - 1;
    }

    @Override
    public String getPath() {
        if (music != null) {
            return music.getFilePath();
        }
        return null;
    }

    @Override
    public void setShuffleMode(int shufflemode) {

    }

    public int getCurPosition() {
        return curPosition;
    }

    public JCMusic getCurrentPlayMusic() {
        if (music != null) {
            Log.d("Test_Music", "LocalPlayer getCurrentPlayMusic() " + music.toString());
        }
        return music;
    }

    @Override
    public int getShuffleMode() {
        return 0;
    }

    @Override
    public int removeTracks(int first, int last) {
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if (isPrepared) {
            return mediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
        if (mPlayStatusChangedListener != null) {
            mPlayStatusChangedListener.onPlayStatusChanged(PlayConstants.NEXT);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mPlayStatusChangedListener != null) {
            mPlayStatusChangedListener.onPlayStatusChanged(PlayConstants.PLAY_ERROR);
        }
        return false;
    }

    private OnPlayStatusChangedListener mPlayStatusChangedListener;

    public void setPlayStatusChangedListener(OnPlayStatusChangedListener listener) {
        mPlayStatusChangedListener = listener;
    }

    public interface OnPlayStatusChangedListener {
        public void onPlayStatusChanged(int status);
    }
}
