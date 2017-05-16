package com.hust.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sun.nio.cs.ext.GBK;

public class ReadTxt {
	
	/**
	 * 
	 * @param filePath
	 * @return List<String>,每一行都是一个节点
	 */
	public List<String> readtxt(String filePath)
	{
		List<String> word = new ArrayList<String>();
		try {
			File file = new File(filePath);
			if(file.isFile()&&file.exists())
			{
				
					InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"GBK");
					BufferedReader read = new BufferedReader(reader);
					String line ;
					while((line=read.readLine())!=null)
					{
						word.add(line);
					}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return word;
		
	}
	
}
