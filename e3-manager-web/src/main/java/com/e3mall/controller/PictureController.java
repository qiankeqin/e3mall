package com.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.e3mall.common.utils.FastDFSClient;
import com.e3mall.common.utils.JsonUtils;

/**
 * 图片上传处理Controller
 * @author qiankeqin
 *
 */
@Controller
public class PictureController {
	
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		Map result = null;
		try{
			//把图片上传到图片服务器
			FastDFSClient fastDfsClient = new FastDFSClient("classpath:conf/client.conf");
			//获取到图片的地址和文件名
			String originalName = uploadFile.getOriginalFilename();
			String url = fastDfsClient.uploadFile(uploadFile.getBytes(), originalName.substring(originalName.lastIndexOf(".")+1));
			//补充为完整的url
			url = IMAGE_SERVER_URL+url;
			//封装到map中返回
			result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch(Exception ex) {
			result.put("error", 0);
			result.put("message", "图片上传失败！");
			return JsonUtils.objectToJson(result);
		}
	}
}
