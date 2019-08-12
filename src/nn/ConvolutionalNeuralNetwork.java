package nn;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

import utils.Utils;
import utils.Utils.ActivationFunction;

public class ConvolutionalNeuralNetwork {
	private int extractionWindowSize = 64;
	private int extractionStride = 64;
	
	private int featureIndex = 0;
	private int filterPixelAmount = 0;
	private int outputAmount = 0;
	
	private int convolutionWindowSize = 64;
	private int convolutionStride = 1;
	
	private int poolingWindowSize = 20;
	private int poolingStride = 20;
	
	ArrayList<double[]> features = new ArrayList<double[]>();
	HashMap<Integer, double[]> filters = new HashMap<Integer, double[]>();
	
	private NeuralNetwork brain;
	
	public ConvolutionalNeuralNetwork() {
		
	}
	
	public void feedForward(int[] pixels, int width, int height) throws IOException {
		int xAmount = (int)Math.floor(width/convolutionStride) - (convolutionWindowSize - convolutionStride);
		int yAmount = (int)Math.floor(height/convolutionStride) - (convolutionWindowSize - convolutionStride);
		
		convolution(Utils.normalise(pixels, width, height), width, height);
		pooling(xAmount, yAmount);
		
		int inputAmount = filterPixelAmount * filters.size();
		
		double[] inputs = new double[inputAmount];
		
		if (brain == null)
			brain = new NeuralNetwork(ActivationFunction.SIGMOID, inputAmount, outputAmount);
		
		for (int i = 0; i < filters.size(); i++) {
			for (int j = 0; j < filters.get(i).length; j++) {
				inputs[i * filterPixelAmount + j] = filters.get(i)[j];
			}
		}
		
		System.out.println(Arrays.toString(inputs));
		
		brain.feedForward(inputs);
		
		System.out.println("cross                  naut");
		System.out.println(brain.getOutputs()[0] + "    " + brain.getOutputs()[1]);
	}
	
	public void train(int[] pixels, int width, int height, double... targets) throws IOException {
		int xAmount = (int)Math.floor(width/convolutionStride) - (convolutionWindowSize - convolutionStride);
		int yAmount = (int)Math.floor(height/convolutionStride) - (convolutionWindowSize - convolutionStride);
		
		convolution(Utils.normalise(pixels, width, height), width, height);
		pooling(xAmount, yAmount);
		
		int inputAmount = filterPixelAmount * filters.size();
		
		System.out.println(inputAmount);
		
		double[] inputs = new double[inputAmount];
		
		if (brain == null)
			brain = new NeuralNetwork(ActivationFunction.SIGMOID, inputAmount, outputAmount);
		
		for (int i = 0; i < filters.size(); i++) {
			for (int j = 0; j < filters.get(i).length; j++) {
				inputs[i * filterPixelAmount + j] = filters.get(i)[j];
			}
		}
		
		System.out.println(Arrays.toString(inputs));
		
		System.out.println("Training...");
		for (int i = 0; i < 10000; i++) {
			brain.backPropagation(inputs, i, targets);
		}
	}
	
	private void convolution(double[] pixels, int width, int height) throws IOException {
		
		int xAmount = (int)Math.floor(width/convolutionStride) - (convolutionWindowSize - convolutionStride);
		int yAmount = (int)Math.floor(height/convolutionStride) - (convolutionWindowSize - convolutionStride);
		
		filterPixelAmount = xAmount * yAmount;
		
		for (int f = 0; f < features.size(); f++) {
			double[] featurePixels = features.get(f);
			double[] filterPixels = new double[xAmount * yAmount];
			int[] testPixels = new int[xAmount * yAmount];
			for (int i = 0; i < xAmount * yAmount; i++) {
				int x = (i % xAmount) * convolutionStride;
				int y = (int)Math.floor(i / xAmount) * convolutionStride;
				
				double targetPixel = 0;
				double pixel = 0;
				for (int j = 0; j < featurePixels.length; j++) {
					int jX = j % convolutionWindowSize + x;
					int jY = (int)Math.floor(j / convolutionWindowSize) + y;
					
					pixel += featurePixels[j] * pixels[jX + jY * width];
					targetPixel += Math.pow(pixels[jX + jY * width], 2);
				}
	
				filterPixels[i] = Math.max((Math.min(pixel, targetPixel) / Math.max(pixel, targetPixel)), 0);
				testPixels[i] = (int) Math.max((Math.min(pixel, targetPixel) * 255 / Math.max(pixel, targetPixel)), 0);
			}
			
			BufferedImage feature = new BufferedImage(xAmount, yAmount, BufferedImage.TYPE_INT_RGB);
			feature.setRGB(0, 0, xAmount, yAmount, testPixels, 0, xAmount);
			
			File out = new File("filter" + f+".png");
			ImageIO.write(feature, "png", out);
			
			filters.put(f, filterPixels);
		}
	}
	
	public void pooling(int width, int height) throws IOException {
		int xAmount = (int)Math.ceil(width/(double)poolingStride);
		int yAmount = (int)Math.ceil(height/(double)poolingStride);
		int filterSize = xAmount * yAmount;
				
		filterPixelAmount = filterSize;
		
		for (int f = 0; f < filters.size(); f++) {
			double[] filter = new double[filterSize];
			int[] test = new int[filterSize];
			for (int i = 0; i < filterSize; i++) {
				
				int x = (i % xAmount) * poolingStride;
				int y = (int)Math.floor(i / xAmount) * poolingStride;
							
				double pixel = -1;
				for (int j = 0; j < Math.pow(poolingWindowSize, 2); j++) {
					int jX = j % poolingWindowSize + x; 
					int jY = (int)Math.floor(j / poolingWindowSize + y);
									
					if (jX < width && jY < height)
						pixel = Math.max(pixel, filters.get(f)[jX + jY * width]);
					
				}
				
				filter[i] = pixel;
				test[i] = (int)Math.round(pixel*255);
			}
			
			BufferedImage feature = new BufferedImage(xAmount, yAmount, BufferedImage.TYPE_INT_RGB);
			feature.setRGB(0, 0, xAmount, yAmount, test, 0, xAmount);
			
			File out = new File("filter" + f+".png");
			ImageIO.write(feature, "png", out);
			
			filters.put(f, filter);
		}
	}
	
	public void extractFeatures(double[] pixels, int width, int height) throws IOException {
		outputAmount++;
		
		int xAmount = (int)Math.floor(width/extractionStride) - (extractionWindowSize - extractionStride);
		int yAmount = (int)Math.floor(height/extractionStride) - (extractionWindowSize - extractionStride);
		
		for (int i = 0; i < xAmount * yAmount; i++) {
			int x = (i % xAmount) * extractionStride;
			int y = (int)Math.floor(i / xAmount) * extractionStride;
			
			double[] featurePixels = new double[(int)Math.pow(extractionWindowSize, 2)];
			int[] testPixels = new int[(int)Math.pow(extractionWindowSize, 2)];
			for (int j = 0; j < featurePixels.length; j++) {
				int jX = j % extractionWindowSize + x;
				int jY = (int)Math.floor(j / extractionWindowSize) + y; 

				featurePixels[j] = pixels[jX + jY * width];
				testPixels[j] = (int)pixels[jX + jY * width];
			}
			BufferedImage feature = new BufferedImage(extractionWindowSize, extractionWindowSize, BufferedImage.TYPE_INT_RGB);
			feature.setRGB(0, 0, extractionWindowSize, extractionWindowSize, testPixels, 0, extractionWindowSize);
			
			features.add(featurePixels);
			
			File out = new File("feature" + featureIndex+".png");
			ImageIO.write(feature, "png", out);
			
			featureIndex++;
		}
	}
}
