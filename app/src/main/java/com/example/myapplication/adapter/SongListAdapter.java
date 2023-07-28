package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.myapplication.R;
import com.example.myapplication.element.Song;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter {

    ArrayList<Song> listSong;
    Context context;

    public SongListAdapter(Context context){
        listSong = new ArrayList<>();
        this.context = context;
    }
    @Override
    public int getCount() {
        return listSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(context, R.layout.dong_bai_hat, null);
        return view;
    }
}
