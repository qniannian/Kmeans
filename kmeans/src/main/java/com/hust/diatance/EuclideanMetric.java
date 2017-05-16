package com.hust.diatance;

public class EuclideanMetric {
	
	
	//计算每一行与簇中心的距离，此处用的为欧式距离
	 public double calculate(double[] vector1, double[] vector2) 
	 {
		    double dis = 0.0;
	        for(int i=0; i<vector1.length; i++)
	        {
	        	dis +=(vector1[i]-vector2[i])*(vector1[i]-vector2[i]);
	        	
	        }
	        dis = Math.sqrt(dis);
	        return dis;
	 }

}
