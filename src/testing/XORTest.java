package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import pp.NeuralNetwork;
import pp.Utils.ActivationFunction;
import pp.Window;

public class XORTest {

	@Test
	public void test() throws InterruptedException {
		NeuralNetwork brain = new NeuralNetwork(ActivationFunction.SIGMOID, 2, 3, 2, 1);
		Window window = new Window(brain);
		window.dataSetsAmount = 4;
		window.inputAmount = 1;
		window.outputAmount = 1;
		
		window.inputs[0][0].setText("0");
		window.inputs[0][1].setText("0");
		
		window.inputs[1][0].setText("0");
		window.inputs[1][1].setText("1");
		
		window.inputs[2][0].setText("1");
		window.inputs[2][1].setText("0");
		
		window.inputs[3][0].setText("1");
		window.inputs[3][1].setText("1");
		
		window.expectedOutputs[0][0].setText("0");
		window.expectedOutputs[1][0].setText("1");
		window.expectedOutputs[2][0].setText("1");
		window.expectedOutputs[3][0].setText("0");
		
		window.trainBtn.doClick();
		
		Thread.sleep(1000);
		
		System.out.println(brain.getOutputs()[0]);
	}

}
