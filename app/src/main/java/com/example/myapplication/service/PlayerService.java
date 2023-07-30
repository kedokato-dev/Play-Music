package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import java.io.File;
import java.io.IOException;

public class PlayerService extends Service {

    Thread t; // chay song song vs main thear nghe nhac
    Handler h;
    MediaPlayer mediaPlayer;
    String url;
    String name;
    public PlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("songUrl");
        name = intent.getStringExtra("songName");
        playAdudio();

        return super.onStartCommand(intent, flags, startId);
    }

    private void playAdudio() {
        if(h==null) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    h = new Handler();
                    File f = new File(url);
                    if(f.exists() && f.isFile()){
                        h.post(playAudioFromFile());
                    }else {
                        h.post(playAudioFromUrl());
                    }
                    Looper.loop();
                }
            });
            t.start();
        }else {
            File f = new File(url);
            if(f.exists() && f.isFile()){
                h.post(playAudioFromFile());
            }else {
                h.post(playAudioFromUrl());
            }
        }
    }

    private Runnable playAudioFromFile(){
        return new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                File f = new File(url);
                if(!f.exists()){
                    stopSelf();
                    return;
                }
                try {
                    mediaPlayer.setDataSource(f.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        };

    }

    private Runnable playAudioFromUrl(){
        return  new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}