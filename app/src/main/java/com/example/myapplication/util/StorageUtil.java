package com.example.myapplication.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

// thao tac voi bo nho may de lay data ra su dung
public class StorageUtil {
    public static Cursor getMp3FileCursor(Context context){
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mime_type = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");

        String[] selectionArgMp3 = new String[]{mime_type};
        return cr.query(uri, null, selection, selectionArgMp3, null );
    }
}
