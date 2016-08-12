package com.tlw.download;

import java.io.File;
import java.net.URL;

import com.tlw.download.Task;

public class TestDownload {
	
	public static void main(String[] args) {
		new TestDownload();
	}
	
	public TestDownload() {
		try {
//			String urlPath="http://downloads.sourceforge.net/project/hibernate/hibernate-ogm/4.2.0.Final/hibernate-ogm-4.2.0.Final-dist.zip?r=&ts=1446676490&use_mirror=nchc";
//			String urlPath="https://github.com/querydsl/querydsl/archive/master.zip";
//			String urlPath="http://127.0.0.1:8080/antlr.jar";
//			String urlPath="http://download.oracle.com/otn-pub/java/jdk/8u66-b17/jdk-8u66-windows-x64.exe";
//			String urlPath="https://edelivery.oracle.com/otn-pub/java/jdk/8u66-b17/jdk-8u66-windows-x64.exe";
//			String urlPath="http://download.oracle.com/otn-pub/java/jdk/8u66-b17/jdk-8u66-windows-x64.exe?AuthParam=1447247622_251b39f450094a407ecc8c61345eb023";
//			String urlPath = "https://s3.amazonaws.com/jruby.org/downloads/9.0.5.0/jruby-complete-9.0.5.0.jar";
			String urlPath = "http://plugins.jetbrains.com/plugin/download?pr=&updateId=27110";
			URL url=new URL(urlPath);
			Task fileFetch = new Task(url, new File("D:"), 5);
			fileFetch.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}