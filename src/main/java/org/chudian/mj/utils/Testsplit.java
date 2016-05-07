package org.chudian.mj.utils;

/**
 * Created by onglchen
 * on 15-3-21.
 */
public class Testsplit {

    public static void main(String args[]) {
        String filePath = "2.jpg";
        String str=filePath.substring(0, filePath.indexOf("."));
        System.out.println(str);
//        String temp[] = filePath.replaceAll("\\\\","/").split("/");
//        String fileName = "";
//        if(temp.length > 1){
//            fileName = temp[temp.length - 1];
//            }
//        System.out.println("filename= "+ fileName);
}

    public String getBackPath(String filePath){
        String temp[] = filePath.replaceAll("\\\\","/").split("/");
        int length = temp.length;
        String result[] = new String[length];
        String restult2 = "";
        for(int i = 1; i<length-1; i++){
                result[i] = temp[i];
                restult2 += "/" + temp[i];
            System.out.println("restulet==" + restult2);
        }
        System.out.println("restulet==" + restult2);
        return restult2;
    }
    public String getFileName(String filePath){
        String[] result1 = filePath.replaceAll("\\\\","/").split("/");
        int lenght = result1.length;
        String fileName = result1[lenght-1];
        System.out.println(fileName);
        String[] result = fileName.split("\\.");
        String result2 = result[0];
        return result2;
    }
    }