package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.musicplayer.PlayerActivity.listSongs;


public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int position = -1;
    ActionPlaying actionPlaying;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Bind", "Method");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");

        if (myPosition != -1) {
            playMedia(myPosition);
        }
        if (actionName != null) {
            switch (actionName) {
                case "playPause":
                    //  Toast.makeText(this, "PlayPause", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null) {
                        actionPlaying.playPauseBtnClicked();
                    }

                    break;
                case "next":
                    // Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null) {
                        actionPlaying.nextBtnClicked();
                    }
                    break;
                case "previous":
                    // Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null) {
                        actionPlaying.prevBtnClicked();
                    }
                    break;

            }
        }
        return START_STICKY;
    }

    private void playMedia(int StartPosition) {
        musicFiles = listSongs;
        position = StartPosition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }

    }

    void start() {
        mediaPlayer.start();
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    void stop() {
        mediaPlayer.stop();
    }

    void release() {
        mediaPlayer.release();
    }

    int getDuration() {
        return mediaPlayer.getDuration();
    }

    void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    void createMediaPlayer(int position) {
        uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    void pause() {
        mediaPlayer.pause();
    }

    void OnCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying != null) {
            actionPlaying.nextBtnClicked();
        }
        createMediaPlayer(position);
        start();
        OnCompleted();
    }

    void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}