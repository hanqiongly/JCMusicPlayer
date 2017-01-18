package com.jack.music.widgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.aidl.JCMusic;
import com.jack.music.R;
import com.jack.music.service.utils.PlayConstants;
import com.jack.music.service.utils.PlayUtils;
import com.jack.music.utils.UIConstants;
import com.jack.music.widgets.roundedimageview.RoundedImageView;

/**
 * Created by liuyang on 16/11/5.
 */

/*Bottom controller pannel */
public class BottomPlayController extends RelativeLayout implements View.OnClickListener{
    private static final String TAG_PROG = "test_progress";

    private static final int PROGRESS_STEP_SECONDS = 200;
    private static final int PROGRESS_STEP_LENGTH = 1;
    private RoundedImageView roundedImageView;
    private TextView songName;
    private TextView artistName;
    private ImageView btnNext;
    private ImageView btnPlayPause;
    private ProgressBar progressBar;
    private ImageView nowPlayingList;
    private ObjectAnimator circleImageRotation;

    private String unknowMusicName;
    private String unKnowArtist;

    private JCMusic mMusic;

    public BottomPlayController(Context context) {
        this(context,null);
    }

    public BottomPlayController(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomPlayController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomPlayController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    //init components
    private void init(Context context){
        Log.d(TAG_PROG,"init() begin");
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layer_bottom_play_controller,this,true);
        unknowMusicName = getResources().getString(R.string.unknown_music_name);
        unKnowArtist = getResources().getString(R.string.unknown_artist_name);
        mHandler = new ProgressHandler();
        PlayUtils.registerPlayStatusChangedListener(this.toString(),playStatusChangedListener);
        Log.d(TAG_PROG,"init() end");
    }
    //init views
    private void initViews() {
        Log.d(TAG_PROG,"initViews() begin");
        roundedImageView = (RoundedImageView)findViewById(R.id.bottom_controller_img);
        songName = (TextView)findViewById(R.id.bottom_controller_song_name);
        artistName = (TextView)findViewById(R.id.bottom_controller_artist);
        btnNext = (ImageView)findViewById(R.id.bottom_controller_next);
        btnNext.setOnClickListener(this);
        btnPlayPause = (ImageView)findViewById(R.id.bottom_controller_play_pause);
        btnPlayPause.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.bottom_controller_progress);
        nowPlayingList = (ImageView)findViewById(R.id.bottom_controller_play_list);
        constructAnimation();
        Log.d(TAG_PROG,"initViews() end");
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG_PROG,"onFinishInflate() begin");
        initViews();
        Log.d(TAG_PROG,"onFinishInflate() end");
    }



    public void updateMusicContent(JCMusic music) {
        Log.d(TAG_PROG,"updateMusicContent() begin");
        mMusic = music;
        if (mMusic == null) {
            return ;
        } else {
            Log.d(TAG_PROG,"updateMusicContent() music info :" + music.getName() + " artist :" + music.getArtistName());
        }
        updateMusicTextInfo();
        updateControllerImg();
        Log.d(TAG_PROG,"updateMusicContent() end");
    }

    public void startProgress() {
        Log.d(TAG_PROG,"startProgress() begin");
        rewindProgress();
        refreshProgress(true);
        Log.d(TAG_PROG,"startProgress() end");
    }

    //TODO -- 开始底部控制条的旋转动画
    public void startCircleImageRotation() {
        Log.d(TAG_PROG,"startCircleImageRotation() begin");
        restartAnimation();
        Log.d(TAG_PROG,"startCircleImageRotation() end");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void resumeAnim(){
        circleImageRotation.resume();
    }

    //// TODO: 2016/12/28 需要对低版本的sdk做兼容
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void pauseAnim() {
        circleImageRotation.pause();
    }

    private float currentAngel = 0;
    private void constructAnimation() {
        Log.d(TAG_PROG,"constructAnimation() begin");

        circleImageRotation = ObjectAnimator.ofFloat(roundedImageView,"rotation",currentAngel - 360,currentAngel);
        circleImageRotation.setDuration(18000);
        circleImageRotation.setRepeatCount(ObjectAnimator.INFINITE);
        circleImageRotation.setInterpolator(new LinearInterpolator());
        circleImageRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngel = (float)animation.getAnimatedValue();
            }
        });
        Log.d(TAG_PROG,"constructAnimation() end");

    }



    public void restartAnimation() {
        currentAngel = 0;
        circleImageRotation.start();
    }

    private void updateControllerImg() {
        Log.d(TAG_PROG,"updateControllerImg() begin");
        if (!TextUtils.isEmpty(mMusic.getMusicImgPath() )) {
            //TODO -- Set Default Image
            roundedImageView.setImageURI(Uri.parse(mMusic.getMusicImgPath()));
        } else {
            //TODO -- Add Update Image Procedure
            roundedImageView.setImageResource(R.mipmap.circle_bottom_test);
        }
        Log.d(TAG_PROG,"updateControllerImg() end");
    }

    private void updateMusicTextInfo() {
        Log.d(TAG_PROG,"updateMusicTextInfo() begin");
        if (!TextUtils.isEmpty(mMusic.getName())) {
            songName.setText(mMusic.getName());
        } else {
            songName.setText(unknowMusicName);
        }
        if (!TextUtils.isEmpty(mMusic.getArtistName())) {
            artistName.setText(mMusic.getArtistName());
        } else {
            artistName.setText(unKnowArtist);
        }

        progressBar.setMax((int)mMusic.getDuration());
        Log.d(TAG_PROG,"updateMusicTextInfo() end");
    }

    private int accumProgress = 0;
    private void rewindProgress() {
        accumProgress = 0;
    }

    //当前函数用于Activity界面刚刚加载之后，需要完成界面刷新的API。
    public void restoreStatus() {
        Log.d(TAG_PROG,"restoreStatus() begin");
        if (PlayUtils.isPlaying()) {
            mMusic = PlayUtils.getNowPlayingMusic();
            updateMusicContent(mMusic);
            accumProgress = (int) PlayUtils.position();
            refreshProgress(true);
            startCircleImageRotation();
        } else {
            //// TODO: 2016/11/28 添加上次播放之后保存的歌曲信息状态
        }

        Log.d(TAG_PROG,"restoreStatus() end");
    }

    private int refreshProgress(boolean needRefresh) {
        accumProgress += PROGRESS_STEP_LENGTH;
        progressBar.setProgress((int)PlayUtils.position());

        if (needRefresh)
        mHandler.sendEmptyMessageDelayed(UIConstants.CONST_MSG_PROGRESS_START_REFRESH,PROGRESS_STEP_SECONDS);

        return accumProgress;
    }

    ProgressHandler mHandler ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_controller_play_pause:
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
                if (PlayUtils.isPlaying()) {
                    PlayUtils.pause();
                } else {
                    PlayUtils.play();
                }
                break;
            case R.id.bottom_controller_next:
                PlayUtils.next();
                break;
            case R.id.bottom_controller_play_list:
                //TODO -- Popup current play list
                break;
            case R.id.bottom_controller_img:
                //TODO -- Go Now Playing Page
                break;
            default:
                break;
        }
    }

    private class ProgressHandler extends Handler {

        public void handleMessage(Message message) {
            Log.d(TAG_PROG,"ProgressHandler handleMessage() begin");
            switch (message.what) {
                case UIConstants.CONST_MSG_PROGRESS_START_REFRESH:
                    //刷新当前进度条
                    Log.d(TAG_PROG,"ProgressHandler handleMessage() will refeshProgress()");
                    refreshProgress(true);
                    break;
                case UIConstants.CONST_MSG_PROGRESS_PAUSE_REFRESH:
                    break;
                case UIConstants.MSG_FROM_SERVICE_PLAY_STATUS:
                    Log.d(TAG_PROG,"ProgressHandler handleMessage() play status changed");
                    if (UIConstants.CONST_MSG_BOTTOM_REFRESH_ALL == message.arg1) {
                        Log.d(TAG_PROG,"ProgressHandler handleMessage() will refresh all info");
                        updateMusicContent(PlayUtils.getNowPlayingMusic());
                        //TODO -- 切换当前的控制播放按钮为播放状态按钮显示
                        startProgress();
                        restartAnimation();
                    } else if (UIConstants.CONST_MSG_BOTTOM_REFRESH_IMAGE_PROGRESS == message.arg1) {
                        Log.d(TAG_PROG,"ProgressHandler handleMessage() will refresh PROG_IMAGE_ROTATION");
                        refreshProgress(true);
                        resumeAnim();
                    } else if (UIConstants.CONST_MSG_BOTTOM_PAUSE_PROGRESS == message.arg1) {
                        Log.d(TAG_PROG,"ProgressHandler handleMessage() will pause image rotation .");
                        refreshProgress(false);
                        pauseAnim();
                    }
                    break;
                default:
                    break;

            }
            Log.d(TAG_PROG,"ProgressHandler handleMessage() end");
        }
    }

    private PlayUtils.PlayStatusChangedListener playStatusChangedListener = new PlayUtils.PlayStatusChangedListener() {
        @Override
        public void playStatusChanged(int status) {
            Log.d(TAG_PROG,"playStatusChangedListener playStatusChanged() begin " + status);
            switch (status) {
                case PlayConstants.NEW_PLAY:
                case PlayConstants.NEXT:
                case PlayConstants.PREVIOUS:
                    Message message = new Message();
                    message.what = UIConstants.MSG_FROM_SERVICE_PLAY_STATUS;
                    message.arg1 = UIConstants.CONST_MSG_BOTTOM_REFRESH_ALL;
                    message.obj = PlayUtils.getNowPlayingMusic();
                    if (mHandler.hasMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS)) {
                        mHandler.removeMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS);
                    }
                    mHandler.sendMessage(message);
                    break;
                case PlayConstants.RESUME_PLAY:
                    //TODO -- 切换当前的控制播放按钮为播放状态按钮显示
                    message = new Message();
                    message.what = UIConstants.MSG_FROM_SERVICE_PLAY_STATUS;
                    message.arg1 = UIConstants.CONST_MSG_BOTTOM_REFRESH_IMAGE_PROGRESS;
                    if (mHandler.hasMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS)) {
                        mHandler.removeMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS);
                    }
                    mHandler.sendMessage(message);
                    break;
                case PlayConstants.PAUSE:
                    message = new Message();
                    message.arg1 = UIConstants.CONST_MSG_BOTTOM_PAUSE_PROGRESS;
                    message.what = UIConstants.MSG_FROM_SERVICE_PLAY_STATUS;
                    if (mHandler.hasMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS)) {
                        mHandler.removeMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS);
                    }
                    mHandler.sendMessage(message);

                    //// TODO: 16/11/25 change play status button is paused
                    break;
                default:
                    break;
            }
            Log.d(TAG_PROG,"playStatusChangedListener playStatusChanged() end ");
        }
    };

    private void stopUIJobs() {
        if (mHandler != null) {
            if (mHandler.hasMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS))
                mHandler.removeMessages(UIConstants.MSG_FROM_SERVICE_PLAY_STATUS);
            if (mHandler.hasMessages(UIConstants.CONST_MSG_PROGRESS_START_REFRESH))
                mHandler.removeMessages(UIConstants.CONST_MSG_PROGRESS_START_REFRESH);
            if (mHandler.hasMessages(UIConstants.CONST_MSG_PROGRESS_PAUSE_REFRESH))
                mHandler.removeMessages(UIConstants.CONST_MSG_PROGRESS_PAUSE_REFRESH);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopUIJobs();
    }

}
