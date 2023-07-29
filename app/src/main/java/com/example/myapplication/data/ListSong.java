package com.example.myapplication.data;

import com.example.myapplication.element.Song;

import java.util.ArrayList;

public class ListSong {
    private static ArrayList<Song> listSong;

    public static ArrayList<Song> getListSong(){
        if(listSong == null){
            listSong = new ArrayList<>();
            listSong.add(new Song("À lôi", "Double2T, Masew","https://www.nhaccuatui.com/mh/background/ts2RU6aDicvX", "https://avatar-ex-swe.nixcdn.com/song/2023/07/13/a/e/f/0/1689234585612_640.jpg","hihi"));
            listSong.add(new Song("Anh yêu em", "Double2T, Masew","https://www.nhaccuatui.com/mh/background/ts2RU6aDicvX", "https://avatar-ex-swe.nixcdn.com/song/2023/07/13/a/e/f/0/1689234585612_640.jpg","hihi"));
            listSong.add(new Song("Mối tình chiều mưa bay ", "Double2T, Masew","https://www.nhaccuatui.com/mh/background/ts2RU6aDicvX", "https://avatar-ex-swe.nixcdn.com/song/2023/07/13/a/e/f/0/1689234585612_640.jpg","hihi"));
            listSong.add(new Song("Thanh âm miền núi", "Double2T, Masew","https://www.nhaccuatui.com/mh/background/ts2RU6aDicvX", "https://avatar-ex-swe.nixcdn.com/song/2023/07/13/a/e/f/0/1689234585612_640.jpg","hihi"));
            listSong.add(new Song("Thanh âm ", "Double2T, Masew","https://www.nhaccuatui.com/mh/background/ts2RU6aDicvX", "https://avatar-ex-swe.nixcdn.com/song/2023/07/13/a/e/f/0/1689234585612_640.jpg","hihi"));
        }
        return listSong;
    }
}
