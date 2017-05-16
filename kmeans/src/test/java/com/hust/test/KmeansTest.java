package com.hust.test;

import java.util.ArrayList;
import java.util.List;

import com.hust.Kmeans;
import com.hust.convertor.TFIDF;
import com.hust.splitWord.SplitWordbyAnsj;
import com.hust.util.ReadTxt;

public class KmeansTest {
	
	 public static void main(String[] args)
	    { 
			
	    	ReadTxt readTxt = new ReadTxt();
			List<String> word = new ArrayList<String>(readTxt.readtxt("f://test.txt"));
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
			
			Kmeans kmeans = new Kmeans();
			System.out.println("********");
			kmeans.setTFIDF(tfidf);
			kmeans.setIterationTimes(20);
			kmeans.setK(5);
			try {
				kmeans.clustering();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		  List<List<Integer>> resultlist = kmeans.getResultIndex();
		  
		  for(List<Integer> set: resultlist)
		  {
			  for(int index : set)
			  {
				  System.out.println(word.get(index));
			  }
			  System.out.println();
		  }
			
		}  

}
