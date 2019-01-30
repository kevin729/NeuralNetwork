package pp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class Window extends JFrame {
	
	private NeuralNetwork brain;
	
	private Thread learn;
	
	public int dataSetsAmount = 2;
	public int inputAmount = 2;
	public int outputAmount = 1;
		
	public JTextField[][] inputs;
	public JTextField[][] expectedOutputs;
	public JTextField[][] outputs;
	
	public JButton goBtn;
	public JButton trainBtn;
	public JButton stopBtn;
	public JButton resetBtn;
	
	private JButton addDataSetBtn;
	private JButton removeDataSetBtn;
			
	public Window(NeuralNetwork brain) {
		super("Neural Network");
		this.brain = brain;
		
		inputAmount = brain.getLayerSizes()[0];
		outputAmount = brain.getLayerSizes()[brain.getLayers_amount()-1];
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setLayout(new MigLayout("fill"));
				
		init();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void init() {
		inputs = new JTextField[dataSetsAmount][inputAmount];
		expectedOutputs = new JTextField[dataSetsAmount][outputAmount];
		outputs = new JTextField[dataSetsAmount][outputAmount];
		
		for (int ds = 0; ds < dataSetsAmount; ds++) {
			for (int in = 0; in < inputAmount; in++) {
				inputs[ds][in] = new JTextField(5);
			}
			
			for (int out = 0; out < outputAmount; out++) {
				expectedOutputs[ds][out] = new JTextField(5);
				outputs[ds][out] = new JTextField(20);
				outputs[ds][out].setEditable(false);
			}
		}
		goBtn = new JButton("GO!");
		trainBtn = new JButton("Train");
		stopBtn = new JButton("Stop");
		stopBtn.setEnabled(false);
		resetBtn = new JButton("Reset");
		
		addDataSetBtn = new JButton("Add DataSet");
		removeDataSetBtn = new JButton("Remove DataSet");
		
		addDataSetBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dataSetsAmount++;
				getContentPane().removeAll();
				init();
			}
		});
		
		removeDataSetBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dataSetsAmount--;
				getContentPane().removeAll();
				init();
			}
		});
		
		goBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				double[] ins = new double[inputAmount];
				
					for (int ds = 0; ds < dataSetsAmount; ds++) {
						for (int in = 0; in < inputAmount; in++) {
							ins[in] = Double.parseDouble(inputs[ds][in].getText());
						}
						
						brain.feedForward(ins);
						
						for (int out = 0; out < outputAmount; out++) {
							outputs[ds][out].setText(Double.toString(brain.getOutputs()[out]));
						}
					}
			}
		});
		
		trainBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				double[] ins = new double[inputAmount];
				double[] eOut = new double[outputAmount];
				try {
					
					learn = new Thread() {
						public void run() {
							stopBtn.setEnabled(true);
							trainBtn.setEnabled(false);
							while(!Thread.currentThread().isInterrupted()) {
								for (int ds = 0; ds < dataSetsAmount; ds++) {
									for (int in = 0; in < inputAmount; in++) {
										ins[in] = Double.parseDouble(inputs[ds][in].getText());
									}
									
									for (int out = 0; out < outputAmount; out++) {
										eOut[out] = Double.parseDouble(expectedOutputs[ds][out].getText());
									}
																	
									brain.backPropagation(ins, dataSetsAmount, eOut);
								
									for (int out = 0; out < outputAmount; out++) {
										outputs[ds][out].setText(Double.toString(brain.getOutputs()[out]));
									}
								}
							}
							trainBtn.setEnabled(true);
							stopBtn.setEnabled(false);
						}
					};
					
					learn.start();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		stopBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				learn.interrupt();
			}
		});
		
		resetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				brain.setRandomWeights();
			}
		});
		
		add(new NeuralNetworkPanel(), "height 100:, span, dock north");
		add(new InputPanel(), "align left");
		add(new ButtonPanel(), "align center");
		add(new OutputPanel(), "align right, wrap");
		add(new InputButtonsPanel(), "span");

		pack();
	}
	
	private class NeuralNetworkPanel extends JPanel {
		
		public NeuralNetworkPanel() {
			setLayout(null);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			setBackground(new Color(190, 81, 215));
			g.setColor(new Color(0, 0, 0));
			
			double layerWidth = getWidth()/brain.getLayers_amount();
			
			for (int l = 0; l < brain.getLayers_amount(); l++) {
				double xPos = (l*layerWidth) + (layerWidth/2);
				double layerHeight = getHeight()/brain.getLayerSizes()[l];
				
				for (int n = 0; n < brain.getLayerSizes()[l]; n++) {
					double yPos = (n*layerHeight) + (layerHeight/2);
					g.fillOval((int)xPos, (int)yPos, 10, 10);
					
					if (l != 0) {
						for(int pn = 0; pn < brain.getLayerSizes()[l-1]; pn++) {
							double pLayerHeight = getHeight()/brain.getLayerSizes()[l-1];
							double pXPos = ((l-1)*layerWidth) + (layerWidth/2);
							double pYPos = (pn * pLayerHeight) + (pLayerHeight/2);
							
							g.drawLine((int)xPos+5, (int)yPos+5, (int)pXPos+5, (int)pYPos+5);
							g.drawString(Double.toString(brain.getWeights()[l-1][n][pn]), (int)((pXPos+xPos)/2), (int)(pYPos+yPos)/2);
						}
					}
				}
			}
		}
	}
	
	private class InputPanel extends JPanel {
		
		public InputPanel() {
			int inputAndExpectedOutput = inputAmount+outputAmount;
			setLayout(new MigLayout("wrap "+inputAndExpectedOutput));
			
			for (int i = 0; i < inputAndExpectedOutput; i++) {
				if (i < inputAmount) {
					add(new JLabel("Input "+i));
				} else {
					add(new JLabel("E Output"));
				}
			}
			
			for (int ds = 0; ds < dataSetsAmount; ds++) {
				for (int in = 0; in < inputAmount; in++) {
					add(inputs[ds][in]);
				}
				
				for(int out = 0; out < outputAmount; out++) {
					add(expectedOutputs[ds][out]);
				}
			}
		}
	}
	
	private class ButtonPanel extends JPanel {
		
		public ButtonPanel() {
			setLayout(new MigLayout());
			
			add(goBtn, "wrap, alignx center");
			add(trainBtn, "wrap, alignx center");
			add(stopBtn, "alignx center");
			add(resetBtn, "alignx center");
		}
	}
	
	private class OutputPanel extends JPanel {
		
		public OutputPanel() {
			setLayout(new MigLayout("wrap "+outputAmount));
			
			for (int i = 0; i < outputAmount; i++) {
				add(new JLabel("Output "+ i));
			}
			
			for (int ds = 0; ds < dataSetsAmount; ds++) {
				for (int out = 0; out < outputAmount; out++) {
					add(outputs[ds][out]);
				}
			}
		}
	}
	
	private class InputButtonsPanel extends JPanel {
		
		public InputButtonsPanel () {
			setLayout(new MigLayout());
			
			add(addDataSetBtn);
			add(removeDataSetBtn);
		}
	}
}
