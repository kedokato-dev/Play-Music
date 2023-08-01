package com.example.myapplication.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.core.app.NotificationCompat;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;
import com.example.myapplication.notification.MyAplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PlayerService extends Service {

    Thread t; // chay song song vs main thear nghe nhac
    Handler h;
    MediaPlayer mediaPlayer;
    String url;
    String name;
    String singer;

    updateTimerRunable updateTimer;

    NotificationManager notificationManager;

    BroadcastReceiver notificationRecever;
    public PlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("songUrl");
        name = intent.getStringExtra("songName");
        singer = intent.getStringExtra("songSinger");
        playAdudio();
        showNotification();
        IntentFilter filter = new IntentFilter("ChangeStatusMedia");

        filter.addAction("SeekChange");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver(), filter);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationRecever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "changeStatus":
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            updateTimer.onPause();

                        }else {
                            mediaPlayer.start();
                            updateTimer.onResum();
                        }
                        break;
                    case "stopAudio":
                        mediaPlayer.pause();
                        updateTimer.onPause();
                        notificationManager.cancelAll();
                        stopSelf();
                        break;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("changeStatus");
        intentFilter.addAction("stopAudio");

        registerReceiver(notificationRecever, intentFilter);

        PendingIntent changeStatusIntent = PendingIntent.getBroadcast(this, 0, new Intent("changeStatus"), PendingIntent.FLAG_IMMUTABLE |PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent stopAudioIntent = PendingIntent.getBroadcast(this, 0, new Intent("stopAudio"), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, MyAplication.CHANNEL_ID)
//        Notification notification = new Notification.Builder(this)
                .setContentTitle(name)
                .setContentText(singer)
                .addAction(R.drawable.play, "pause/resum", changeStatusIntent)
                .addAction(R.drawable.pause, "stop", stopAudioIntent)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setSmallIcon(R.drawable.icon200).build();

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        notificationManager.notify(1, notification);
        startForeground(1, notification);
    }

    private BroadcastReceiver receiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "ChangeStatusMedia" :
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            updateTimer.onPause();

                        }else {
                            mediaPlayer.start();
                            updateTimer.onResum();
                        }
                        break;

                    case "SeekChange":
                        int currentPosition = intent.getIntExtra("currentPosition", 0);
                        mediaPlayer.seekTo(currentPosition *mediaPlayer.getDuration() / 100);
                        break;
                }
            }
        };
    }



    private void sendDurationToActivity(int dur){
        Intent intent = new Intent("SendDuration");
        intent.putExtra("duration", dur);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }

    private void sendProgressToActivity(int progress, int currentPosition){
        Intent intent = new Intent("SendProgress");
        intent.putExtra("progress", progress);
        intent.putExtra("currentPosition", currentPosition);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    class updateTimerRunable implements Runnable{

        private boolean isPause;
        private Object pauseLock;
        public updateTimerRunable(){
            isPause = false;
            pauseLock = new Object();
        }

        @Override
        public void run() {
            while(mediaPlayer.isPlaying()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendProgressToActivity(mediaPlayer.getCurrentPosition() * 100/mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
                synchronized (pauseLock){
                    while (isPause){
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        public void onPause(){
            synchronized (pauseLock){
                isPause = true;
            }
        }
        public void onResum(){
            synchronized (pauseLock){
                isPause = false;
                pauseLock.notifyAll();
            }
        }
    }

    private void createUpdateTimer(){
         updateTimer = new updateTimerRunable();
        new Thread(updateTimer).start();
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
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            sendDurationToActivity(mediaPlayer.getDuration());
                            createUpdateTimer();
                        }
                    });
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
                String path = getExternalCacheDir() + "/.Audio/";
                File folder = new File(path);
                //kiem tra folder co ton tai hay chua, neu chua thi tao folder ra
                if(!folder.exists()){
                    // tao folder
                    folder.mkdir();
                }
                File f = new File(getExternalCacheDir()+"/.Audio/"+String.valueOf(url.hashCode()) + ".mp3");
                if(!f.exists()){
                    try {
                        f.createNewFile();
                        InputStream in = new URL(url).openStream();
                        BufferedInputStream bis = new BufferedInputStream(in);
                        FileOutputStream fos = new FileOutputStream(f);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        do{
                            int byteOfFile = bis.read();
                            if(byteOfFile == -1){
                                break;
                            }
                            bos.write(byteOfFile);
                        }while (true);
                        bos.close();
                        in.close();
                    } catch (IOException e) {
                       e.printStackTrace();
                    }

                }
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(f.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            sendDurationToActivity(mediaPlayer.getDuration());
                            createUpdateTimer();
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}