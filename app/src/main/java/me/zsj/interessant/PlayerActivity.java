package me.zsj.interessant;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import me.zsj.interessant.base.ToolbarActivity;

/**
 * Created by zsj on 2016/10/8.
 */

public class PlayerActivity extends ToolbarActivity
        implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {


    private VideoView mVideoView;
    private ProgressBar progressBar;
    private TextView downloadRateView, loadRateView;

    private boolean showSystemUi;


    @Override
    public int providerLayoutId() {
        return R.layout.player_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        if (!LibsChecker.checkVitamioLibs(this)) return;

        mVideoView = (VideoView) findViewById(R.id.buffer);
        progressBar = (ProgressBar) findViewById(R.id.probar);

        String path = getIntent().getStringExtra(MovieDetailActivity.PLAY_URL);

        downloadRateView = (TextView) findViewById(R.id.download_rate);
        loadRateView = (TextView) findViewById(R.id.load_rate);
        if (path.equals("")) {
            // Tell the user to provide a media file URL/path.
            Toast.makeText(
                    PlayerActivity.this,
                    "Please edit VideoBuffer Activity, and set path"
                            + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
        } else {
            Uri uri = Uri.parse(path);
            mVideoView.setVideoURI(uri);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();
            mVideoView.setOnInfoListener(this);
            mVideoView.setOnBufferingUpdateListener(this);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
        }

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    progressBar.setVisibility(View.VISIBLE);
                    downloadRateView.setText("");
                    loadRateView.setText("");
                    downloadRateView.setVisibility(View.VISIBLE);
                    loadRateView.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mVideoView.start();
                progressBar.setVisibility(View.GONE);
                downloadRateView.setVisibility(View.GONE);
                loadRateView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadRateView.setText(extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    private static final int FLAG_HIDE_SYSTEM_UI = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE;

    private static final int FLAG_SHOW_SYSTEM_UI = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;


    private void hideSystemUI() {
        toolbar.setAlpha(0);
        getWindow().getDecorView().setSystemUiVisibility(FLAG_HIDE_SYSTEM_UI);
    }

    private void showSystemUI() {
        toolbar.setAlpha(1f);
        getWindow().getDecorView().setSystemUiVisibility(FLAG_SHOW_SYSTEM_UI);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (showSystemUi) {
                hideSystemUI();
                showSystemUi = false;
            } else {
                showSystemUI();
                showSystemUi = true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent + "%");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
