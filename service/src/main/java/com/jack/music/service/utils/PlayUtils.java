package com.jack.music.service.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.jack.aidl.IMusicPlayService;
import com.jack.aidl.IPlayStatusCallback;
import com.jack.aidl.JCMusic;
import com.jack.aidl.MusicPlayService;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by liuyang on 16/9/11.
 */
public class PlayUtils {
    private static final String TAG = "test_PlayStaChanged";
    private static IMusicPlayService mService = null;
    private static HashMap<Context,LocalServiceConnection> sConnectionmap = new HashMap<Context, LocalServiceConnection>();

    public static class ServiceToken {
        ContextWrapper mWrappedContext;
        public ServiceToken(ContextWrapper contextWrapper) {
            mWrappedContext = contextWrapper;
        }
    }

    private static class LocalServiceConnection implements ServiceConnection {
        ServiceConnection mOnServiceConnectionListener;

        LocalServiceConnection(ServiceConnection onServiceConnectionListener) {
            mOnServiceConnectionListener = onServiceConnectionListener;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mService = IMusicPlayService.Stub.asInterface(binder);
            try {
                mService.registerPlayStatusCallback(mPlayStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (mOnServiceConnectionListener != null) {
                mOnServiceConnectionListener.onServiceConnected(name,binder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mOnServiceConnectionListener != null) {
                mOnServiceConnectionListener.onServiceDisconnected(name);
//                mOnServiceConnectionListener = null
            }
            try {
                mService.unRegisterPlayStatusCallback(mPlayStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mPlayStatusChangedListeners.clear();
            try {
                if (!mService.isPlaying())
                mService = null;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static ServiceToken bindToService(Activity context) {
        return bindToService(context,null);
    }

    public static ServiceToken bindToService(Activity context, ServiceConnection connection) {
        Activity realActivity = context.getParent();

        if (realActivity == null) {
            realActivity = context;
        }

        ContextWrapper cw = new ContextWrapper(realActivity);
        //TODO Delete Code Below To See Is Service Ok .
        cw.startService(new Intent(cw, MusicPlayService.class));
        LocalServiceConnection sb = new LocalServiceConnection(connection);
        if (cw.bindService(new Intent(cw,MusicPlayService.class),sb,0)) {
            sConnectionmap.put(cw,sb);
            return new ServiceToken(cw);
        }
        return null;
    }

    public static void unBindFromService(ServiceToken token) {
        if (token == null) {
            return ;
        }
        ContextWrapper cw = token.mWrappedContext;
        LocalServiceConnection sb = sConnectionmap.remove(cw);
        if (sb == null) {
            return;
        }
        cw.unbindService(sb);
        if (sConnectionmap.isEmpty()) {
            mService = null;
        }
    }

    public static void playAll() {
        if (mService != null) {
            try {
                mService.playAll();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPlaying() {
        if (mService == null) {
            return false;
        }

        try {
            return mService.isPlaying();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void pause() {
        if (mService != null) {
            try {
                mService.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void play() {
        if (mService != null) {
            try {
                mService.resumePlay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void prev() {
        if (mService != null) {
            try {
                mService.prev();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void next() {
        if (mService != null) {
            try {
                mService.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stop() {
        if (mService != null) {
            try {
                mService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static JCMusic getNowPlayingMusic() {
        JCMusic music = null;
        if (mService != null) {
            try {
                music = mService.getCurrentMusic();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (music != null) {
            Log.d("test_info","PlayUtils getNowPlayingMusic() :" + music.toString());
        }
        return music;
    }

    public static long position() {
        if (mService != null) {
            try {
                return mService.position();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public interface PlayStatusChangedListener{
        public void playStatusChanged(int status);
    }

    static HashMap<String,PlayStatusChangedListener> mPlayStatusChangedListeners = new HashMap<>();

    public static void registerPlayStatusChangedListener(String key,PlayStatusChangedListener playStatusChangedListener) {
        Log.d(TAG,"registerPlayStatusChangedListener() :" + key);
        mPlayStatusChangedListeners.put(key,playStatusChangedListener);
    }

    public void unRegisterPlayStatusChangedListener(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (mPlayStatusChangedListeners.containsKey(key)) {
            mPlayStatusChangedListeners.remove(key);
        }
    }

    static IPlayStatusCallback mPlayStatusCallback = new IPlayStatusCallback.Stub() {
        @Override
        public void playStatusChanged(int status) throws RemoteException {
            Log.d(TAG,"mPlayStatusCallback : status " + status);
            Iterator<String> keys = mPlayStatusChangedListeners.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                PlayStatusChangedListener listener = mPlayStatusChangedListeners.get(key);
                Log.d(TAG,"mPlayStatusCallback : status " + key);
                if (listener != null) {
                    listener.playStatusChanged(status);
                    Log.d(TAG,"mPlayStatusCallback : callback " + key);
                }
            }
        }
    };
}
