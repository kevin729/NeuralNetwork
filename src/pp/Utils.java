package pp;

public class Utils {
	
	public enum ActivationFunction {
		NONE, SIGMOID
	}
	
	public static double sigmoid(double x) {
		return 1d/(1 + Math.exp(-x));
	}
}
