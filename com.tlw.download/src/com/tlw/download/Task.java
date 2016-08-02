package com.tlw.download;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author tlw_ray@163.com
 * @since 2016-08-01
 * File download task.
 */

@XmlRootElement
public class Task extends Thread {
	
	private Logger log = Logger.getLogger(getClass().getName());

	@XmlAttribute
	protected URL url;
	@XmlAttribute
	protected File directory;
	@XmlElement
	protected Splitter[] fileSplitterFetch;

	transient protected DataOutputStream output;
	
	public Task(URL url, File directory, int splitCount) throws IOException {
		this.url = url;
		this.fileSplitterFetch = new Splitter[splitCount];
		if(directory == null){
			directory = new File("");
		}else{
			this.directory = directory;
		}
	}

	public void run() {
		try {

			FileInfo info = getFileInfo();

			if (info.getHttpStatus() >= HttpURLConnection.HTTP_BAD_REQUEST) {
				
				log.severe("Download fail, http status " + info.getHttpStatus() + ".");
				
			} else {
				directory.mkdirs();
				File file = new File(directory.getAbsolutePath() + File.separator + info.getFileName());
				log.info(file.getAbsolutePath());
				
				if(info.getLength() > 0 ){
					long avgLength = info.getLength() / fileSplitterFetch.length;
					long startPosition = 0l;
					long endPosition = 0l;
					
					for (int i = 0; i < fileSplitterFetch.length; i++) {
						startPosition = endPosition;
						endPosition = startPosition + avgLength;
						
						fileSplitterFetch[i] = new Splitter(url, file, startPosition, endPosition - 1);
					}
					fileSplitterFetch[fileSplitterFetch.length - 1].endPosition = info.getLength();

					for (Splitter splitterFetch : fileSplitterFetch) {
						splitterFetch.start();
					}

					for (Splitter splitterFetch : fileSplitterFetch) {
						splitterFetch.join();
					}
					log.info("download success...");
				}else{
					log.severe("Download fail, content length: " + info.getLength());
				}
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "Download fail: " + e.getMessage());
		}
	}

	public FileInfo getFileInfo() throws IOException {
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

		assert HttpURLConnection.getFollowRedirects() == true;

		httpConnection.setRequestProperty("User-Agent", "NetFox");
		int responseCode = httpConnection.getResponseCode();
		String encoding = httpConnection.getContentEncoding();
		if(encoding == null){
			encoding = "UTF-8";
		}
		String urlRedirected = URLDecoder.decode(httpConnection.getURL().toString(), encoding);
		String fileName = urlRedirected.substring(urlRedirected.lastIndexOf("/") + 1);

		if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
			return new FileInfo(responseCode);
		}else{
			int contentLength = httpConnection.getHeaderFieldInt("Content-Length", 0);
			return new FileInfo(contentLength, responseCode, fileName);
		}

		// for(Map.Entry<String, List<String>>
		// entry:httpConnection.getHeaderFields().entrySet()){
		// System.out.print(entry.getKey()+"\t:");
		// for(String value:entry.getValue()){
		// System.out.println("\t\t\t"+value);
		// }
		// }
	}

}
