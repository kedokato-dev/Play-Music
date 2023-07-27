package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.element.BaseListElement;

import java.util.List;

public class ElementListAdapter extends BaseAdapter {

    public List<BaseListElement> listElements;
    public Context context;

    public ElementListAdapter(Context context,List<BaseListElement> listElements){
            this.context = context;
            this.listElements = listElements;
    }

    @Override
    public int getCount() {
        return listElements.size();
    }

    @Override
    public Object getItem(int i) {
        return listElements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView image;
        TextView name, number;

        view = View.inflate(context, R.layout.dong_item, null );

        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        number = view.findViewById(R.id.number);

        image.setImageResource(listElements.get(i).getIconResource());
        name.setText(listElements.get(i).getElementName());
        number.setText(""+listElements.get(i).getNumber());


        return view;
    }
}
