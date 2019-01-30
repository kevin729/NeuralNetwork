package pp;

import pp.Utils.ActivationFunction;

public class Main {

	public static void main(String[] args) {
		NeuralNetwork brain = new NeuralNetwork(ActivationFunction.SIGMOID, 2, 2, 1);
		DRNetwork brain2 = new DRNetwork(ActivationFunction.NONE, 128*128, 3);
		new Window(brain);
		new DrawingWindow(brain2);
	}
}
