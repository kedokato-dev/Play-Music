package com.example.myapplication.element;

public class Song {
    private String songName;
    private String songSinger;
    private String songUrl;
    private String songImageUrl;
    private String lyric;

    public Song(String songName, String songSinger, String songUrl, String songImageUrl, String lyric) {
        this.songName = songName;
        this.songSinger = songSinger;
        this.songUrl = songUrl;
        this.songImageUrl = songImageUrl;
        this.lyric = lyric;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongSinger() {
        return songSinger;
    }

    public void setSongSinger(String songSinger) {
        this.songSinger = songSinger;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public void setSongImageUrl(String songImageUrl) {
        this.songImageUrl = songImageUrl;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
