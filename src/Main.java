import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ga.GeneticAlgorithm;
import nn.*;
import utils.Utils;
import utils.Utils.*;

public class Main {
	
public static void main(String[] args) {
		ConvolutionalNeuralNetwork cnn = new ConvolutionalNeuralNetwork();
			
		try {
			BufferedImage img = ImageIO.read(new File("naut.png"));
			BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(img, 0, 0, null);
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			
			BufferedImage img2 = ImageIO.read(new File("cross.png"));
			BufferedImage image2 = new BufferedImage(img2.getWidth(), img2.getHeight(), BufferedImage.TYPE_INT_RGB);
			image2.getGraphics().drawImage(img2, 0, 0, null);
			int[] pixels2 = new int[image2.getWidth() * image2.getHeight()];
			image2.getRGB(0, 0, image2.getWidth(), image2.getHeight(), pixels2, 0, image2.getWidth());
			
			BufferedImage img3 = ImageIO.read(new File("cross2.png"));
			BufferedImage image3 = new BufferedImage(img3.getWidth(), img3.getHeight(), BufferedImage.TYPE_INT_RGB);
			image3.getGraphics().drawImage(img3, 0, 0, null);
			int[] pixels3 = new int[image3.getWidth() * image3.getHeight()];
			image3.getRGB(0, 0, image3.getWidth(), image3.getHeight(), pixels3, 0, image3.getWidth());
			
			BufferedImage img4 = ImageIO.read(new File("naut2.png"));
			BufferedImage image4 = new BufferedImage(img4.getWidth(), img4.getHeight(), BufferedImage.TYPE_INT_RGB);
			image4.getGraphics().drawImage(img4, 0, 0, null);
			int[] pixels4 = new int[image4.getWidth() * image4.getHeight()];
			image3.getRGB(0, 0, image4.getWidth(), image4.getHeight(), pixels4, 0, image4.getWidth());
			
			cnn.extractFeatures(Utils.normalise(pixels, image.getWidth(), image.getHeight()), image.getWidth(), image.getHeight());
			cnn.extractFeatures(Utils.normalise(pixels2, image2.getWidth(), image2.getHeight()), image2.getWidth(), image2.getHeight());
			
			cnn.train(pixels, image.getWidth(), image.getHeight(), 0.0, 1.0); //naut
			cnn.train(pixels2, image2.getWidth(), image2.getHeight(), 1.0, 0.0); //cross
			
			cnn.feedForward(pixels, image4.getWidth(), image4.getHeight()); //naut
			cnn.feedForward(pixels2, image3.getWidth(), image3.getHeight()); //cross
			cnn.feedForward(pixels3, image3.getWidth(), image3.getHeight()); //cross
			cnn.feedForward(pixels4, image3.getWidth(), image3.getHeight()); //naut
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
