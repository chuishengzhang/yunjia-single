package com.zcs.yunjia.service;



import com.zcs.yunjia.pojo.UploadResult;

public interface UploadService {
	//上传文件
	public UploadResult upload(byte[] bytes, String extName);
}
