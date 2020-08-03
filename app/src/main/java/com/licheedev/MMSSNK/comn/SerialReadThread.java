package com.licheedev.MMSSNK.comn;

import android.os.SystemClock;

import com.licheedev.myutils.LogPlus;
import com.licheedev.MMSSNK.comn.message.LogManager;
import com.licheedev.MMSSNK.comn.message.RecvMessage;
import com.licheedev.MMSSNK.util.ByteUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读串口线程
 */

public class SerialReadThread extends Thread {

    private static final String TAG = "SerialReadThread";

    private BufferedInputStream mInputStream;
    public static String data;

    public SerialReadThread(InputStream is) {
        mInputStream = new BufferedInputStream(is);
    }

    @Override
    public void run() {
        byte[] received = new byte[1024];
        int size;

        LogPlus.e("Start reading threads");

        while (true) {

            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {

                int available = mInputStream.available();

                if (available > 0) {
                    size = mInputStream.read(received);
                    if (size > 0) {
                        onDataReceive(received, size);
                    }
                } else {
                    // 暂停一点时间，免得一直循环造成CPU占用率过高
                    SystemClock.sleep(1);
                }
            } catch (IOException e) {
                LogPlus.e("Failed to read data", e);
            }
            //Thread.yield();
        }

        LogPlus.e("End the reading process");
    }

    /**
     * 处理获取到的数据
     *
     * @param received
     * @param size
     */

    private void  onDataReceive(byte[] received, int size) {
        // TODO: 2018/3/22 解决粘包、分包等
        //String hexStr = ByteUtil.bytes2HexStr(received, 0, size);
        String hexStr = ByteUtil.bytes2HexString(received, 0, size);
        LogManager.instance().post(new RecvMessage(hexStr));
       // if(hexStr.length() < 15 && 11 < hexStr.length()) {
            hexStr = hexStr.trim();
          //  hexStr = hexStr.substring(0,hexStr.length()-1);
           // hexStr = hexStr.trim();
            data = hexStr;
        //Log.e("Kg",hexStr);
        //}
        //LogFragment if(cannang.length() < 15 && 11 < cannang.length()) {
        // LogManager.instance().post(new RecvMessage(received.toString()));
    }

    /**
     * 停止读线程
     */
    public void close() {

        try {
            mInputStream.close();
        } catch (IOException e) {
            LogPlus.e("abnormal", e);
        } finally {
            super.interrupt();
        }
    }
}
