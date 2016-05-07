package org.chudian.mj.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/uploadController")
public class UploadFileController {
	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		System.out.println("上传文件正在进行.............");
		System.out.println("file.getName():" + file.getName());
		System.out.println("file.getOriginalFilename():"
				+ file.getOriginalFilename());
		System.out.println("file.getSize():" + file.getSize());
		System.out.println("file.getClass():" + file.getClass());
		System.out.println("file.isEmpty():" + file.isEmpty() + "");
		System.out.println("file.getContentType():" + file.getContentType());

		String path = request.getSession().getServletContext()
				.getRealPath("/upload");
		String filename = file.getOriginalFilename();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = format.format(calendar.getTime());

		System.out.println(filename + date);

		File targetFile = new File(path, filename + date);

		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("上传文件失败");
			return "failed";
		}

		// FileCopyUtils.copy(file.getBytes(),
		// new File(path + "/" + file.getOriginalFilename()));

		System.out.println("上传文件成功");
		return "ok";
	}
}
