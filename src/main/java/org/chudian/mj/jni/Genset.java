package org.chudian.mj.jni;

import org.chudian.mj.utils.FileUtil;

import java.io.File;

public class Genset {
	public static Genset ar;
	public static Genset getInstance(){
		if(ar==null)
			ar=new Genset();
			ar.setDpi(96);
			ar.setMaxDpi(96);
			ar.setMinDpi(96);
			ar.setLevel(2);
			ar.setLevelI(1);
		return ar;
	}
	public native void setDpi(float dpi);
	public native void setLevel(int level);
	public native void setLevelI(int leveli);
	public native void setMaxDpi(float maxdpi);
	public native void setMinDpi(float mindpi);
	public native void setPath(String input,String output);
	public native String getOutPath();
	public native String getFlieName();
	static {
		System.load("/home/onglchen/proenv/userlib/libgenset.so");
	}

	public  void pictureGenset(String src, String dir){
		String backHomePath = FileUtil.getBackPath(dir);
		File fdir = new File(backHomePath);
		if(!fdir.exists()){
			fdir.mkdir();
		}
		ar.setPath(src, dir);

		System.out.println(ar.getOutPath());
		System.out.println(ar.getFlieName());
	}
	public static void main(String args[]){
		String src=new String("/home/onglchen/temp/chanise.png");
		String dir=new String("/home/onglchen/temp/test/");
		File fdir = new File(dir);
		if(!fdir.exists()){
			fdir.mkdir();
		}
		Genset s=new Genset();
		s.setDpi(96);
		s.setMaxDpi(96);
		s.setMinDpi(96);
		s.setLevel(2);
		s.setLevelI(1);
		s.setPath(src,dir);
		System.out.println(Genset.getInstance().getOutPath());
		System.out.println(s.getFlieName());
	}

}
