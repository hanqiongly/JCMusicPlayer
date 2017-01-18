package com.jack.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jack.music.service.database.MusicDataBaseHelper;
import com.jack.music.service.player.impl.LocalPlayer;
import com.jack.music.service.scanner.MusicScanner;
import com.jack.music.service.utils.PlayConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 16/8/27.
 */
public class MusicPlayService extends Service{
    private LocalPlayer localPlayer;
    private MusicScanner musicScanner;
    private MusicDataBaseHelper musicDataBaseHelper;
    private List<JCMusic> musicList;

    public void onCreate() {
        super.onCreate();
        localPlayer = new LocalPlayer();
        localPlayer.setPlayStatusChangedListener(mPlayStatusChangedListener);
        discoverMusics();
        constructDataBase();
    }

    private void discoverMusics() {
        musicScanner = MusicScanner.getInstance();
        musicScanner.traverseRoot();
        musicList = musicScanner.getAllMusic();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void constructDataBase() {
        musicDataBaseHelper = MusicDataBaseHelper.getInstance(this);
        if (musicList != null && musicList.size() > 0) {
            musicDataBaseHelper.addAllMusicTable(musicList);
        }
    }

    public void seek(int pos) {
        if (localPlayer != null) {
            localPlayer.seek(pos);
        }
    }

    public void play(String path) {
            if (localPlayer.play(path))
                callBackStatus(PlayConstants.NEW_PLAY);
    }

    public int onStartCommand(Intent intent,int flags,int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void pause() {
        if (localPlayer.pause())
            callBackStatus(PlayConstants.PAUSE);
    }

    public void play() {
        if (musicList == null || musicList.size() <= 0) {
            playAll(null);
        } else {
            int status = localPlayer.playCurrent();
            if (status != PlayConstants.PLAY_ERROR) {
                callBackStatus(status);
            }
        }
    }

    public boolean isPlaying() {
        return localPlayer.isPlaying();
    }

    public void playAll(List<JCMusic> musics) {
        boolean res ;
        if (musics == null || musics.size() <= 0) {
            discoverMusics();
            res = localPlayer.playAll(musicList);
            //TODO -- Update current play list db
//            constructDataBase();
        } else {
            res = localPlayer.playAll(musics);
            //todo -- update current play list db
            musicList = new ArrayList<>();
            musicList.addAll(musics);
            musicDataBaseHelper.updateCurrentPlayingTable(musics);
        }

        if (res)
        callBackStatus(PlayConstants.NEW_PLAY);

    }
    LocalPlayer.OnPlayStatusChangedListener mPlayStatusChangedListener = new LocalPlayer.OnPlayStatusChangedListener() {
        @Override
        public void onPlayStatusChanged(int status) {
            callBackStatus(status);
        }
    };

    public String getAlbumName() {
        if (localPlayer != null) {
            return localPlayer.getAlbumName();
        }
        return "";
    }

    public void stop() {
        if (localPlayer.stop())
        callBackStatus(PlayConstants.PAUSE);
    }

    public void next() {
        localPlayer.next();
        callBackStatus(PlayConstants.NEXT);
    }

    public void prev() {
        if (localPlayer.prev())
        callBackStatus(PlayConstants.PREVIOUS);
    }

    public long duration() {
        if (localPlayer != null) {
            return localPlayer.duration();
        }
        return 0;
    }

    public String getArtistId() {
        if (localPlayer != null) {
            return localPlayer.getArtistId();
        }
        return "";
    }

    public long position() {
        if (localPlayer != null)
        return localPlayer.position();
        return 0;
    }

    public String getMusicName() {
        if (localPlayer != null) {
            return localPlayer.getArtistName();
        }
        return "";
    }

    public JCMusic getCurrentPlayMusic() {
        return localPlayer.getCurrentPlayMusic();
    }

    public void callBackStatus(int actionType) {
        int status = isPlaying() ? actionType : PlayConstants.PAUSE;

        final int n = mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                mCallbacks.getBroadcastItem(i).playStatusChanged(status);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }

    static class ServiceStub extends IMusicPlayService.Stub {
        WeakReference<MusicPlayService> mService;

        ServiceStub(MusicPlayService service) {
            mService = new WeakReference<MusicPlayService>(service);
        }

        @Override
        public void play(String path) throws RemoteException {
             mService.get().play(path);
        }

        public void resumePlay() {
            mService.get().play();
        }

        @Override
        public void pause() throws RemoteException {
            mService.get().pause();
        }

        @Override
        public void playAll() throws RemoteException {
            mService.get().playAll(null);
        }

        @Override
        public void next() throws RemoteException {
            mService.get().next();
        }

        @Override
        public void prev() throws RemoteException {
           mService.get().prev();
        }

        @Override
        public long duration() throws RemoteException {
            return mService.get().duration();
        }

        @Override
        public void seek(int pos) throws RemoteException {
            mService.get().seek(pos);
        }

        @Override
        public String getMusicName() throws RemoteException {
            return mService.get().getMusicName();
        }

        @Override
        public String getAlbumName() throws RemoteException {
            return mService.get().getAlbumName();
        }

        @Override
        public String getArtistId() throws RemoteException {
            return mService.get().getArtistId();
        }

        @Override
        public void stop() throws RemoteException {
           mService.get().stop();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }

        public void unRegisterPlayStatusCallback(IPlayStatusCallback callback) {
            Log.d("test_PlayStaChanged","unRegisterPlayStatusCallback()");
            if (callback != null) {
                mCallbacks.unregister(callback);
            }
        }

        public long position() {
            return mService.get().position();
        }

        public void registerPlayStatusCallback(IPlayStatusCallback callback) {
            Log.d("test_PlayStaChanged","registerPlayStatusCallback()");
            if (callback != null) {
                mCallbacks.register(callback);

            }
        }

        public JCMusic getCurrentMusic() {
            return mService.get().getCurrentPlayMusic();
        }

        public void playList(List<JCMusic> musicList) {
            mService.get().playAll(musicList);
        }
    }

    private final IBinder mBinder = new ServiceStub(this);
    public static RemoteCallbackList<IPlayStatusCallback> mCallbacks = new RemoteCallbackList<>();
}
