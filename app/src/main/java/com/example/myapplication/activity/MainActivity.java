package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ElementListAdapter;
import com.example.myapplication.data.ListSong;
import com.example.myapplication.element.AlbumElemnet;
import com.example.myapplication.element.BaseListElement;
import com.example.myapplication.element.SongsElement;
import com.example.myapplication.util.StorageUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    ElementListAdapter adapter;
    TextView song_sum;
    List<BaseListElement> listOfflineElement;
    View listHeader;
    View listFooter;

    SimpleCursorAdapter cursorAdapter;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
        }

        final String[] from = new String[]{"songName"};
        final int[] to = new int[]{android.R.id.text1};

        cursorAdapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        listOfflineElement = new ArrayList<>();
        addElement();
        listView.setAdapter(adapter);
        setUpHeaderAndFooter();
        song_sum = findViewById(R.id.song_sum);

        song_sum.setText(""+StorageUtil.getMp3FileCursor(this).getCount()+" bài hát trong máy bạn");
    }
    private void addElement(){
        listOfflineElement.add(new SongsElement(this));
        listOfflineElement.add(new AlbumElemnet(this));

        adapter = new ElementListAdapter(this, listOfflineElement);
    }

    private void setUpHeaderAndFooter(){
        listHeader = View.inflate(MainActivity.this, R.layout.home_list_header, null);
        listFooter = View.inflate(MainActivity.this, R.layout.home_list_footer, null);
        listView.addHeaderView(listHeader);
        listView.addFooterView(listFooter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.tim_kiem).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(cursorAdapter);
        getSuggestion("test");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getSuggestion(s);
                return true;
            }
        });

       searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
           @Override
           public boolean onSuggestionSelect(int i) {
               return true;
           }

           @Override
           public boolean onSuggestionClick(int i) {
               cursorAdapter.getCursor().moveToPosition(i);
               String query = cursorAdapter.getCursor().getString(1);
               Intent intent = new Intent(MainActivity.this, ListSongActivity.class);
               intent.setAction(intent.ACTION_SEARCH);
               intent.putExtra(ListSongActivity.SONG_NAME, query);
               startActivity(intent);
               return true;
           }
       });
        return true;
    }

    private void getSuggestion(String text){
        MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "songName"});
        for (int i = 0; i < ListSong.getListSong().size(); i++){
            if(ListSong.getListSong().get(i).getSongName().toLowerCase().contains(text.toLowerCase())){
                c.addRow(new Object[]{1, ListSong.getListSong().get(i).getSongName()});
            }
        }
        cursorAdapter.changeCursor(c);
    }
}