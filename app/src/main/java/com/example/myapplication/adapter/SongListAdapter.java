package com.example.myapplication.adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.PlayerActivity;
import com.example.myapplication.data.ListSong;
import com.example.myapplication.element.Song;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SongListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    ArrayList<Song> listSong;
    Context context;
    DownloadManager downloadManager;
    long enqueu;
    DisplayImageOptions options;


    public SongListAdapter(Context context) {
        listSong = new ArrayList<>();
        this.context = context;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.img)
                .showImageOnFail(R.drawable.img)
                .build();
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
        ImageButton btn_download = view.findViewById(R.id.btn_download);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                    Toast.makeText(context, "Đã tải nhạc xong thưa ngài!", Toast.LENGTH_LONG).show();
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(listSong.get(i).getSongUrl()));


                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                        .setTitle(listSong.get(i).getSongName() + ".mp3")
                        .setDescription(listSong.get(i).getSongName()+"-"+listSong.get(i).getSongSinger())
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, listSong.get(i).getSongName() + ".mp3");
                enqueu = downloadManager.enqueue(request);
                Intent intent = new Intent();
                intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                context.startActivity(intent);
            }
        });

        songName.setText(listSong.get(i).getSongName());
        songSinger.setText(listSong.get(i).getSongSinger());
        ImageLoader.getInstance().displayImage(listSong.get(i).getSongImageUrl(), songImage, options);

        return view;
    }

    public void addItem(Song song) {
        if (listSong != null && song != null) {
            listSong.add(song);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("songName", listSong.get(i).getSongName());
        intent.putExtra("songUrl", listSong.get(i).getSongUrl());
        context.startActivity(intent);
    }
}
