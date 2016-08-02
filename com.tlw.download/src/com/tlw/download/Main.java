package com.tlw.download;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author tlw_ray@163.com
 * @science 2016年7月31日
 */
public class Main {

	public static void main(String[] args){
		String cmdLineSyntax = "download";
		
		String urlKey = "url";
		Option urlOption = new Option(urlKey, "Download file's URL.");
		urlOption.setArgName(urlKey);
		urlOption.setArgs(1);
		urlOption.setRequired(true);
		
		String taskKey = "task";
		Option taskOption = new Option(taskKey, "Download thread count.");
		taskOption.setArgName(taskKey);
		taskOption.setRequired(false);
		taskOption.setArgs(1);
		taskOption.setType(Integer.class);
		
		String pathKey = "path";
		Option pathOption = new Option(pathKey, "Download file to path.");
		pathOption.setArgName(pathKey);
		pathOption.setRequired(false);
		pathOption.setArgs(1);
		
		Options options = new Options();
		options.addOption(urlOption);
		options.addOption(taskOption);
		options.addOption(pathOption);
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			
			//URL not null and start with http://
			String urlStr = cmd.getOptionValue(urlKey);
			if(urlStr == null || urlStr.trim().length()==0){
				System.out.println("Parameter -url not null.");
				return;
			}else if(!urlStr.toLowerCase().startsWith("http://")){
				System.out.println("Parameter -url must start with 'http://'.");
				return;
			}
			
			//parse task to integer and 0<task<256
			String taskCountStr = cmd.getOptionValue(taskKey, "5");
			int taskCount = 5;
			try{
				taskCount = Integer.parseUnsignedInt(taskCountStr);
				if(taskCount > 255){
					taskCount = 255;
				}
			}catch(Exception e){
				System.out.println("Miss '-task " + taskCountStr + "', use default '-task 5'.");
				return;
			}
			String path = cmd.getOptionValue(pathKey, "");
			File pathFile = new File(path);
			try{
				URL url=new URL(urlStr);
				Task task = new Task(url, pathFile, taskCount);
				task.start();
			}catch(Exception ex){
				System.out.println("Http get " + urlStr + " fail: " + ex.getMessage());
				return;
			}
			
		} catch (ParseException e) {
			printUsage(cmdLineSyntax, options);
			System.out.println(e.getMessage());
		}
	}

	private static void printUsage(String cmdLineSyntax, Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(cmdLineSyntax, options);
		System.out.println("\n");
		System.out.println("example:\n");
		System.out.println("\tdownload -url http://www.softwares.com/some.zip\n");
	}

}
