package com.jack.music.service.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liuyang on 16/10/24.
 */

public class JCArtist implements Serializable,Parcelable,Comparable<JCArtist>{
    private String artistId;
    private String artistName;
    private String artistMusicIds;
    private String artistImg;

    private int sortType;
    private int artistMusicNum;

    public JCArtist(){}

    public JCArtist(Parcel in) {
        artistId = in.readString();
        artistName = in.readString();
        artistMusicIds = in.readString();
        artistImg = in.readString();
        artistMusicNum = in.readInt();
        sortType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(artistMusicIds);
        dest.writeString(artistImg);
        dest.writeInt(artistMusicNum);
        dest.writeInt(sortType);
    }


    public String getArtistId() {
        return artistId;
    }


    public String getArtistName() {
        return artistName;
    }


    public String getArtistMusicIds() {
        return artistMusicIds;
    }


    public String getArtistImg() {
        return artistImg;
    }


    public int getSortType() {
        return sortType;
    }


    public int getArtistMusicNum() {
        return artistMusicNum;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistMusicIds(String artistMusicIds) {
        this.artistMusicIds = artistMusicIds;
        if (!TextUtils.isEmpty(artistMusicIds)) {
            String[] musicIds = artistMusicIds.split(",");
            this.artistMusicNum = (musicIds == null) ? 0 : musicIds.length;
        }
    }

    public void setArtistImg(String artistImg) {
        this.artistImg = artistImg;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public void setArtistMusicNum(int artistMusicNum) {
        this.artistMusicNum = artistMusicNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JCArtist> CREATOR = new Creator<JCArtist>() {
        @Override
        public JCArtist createFromParcel(Parcel in) {
            return new JCArtist(in);
        }

        @Override
        public JCArtist[] newArray(int size) {
            return new JCArtist[size];
        }
    };

    @Override
    public int compareTo(JCArtist another) {
        int result ;
        switch (sortType) {
            case ARTIST_SORT_TYPE.TYPE_ARTIST_ID:
                result = (another == null) ? 1 :artistId.compareTo(another.getArtistId()) ;
                break;
            case ARTIST_SORT_TYPE.TYPE_ARTIST_MUSIC_NUM:
                result = (another == null) ? 1 : (artistMusicNum > another.getArtistMusicNum() ? 1 : 0);
                break;
            case ARTIST_SORT_TYPE.TYPE_ARTIST_NAME :
                result = (another == null) ? 1 : artistName.compareTo(another.getArtistName());
                break;
            default:
                result = (another == null) ? 1 : artistName.compareTo(another.getArtistName());
                break;
        }
        return result;
    }

    public interface ARTIST_SORT_TYPE {
        public static final int TYPE_ARTIST_MUSIC_NUM = 0;
        //根据专辑ID进行排序
        public static final int TYPE_ARTIST_ID = 4;
        //根据专辑名进行排序
        public static final int TYPE_ARTIST_NAME = 5;
    }

}
