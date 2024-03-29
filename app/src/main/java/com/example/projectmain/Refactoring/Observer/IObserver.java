package com.example.projectmain.Refactoring.Observer;

import com.example.projectmain.Model.Post;
import com.example.projectmain.Model.User;

// giao diện thực hiện việc update là gọi Pushlisher để thông báo đến cáo subscriber là những người follower khi có bài đăng mới xuất hiện
public interface IObserver {
    void update();
}
