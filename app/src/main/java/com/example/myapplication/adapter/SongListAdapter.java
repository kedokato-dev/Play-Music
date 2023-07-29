package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.ListSong;
import com.example.myapplication.element.Song;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter {

    ArrayList<Song> listSong;
    Context context;

    public SongListAdapter(Context context){
        listSong = new ArrayList<>();
        this.context = context;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
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
        TextView songName = view.findViewById(R.id.song_name);
        TextView songSinger = view.findViewById(R.id.song_singer);
        ImageView songImage = view.findViewById(R.id.song_image);

        songName.setText(listSong.get(i).getSongName());
        songSinger.setText(listSong.get(i).getSongSinger());
        ImageLoader.getInstance().displayImage(listSong.get(i).getSongImageUrl(), songImage);


        return view;
    }
    public   void addItem(Song song){
        if(listSong != null && song!=null){
            listSong.add(song);
        }
    }
}
