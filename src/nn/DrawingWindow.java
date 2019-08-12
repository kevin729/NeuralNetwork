package nn;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class DrawingWindow extends JFrame {
	
	private JButton go = new JButton("Go");
	private JButton train = new JButton("Train");
	private DrawingPanel panel = new DrawingPanel();
	
	public DrawingWindow(DRNetwork brain) {
		go.setFont(new Font("Arial", Font.PLAIN, 40));
		train.setFont(new Font("Arial", Font.PLAIN, 40));
		go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				brain.feedForward(panel.getPixels(), panel.getSize().width, panel.getSize().height);
				panel.clear();
			}
		});
		
		train.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					brain.addImage(panel.getPixels(), panel.getSize().width, panel.getSize().height);
				} catch (IOException e1) {e1.printStackTrace();}
				panel.clear();
			}
		});
		
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(go, BorderLayout.NORTH);
		getContentPane().add(train, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public class DrawingPanel extends JComponent {
		private BufferedImage image;
		private Graphics2D g;
		private int cX, cY, oX, oY;
	
		public DrawingPanel() {
			setDoubleBuffered(false);
			
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					oX = e.getX();
					oY = e.getY();

				}
			});
			
			addMouseMotionListener(new MouseMotionAdapter() {			
				public void mouseDragged(MouseEvent e) {
					cX = e.getX();
					cY = e.getY();
				
					if (g != null) {
						g.drawLine(oX, oY, cX, cY);
						repaint();
						oX = cX;
						oY = cY;
					}
				}
			});
		}
	
		protected void paintComponent(Graphics g) {
			if (image == null) {
				image = (BufferedImage)createImage(getSize().width, getSize().height);
				this.g = (Graphics2D) image.getGraphics();
				clear();
			}
		
			g.drawImage(image, 0, 0, null);
		}
	
		public void clear() {
			g.setPaint(Color.WHITE);
			g.fillRect(0, 0, getSize().width, getSize().height);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(20));
			repaint();
		}
		
		private int[] getPixels() {
			return ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		}
	}
}
