package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.service.PlayerService;

import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    TextView time_start, time_end, song_name, song_singer;
    SeekBar seekBar;
    boolean isPlay = true;
    ImageButton statusButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        time_start = findViewById(R.id.time_start);
        time_end  = findViewById(R.id.time_end);
        seekBar = findViewById(R.id.progress_player);
        statusButton = findViewById(R.id.btn_pause);
        song_name = findViewById(R.id.song_name);
        song_singer = findViewById(R.id.song_singer);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    sendCurrentPositionToService(seekBar.getProgress());
            }
        });
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusMedia();
            }
        });

        IntentFilter filter = new IntentFilter("SendDuration");
        filter.addAction("SendProgress");

        // lang nghe su kien tong thoi gian cua bai hat
        LocalBroadcastManager.getInstance(this).registerReceiver(createMessageRecevier(), filter);

        Intent intent = getIntent();
        startService(createIntentService(intent.getStringExtra("songName"), intent.getStringExtra("songUrl")));

        String singer = intent.getStringExtra("songSinger");
        song_singer.setText(singer);
        String songName = intent.getStringExtra("songName");
        song_name.setText(songName);
    }

    private void changeStatusMedia(){
        Intent intent = new Intent("ChangeStatusMedia");
        isPlay =! isPlay;

        statusButton.setBackgroundResource(isPlay ? R.drawable.pause : R.drawable.play);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void sendCurrentPositionToService(int currentPosition){
        Intent intent = new Intent("SeekChange");
        intent.putExtra("currentPosition", currentPosition);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private BroadcastReceiver createMessageRecevier(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "SendDuration":
                        int dur = intent.getIntExtra("duration", 0);
                            String endTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(dur)
                                    , TimeUnit.MILLISECONDS.toSeconds(dur) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur)));
                            time_end.setText(endTime);
                        break;


                    case "SendProgress":
                        int progress = intent.getIntExtra("progress", 0);
                        int currentPosition = intent.getIntExtra("currentPosition", 0);

                        seekBar.setProgress(progress);
                        String startTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentPosition)
                                , TimeUnit.MILLISECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition)));
                        time_start.setText(startTime);
                        break;
                }
            }
        };
    }


    private Intent createIntentService(String name, String url){
       Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
        intent.putExtra("songName", name);
        intent.putExtra("songUrl", url);
//        song_name.setText(name);
        return intent;
    }
}