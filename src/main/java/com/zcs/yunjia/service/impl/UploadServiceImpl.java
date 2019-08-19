package com.zcs.yunjia.service.impl;


import com.zcs.yunjia.pojo.UploadResult;
import com.zcs.yunjia.utils.FastDFSUtil2;
import com.zcs.yunjia.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {

	@Value("${PIC_PREFIX}")
	private String PIC_PREFIX;
	
	@Override
	public UploadResult upload(byte[] bytes,String extName) {
		String url = "";
		UploadResult result = new UploadResult();
		try {
			FastDFSUtil2 my = new FastDFSUtil2();
			url = my.uploadFile("resources/resource.conf", bytes,extName);
		} catch (Exception e) {
			result.setError(1);
			result.setMessage("上传失败");
			e.printStackTrace();
			return result;
		}
		result.setError(0);
		result.setUrl(PIC_PREFIX+url);
		System.out.println("pic_url:"+PIC_PREFIX+url);
		return result;
	}

}
