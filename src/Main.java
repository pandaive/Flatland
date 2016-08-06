import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main extends JComponent implements Runnable {
	
	private static boolean dynamic = false;
	private static boolean hidden = false;
	private static int scenario;
	private static boolean randomBoard = true;
	private static int[][] predefinedBoard;
	
	public static int animationSpeed = 5;
	public static boolean start = false;
	
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	private static int gridSize = 10;
	private static int cellSize = 60;
	
	private static int width = (gridSize+1)*cellSize;
	private static int height = (gridSize+1)*cellSize;
	
	private static int stepsLeft = 60;
	public int[][] stepsMap = new int[stepsLeft][2]; 
	
	private static double foodWeight = 0.0;
	private static double poisonWeight = 0.0;
	private static double hiddenLayerWeight = 0.0;
	private static double biasWeight = 0.0;
	
	static Flatland flatland;

	public Main() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while(!start) {
			System.out.print("");
		}
		try {
			while(stepsLeft > 0) {
				stepsLeft--;
				flatland.updatePacmanDirection();
				repaint();
				Thread.sleep(1000 / (animationSpeed/2+1));
				flatland.pacmanMove();
				flatland.refresh();
				repaint();
				Thread.sleep(1000 / (animationSpeed+1));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Fitness: " + flatland.getPacmanFitness());
	}
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setPaint(Color.BLACK);
		int size = gridSize;
		int r = cellSize/2; //object's radius

		//draw grid
		for (int i = 0; i < size+1; i++) {
			Line2D.Double h = new Line2D.Double(0, cellSize*i, cellSize*size, cellSize*i);
			Line2D.Double v = new Line2D.Double(cellSize*i, 0, cellSize*i, cellSize*size);
			g2D.draw(h);
			g2D.draw(v);
		}
		
		//draw pacman
		Ellipse2D.Double p = new Ellipse2D.Double(flatland.getPacmanX()*cellSize+cellSize/4, 
				flatland.getPacmanY()*cellSize+cellSize/4, r, r);
		g2D.setPaint(Color.YELLOW);
		g2D.fill(p);
		g2D.setPaint(Color.BLACK);
		g2D.draw(p);
		
		int[][] dir = getTriangle((int)p.getCenterX(), (int)p.getCenterY(), flatland.getPacmanDirection(), r/2);
		
		Polygon triangle = new Polygon(dir[0], dir[1], 3);
		g2D.setPaint(Color.BLACK);
		g2D.fillPolygon(triangle);
		g2D.setPaint(Color.BLACK);
		g2D.drawPolygon(triangle);
		
		//draw food and poison
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++) {
				int boardValue = flatland.getBoardAtXYValue(i, j);
				if (boardValue == flatland.FOOD) {
					Ellipse2D.Double object = new Ellipse2D.Double(i*cellSize+r/2, j*cellSize+cellSize/4, r, r);
					g2D.setPaint(Color.GREEN);
					g2D.fill(object);
					g2D.setPaint(Color.BLACK);
					g2D.draw(object);
				}
				else if (boardValue == flatland.POISON) {
					Ellipse2D.Double object = new Ellipse2D.Double(i*cellSize+r/2, j*cellSize+cellSize/4, r, r);
					g2D.setPaint(Color.RED);
					g2D.fill(object);
					g2D.setPaint(Color.BLACK);
					g2D.draw(object);
				}
			}
	}
	
	private int[][] getTriangle(int x, int y, int direction, int size){
		int triangle[][] = new int[2][3];
		switch (direction) {
		case UP:
			triangle[0][0] = x-size; triangle[1][0] = y;
			triangle[0][1] = x; triangle[1][1] = y-(size*2);
			triangle[0][2] = x+size; triangle[1][2] = y;
			break;
		case RIGHT:
			triangle[0][0] = x; triangle[1][0] = y+size;
			triangle[0][1] = x; triangle[1][1] = y-size;
			triangle[0][2] = x+size*2; triangle[1][2] = y;
			break;
		case DOWN:
			triangle[0][0] = x-size; triangle[1][0] = y;
			triangle[0][1] = x; triangle[1][1] = y+size*2;
			triangle[0][2] = x+size; triangle[1][2] = y;
			break;
		case LEFT:
			triangle[0][0] = x-size*2; triangle[1][0] = y;
			triangle[0][1] = x; triangle[1][1] = y-size;
			triangle[0][2] = x; triangle[1][2] = y+size;
			break;
		}
		
		return triangle;
	}
	
	private static void getParameters() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("parameters.txt"));
		foodWeight = Double.parseDouble(br.readLine());
		poisonWeight = Double.parseDouble(br.readLine());
		hiddenLayerWeight = Double.parseDouble(br.readLine());
		biasWeight = Double.parseDouble(br.readLine());
		br.close();
		br = new BufferedReader(new FileReader("options.txt"));
		dynamic = Boolean.parseBoolean(br.readLine());
		hidden = Boolean.parseBoolean(br.readLine());
	}
	
	private static void saveParameters() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("parameters.txt", "UTF-8");
		writer.println(foodWeight);
		writer.println(poisonWeight);
		writer.println(hiddenLayerWeight);
		writer.println(biasWeight);
		writer.close();
	}
	
	private static void runVisualization(){
		System.out.println("Parameters:");
		System.out.println("Weights1: " + foodWeight);
		System.out.println("Weights2: " + poisonWeight);
		System.out.println("Weights3: " + hiddenLayerWeight);
		System.out.println("Weights4: " + biasWeight);
		
		JFrame s = new JFrame("Parameters");
	    s.add(new SpeedSlider(animationSpeed));
	    s.setSize(280, 700);
	    s.setVisible(true);
		
		if (randomBoard)
			flatland = new Flatland(gridSize, foodWeight, poisonWeight, hiddenLayerWeight, biasWeight);
		else
			flatland = new Flatland(gridSize, foodWeight, poisonWeight, hiddenLayerWeight, biasWeight, predefinedBoard);
		
		flatland.prepare();
		JFrame f = new JFrame("Flatland");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new Main());
		f.setSize(width, height);
		f.setVisible(true);
	}
	
	private static String getConsoleInput(String text) throws IOException{
		System.out.println(text);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();		
	}
	
	public static void setParameters(double fw, double pw, double hw, double bw){
		foodWeight = fw;
		poisonWeight = pw;
		hiddenLayerWeight = hw;
		biasWeight = bw;
	}
	
	public static void main(String[] args) throws IOException {
		getParameters();
		if (!hidden) {
			runVisualization();
		}
		else {
			scenario = Integer.parseInt(getConsoleInput("Choose scenario \n1-static 1\n2-static 5\n"
					+ "3-dynamic 1\n4-dynamic 5)"));
			EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(dynamic, gridSize, stepsLeft, 
					scenario);
			predefinedBoard = ea.getMap();
			/*
			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++)
					System.out.print(predefinedBoard[j][i]);
				System.out.println();
			}
			*/
			String simulation = getConsoleInput("Run simulation? (y/n)");
			if (simulation.equals("y")) {
				simulation = getConsoleInput("Random map? (y/n)");
				randomBoard = simulation.equals("n") ? false : true;
				double[] params = ea.getBestParams();
				foodWeight = params[0];
				poisonWeight = params[1];
				hiddenLayerWeight = params[2];
				biasWeight = params[3];
				
			    runVisualization();
			}
			saveParameters();		
		}
	}
}