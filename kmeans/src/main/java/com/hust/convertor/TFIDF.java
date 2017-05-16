package com.hust.convertor;

import java.util.ArrayList;
import java.util.List;

import com.hust.splitWord.SplitWordbyAnsj;
import com.hust.util.ReadTxt;

public class TFIDF {
	
	
	/**
	 * 判断s是否与list中的的某个String相同
	 * @param s 某个单词
	 * @param list   列
	 * @return
	 */
	
	//判断
	private boolean IsRepeat(String s,List<String> list)
	{
		boolean r = false; //表示不重复
		for(String a:list)
		{
			if(a.equals(s))
			{
				r = true; //重复
			}
		}
		return r;
	}
	
	//把所有文档中的词语，去掉重复的。形成一个新的链表
	private List<String> getNoRepeat(List<List<String>> splitlist)
	{
		List<String> allSplitlist =  new ArrayList<String>(); //用于存储去掉重复后的词语组
		for(List<String> list:splitlist)
		{
			for(String s:list)
			{
				if(IsRepeat(s,allSplitlist)==false)//表示要添加的与链表中已有的词语不重复
				{
					allSplitlist.add(s);
				}
			}
		}
		return allSplitlist;
		
	}
	 
	
	//计算包含词语A的文件的个数
	private int WordNumberOfAll(String word,List<List<String>> splitlist)
	{
		if(word == null)
		{
			return 0;
		}
		int num = 0;
		for(List<String> list:splitlist)
		{
			for(String s:list)
			{
				if(word.equals(s))
				{
					num++;
					break;
				}
			}
		}
		return num;
	}
	
	//计算词语A在某篇文章中出现的次数
	private int WordNumberOfArtical(String word, List<String> words)
	{
		if(word == null || words.size() == 0 || words == null)
		{
			return 0;
		}
		int num = 0;
		for(String freWord:words)
		{
			if(word.equals(freWord))
			{
				num++;
			}
		}
		return num;
	}
	/**
	 * 
	 * @param splitlist  分词后的结果
	 * @return
	 */
	public List<double[]> getTFIDF(List<List<String>> splitlist)
	{
		 double tf,idf,tfidf; 
		  List<double[]> TFIDF = new ArrayList<double[]>();  //用于存储TF-IDF的结果
		  List<String> l = getNoRepeat(splitlist);  //不重复的链
		  for(List<String> list:splitlist)
		  {
			   double[] weight = new double[l.size()];
			   for(String word:l)
			   {
				   tf  = (double)WordNumberOfArtical(word,list)/list.size();
				   idf = Math.log10((double)splitlist.size()/(WordNumberOfAll(word,splitlist)+1));
				   tfidf = tf*idf;
				   int i= l.indexOf(word);
				   weight[l.indexOf(word)] = tfidf; 
			   }
			   TFIDF.add(weight);
		  }
		  return TFIDF;
	}
	
	public static void main(String args[])
	{
		ReadTxt readTxt = new ReadTxt();
		List<String> word = new ArrayList<String>(readTxt.readtxt("f://111.txt"));
		SplitWordbyAnsj teSplitWordbyAnsj= new SplitWordbyAnsj();
		List<List<String>> splitlist = new ArrayList<List<String>>();
		splitlist = teSplitWordbyAnsj.getSplitlist(word);
		//输出分词
		for (List<String> list : splitlist) {
			for (String string : list) {
				System.out.print(string+" ,");
			}
			System.out.println();
		}
		
		TFIDF tftest = new TFIDF();
		List<double[]> tfidf = new ArrayList<double[]>();
		tfidf=tftest.getTFIDF(splitlist);
		//输出tfidf
		for (double[] ds : tfidf) {
			for(int i=0; i < ds.length; i++)
			{
				System.out.print(ds[i]+" ,");
			}
			System.out.println();
		}
		
	}
	
	

}
