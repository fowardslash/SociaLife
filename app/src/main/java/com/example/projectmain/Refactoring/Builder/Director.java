package com.example.projectmain.Refactoring.Builder;

import android.content.ContentValues;
import android.net.Uri;
import android.telephony.data.UrspRule;
import android.util.Log;

import java.util.Calendar;

public class Director {
    // dùng class Director này để điều khiển việc Build như thế nào ví dụ bài đăng chỉ có text thì build trường chỉ dùng Text

    private final IPostContentValueBuilder _builder; // dùng final để xác nhận rằng một cái chỉ build được cho 1 đối tượng.

    public Director(IPostContentValueBuilder builder, int idUser) {
        this._builder = builder;
        this._builder.buildIdUser(idUser);
    }
    Calendar c = Calendar.getInstance();
    long t = c.getTimeInMillis();

    public ContentValues buildOnlyTextPost(String content, int isShare){
        return _builder.buildContent(content)
                .buidIsShare(isShare)
                .buildTimeOfPost(String.valueOf(t))
                .build();
    }

    public ContentValues builOnlyImagePost(Uri image, int isShae){
        return _builder.buildImage(String.valueOf(image))
                .buidIsShare(isShae)
                .build();
    }

    public ContentValues buildImageAndContentPost(Uri image, String content, int isShare){
        return _builder.buildContent(content)
                .buildImage(String.valueOf(image))
                .buidIsShare(isShare)
                .buildTimeOfPost(String.valueOf(t))
                .build();
    }

}
