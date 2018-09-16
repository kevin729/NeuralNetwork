
public class Neuron {
	
	public double[] inputs;
	public double[] weights;
	private double weightedSum;
	private double fire = 0;
	private double fire_derivative = 0;
	private double error = 0;
	private double error_derivative = 0;
	public boolean bias = false;
	
	/**
	 * Constructor for input neuron
	 */
	public Neuron() {}
	
	/**
	 * Constructor for hidden and output neuron
	 * @param inputSize - amount of inputs and weights for the neuron
	 */
	public Neuron(int inputSize) {
		this.inputs = new double[inputSize];
		this.weights = new double[inputSize];
	}
	
	/**
	 * Constructor for bias neuron
	 * @param output - output from neuron
	 */
	public Neuron(double output)
	{
		this.bias = true;
		this.fire = output;
	}
	
	public void connect(double weight, Neuron neuron, int index)
	{
		this.inputs[index] = neuron.getFired();
		this.weights[index] = weight;

		this.weightedSum += this.inputs[index]*this.weights[index];
	}
		
	public double[] adjustWeights()
	{
		if (inputs != null) {
			for (int input = 0; input < inputs.length; input++) {
				weights[input] -= NeuralNetwork.LEARNING_RATE * this.error_derivative * this.fire_derivative * inputs[input];
			}
		}
		
		return this.weights;
	}
		
	public void activationFunction()
	{		
		this.fire = Utils.sigmoid(this.weightedSum);
		this.fire_derivative = Utils.sigmoid(this.weightedSum) * (1 - Utils.sigmoid(this.weightedSum));
		
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
		
	public double getError() {
		return this.error;
	}
	
	public double getFire_deriavative() {
		return this.fire_derivative;
	}
}
