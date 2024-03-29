package com.example.projectmain.Refactoring.Decorator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.projectmain.Database.DB;
import com.example.projectmain.Refactoring.Singleton.GlobalUser;

// các class vaajt phẩm điều có hàm addItem để có thể xử lý logic mà nó sử dụng ví dụ như goij ham của db để update vât phẩm
public class PurpleFrameDecorator extends Decorator{
    public PurpleFrameDecorator(IDecorator decorator) {
        super(decorator);
    }
    @Override
    public void addItem(Context context) {
        super.addItem(context);
        DB myDB = new DB(context);
        GlobalUser user = GlobalUser.getInstance(context);
        myDB.buyBlueFrame(user.getUser().getId());
       // Log.d("ID USER: ", String.valueOf(user.getUser().getId()));
        Toast.makeText(context, "Mua thành công khung xanh!!", Toast.LENGTH_SHORT).show();
    }
}
