package com.jack.music.service.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.jack.aidl.JCMusic;

import java.util.List;

/**
 * Created by liuyang on 16/9/11.
 */
public class MusicDataProvider extends ContentProvider{
    private static final int CODE_MUSIC_INSERT = 0;
    private static final int CODE_MUSIC_QUERY = 1;
    private static final int CODE_MUSIC_DELETE = 2;
    private static final int CODE_MUSIC_UPDATE = 3;

    private static final String INSERT = "insert";
    private static final String QUERY = "query";
    private static final String DELETE = "delete";
    private static final String UPDATE = "update";

    private static final String authority = "com.jack.music.service";

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final Uri BASE_URI = Uri.parse("content://com.jack.music.service");

    {
        uriMatcher.addURI(authority,INSERT,CODE_MUSIC_INSERT);
        uriMatcher.addURI(authority,QUERY,CODE_MUSIC_QUERY);
        uriMatcher.addURI(authority,DELETE,CODE_MUSIC_DELETE);
        uriMatcher.addURI(authority,UPDATE,CODE_MUSIC_UPDATE);
    }

    public interface JCMUSIC_QUERY_URI {
        Uri CODE_JCMUSIC_INSERT = Uri.parse("content://" + authority + "/" + INSERT);
        Uri CODE_JCMUSIC_QUERY = Uri.parse("content://" + authority + "/" + QUERY);
        Uri CODE_JCMUSIC_DELETE = Uri.parse("content://" + authority + "/" + DELETE);
        Uri CODE_JCMUSIC_UPDATE = Uri.parse("content://" + authority + "/" + UPDATE);
    }

    private MusicDataBaseHelper dataBaseHelper = null;

    @Override
    public boolean onCreate() {
        dataBaseHelper = MusicDataBaseHelper.getInstance(getContext());
        return true;
    }
// --------------------------提供给外部的标准API查询接口----------------------------------------------------------
    @Nullable
    @Override
    //提供外部标准音乐查询接口
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == CODE_MUSIC_QUERY) {
            return dataBaseHelper.query(MusicDataBaseHelper.MUSIC_TABLE_NAME,projection,selection,selectionArgs,sortOrder);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    //提供外部标准插入数据库操作的API接口,成功返回true,失败返回false
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) == CODE_MUSIC_INSERT) {
            dataBaseHelper.insert(MusicDataBaseHelper.MUSIC_TABLE_NAME,values);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] selectionArgs) {
        if (uriMatcher.match(uri) == CODE_MUSIC_DELETE) {
            dataBaseHelper.delete(MusicDataBaseHelper.MUSIC_TABLE_NAME,whereClause,selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause, String[] selectionArgs) {
        if (uriMatcher.match(uri) == CODE_MUSIC_UPDATE) {
            dataBaseHelper.update(MusicDataBaseHelper.MUSIC_TABLE_NAME,values,whereClause,selectionArgs);
        }
        return 0;
    }
//------------------------------------------end

    public boolean batchInsertMusics(List<JCMusic> musics) {
        boolean isSuccess = true;
        isSuccess = dataBaseHelper.addAllMusicTable(musics);
        return isSuccess;
    }

    public void batchDeleteMusics(List<JCMusic> musics) {
        dataBaseHelper.batchDeleteMusic(musics);
    }

}
