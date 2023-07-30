package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.SongListAdapter;
import com.example.myapplication.data.ListSong;
import com.example.myapplication.element.Song;
import com.example.myapplication.element.SongsElement;
import com.example.myapplication.util.StorageUtil;

public class ListSongActivity extends AppCompatActivity {

    public static String SONG_NAME = "songName";

    ListView listSong;
    SongListAdapter songListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        listSong = findViewById(R.id.listSong);

        songListAdapter = new SongListAdapter(this);
        listSong.setAdapter(songListAdapter);
        listSong.setOnItemClickListener(songListAdapter);
        handleIntent(getIntent());

    }



    private void handleIntent(Intent intent){
        if(intent.getAction().equals(intent.ACTION_SEARCH)){
        String query = intent.getStringExtra(SONG_NAME);
        for (int i = 0; i < ListSong.getListSong().size(); i++){
            if(ListSong.getListSong().get(i).getSongName().toLowerCase().contains(query.toLowerCase())){
                songListAdapter.addItem(ListSong.getListSong().get(i));
            }
        }
        songListAdapter.notifyDataSetChanged();
        }else if(intent.getAction().equals(SongsElement.OFF_SONG)){
            Cursor c = StorageUtil.getMp3FileCursor(ListSongActivity.this);
            if(c!=null){
                if(c.moveToFirst()){
                    int title = c.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int artist = c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int data = c.getColumnIndex(MediaStore.Images.Media.DATA);
                    do{
//                        songListAdapter.addItem(new Song(
//                                c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)),
//                                c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
//                                "",
//                                c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)),
//                                ""

//                        ));
                        String tt = c.getString(title);
                        String ar = c.getString(artist);
                        String dt = c.getString(data);

                        songListAdapter.addItem(new Song(tt, ar, dt, "",""));
                    }while (c.moveToNext());
                }
            }
        }
    }
}