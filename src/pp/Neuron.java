package pp;

import pp.Utils.ActivationFunction;

public class Neuron {
	
	public double[] inputs;
	public double[] weights;
	private double weightedSum;
	private double fire = 0;
	private ActivationFunction af;

	
	/**
	 * Constructor for input neuron
	 */
	public Neuron(ActivationFunction af) {this.af = af;}
	
	/**
	 * Constructor for hidden and output neuron
	 * @param inputSize - amount of inputs and weights for the neuron
	 */
	public Neuron(ActivationFunction af, int inputSize) {
		this.af = af;
		this.inputs = new double[inputSize];
		this.weights = new double[inputSize];
	}
	
	/**
	 * Constructor for bias neuron
	 * @param output - output from neuron
	 */
	public Neuron(ActivationFunction af, double output) {
		this.af = af;
		this.fire = output;
	}
	
	public void connect(double weight, Neuron neuron, int index)
	{
		this.inputs[index] = neuron.getFired();
		this.weights[index] = weight;

		this.weightedSum += this.inputs[index]*this.weights[index];
	}
		
	public void activationFunction()
	{
		switch (af) {
			case NONE:
				this.fire = this.weightedSum;
				break;
			case SIGMOID:
				this.fire = Utils.sigmoid(this.weightedSum);
				break;
		}
			
		this.weightedSum = 0;
	}
	
	public double getFired()
	{
		return fire;
	}
	
	public void setFire(double Fire)
	{
		this.fire = Fire;
	}
}
