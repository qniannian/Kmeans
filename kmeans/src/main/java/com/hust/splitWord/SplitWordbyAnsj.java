package com.hust.splitWord;

import java.util.ArrayList;
import java.util.List;
import org.ansj.domain.Result;
import org.ansj.library.DicLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.DicAnalysis;

import com.hust.util.Config;
import com.hust.util.ReadTxt;

public class SplitWordbyAnsj{
	
	/* 添加用户自定义词典
		static {
			List<String> list = new ReadTxt().readtxt(Config.userWordPath);
			for (String str : list) {
				DicLibrary.insert(DicLibrary.DEFAULT, str);
			}

		}*/
	
	//读取停用词表
	private StopRecognition loadingStopWord()
	{
		//读取停用词表
		List<String> Stopwordlist = new ReadTxt().readtxt(Config.StopwordPath);
		StopRecognition stopRecognition= new StopRecognition();
		stopRecognition.insertStopWords(Stopwordlist);
		System.out.println(Stopwordlist.size()+"************");
		return stopRecognition;	
		
	}
	
	/**
	 * 
	 * @param 分词前的数据
	 * @return 每一行为一个List<String>
	 */
	public List<List<String>> getSplitlist(List<String> word)
	{
		
		List<List<String>> splitlist = new ArrayList<List<String>>();
	     
		 for (String string : word)
		 {
			 List<String> list =new ArrayList<String>();
			 //用于存储分词的结果
			 Result result = new Result(null);
			 // 用用户自定义优先分词模式得到一个未过滤的记过
			 result = DicAnalysis.parse(string);
			 //过滤停用词
			 result = result.recognition(loadingStopWord());
			// 将result去掉词性后（toStringWithOutNature）按（","）切分为一个一个的单词
			 String[] aStrings=result.toStringWithOutNature().split(",");
			 for(int i=0; i < aStrings.length; i++)
			 {
				 list.add(aStrings[i]);
			 }
			 splitlist.add(list);		 
		 }
	
		 return splitlist;		
	 }	
	

	
}
		
		 
	   
