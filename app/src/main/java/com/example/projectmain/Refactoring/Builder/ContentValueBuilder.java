package com.example.projectmain.Refactoring.Builder;

import android.content.ContentValues;

// tao class ContentValueBuilder đây là class có chức năng xac định đối tượng cần build là ai
// và kế implements interface để tạo các hàm build cho từng trường của đối tượng
public class ContentValueBuilder implements IPostContentValueBuilder{
    private ContentValues contentValues = new ContentValues();

    public ContentValues GetContentValue(){
        ContentValues result = this.contentValues;
        this.Reset();
        return result;
    }

    public ContentValueBuilder() {
    }

    @Override
    public IPostContentValueBuilder buildContent(String content) {
        contentValues.put("content", content);
        return this;
    }

    @Override
    public IPostContentValueBuilder buildImage(String image) {
        contentValues.put("image", image);
        return this;
    }

    @Override
    public IPostContentValueBuilder buidIsShare(int isShare) {
        contentValues.put("isshare", isShare);
        return  this;
    }

    @Override
    public IPostContentValueBuilder buildTimeOfPost(String dateTime) {
        contentValues.put("datetime", dateTime);
        return this;
    }

    @Override
    public IPostContentValueBuilder buildIdUser(int idUser) {
        contentValues.put("iduser", idUser);
        return this;
    }

    @Override
    public ContentValues build() {
        return contentValues;
    }

    @Override
    public void Reset() {
        this.contentValues = new ContentValues();
    }

}
