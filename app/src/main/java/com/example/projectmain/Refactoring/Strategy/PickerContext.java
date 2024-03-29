package com.example.projectmain.Refactoring.Strategy;

import android.app.Activity;

// dùng để thiết lập việc tham chiếu thuật toán nào để sửa dụng
public class PickerContext {
    private final IimagePicker pickerStrategy;
    public PickerContext(IimagePicker strategy){
        this.pickerStrategy = strategy;
    }

    public void setPickerStrategy(Activity activity){
         pickerStrategy.pickImage(activity);
    }
}
