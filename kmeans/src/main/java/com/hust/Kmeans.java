package com.hust;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.omg.CORBA.INTERNAL;

import com.hust.convertor.TFIDF;
import com.hust.diatance.EuclideanMetric;
import com.hust.splitWord.SplitWordbyAnsj;
import com.hust.util.ReadTxt;

public class Kmeans {

	private int K;
	private List<double[]> centerPoints; //簇中心 ，double[]存的是向量
	private List<double[]> oldCenterPoints; //之前的簇中心
	private int iterationTimes = 10; //迭代次数
	private List<Integer> centerIndexs;
	private List<double[]> TFIDF;
	
	
    private List<List<Integer>> resultIndex;  //存储聚类的结果
	
	public List<double[]> getTFIDF() {
		return TFIDF;
	}
	public void setTFIDF(List<double[]> tFIDF) {
		TFIDF = tFIDF;
		
	}
	public int getK() {
		return K;
	}
	public void setK(int k) {
		K = k;
	}
	public List<double[]> getCenterPoints() {
		return centerPoints;
	}
	public void setCenterPoints(List<double[]> centerPoints) {
		this.centerPoints = centerPoints;
	}
	public List<double[]> getOldCenterPoints() {
		return oldCenterPoints;
	}
	public void setOldCenterPoints(List<double[]> oldCenterPoints) {
		this.oldCenterPoints = oldCenterPoints;
	}
	public int getIterationTimes() {
		return iterationTimes;
	}
	public void setIterationTimes(int iterationTimes) {
		this.iterationTimes = iterationTimes;
	}
	
	public List<List<Integer>> getResultIndex() {
		return resultIndex;
	}
	public void setResultIndex(List<List<Integer>> resultIndex) {
		this.resultIndex = resultIndex;
	}
	
	//初始化，List<Interger>代表一个类
	private void initResultIndex()
	{
		resultIndex = new ArrayList<List<Integer>>();
        for (int i = 0; i < K; i++) {
            List<Integer> setIndex = new ArrayList<Integer>();
            resultIndex.add(setIndex);
        }
        centerIndexs = new ArrayList<Integer>(); //初始化
	}
	
	
	//把簇中心加入到结果索引中
	private void AddCenterToIndex()
	{
		for(int i=0; i<resultIndex.size();i++)
		{
			List<Integer> list=resultIndex.get(i);
			list.add(centerIndexs.get(i));
		}
	}
	
	//返回向量的索引
	private int FindIndex(double[] vector)
	{
		int r = 0;
		for(int i=0; i<TFIDF.size();i++)
		{
			if (vector.equals(TFIDF.get(i))) {
				r=i;
				
			}
		}
		return r;
	}
	
	 //随机生成K个初始簇中心
	 private void RandomCenterPoints() throws Exception 
	 {
		 if(K >= TFIDF.size())
		 {
			 throw new IllegalArgumentException("K 的值必须小于元素的个数");
		 } 
		 
	     centerPoints = new ArrayList<double[]>();
	     Random randomMaker = new Random();
         for (int i = 0; i < K;) {
            int random = randomMaker.nextInt(TFIDF.size());
            if (centerIndexs.contains(random)==false)
            {
                centerIndexs.add(random);
                centerPoints.add(TFIDF.get(random));
                i++;
                
            } 
           
        }
      
         
	 }
	 
	 //计算每一行与簇中心的距离，此处用的为欧式距离

	        	
	    
	    //将每一行依次加入 距她最近的簇
	    private void addvectors(int index) 
	    {
	    	EuclideanMetric euclideanMetric = new EuclideanMetric();
	        double maxsim = 9999;  //最大距离
	        int minset = -1;
	        double[] vector = TFIDF.get(index); //获取第index的向量
	        for (int i = 0; i < K; i++) {  //遍历簇中心
	            double[] centerPoint = centerPoints.get(i);  //簇中心
	            double sim = euclideanMetric.calculate(vector, centerPoint);
	            if (sim < maxsim) { //若距离比maxsim小，则minset=i
	                maxsim = sim;
	                minset = i;
	            }
	        }
	        if (minset == -1) {
	            return;
	        }
	        List<Integer> setIndex = resultIndex.get(minset);
	        setIndex.add(index);
	    }
	    
	    //更新簇中心
	    private void updateCenterPoints() {
	    	centerIndexs.clear();//清空簇中心索引
	        oldCenterPoints = new ArrayList<double[]>(centerPoints);  
	        for (int i = 0; i < K; i++) {
	            List<Integer> setIndex = resultIndex.get(i);
	            double[] newCenterPoint = findNewCenter(setIndex);
	            int j = FindIndex(newCenterPoint);
	            centerIndexs.add(j); //把新的中心加入索引中
	            centerPoints.set(i, newCenterPoint);
	        }
	    }
	    
	    private double[] getShort(double a[], List<Integer> setIndex)
	    {
	    	EuclideanMetric euclideanMetric = new EuclideanMetric();
	    	double mix = 999;
	    	int r = 0;
	    	for(int i = 0; i < setIndex.size();i++)
	    	{
	    		double[] c = TFIDF.get(setIndex.get(i));
	            double sim = euclideanMetric.calculate(a, c);
	            if(mix > sim)
	            {
	            	mix = sim;
	            	r = i;
	            }
	    	}
	    	return TFIDF.get(setIndex.get(r));
	    }
	    
	    //找到新的聚类中心
	    private double[] findNewCenter(List<Integer> setIndex)
	    {
	        int length = 0;
	        double[] centerPoint = new double[TFIDF.get(0).length]; //用于存储新的中心
	        for (int i = 0; i < setIndex.size(); i++) {
	            for (int j = 0; j < TFIDF.get(0).length; j++)
	            {
	            	
	            	centerPoint[j] += TFIDF.get(setIndex.get(i))[j];
	            	
	            }
	        }
	        for (int i = 0; i < length; i++) {
	            centerPoint[i] /= setIndex.size();
	        }
	        //返回当前类簇中离运用平均法算出的中心，最近的那个。做为新的中心！
	        centerPoint = getShort(centerPoint, setIndex); 
	        return centerPoint;
	    }
	   
	    
	    private void clear() {
	        for (int i = 0; i < K; i++) {
	            resultIndex.get(i).clear();
	        }
	    }
	    
	    
	    private boolean isContinue(int time) {
	        boolean centerChanged = false; //中心是否发生改变
	        boolean overtime = false;  //是否超过规定次数
	        if (time != 0) {
	            for (int i = 0; i < K; i++) 
	            {
	                if (oldCenterPoints.get(i) != centerPoints.get(i)) 
	                {
	                    centerChanged = true;
	                }
	            }
	        } 
	        else 
	        {
	            centerChanged = true;
	        }
	        if (time > iterationTimes) { //如果次数大于规定的次数
	            overtime = true;
	        }
	        if (centerChanged == false || overtime == true) {
	            return false;
	        }
	        return true;
	    }
	    
	    //判断i是否在索引中心
	    private boolean IsExsit(int n) 
	    {
	    	boolean result = false;
	    	for (Integer index : centerIndexs) 
	    	{
				if (index.equals(n))
				{
					result = true;
				}
			}
	    	return result;
	    }
	    
	    public void clustering() throws Exception {
	        if (null == TFIDF || TFIDF.size() == 0) {
	            throw new IllegalArgumentException("must init vectors before clustering");
	        }
	        initResultIndex();
	        RandomCenterPoints();
	        int time = 0;
	        while (isContinue(time)) {
	            time++;
	            clear();
	            AddCenterToIndex(); //把簇中心加入到结果索引中
	            for (int i = 0; i < TFIDF.size(); i++) {
	            	if (IsExsit(i)==false) { //若不是中心
	            		  addvectors(i);  
					}
	            }
	            System.out.println("第"+time+"次循环的结果是：");
	            for (List<Integer> set : resultIndex) {
		            for (int index : set) 
		            {
		                System.out.print(index +"  ");
		            }
		            System.out.println();
	            }
	            System.out.println("第"+time+"后次的中心为：");
	            for (Integer ds : centerIndexs) {
					System.out.print(ds+"  **  ");
				}
	            updateCenterPoints();
	            System.out.println();
	            System.out.println("第"+time+1+"后次的中心为：");
	            for (Integer ds : centerIndexs) {
					System.out.print(ds+"  **  ");
				}
	            System.out.println();
	        }
	    }
	    
	   
 }
