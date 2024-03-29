package com.example.projectmain.Refactoring.Decorator;
import android.content.Context;

// tạo class decorator là abstract class để có thể điều khiển việc Decord những vật phẩm nào
public abstract class Decorator implements IDecorator{
    protected  IDecorator decoratorWrap;
    // tạo constructor dùng để tham chiếu các vật phẩm vào để tiến hành add vật phẩm
    public Decorator(IDecorator decorator){
        this.decoratorWrap = decorator;
    }
    // hàm AddItem này để add những Item được tham chiếu vào tron Decorator class Decortator này
    @Override
    public void addItem(Context context) {
        if (decoratorWrap != null) {
            decoratorWrap.addItem(context);
        }
    }
}
