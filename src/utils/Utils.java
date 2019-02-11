package utils;

import java.util.Random;

public class Utils {
	
	private static Random rand = new Random();
	
	public enum ActivationFunction {
		NONE, HEAVISDE, SIGNUM, SIGMOID
	}
	
	public static double sigmoid(double x) {
		return 1d/(1 + Math.exp(-x));
	}
	
	public static int randomInt(int min, int max) {
		return rand.nextInt(max - min + 1) + min;
	}
}
