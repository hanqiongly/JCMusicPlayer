package com.jack.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liuyang on 16/8/27.
 */
public class JCMusic implements Parcelable,Serializable,Comparable<JCMusic>{
    /*数据库中的id,由此标识*/
    private int id;
    /*当前音乐的ID,标识歌曲的唯一性*/
    private String musicId;
    /*音乐名*/
    private String musicName;
    /*音乐的演唱者在数据库中的ID*/
    private String artistId;
    /*音乐的演唱者姓名*/
    private String artistName;
    /*音乐的专辑在数据库中的ID*/
    private String albumId;
    /*音乐的专辑名*/
    private String albumName;

    //音乐的歌词地址
    private String musicLyricPath;
    //音乐的图片地址
    private String musicImagePath;

    /*音乐播放长度*/
    private long duration;
    /*音乐所属播放列表的ID*/
    private String playListId;
    /*音乐的文件路径*/
    private String filePath;
    /*音乐排序类型*/
    private int sortType = 1;

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setPlayListId(String playListId) {
        this.playListId = playListId;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public void setMusicName(String musicName) {

        this.musicName = musicName;
    }

    public JCMusic(){
    }

    protected JCMusic(Parcel in) {
        id = in.readInt();
        musicId = in.readString();
        musicName = in.readString();
        artistId = in.readString();
        artistName = in.readString();
        albumId = in.readString();
        albumName = in.readString();
        duration = in.readLong();
        playListId = in.readString();
        filePath = in.readString();
        musicLyricPath = in.readString();
        musicImagePath = in.readString();
        sortType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(musicId);
        dest.writeString(musicName);
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(albumId);
        dest.writeString(albumName);
        dest.writeLong(duration);
        dest.writeString(playListId);
        dest.writeString(filePath);
        dest.writeString(musicLyricPath);
        dest.writeString(musicImagePath);
        dest.writeInt(sortType);
    }


    public static final Creator<JCMusic> CREATOR = new Creator<JCMusic>() {
        @Override
        public JCMusic createFromParcel(Parcel in) {
            return new JCMusic(in);
        }

        @Override
        public JCMusic[] newArray(int size) {
            return new JCMusic[size];
        }
    };


    public String getMusicId() {
        return musicId;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getDuration() {
        return duration;
    }

    public String getName() {
        return musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getPlayList() {
        return playListId;
    }

    public void setMusicLyricPath(String musicLyricPath) {
        this.musicLyricPath = musicLyricPath;
    }

    public void setMusicImagePath(String musicImagepath) {
        this.musicImagePath = musicImagepath;
    }

    public String getMusicImgPath() {
        return musicImagePath;
    }

    public String getMusicLyrcPath() {
        return musicLyricPath;
    }

    public int getSortType() {
        return sortType;
    }

    public int describeContents() {
        return 0;
    }


    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    @Override
    public int compareTo(JCMusic another) {
        int result ;
        switch (sortType) {
            case MUSIC_SORT_TYPE.TYPE_MUSIC_ID:
                result = musicId.compareTo(another.getMusicId());
                break;
            case MUSIC_SORT_TYPE.TYPE_MUSIC_NAME:
                result = musicName.compareTo(another.getName());
                break;
            case MUSIC_SORT_TYPE.TYPE_ARTIST_ID:
                result = artistId.compareTo(another.getArtistId());
                break;
            case MUSIC_SORT_TYPE.TYPE_ARTIST_NAME:
                result = artistName.compareTo(another.getArtistName());
                break;
            case MUSIC_SORT_TYPE.TYPE_ALBUME_ID:
                result = albumId.compareTo(another.getAlbumId());
                break;
            case MUSIC_SORT_TYPE.TYPE_ALBUME_NAME:
                result = albumName.compareTo(another.getAlbumName());
                break;
            case MUSIC_SORT_TYPE.TYPE_DURATION:
                result = duration < another.getDuration() ? 0 : 1;
                break;
            default:
                result = musicName.compareTo(another.getName());
                break;
        }
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DB id is :" + id);
        sb.append(TextUtils.isEmpty(musicId) ? "musicId is empty " : musicId);
        sb.append(TextUtils.isEmpty(musicName) ? "\nmusicName is null" :" \nmusicName: " + musicName);
        sb.append(TextUtils.isEmpty(albumId) ? "\nalbumId is null" :"\nalbumId: " + albumId);
        sb.append(TextUtils.isEmpty(albumName) ? "\nalbumName is null" :"\nalbumName: " + albumName);
        sb.append(TextUtils.isEmpty(artistId) ? "\nartistId is null" :"\nartistId " + artistId);
        sb.append(TextUtils.isEmpty(artistName) ? "\nartistName is null" :"\nartistName " + artistName);
        sb.append(TextUtils.isEmpty(musicLyricPath) ? "\nmusicLyricPath is null" :"\nmusicLyricPath " + musicLyricPath);
        sb.append(TextUtils.isEmpty(musicImagePath) ? "\nmusicImagePath is null" :"\nmusicImagePath " + musicImagePath);
        sb.append("\nduration:" + duration);
        sb.append(TextUtils.isEmpty(playListId) ? "\nplayListId is null" :"\nplayListId " + playListId);
        sb.append(TextUtils.isEmpty(filePath) ? "\nfilePath is null" :"\nfilePath " + filePath);
        sb.append( "\nsortType:" + sortType);

        return sb.toString();
    }

    //音乐排序类型，根据某个维度的数据进行排序,默认按照歌曲名排序
    public interface MUSIC_SORT_TYPE {
        //根据音乐ID进行歌曲排序
        public static final int TYPE_MUSIC_ID = 0;
        //根据音乐名进行排序
        public static final int TYPE_MUSIC_NAME = 1;
        //根据歌手ID进行排序
        public static final int TYPE_ARTIST_ID = 2;
        //根据歌手名进行排序
        public static final int TYPE_ARTIST_NAME = 3;
        //根据专辑ID进行排序
        public static final int TYPE_ALBUME_ID = 4;
        //根据专辑名进行排序
        public static final int TYPE_ALBUME_NAME = 5;
        //根据歌曲时间长度进行排序
        public static final int TYPE_DURATION = 6;
    }

}
