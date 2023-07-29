package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.SongListAdapter;
import com.example.myapplication.data.ListSong;

public class ListSongActivity extends AppCompatActivity {

    ListView listSong;
    SongListAdapter songListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        listSong = findViewById(R.id.listSong);

        songListAdapter = new SongListAdapter(this);
        listSong.setAdapter(songListAdapter);

        handleIntent(getIntent());

    }
    private void handleIntent(Intent intent){
        String query = intent.getStringExtra("songName");
        for (int i = 0; i < ListSong.getListSong().size(); i++){
            if(ListSong.getListSong().get(i).getSongName().toLowerCase().contains(query.toLowerCase())){
                songListAdapter.addItem(ListSong.getListSong().get(i));
            }
        }
        songListAdapter.notifyDataSetChanged();
    }
}