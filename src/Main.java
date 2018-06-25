public class Main {
	private static Window window;
	//                                      i0 i1  o 
	public static final double[][][] IN = {{{0,0},{1}},
      	   			                      {{0,1},{0}},
                                          {{1,0},{1}},
                                          {{1,1},{1}}};
	
	
	public static void main(String[] args) {
		NeuralNetwork brain = new NeuralNetwork(2, 2, 1);
				
		window = new Window(brain);
	}
}
