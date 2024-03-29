package com.example.projectmain.Refactoring.Command;

// class Invoker nayf là người triệu hồi lệnh tham chiếu các lệnh từ Client để thực hiện
// lệnh bằng haàm executeCommand
public class Invoker {
    private Command command;
    public Invoker(Command command){
        this.command = command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand(){
        command.execute();
    }
}
