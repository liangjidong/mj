//package org.chudian.mj.utils;
//
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
///**
// * Created by onglchen
// * on 15-3-23.
// */
//public class OpencvUtil {
//    static
//    {
//        //System.loadLibrary("Match");
//        System.load("/home/onglchen/proenv/opencv/opencv-2.4.5/opencv-2.4.5/build/opencv_246.jar");
//    }
//
//    public static void main(String args[])
//    {
//        String baseurl = "/home/onglchen/proenv/userlib";
//        Mat img = Highgui.imread(baseurl + "B-3.JPG",0);
//        boolean flag = Highgui.imwrite(baseurl + "new.jpg",img);
//        System.out.print(flag);
//    }
//}
