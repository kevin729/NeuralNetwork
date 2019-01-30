package pp;
import java.util.stream.*;
import pp.Utils.ActivationFunction;

public class NeuralNetwork {
	
	private Neuron[][] neurons;
	private double[][][] weights;
	private int[] layerSizes;
	private int layers_amount;
	private int neuron_amount;
	
	private double maxWeight = -0.5;
	private double minWeight = 0.5;
	
	private ActivationFunction af;
	
	public static final double LEARNING_RATE = 1;
	
	
	public NeuralNetwork(ActivationFunction af, int... layerSizes) {
		this.af = af;
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
					neurons[l][n] = new Neuron(af);
				} else {
					neurons[l][n] = new Neuron(af, layerSizes[l-1] + 1);
				}
			}
			
			//setup bias neurons
			if (l != layers_amount-1) {
				neurons[l][this.layerSizes[l]] = new Neuron(af, 1.0);
			}
			
			//setup weights
			if (l != 0) {
				this.weights[l-1] = new double[layerSizes[l]][layerSizes[l-1]+1];
				for (int n = 0; n < this.layerSizes[l]; n++) {
					for (int pn = 0; pn < this.layerSizes[l-1]+1; pn++) {
						if ((n == 0 && pn == 1) || (n == 1 && pn == 0)) {
							this.weights[l-1][n][pn] = minWeight;
						} else {
							this.weights[l-1][n][pn] = maxWeight;
						}
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

	public void backPropagation(double[] inputs, int datasets, double ... targets) {
		if (targets.length != layerSizes[layers_amount-1]) {
			return;
		}
		
		feedForward(inputs);

		double errorD = 0;
		
		for (int l = layers_amount-1; l > 0; l--) {
			for (int n = 0; n < layerSizes[l]; n++) {
				if (l == layers_amount-1) {
					errorD = 2 * (neurons[l][n].getFired() - targets[n]);
				}
				
				double fireD = 1;
				
				if (this.af == ActivationFunction.SIGMOID) {
					fireD = neurons[l][n].getFired() * (1 - neurons[l][n].getFired());
				}
				
				for (int w = 0; w < neurons[l][n].weights.length; w++) {
					weights[l-1][n][w] -= errorD * fireD * neurons[l][n].inputs[w] * LEARNING_RATE;
				}
			}
		}
		
		feedForward(inputs);
	}
		
	public double[] getOutputs() {
		double[] outputs = new double[this.layerSizes[this.layers_amount-1]];
		
		for (int n = 0; n < outputs.length; n++) {
			outputs[n] = this.neurons[this.layers_amount-1][n].getFired();
		}
		
		return outputs;
	}
	
	public void setRandomWeights() {
		for (int l = 0; l < this.layers_amount; l++) {
			
			//setup weights
			if (l != 0) {
				this.weights[l-1] = new double[layerSizes[l]][layerSizes[l-1]+1];
				for (int n = 0; n < this.layerSizes[l]; n++) {
					for (int pn = 0; pn < this.layerSizes[l-1]+1; pn++) {
						if ((n == 0 && pn == 1) || (n == 1 && pn == 0)) {
							this.weights[l-1][n][pn] = minWeight;
						} else {
							this.weights[l-1][n][pn] = maxWeight;
						}
					}
				}
			}
		}
	}
	
	public void setWeights(double[] inputs, int index) {
		for (int i = 0; i < inputs.length; i++) {
			weights[0][index][i] = inputs[i];
		}
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
