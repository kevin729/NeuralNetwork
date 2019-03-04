import java.util.Arrays;

import ga.GeneticAlgorithm;
import nn.*;
import utils.Utils.*;

public class Main {
	
public static void main(String[] args) {
		DRNetwork brain = new DRNetwork(ActivationFunction.NONE, 128*128, 7);
		new DrawingWindow(brain);
	}
}
