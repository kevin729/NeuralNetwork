import java.util.stream.*;

public class NeuralNetwork {
	
	private Neuron[][] neurons;
	private double[][][] weights;
	private int[] layerSizes;
	private int layers_amount;
	private int neuron_amount;
	
	public static final double LEARNING_RATE = 0.5;
	
	
	public NeuralNetwork(int... layerSizes) {
		this.layerSizes = layerSizes;
		this.layers_amount = layerSizes.length;
		this.neuron_amount = IntStream.of(this.layerSizes).sum();
		
		this.weights = new double[this.layers_amount-1][][];
		this.neurons = new Neuron[this.layers_amount][];
		
		for (int l = 0; l < this.layers_amount; l++) {
			if (l != layers_amount-1) {
				this.neurons[l] = new Neuron[this.layerSizes[l] + 1];
			} else {
				this.neurons[l] = new Neuron[this.layerSizes[l]];
			}
			
			//setup input and hidden neurons
			for (int n = 0; n < this.layerSizes[l]; n++) {
				if (l == 0) {
					neurons[l][n] = new Neuron();
				} else {
					neurons[l][n] = new Neuron(layerSizes[l-1] + 1);
				}
			}
			
			//setup bias neurons
			if (l != layers_amount-1) {
				neurons[l][this.layerSizes[l]] = new Neuron(1.0);
			}
			
			//setup weights
			if (l != 0) {
				this.weights[l-1] = new double[layerSizes[l]][layerSizes[l-1]+1];
				for (int n = 0; n < this.layerSizes[l]; n++) {
					for (int pn = 0; pn < this.layerSizes[l-1]+1; pn++) {
						this.weights[l-1][n][pn] = 1.0;
					}
				}
			}
		}
	}

	public void feedForward(double ... inputs) {
		if (inputs.length != layerSizes[0]) {
			return;
		}
		
		for (int l = 0; l < this.layers_amount; l++) {
			for (int n = 0; n < this.layerSizes[l]; n++) {
				if (l == 0) {
					neurons[l][n].setFire(inputs[n]);
				} else {
					for (int pn = 0; pn < this.layerSizes[l-1]+1; pn++) {
						neurons[l][n].connect(weights[l-1][n][pn], neurons[l-1][pn], pn);
					}
					neurons[l][n].activationFunction();
				}
			}
		}
	}
	
	public void backPropagation(double[] inputs, double ... targets) {
		if (targets.length != layerSizes[layers_amount-1]) {
			return;
		}
		feedForward(inputs);
		calculateErrors(targets);
		adjustWeights();
		feedForward(inputs);
	}
	
	private void calculateErrors(double ... targets) {
		for (int l = layers_amount-1; l > 0; l--) {
			for (int n = 0; n < layerSizes[l]+1; n++) {
				//calculates error from output neurons
				if (l == layers_amount-1 && n != layerSizes[layers_amount-1]) {
					neurons[l][n].calculateError(targets[n]);
				//sets error to other neurons
				} else if (l != layers_amount-1) {
					neurons[l][n].addErrors(n, neurons[l+1]);
				}
			}
		}
	}
	
	private void adjustWeights() {
		for (int l = 1; l < layers_amount; l++) {
			for (int n = 0; n < layerSizes[l]; n++) {				
				this.weights[l-1][n] = neurons[l][n].adjustWeights();
			}
		}
	}
	
	public double[] getOutputs() {
		double[] outputs = new double[this.layerSizes[this.layers_amount-1]];
		
		for (int n = 0; n < outputs.length; n++) {
			outputs[n] = this.neurons[this.layers_amount-1][n].getFired();
		}
		
		return outputs;
	}

	public Neuron[][] getNeurons() {
		return neurons;
	}

	public double[][][] getWeights() {
		return weights;
	}

	public int[] getLayerSizes() {
		return layerSizes;
	}

	public int getLayers_amount() {
		return layers_amount;
	}

	public int getNeuron_amount() {
		return neuron_amount;
	}
}
