package org.chudian.mj.jni;

/**
 * Created by onglchen on 11/20/15.
 */
public class GenAssert {
    //input 为输入图片路径 如/home/99.jpg
    //outpath为生成文件保存路径 如/home/yan/88.assert
    //name为什么，貌似
    public static native int Train(String input,String outpath,String name);
    static {
        System.load("/home/onglchen/proenv/userlib/CODELIB/GenAssert/libGenAssert.so");
    }

    public static void main(String args[]){
        System.out.println("TTTTT");
        String src=new String("/home/onglchen/proenv/userlib/20150801150012PM.jpg.jpg");
        String dir=new String("/home/onglchen/proenv/userlib/xxxxx.asset");
        String name=new String("shot");
        int a=GenAssert.Train(src,dir,name);
        System.out.println(a);
        System.out.println("EEEEEE");
    }

}