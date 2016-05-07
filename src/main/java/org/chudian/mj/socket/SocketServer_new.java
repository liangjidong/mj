package org.chudian.mj.socket;

import com.alibaba.fastjson.JSONObject;
import org.chudian.mj.bean.Match;
import org.chudian.mj.bean.Picture;
import org.chudian.mj.dao.RealityTypeDao;
import org.chudian.mj.dao.ThreeDModelDaoImpl;
import org.chudian.mj.dao.VideoDaoImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by onglchen on 15-3-30.d
 */
public class SocketServer_new {
	private String uploadPath = "D:/uploadFile/";
	private ExecutorService executorService;// 线程池
	private ServerSocket ss = null;
	private int port;// 监听端口
	private boolean quit;// 是否退出


	public static int Service_Port = 80;
	public static double Time_Out = 60000;

	private Match match;

	public SocketServer_new(int port, Match match) {
		this.port = port;
		this.match = match;
		// 初始化线程池
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * 50);
	}

	// 启动服务
	public void start() throws Exception {
		ss = new ServerSocket(port);
		while (!quit) {
			Socket socket = ss.accept();// 接受客户端的请求
			// 为支持多用户并发访问，采用线程池管理每一个用户的连接请求
			executorService.execute(new SocketTask(socket, this.match));// 启动一个线程来处理请求
		}
	}

	// 退出
	public void quit() {
		this.quit = true;
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Match match = new Match();
		match.train();
		SocketServer_new server = new SocketServer_new(Service_Port, match);
		server.start();
	}

	private class SocketTask implements Runnable {
		private Socket socket;
		private Match match;
		private VideoDaoImpl videoImpl = null;
		private ThreeDModelDaoImpl modelImpl = null;
		private RealityTypeDao realityTypeDao = null;

		public SocketTask(Socket socket, Match match) {
			this.socket = socket;
			this.match = match;
			videoImpl = new VideoDaoImpl();
			modelImpl = new ThreeDModelDaoImpl();
			realityTypeDao = new RealityTypeDao();
		}

		@Override
		public void run() {
			RandomAccessFile fileOutStream = null;
			PushbackInputStream inStream = null;
			OutputStream outStream = null;
			File file = null;
			double time_now = 0;
			double time_start = 0;
			String hello = "";
			try {
				socket.setKeepAlive(true);
				inStream = new PushbackInputStream(socket.getInputStream());
				hello = "accepted connenction from " + socket.getInetAddress()
						+ " @ " + socket.getPort();
				while (true) {
					// why suspend 10 ms
					Thread.sleep(10);

					if (time_start != 0) {
						time_now = System.currentTimeMillis() - time_start;
					}
					if (time_now >= Time_Out) {
						break;
					}

					String keepon = ""; // 长连接保持字段
					keepon = StreamTool.readLine(inStream);
					if (keepon != null) {
						System.out.println("keepon = " + keepon);
						// what is 1
						if (!keepon.equals("1")) {
							break;
						}
					}

					String reload = ""; // 重新加载索引
					reload = StreamTool.readLine(inStream);
					if (reload != null) {
						if (reload.equals("1")) {
							match.reLoad_java(Match.base_url + "/index.data");
							System.out.println("reLoad index.data success");
							continue;
						}
					}

					// 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=;fileType=0
					// 0:video 1:3D Model
					// 如果用户初次上传文件，sourceid的值为空。
					String head = StreamTool.readLine(inStream);

					if (head != null) {
						long time_all_start = System.currentTimeMillis();
						System.out.println(hello + head + ";"
								+ System.currentTimeMillis());
						time_start = System.currentTimeMillis();
						// 下面从协议数据中读取各种参数值
						String[] items = head.split(";");
						String filelength = items[0].substring(items[0]
								.indexOf("=") + 1);
						String filename = items[1].substring(items[1]
								.indexOf("=") + 1);

						String timestamp = items[2].substring(items[2]
								.indexOf("=") + 1);
						Long id = System.currentTimeMillis();

						String filePath = Match.base_url + "/upload/"
								+ filename;
						file = new File(filePath);
						int position = 0;

						outStream = socket.getOutputStream();

						fileOutStream = new RandomAccessFile(file, "rwd");
						if (position == 0)
							fileOutStream
									.setLength(Integer.valueOf(filelength));// 设置文件长度
						fileOutStream.seek(position);// 移动文件指定的位置开始写入数据
						byte[] buffer = new byte[8192];
						long total_length = Integer.valueOf(filelength);
						int len = -1;
						int length = position;

						long start_transfer = System.currentTimeMillis();
						while ((len = inStream.read(buffer)) != -1) {// 从输入流中读取数据写入到文件中
							fileOutStream.write(buffer, 0, len);
							length += len;
							if (length >= total_length) {
								System.out.println("文件传输完成");
								break;
							}
						}
						long end_transfer = System.currentTimeMillis();
						long time_transfer = end_transfer - start_transfer;

						System.out.println("length=" + length);

						long start_match = System.currentTimeMillis();

						String result = this.match.match_java(filePath,
								Match.base_url + "/picFeatures", Match.base_url
										+ "/TrainDataDir");
						String[] result_arr = result.split("&&");
						int length_arr = result_arr.length;
						String videoUrl = "";
						String points = "";
						int status = -1;
						System.out.println("result_length = " + length_arr);
						/*
						 * String fileName = ""; if(length_arr > 1){ status = 1;
						 * fileName = result_arr[0]; }
						 */
						Map<String, String> result_db;
						String fileName = "";
						String pictureUrl = "";
						String webUrl = "";
						String mjProductId = "";
						String threeDUrl = "";
						String trackerUrl = "";
						String fileType = "";
						if (length_arr > 1) {
							status = 1;
							System.out.println("fileName == " + filename);
							fileName = result_arr[0];
							pictureUrl = Picture.CACHE_URL + "/" + fileName
									+ ".jpg";

							// SocketDB socketDB = new SocketDB();
							// result_db = socketDB.getVideoUrl(fileName);
							// if fileType = 0 ,get URL from video table
							System.out.println("fileName:" + fileName);
							result_db = realityTypeDao.getRealityType(fileName);
							fileType = result_db.get("realitytype");
							if (fileType.equals("0")) {
								result_db = videoImpl.getVideoUrl(fileName);
								videoUrl = result_db.get("videoUrl");
								webUrl = result_db.get("webUrl");
								mjProductId = result_db.get("mjProductId");
								System.out.println("videoUrl==" + videoUrl);
								if (videoUrl.equals("")) {
									videoUrl = "/files/llsw.mp4";
								}
							} else if (fileType.equals("1")) {
								result_db = modelImpl.getThreeDUrl(fileName);
								mjProductId = result_db.get("mjProductId"); // result_db.get("mjProductId");
								threeDUrl = result_db.get("threeDUrl");
								System.out.println("Matlab" + threeDUrl
										+ trackerUrl + mjProductId);
							}

							for (int i = 1; i < length_arr; i++) {
								points = points + result_arr[i] + "&&";

							}

							long end_match = System.currentTimeMillis();
							long time_match = end_match - start_match;
							System.out.println("match result: " + result);

						}
						double time_all = System.currentTimeMillis()
								- time_all_start;
						String final_reponse = "status=" + status + ";"
								+ "points=" + points + ";" + "videourl="
								+ videoUrl + ";" + "fileName=" + fileName + ";"
								+ "pictureUrl=" + pictureUrl + "\r\n";
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("status", status);
						jsonObject.put("points", points);
						if (fileType.equals("0")) {
							jsonObject.put("videoUrl", videoUrl);
							jsonObject.put("pictype", 0);
							jsonObject.put("status", status);
							jsonObject.put("points", points);
							jsonObject.put("fileName", fileName);
							jsonObject.put("pictureUrl", pictureUrl);
							jsonObject.put("webUrl", webUrl);
							jsonObject.put("mjProductId", mjProductId);
							String json_result = jsonObject.toJSONString()
									+ "\r\n";
							System.out.println("json:==" + json_result);
							System.out.println("normal:==" + final_reponse);
							outStream.write(json_result.getBytes());
						}
						if (fileType.equals("1")) {
							jsonObject.put("threeDUrl", threeDUrl);
							String str = null;
							try {
								str = fileName.substring(0,
										fileName.indexOf("."));
								jsonObject.put("trackerUrl", "/mj/tracker/"
										+ str + ".zip");
								jsonObject.put("pictype", 1);
								jsonObject.put("fileName", fileName);
								jsonObject.put("pictureUrl", pictureUrl);
								jsonObject.put("webUrl", webUrl);
								jsonObject.put("mjProductId", mjProductId);
								String json_result = jsonObject.toJSONString()
										+ "\r\n";
								System.out.println("json:==" + json_result);
								System.out.println("normal:==" + final_reponse);
								outStream.write(json_result.getBytes());
							} catch (Exception e) {
								jsonObject.put("trackerUrl", "");
								jsonObject.put("pictype", 1);
								jsonObject.put("fileName", "");
								jsonObject.put("pictureUrl", "");
								jsonObject.put("webUrl", "");
								jsonObject.put("mjProductId", mjProductId);
								String json_result = jsonObject.toJSONString()
										+ "\r\n";
								System.out.println("json:==" + json_result);
								System.out.println("normal:==" + final_reponse);
								outStream.write(json_result.getBytes());
								e.printStackTrace();
							}
						}
						System.out.println(fileName);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null && !socket.isClosed())
						socket.close();
					if (fileOutStream != null)
						fileOutStream.close();
					if (inStream != null)
						inStream.close();
					if (outStream != null)
						outStream.close();
					file = null;
				} catch (IOException e) {
				}
			}

		}
	}
}
