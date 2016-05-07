package org.chudian.mj.utils;

import org.chudian.mj.common.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by onglchen on 5/24/15.
 */
public class FileUtil {
	private static String picture_fomate = "jpeg;gif;png;jpg;bmp";
	private static String video_fomate = "mp4;avi;flv;rmvb;3gp;rm;wma;mid;flash";
	public static int FOMATE_PICTURE = 2, FOMATE_VIDEO = 4, FOMATE_EERO = 6;

	public static String getExtensions(String fileName) {
		String extention = "";
		int i = fileName.lastIndexOf(".");
		if (i > -1 && i < fileName.length()) {
			extention = fileName.substring(i + 1); // --扩展名
		}
		return extention;
	}
	public static String getNameWithoutExtensions(String fileName) {
		String name = "";
		int i = fileName.indexOf(".");
		if (i > -1 && i < fileName.length()) {
			name = fileName.substring(0, i ); // --扩展名
		}
		return name;
	}

	public static boolean isPicture(String fileName) {
		boolean status = false;
		String extention = getExtensions(fileName);
		String[] fomate = picture_fomate.split(";");
		for (int i = 0; i < fomate.length; i++) {
			if (extention.compareToIgnoreCase(fomate[i]) == 0) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public static boolean isVideo(String fileName) {
		boolean status = false;
		String extention = getExtensions(fileName);
		String[] fomate = video_fomate.split(";");
		for (int i = 0; i < fomate.length; i++) {
			if (extention.compareToIgnoreCase(fomate[i]) == 0) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public static int judgeFileType(String fileName) {
		if (isPicture(fileName)) {
			return FOMATE_PICTURE;
		} else if (isVideo(fileName)) {
			return FOMATE_VIDEO;
		} else {
			return FOMATE_EERO;
		}
	}

	public static String getBackPath(String filePath) {
		String temp[] = filePath.replaceAll("\\\\", "/").split("/");
		int length = temp.length;
		String result[] = new String[length];
		String restult2 = "";
		for (int i = 0; i < length - 1; i++) {
			result[i] = temp[i];
			restult2 += "/" + temp[i];
		}
		System.out.println(restult2);
		return restult2;
	}

	public static String getFileName(String fileName) {
		String[] result = fileName.split("\\.");
		String result2 = result[0];
		return result2;
	}

	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath); 
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			oldfile.delete();
		} catch (Exception e) {
			System.out.println("error  ");
			e.printStackTrace();
		}
	}

	public static boolean move(String srcFile, String destPath) {
		// File (or directory) to be moved
		File file = new File(srcFile);

		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));

		return success;
	}

	public static void fileMove(String from, String to) throws Exception {// 移动指定文件夹内的全部文件
		try {
			File dir = new File(from);
			File[] files = dir.listFiles();// 将文件或文件夹放入文件集
			if (files == null)// 判断文件集是否为空
				return;
			File moveDir = new File(to);// 创建目标目录
			if (!moveDir.exists()) {// 判断目标目录是否存在
				moveDir.mkdirs();// 不存在则创建
			}
			for (int i = 0; i < files.length; i++) {// 遍历文件集
				if (files[i].isDirectory()) {// 如果是文件夹或目录,则递归调用fileMove方法，直到获得目录下的文件
					fileMove(files[i].getPath(), to + "\\" + files[i].getName());// 递归移动文件
					files[i].delete();// 删除文件所在原目录
				}
				File moveFile = new File(moveDir.getPath() + "\\"// 将文件目录放入移动后的目录
						+ files[i].getName());
				if (moveFile.exists()) {// 目标文件夹下存在的话，删除
					moveFile.delete();
				}
				files[i].renameTo(moveFile);// 移动文件
				System.out.println(files[i] + " 移动成功");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static int renameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {
			File oldFile = new File(path + "/" + oldname);
			File newFile = new File(path + "/" + newname);
			if (newFile.exists()) {
				return Status.EXISTS;
			} else {
				oldFile.renameTo(newFile);
				return Status.SUCCESS;
			}
		}
		return Status.ERROR;
	}

	public static String FormetFileSize(int fileSize) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static void deleteFile(String fileUrl) {
		File file = new File(fileUrl);
		if (file.exists() && file.isFile() || file.isDirectory()) {
			file.delete();
		}
	}

	public static String rename(String fileName) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		String extention = "";
		int i = fileName.lastIndexOf(".");
		if (i > -1 && i < fileName.length()) {
			extention = fileName.substring(i + 1); // --扩展名
		}
		return f.format(c.getTime()) + "." + extention;
	}


	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
