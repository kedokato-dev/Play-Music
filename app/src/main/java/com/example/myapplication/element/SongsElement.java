package com.example.myapplication.element;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.activity.ListSongActivity;
import com.example.myapplication.util.StorageUtil;

public class SongsElement extends BaseListElement {

    public static String OFF_SONG = "off_song";

    public SongsElement(Context context){
        this.context = context;
        updateData();
    }

//    public SongsElement(int iconResource, String elementName, int number) {
//        super(iconResource, elementName, number);
//    }

    @Override
    public void updateData() {
        this.setElementName("Song");
        this.setIconResource(R.drawable.icons8_music_note_48);
        this.setNumber(StorageUtil.getMp3FileCursor(this.context).getCount());
    }

    @Override
    public View.OnClickListener getOnclickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongsElement.this.context, ListSongActivity.class);
                intent.setAction(OFF_SONG);
                SongsElement.this.context.startActivity(intent);
            }
        };
    }
}
