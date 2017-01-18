package com.jack.music.service.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liuyang on 16/10/24.
 */

public class JCAlbum implements Serializable,Parcelable,Comparable<JCAlbum>{
    private String albumId;
    private String albumName;
    private String albumImagePath;
    private String albumMusicIds;
    private int albumMusicNumber;

    private int sortType;

    public JCAlbum() {

    }

    public JCAlbum(Parcel in) {
        albumId = in.readString();
        albumName = in.readString();
        albumImagePath = in.readString();
        albumMusicIds = in.readString();
        sortType = in.readInt();
        albumMusicNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumId);
        dest.writeString(albumName);
        dest.writeString(albumImagePath);
        dest.writeString(albumMusicIds);
        dest.writeInt(sortType);
        dest.writeInt(albumMusicNumber);
    }


    public String getAlbumId() {
        return albumId;
    }


    public String getAlbumName() {
        return albumName;
    }


    public String getAlbumMusicIds() {
        return albumMusicIds;
    }


    public String getAlbumImgPath() {
        return albumImagePath;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setAlbumImagePath(String albumImagePath) {
        this.albumImagePath = albumImagePath;
    }

    public void setAlbunMusicIds(String albumMusicIds) {
        this.albumMusicIds = albumMusicIds;
        if (!TextUtils.isEmpty(albumMusicIds)) {
            String[] musicIds = albumMusicIds.split(",");
            this.albumMusicNumber = (musicIds == null) ? 0 : musicIds.length;
        }
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }


    public int getSortType() {
        return sortType;
    }


    public int getAlbumMusicNum() {
        return albumMusicNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(JCAlbum another) {
        int result ;
        switch (sortType) {
            case ALBUM_SORT_TYPE.TYPE_ALBUM_MUSIC_NUM:
                result = albumMusicNumber > another.getAlbumMusicNum() ? 1 : 0;
                break;
            case ALBUM_SORT_TYPE.TYPE_ALBUME_ID:
                result = albumId.compareTo(another.getAlbumId()) ;
                break;
            case ALBUM_SORT_TYPE.TYPE_ALBUME_NAME:
                result = TextUtils.isEmpty(albumName) ? 0 : albumName.compareTo(another.getAlbumName());
                break;
            default:
                result = TextUtils.isEmpty(albumName) ? 0 : albumName.compareTo(another.getAlbumName());
                break;
        }
        return result;
    }

    public static final Creator<JCAlbum> CREATOR = new Creator<JCAlbum>() {
        @Override
        public JCAlbum createFromParcel(Parcel in) {
            return new JCAlbum(in);
        }

        @Override
        public JCAlbum[] newArray(int size) {
            return new JCAlbum[size];
        }
    };

    public void setAlbumMusicNumber(int albumMusicNumber) {
        this.albumMusicNumber = albumMusicNumber;
    }

    public interface ALBUM_SORT_TYPE {
        public static final int TYPE_ALBUM_MUSIC_NUM = 0;
        //根据专辑ID进行排序
        public static final int TYPE_ALBUME_ID = 4;
        //根据专辑名进行排序
        public static final int TYPE_ALBUME_NAME = 5;
    }
}
