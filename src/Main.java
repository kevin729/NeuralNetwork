import java.util.Arrays;

import ga.GeneticAlgorithm;
import nn.*;
import utils.Utils.*;

public class Main {
	
	public static void main(String[] args) {
//		NeuralNetwork brain = new NeuralNetwork(ActivationFunction.HEAVISDE, 2, 2, 1);
//		DRNetwork brain2 = new DRNetwork(ActivationFunction.NONE, 128*128, 3);
//		
//		try {
//			brain2.loadWeights("weights.nn");
//		} catch (Exception e) {e.printStackTrace();}
//		
//		new Window(brain);
//		new DrawingWindow(brain2);
		
		GeneticAlgorithm ga = new GeneticAlgorithm(10);
		ga.start();
	}
}
