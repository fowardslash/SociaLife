package com.example.projectmain.Refactoring.Decorator;

import android.content.Context;

// tạo interface cos hàm thực thi dùng để add item (truyền context để gọi hàm DB để add)
public interface IDecorator {
    void addItem(Context context);
}
