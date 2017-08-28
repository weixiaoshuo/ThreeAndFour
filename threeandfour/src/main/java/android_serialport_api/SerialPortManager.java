package android_serialport_api;


import android.util.Log;

import java.io.File;

public class SerialPortManager {

    private static SerialPortManager manager = null;
    private String path = "/dev/ttyMT0";
    private int baudrate = 115200;
    private SerialPort serialPort;
    private ReadThread readthread;
    private OnDataReceiverListener onDataReceiverListener;
    private boolean isStop = false;


    public synchronized static SerialPortManager getInstance() {
        if (manager == null) {
            manager = new SerialPortManager();
        }
        return manager;
    }

    private SerialPortManager() {
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0);
            readthread =new ReadThread();
            isStop = false;
            readthread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 从串口读数据的线程
    private class ReadThread extends  Thread {

        @Override
        public void run() {
            super.run();
            while (!isStop){
                byte[] buffer = new byte[4096];
                int size = serialPort.readFromSerialPort(buffer);
                if (size > 0){
                    Log.d("serial","read from serial size: "+size);
                    // 从流中读取的字节存储在buffer当中，读取的字节长度size
                    onDataReceiverListener.onDataReceiver(buffer, size);
                }
            }
        }
    }

    // 往串口写数据
    public void writeToSerialPort(byte cmd , byte[] data){
        byte[] serial_data = new byte[data.length + 5];
        serialPort.writeToSerialPort(serial_data);
    }


    // 关闭串口
    public void stopSerialPort(){
        isStop  = true;
        serialPort.release();
    }

    public void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        this.onDataReceiverListener = onDataReceiverListener;
    }

    public interface OnDataReceiverListener{
        void onDataReceiver(byte[] data, int size);
    }
}
