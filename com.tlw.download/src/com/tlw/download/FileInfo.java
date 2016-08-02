package com.tlw.download;
/**
 * @author tlw_ray@163.com
 * @science 2016年8月1日
 * Download file information POJO.
 */
public class FileInfo {

	private long length;
	private int httpStatus;
	private String fileName;
	
	public FileInfo(int httpStatus) {
		super();
		this.httpStatus = httpStatus;
	}

	public FileInfo(long length, int httpStatus, String fileName) {
		super();
		this.length = length;
		this.httpStatus = httpStatus;
		this.fileName = fileName;
	}

	public long getLength() {
		return length;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getFileName() {
		return fileName;
	}
	
}
