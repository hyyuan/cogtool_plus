package cogtoolplus_utility;

import java.util.Random;

public class sampleGenerator {
	protected static final String NORMAL_DIST = "gaussian";
	protected static final String EXPONENTIAL_DIST = "exponential";
	public sampleGenerator(){}
	public Double[]  normalDistribution(int size, double mean, double std){
		Double[]  data = new Double[size];
		Random r = new Random();
		for (int i = 0; i < size; i++){
			data[i] = r.nextGaussian()*std + mean;
		}
		return data;
	}
	public Double[]  exponentialDistribution(int size, double mean, double lamda){
		Double[]  data = new Double[size];
		for (int i=0; i<size; i++){
			double random = Math.random()*mean;
			data[i] = Math.log(1-random)/(-lamda);
		}
		return data;
	}
}
