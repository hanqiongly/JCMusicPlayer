package com.jack.music.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jack.music.R;
import com.jack.music.service.utils.PlayUtils;

/**
 * Created by liuyang on 16/9/11.
 */
public class MediaPlaybackActivity extends Activity{
    private TextView btnPlay;
    private TextView btnPrev;
    private TextView btnNext;
    private TextView btnPause;
    private TextView btnStop;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_play_page);
        initWidget();
    }

    private void initWidget() {
        btnPlay = (TextView)findViewById(R.id.btn_play_page_play);
        btnPrev = (TextView)findViewById(R.id.btn_play_page_prev);
        btnNext = (TextView)findViewById(R.id.btn_play_page_next);
        btnPause = (TextView)findViewById(R.id.btn_play_page_pause);
        btnStop = (TextView)findViewById(R.id.btn_play_page_stop);
        btnPlay.setOnClickListener(playClickListener);
        btnPrev.setOnClickListener(playClickListener);
        btnNext.setOnClickListener(playClickListener);
        btnPause.setOnClickListener(playClickListener);
        btnStop.setOnClickListener(playClickListener);
    }

    private View.OnClickListener playClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_play_page_next:
                    PlayUtils.next();
                    break;
                case R.id.btn_play_page_pause:
                    PlayUtils.pause();
                    break;
                case R.id.btn_play_page_play:
                    PlayUtils.playAll();
                    break;
                case R.id.btn_play_page_prev:
                    PlayUtils.prev();
                    break;
                case R.id.btn_play_page_stop:
                    PlayUtils.stop();
                    break;
                default:
                    PlayUtils.stop();
                    break;
            }
        }
    };

    public void onDestroy(){
        super.onDestroy();
    }
}
