package pp;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import pp.Utils.ActivationFunction;

public class ConvolutionalNetwork {
	
	public NeuralNetwork brain;
	private double[][] images;
	private int index = 0;
	
	public ConvolutionalNetwork(ActivationFunction af, int... layerSizes) {
		brain = new NeuralNetwork(af, layerSizes);
		images = new double[layerSizes[layerSizes.length-1]][layerSizes[0]];
	}
	
	public void feedForward(int[] pixels, int width, int height) {
		brain.feedForward(normalise(resizeImage(shrinkImage(pixels, width, height), (int)Math.sqrt(brain.getLayerSizes()[0]))));
	}
	
	public void addImage(int[] pixels, int width, int height) {
		images[index] = normalise(resizeImage(shrinkImage(pixels, width, height), (int)Math.sqrt(brain.getLayerSizes()[0])));
		
		brain.setWeights(images[index], index);
		brain.feedForward(images[index]);
		index++;
	}
	
	private int[] resizeImage(BufferedImage img, int newSize) {
		Image tmp = img.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
		BufferedImage newImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = newImage.createGraphics();
		g.drawImage(tmp, 0, 0, null);
		g.dispose();
				
		return ((DataBufferInt)newImage.getRaster().getDataBuffer()).getData();
	}
	
	private double[] normalise(int[] pixels) {
		double[] newPixels = new double[pixels.length];
		for(int i = 0; i < pixels.length; i++) {
			if(pixels[i] != -1) {
				newPixels[i] = 1;
			} else {
				newPixels[i] = -1;
			}
		}
		
		return newPixels;
	}
	
	private BufferedImage shrinkImage(int[] pixels, int screenWidth, int screenHeight) {
		
		int minX = screenWidth, minY = screenHeight;
		int maxX = 0, maxY = 0;
		
		for (int y = 0; y < screenHeight; y++) {
			for (int x = 0; x < screenWidth; x++) {
				if (pixels[x+y*screenWidth] != -1) {
					if (minX > x) {
						minX = x;
					}
					if (maxX < x) {
						maxX = x;
					}
					
					if (minY > y) {
						minY = y;
					} 
					if (maxY < y) {
						maxY = y;
					}
				}
			}
		}
		
		int width = maxX - minX;
		int height = maxY - minY;
		
		int[] newPixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newPixels[x+y*width] = pixels[(x+minX)+(y+minY)*screenWidth];
			}
		}
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, width, height, newPixels, 0, width);
		
		return img;
	}
}
