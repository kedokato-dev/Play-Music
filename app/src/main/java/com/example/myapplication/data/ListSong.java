package com.example.myapplication.data;

import com.example.myapplication.element.Song;

import java.util.ArrayList;

public class ListSong {
    private static ArrayList<Song> listSong;

    public static ArrayList<Song> getListSong(){
        if(listSong == null){
            listSong = new ArrayList<>();
            listSong.add(new Song("Khép Lại Nỗi Đau", "Dương Nhất Linh","https://docs.google.com/uc?id=1pOE1JwugSDzn7XXNuvdIImM2BLA8RfpI", "https://photo-resize-zmp3.zmdcdn.me/w256_r1x1_jpeg/avatars/3/5/351363f3867bc26ff4cd61c2cef70c06_1457432331.jpg","hihi"));
            listSong.add(new Song("À Lôi", "Double2T, Masew","https://docs.google.com/uc?id=1mKznsBMI_CJavZiyrFyzOsKME0c7y0fC", "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/avatars/0/c/8/e/0c8ee432f95f139828b523076c193500.jpg","hihi"));
            listSong.add(new Song("Khu Tao Sống", "Wowy ft Karik","https://docs.google.com/uc?id=1-aWIjOSt1DFL8OcfVhNWjNw8-L7cHVYX", "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/1/6/0/d1600a36b49c9469ce56ac0c1b31fe60.jpg","hihi"));
            listSong.add(new Song("Thanh Âm Miền Núi", "Double2T, Masew","https://docs.google.com/uc?id=181n86kXIRV8uiifodfDJdYmouMGrPfTH", "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/avatars/0/c/8/e/0c8ee432f95f139828b523076c193500.jpg","hihi"));
        }
        return listSong;
    }
}
