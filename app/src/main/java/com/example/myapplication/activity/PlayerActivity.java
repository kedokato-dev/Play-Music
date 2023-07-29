package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.service.PlayerService;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        startService(createIntentService(intent.getStringExtra("songName"), intent.getStringExtra("songUrl")));
    }

    private Intent createIntentService(String name, String url){
       Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
        intent.putExtra("songName", name);
        intent.putExtra("songUrl", url);
        return intent;
    }
}