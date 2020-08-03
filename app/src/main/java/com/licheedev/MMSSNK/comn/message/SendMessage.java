package com.licheedev.MMSSNK.comn.message;

import android.util.Log;

import com.licheedev.MMSSNK.util.TimeUtil;

/**
 * 发送的日志
 */

public class SendMessage implements IMessage {

    private String command;
    private String message;

    public SendMessage(String command) {
        this.command = command;
        this.message = TimeUtil.currentTime() + " send ：" + command;
        Log.d("bbb", command);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isToSend() {
        return true;
    }
}
