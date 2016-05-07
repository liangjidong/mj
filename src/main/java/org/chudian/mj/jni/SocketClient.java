package org.chudian.mj.jni;

/**
 * Created by onglchen on 3/1/16.
 */
public class SocketClient {

    static {
        System.load("/home/onglchen/proenv/userlib/socketClientC.so");
    }
    public native String Upload(byte[] jpgbyte,int length);
    public native void Init();
    public native void Release();


}

