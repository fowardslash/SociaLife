package com.example.projectmain.Refactoring.Observer;

import android.content.Context;
import android.util.Log;

import com.example.projectmain.Model.Post;
import com.example.projectmain.Model.User;


public class AccountFl implements IObserver {

    private User user;
    private final Context context;
    public AccountFl(Context context) {
        this.context = context;
    }

    @Override
    public void update() {
        Publisher publisher = new Publisher(context);
       // Log.d("POST DA DANG: ", post.getContent());
        publisher.thongBao();
    }
}
