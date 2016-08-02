package com.tlw.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author tlw_ray@163.com
 * @since 2016-8-1
 * Split download task
 */

@XmlRootElement
public class Splitter extends Thread {
	
	private Logger log = Logger.getLogger(getClass().getName());
	
	@XmlAttribute
	protected long startPosition; 								// File Snippet Start Position
	@XmlAttribute
	protected long endPosition; 								// File Snippet End Position
	
	transient protected URL url; 								// File URL
	transient protected RandomAccessFile fileAccess = null; 	// File Access interface

	public Splitter(URL url, File file, long start, long end) throws IOException {
		this.url = url;
		this.startPosition = start;
		this.endPosition = end;
		this.fileAccess = new RandomAccessFile(file, "rw");
		this.fileAccess.seek(start);
	}

	public void run() {
		while (startPosition < endPosition) {
			try {
				log.info(getName() + " download from: " + startPosition + "\tto " + endPosition);
				
				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("User-Agent", "NetFox");
				String sProperty = "bytes=" + startPosition + "-" + endPosition;
				httpConnection.setRequestProperty("RANGE", sProperty);
				InputStream input = httpConnection.getInputStream();
				byte[] buffer = new byte[1024];
				int readLength;
				while ((readLength = input.read(buffer, 0, 1024)) > 0 && startPosition < endPosition) {
					startPosition += write(buffer, 0, readLength);
				}
				log.info(getName() + " is over!");
			} catch (Exception e) {
				log.log(Level.WARNING, getName() + " exception: " + e.getMessage());
			}
		}
	}

	private synchronized int write(byte[] data, int offset, int length) {
		int n = 0;
		try {
			fileAccess.write(data, offset, length);
			n = length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return n;
	}
}
