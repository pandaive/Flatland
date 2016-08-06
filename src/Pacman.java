import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pacman {

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	private int x;
	private int y;
	private int direction;
	private int fitness;
	//food weight, poison weight, hidden layer weight, bias weight
	int[] weights;
	int maxWeight = 20;
	
	double foodWeight, poisonWeight, hiddenLayerWeight, biasWeight;
	
	private int gridMax;
	
	private int[] around;
	
	private boolean nextMove = true;
	private int stayCount = 0;
	
	public Pacman(int x, int y, int direction, int gridSize,
			double foodWeight, double poisonWeight, double hiddenLayerWeight, double biasWeight) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.gridMax = gridSize-1;
		this.fitness = 0;
		this.weights = new int[4];
		this.foodWeight = foodWeight;
		this.poisonWeight = poisonWeight;
		this.hiddenLayerWeight = hiddenLayerWeight;
		this.biasWeight = biasWeight;
	}
	
	public void getNewDirection(){
		NeuralNetwork neuralNetwork = new NeuralNetwork(around[0], around[1], around[2], 
				foodWeight, poisonWeight, hiddenLayerWeight, biasWeight, 0.5d);
		chooseDirection(neuralNetwork.getOutput());
	}
	
	private void chooseDirection(double[] outputs){
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.add(0); keys.add(1); keys.add(2);
		Collections.shuffle(keys);
		double max = 0;
		int maxPos = 0;
		for (int i : keys) {
			if (outputs[i] > max) {
				max = outputs[i];
				maxPos = i;
			}
		}
		if (max == 0 && stayCount < 3) {
			nextMove = false;
			stayCount++;
		}
		else {
			nextMove = true; 
			stayCount = 0;
		}
		this.direction = (this.direction+4+maxPos-1) % 4;
	}
	
	public void move(){
		if (nextMove)
			switch(direction) {
			case UP:
				y = (y == 0 ? gridMax : y-1);
				break;
			case RIGHT:
				x = (x == gridMax ? 0 : x+1);
				break;
			case DOWN:
				y = (y == gridMax ? 0 : y+1);
				break;
			case LEFT:
				x = (x == 0 ? gridMax : x-1);
				break;
			}
	}
	
	public void setNeighbourFields(int[] around){
		this.around = around;
	}
	
	public void setFitness(int i) { this.fitness+= i; }
	
	public void setX(int x) { this.x = x; }
	
	public void setY(int y) { this.y = y; }
	
	public void setDirection(int direction){ this.direction = direction; }
	
	public int getFitness () { return this.fitness; }
	
	public int getX() { return x; }
	
	public int getY() {	return y; }
	
	public int getDirection(){ return direction; }	
}