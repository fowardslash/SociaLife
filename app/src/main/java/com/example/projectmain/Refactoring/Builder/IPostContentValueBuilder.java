package com.example.projectmain.Refactoring.Builder;

import android.content.ContentValues;

public interface IPostContentValueBuilder {
    // tạo một interface có  tất cả các hàm duùng để build đối tượng
    IPostContentValueBuilder buildContent(String content);
    IPostContentValueBuilder buildImage(String image);
    IPostContentValueBuilder buidIsShare(int isShare);
    IPostContentValueBuilder buildTimeOfPost(String dateTime);
    IPostContentValueBuilder buildIdUser(int idUser);

    ContentValues build();

    void Reset();

}
