package com.example.myapplication.element;

import android.content.Context;
import android.view.View;

import com.example.myapplication.R;

public class AlbumElemnet extends BaseListElement {


    public AlbumElemnet(Context context){
        this.context = context;
        updateData();
    }
    @Override
    public void updateData() {
        this.setNumber(0);
        this.setElementName("Album");
        this.setIconResource(R.drawable.icons8_album_48);
    }

    @Override
    public View.OnClickListener getOnclickListener() {
        return null;
    }
}
