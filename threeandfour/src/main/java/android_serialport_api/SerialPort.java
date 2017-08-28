package android_serialport_api;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPort {

    private static final String TAG = "SerialPort";
    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flag) throws SecurityException, IOException {
        mFd = open(device.getAbsolutePath(), baudrate, flag);
        if (mFd == null) {
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    private native FileDescriptor open(String path, int baudrate, int flag);

    private native int close();

    static {
        System.loadLibrary("serial_port");
    }


    public void writeToSerialPort(byte[] data) {
        try {
            this.mFileOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int readFromSerialPort(byte[] data) {
        int size = 0;
        try {
            size = this.mFileInputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void release() {
        try {
            if (this.mFileInputStream != null) {
                mFileOutputStream.close();
                mFileOutputStream = null;
            }
            if (this.mFileOutputStream != null) {
                mFileOutputStream.close();
                mFileInputStream = null;
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
