package com.jack.music.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.jack.aidl.JCMusic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuyang on 16/9/11.
 */
public class MusicDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "testMusicDBHelper";
    private static final String DB_NAME = "jcmusic.db";
    private static final int VERSION = 1;
    private SQLiteDatabase musicDB;
    public static final String MUSIC_TABLE_NAME = "JCMusic";
    public static final String CURRENT_PLAYING_MUSIC_TABLE = "currentPlaying";


    private static MusicDataBaseHelper mInstance;

    public static MusicDataBaseHelper getInstance(Context context) {
        Log.d(TAG, "getInstace() begin");
        if (mInstance == null) {
            mInstance = new MusicDataBaseHelper(context);
        }
        Log.d(TAG, "getInstace() end");
        return mInstance;
    }

    private MusicDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        getMusicDB();
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    private MusicDataBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    private MusicDataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        getMusicDB();
    }


    private void getMusicDB() {
        if (musicDB == null) {
            musicDB = getWritableDatabase();
            if (musicDB != null)
                initTabels(musicDB);
        }
    }

    private void initTabels(SQLiteDatabase db) {
        if (musicDB == null) {
            this.musicDB = db;
        }
        musicDB.execSQL(DELETE_MUSIC_TABLE);
        musicDB.execSQL(CREATE_MUSIC_TABLE);

        musicDB.execSQL(DELETE_CURRENT_PLAYINIG_MUSIC_TABLE);
        musicDB.execSQL(CREATE_CURRENT_PLAYING_MUSIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query(String tbName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (musicDB != null) {
            Cursor result = musicDB.query(tbName, projection, selection, selectionArgs, null, null, sortOrder);
            return result;
        } else {
            return null;
        }
    }

    public boolean insert(String tbName, ContentValues values) {
        long result = -1l;
        if (musicDB != null) {
            result = musicDB.insert(tbName, null, values);
        }
        boolean res = result <= 0 ? false : true;
        return res;
    }

    public boolean delete(String tbName, String selection, String[] selectArgs) {
        boolean res = true;

        if (musicDB != null) {
            int tmp = musicDB.delete(tbName, selection, selectArgs);
            res = tmp >= 0 ? true : false;
        }

        return res;
    }

    public boolean update(String tbName, ContentValues values, String whereClause, String[] whereArgs) {
        boolean res = false;

        if (musicDB != null) {
            int tmp = musicDB.update(tbName, values, whereClause, whereArgs);
            res = tmp > 0 ? true : false;
        }

        return res;
    }

    public List<JCMusic> queryMusic(ContentValues conditions) {
        if (musicDB == null) {
            return null;
        }

        List<JCMusic> musicList = null;

        //相当于将rawQuery中的条件查询筛选在这里做了一遍
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(MUSIC_TABLE_NAME);
        if (conditions != null) {
            Iterator<String> keys = conditions.keySet().iterator();
            if (keys.hasNext()) {
                sb.append(" where ");

                while (keys.hasNext()) {
                    String title = keys.next();
                    String value = conditions.getAsString(title);
                    sb.append(title);
                    sb.append("='");
                    sb.append(value);
                    sb.append("'");
                    if (keys.hasNext())
                        sb.append(" and ");
                }
            }
        }
        sb.append(";");
        String sql = sb.toString();
        Log.d(TAG, "Query SQL :" + sql);

        Cursor result = musicDB.rawQuery(sql, null);
        musicList = cursor2List(result);
        result.close();

        return musicList;
    }


    private List<JCMusic> cursor2List(Cursor rawData) {
        if (rawData == null) {
            return null;
        }
        int indexId = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.ID);
        int indexMusicId = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.MUSIC_ID);
        int indexMusicName = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.MUSIC_NAME);
        int indexArtistId = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.ARTIST_ID);
        int indexArtistName = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.ARTIST_NAME);
        int indexAlbumId = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.ALBUM_ID);
        int indexAlbumName = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.ALBUM_NAME);
        int indexDuration = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.DURATION);
        int indexPlayListId = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.PLAYLIST_ID);
        int indexMusicFilePath = rawData.getColumnIndex(TAB_MUSIC_COLUMNS.FILE_PATH);

        List<JCMusic> result = new ArrayList<JCMusic>();
        while (rawData.moveToNext()) {
            JCMusic music = new JCMusic();

            music.setMusicId(rawData.getString(indexMusicId));
            music.setMusicName(rawData.getString(indexMusicName));
            music.setArtistId(rawData.getString(indexArtistId));
            music.setArtistName(rawData.getString(indexArtistName));
            music.setAlbumId(rawData.getString(indexAlbumId));
            music.setAlbumName(rawData.getString(indexAlbumName));
            music.setDuration(rawData.getLong(indexDuration));
            music.setPlayListId(rawData.getString(indexPlayListId));
            music.setFilePath(rawData.getString(indexMusicFilePath));

            result.add(music);
        }

        return result;
    }

    public boolean addAllMusicTable(List<JCMusic> musicList) {
        return batchMusicInsert(musicList, MUSIC_TABLE_NAME);
    }

    public boolean updateCurrentPlayingTable(List<JCMusic> musicList) {
        delete(CURRENT_PLAYING_MUSIC_TABLE, null, null);
        return batchMusicInsert(musicList, CURRENT_PLAYING_MUSIC_TABLE);
    }

    private boolean batchMusicInsert(List<JCMusic> musics, String tbName) {
        boolean res = true;

        if (musics == null || musics.size() <= 0) {
            return false;
        }

        if (musicDB == null) {
            return false;
        }
        musicDB.beginTransaction();
        for (JCMusic music : musics) {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ");
            sb.append(tbName);
            sb.append(" (musicId,musicName,artistId,artistName,albumId,albumName,duration,playListId,filePath) values ('");
            sb.append(dealWithStr(music.getMusicId()));
            sb.append("','");
            sb.append(dealWithStr(music.getName()));
            sb.append("','");
            sb.append(dealWithStr(music.getArtistId()));
            sb.append("','");
            sb.append(dealWithStr(music.getArtistName()));
            sb.append("','");
            sb.append(dealWithStr(music.getAlbumId()));
            sb.append("','");
            sb.append(dealWithStr(music.getAlbumName()));
            sb.append("','");
            sb.append(music.getDuration());
            sb.append("','");
            sb.append(dealWithStr(music.getPlayList()));
            sb.append("','");
            sb.append(dealWithStr(music.getFilePath()));
            sb.append("');");

            musicDB.execSQL(sb.toString());

        }
        musicDB.setTransactionSuccessful();
        musicDB.endTransaction();
        return res;
    }

    public void batchDeleteMusic(List<JCMusic> musics) {
        if (musics == null || musics.size() <= 0) {
            return;
        }

        if (musicDB == null) {
            return;
        }
        musicDB.beginTransaction();

        for (JCMusic music : musics) {
            musicDelete(music);
            musicDB.setTransactionSuccessful();
        }
        musicDB.endTransaction();


    }

    public boolean musicDelete(JCMusic music) {
        return musicDelete(music, true);
    }

    public boolean musicDelete(JCMusic music, boolean needTransact) {
        if (music == null) {
            return false;
        }
        ContentValues values = new ContentValues();
        boolean res = true;

        if (!TextUtils.isEmpty(music.getMusicId())) {
            values.put(TAB_MUSIC_COLUMNS.MUSIC_ID, music.getMusicId());
        } else if (!TextUtils.isEmpty(music.getFilePath())) {
            values.put(TAB_MUSIC_COLUMNS.FILE_PATH, music.getFilePath());
        }

        res = musicDelete(values, needTransact);

        return res;
    }

    public boolean musicDelete(ContentValues values, boolean needTransact) {
        if (values == null) {
            return false;
        }

        if (musicDB == null) {
            return false;
        }
        boolean res = true;

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM " + MUSIC_TABLE_NAME + " WHERE ");

        Iterator<String> keys = values.keySet().iterator();
        if (keys.hasNext()) {
            sb.append(" where ");

            while (keys.hasNext()) {
                String title = keys.next();
                String value = values.getAsString(title);
                sb.append(title);
                sb.append("='");
                sb.append(value);
                sb.append("'");
                if (keys.hasNext())
                    sb.append(" and ");
            }
        }
        sb.append(";");
        String sql = sb.toString();
        if (needTransact) {
            musicDB.beginTransaction();
        }
        musicDB.execSQL(sql);

        if (needTransact) {
            musicDB.setTransactionSuccessful();
            musicDB.endTransaction();
        }


        return res;
    }

    private String dealWithStr(String rawSQL) {
        if (TextUtils.isEmpty(rawSQL)) {
            return "";
        }
        String target = rawSQL.contains("'") ? rawSQL.replace("'", "''") : rawSQL;
        return target;
    }

    private static final String DELETE_MUSIC_TABLE = "drop table if exists " + MUSIC_TABLE_NAME + " ;";
    private static final String DELETE_CURRENT_PLAYINIG_MUSIC_TABLE = "drop table if exists " + CURRENT_PLAYING_MUSIC_TABLE + " ;";


    //创建数据表的时候不能添加drop语句，在手机实际运行时不会走下一条真正建表的语句
    private static final String CREATE_MUSIC_TABLE =
            "create table " + MUSIC_TABLE_NAME + "(" +
                    "  musicId nvarchar(64) NOT NULL," +
                    "  musicName text," +
                    "  artistId nvarchar(64)," +
                    "  artistName nvarchar(64)," +
                    "  albumId varchar(64)," +
                    "  albumName varchar(64)," +
                    "  duration long," +
                    "  playListId nvarchar(64)," +
                    "  filePath text);";

    private static final String CREATE_CURRENT_PLAYING_MUSIC_TABLE =
            "create table " + CURRENT_PLAYING_MUSIC_TABLE + "(" +
                    "  musicId nvarchar(64) NOT NULL," +
                    "  musicName text," +
                    "  artistId nvarchar(64)," +
                    "  artistName nvarchar(64)," +
                    "  albumId varchar(64)," +
                    "  albumName varchar(64)," +
                    "  duration long," +
                    "  playListId nvarchar(64)," +
                    "  filePath text);";

    private interface TAB_MUSIC_COLUMNS {
        public static final String ID = "id";
        public static final String MUSIC_ID = "musicId";
        public static final String MUSIC_NAME = "musicName";
        public static final String ARTIST_ID = "artistId";
        public static final String ARTIST_NAME = "artistName";
        public static final String ALBUM_ID = "albumId";
        public static final String ALBUM_NAME = "albumName";
        public static final String DURATION = "duration";
        public static final String PLAYLIST_ID = "playListId";
        public static final String FILE_PATH = "filePath";
    }

}
