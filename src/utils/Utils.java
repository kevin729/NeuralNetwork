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
	
	public static double[] normalise(int[] pixels, int width, int height) {
		double[] normalisedPixels = new double[pixels.length];
		
		for (int i = 0; i < pixels.length; i++) {
			int r = (pixels[i] >> 16) & 0xFF;
			int g = (pixels[i] >> 8) & 0xFF;
			int b = (pixels[i] & 0xFF);
			
			double gray = (r + g + b) / 3;
			double normalisedPixel = 2 * (gray / 255) - 1;
			
			normalisedPixels[i] = normalisedPixel;
		}
		
		return normalisedPixels;
	}
}
