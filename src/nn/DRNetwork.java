package nn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import utils.Utils.ActivationFunction;

public class DRNetwork {
	
	private NeuralNetwork brain;
	private double[][] images;
	private int index = 0;
	
	public DRNetwork(ActivationFunction af, int... layerSizes) {
		brain = new NeuralNetwork(af, layerSizes);
		images = new double[layerSizes[layerSizes.length-1]][layerSizes[0]];
	}
	
	public void feedForward(double[] pixels) {
		brain.feedForward(pixels);
	}
	
	public void feedForward(int[] pixels, int width, int height) {
		brain.feedForward(normalise(resizeImage(shrinkImage(pixels, width, height), (int)Math.sqrt(brain.getLayerSizes()[0]))));
		
		double result = 0;
		int neuronIndex = 0;
		for (int i = 0; i < brain.getOutputs().length; i++) {
			if (brain.getOutputs()[i] > result && i != 0) {
				result = brain.getOutputs()[i];
				neuronIndex = i;
			} else if (i == 0) {
				result = brain.getOutputs()[i];
				neuronIndex = i;
			}
			System.out.println(i + " " + brain.getOutputs()[i]);
		}
		
		System.out.println(neuronIndex);
	}
	
	public void addImage(int[] pixels, int width, int height) throws IOException {
		int[] imagePixels = resizeImage(shrinkImage(pixels, width, height), (int)Math.sqrt(brain.getLayerSizes()[0]));
		images[index] = normalise(imagePixels);
		
		saveImage(imagePixels);
		brain.setWeights(images[index], index);
		brain.feedForward(images[index]);
		saveWeights();
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
	
	public void saveWeights() throws IOException {
		brain.saveWeights();
	}
	
	public void loadWeights(String file) {
		Thread loadWeights = new Thread() {
			public void run() {
				try {
					brain.loadWeights(file);
				} catch (IOException e) {}
			}
		};
		
		loadWeights.start();
	}
	
	public void loadWeights(InputStream is) {
		Thread loadWeights = new Thread() {
			public void run() {
				try {
					brain.loadWeights(is);
				} catch (IOException e) {}
			}
		};
		
		loadWeights.start();
	}
	
	public void saveImage(int[] imagePixels) throws IOException {
		BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, 128, 128, imagePixels, 0, 128);
		File out = new File("image" + index+".png");
		ImageIO.write(image, "png", out);
	}
	
	public double[] getOutputs() {
		return brain.getOutputs();
	}
}
