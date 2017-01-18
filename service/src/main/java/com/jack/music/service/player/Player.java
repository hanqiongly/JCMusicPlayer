package com.jack.music.service.player;

import com.jack.aidl.JCMusic;

import java.util.List;

/**
 * Created by liuyang on 16/8/27.
 */
public interface Player {
    //当前正在播放的音乐所属的专辑ID，数据模块指定
    public String getCurrentAlbumId();
    //当前正在播放的音乐所属的音频ID，数据模块指定
    public String getCurrentAudioId();
    //当前音乐是否已经加载了，类似于是否准备好了，与isPrepared相关
    public boolean isMusicLoaded();

    //根据歌曲文件的绝对路径播放歌曲，只有在文件管理器(播放器以外)里面点击才能使用这个接口
    public boolean play(String absPath);
    //提供给外部进行列表播放的接口
    public boolean playAll(List<JCMusic> songs);
    //获取当前播放器中的播放列表的歌曲
    public List<JCMusic> getAllSongs();
    //清空当前播放列表
    public void clearPlayList();

    //增加到当前播放列表
    public void addToCurrentPlayList(List<JCMusic> music);

    //获取播放列表中某个位置上的歌曲
    public JCMusic getMusic(int position);
    //获取当前播放歌曲在播放列表中的位置
    public int getQueuePosition();
    //当前是否正在播放
    public boolean isPlaying();
    //停止播放
    public boolean stop();
    //暂停播放
    public boolean pause();
    //开始播放
//    private void play();
    //上一首
    public boolean prev();
    //下一首
    public boolean next();
    //当前播放歌曲的总时长
    public long duration();
    //当前歌曲播放的位置
    public long position();
    //快进到某个时间点
    public void seek(int pos);
    //获取当前播放的歌曲名
    public String getTrackName();
    //获取当前的专辑名
    public String getAlbumName();
    //获取当前歌曲的专辑ID
    public String getAlbumId();
    //获取当前的歌手名
    public String getArtistName();
    //获取当前的歌手ID
    public String getArtistId();

    //设置当前的播放位置
    public void setQueuePosition(int index);
    //获取当前播放歌曲的播放位置
    public String getPath();

    //设置当前的播放模式
    public void setShuffleMode(int shufflemode);
    //获取当前的播放模式
    public int getShuffleMode();
    //移除播放列表中位于first和last位置区域间的歌曲
    public int removeTracks(int first, int last);
    //获取当前媒体会话的ID
    public int getAudioSessionId();

}
