import java.util.Random;

public class Flatland {

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	private int gridSize;
	private int[][] board;
	
	public double FPD_f = 1.0/3.0;
	public double FPD_p = FPD_f + 1.0/3.0;
	
	public static final int EMPTY = 0;
	public static final int FOOD = 1;
	public static final int POISON = -1;
	public static final int PACMAN = 666;
	
	private Pacman pacman;
	private int pacmanX;
	private int pacmanY;
	private int pacmanDirection;
	
	private double foodWeight = 0.0;
	private double poisonWeight = 0.0;
	private double hiddenLayerWeight = 0.0;
	private double biasWeight = 0.0;
	
	public Flatland(int gridSize, 
			double foodWeight, double poisonWeight, double hiddenLayerWeight, double biasWeight) {
		this.gridSize = gridSize;
		board = new int[gridSize][gridSize];
		this.foodWeight = foodWeight;
		this.poisonWeight = poisonWeight;
		this.hiddenLayerWeight = hiddenLayerWeight;
		this.biasWeight = biasWeight;
		this.pacmanX = 5;
		this.pacmanY = 5;
		initMap();
		setObjects();
	}
	
	public Flatland(int gridSize, 
			double foodWeight, double poisonWeight, double hiddenLayerWeight, double biasWeight,
			int[][] board) {
		this.gridSize = gridSize;
		this.board = new int[gridSize][gridSize];

		for (int k = 0; k < gridSize; k++)
			for (int j = 0; j < gridSize; j++)
				this.board[k][j] = board[k][j];
		
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++)
				if (board[i][j] == PACMAN) {
					pacmanX = i; pacmanY = j;
				}
		this.foodWeight = foodWeight;
		this.poisonWeight = poisonWeight;
		this.hiddenLayerWeight = hiddenLayerWeight;
		this.biasWeight = biasWeight;
	}
	
	public void prepare(){
		
		setParams();
	}
	
	public void refresh(){
		int f;
		pacman.setFitness((f = board[pacman.getX()][pacman.getY()]) == PACMAN ? 0 : f == -1 ? 3*f : f);
		board[pacmanX][pacmanY] = EMPTY;
		board[pacman.getX()][pacman.getY()] = PACMAN;
		pacmanX = pacman.getX();
		pacmanY = pacman.getY();
	}
	
	public void pacmanMove(){
		pacman.move();
	}
	
	public void updatePacmanDirection(){
		pacman.setNeighbourFields(getNeighbourFields());
		pacman.getNewDirection();
		this.pacmanDirection = pacman.getDirection();
	}
	
	private int[] getNeighbourFields(){
		int[] around = new int[3];
		int x = pacman.getX();
		int y = pacman.getY();
		int left = board[(x-1) < 0 ? gridSize-1 : x-1][y];
		int up = board[x][(y-1) < 0 ? gridSize-1 : y-1];
		int right = board[(x+1) > gridSize-1 ? 0 : x+1][y];
		int down = board[x][(y+1) > gridSize-1 ? 0 : y+1];
		
		switch(pacman.getDirection()) {
		case UP:
			around[0] = left; around[1] = up; around[2] = right;
			break;
		case RIGHT:
			around[0] = up; around[1] = right; around[2] = down;
			break;
		case DOWN:
			around[0] = right; around[1] = down; around[2] = left;
			break;
		case LEFT:
			around[0] = down; around[1] = left; around[2] = up;
			break;
		}
		
		return around;
	}

	private void initMap(){
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++) {
				board[i][j] = 0;
			}
	}
	
	private void setObjects(){
		board[pacmanX][pacmanY] = PACMAN;
		Random random = new Random();
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++) {
				if (board[i][j] == PACMAN)
					continue;
				double object = random.nextDouble();
				if (object < FPD_f)
					board[i][j] = FOOD;
				else if (object < FPD_p)
					board[i][j] = POISON;
			}
	}
	
	private void setParams(){
		Random random = new Random();
		int pacmanDirection = random.nextInt(4);
		
		pacman = new Pacman(pacmanX, pacmanY, pacmanDirection, gridSize,
				foodWeight, poisonWeight, hiddenLayerWeight, biasWeight);
		pacman.setNeighbourFields(getNeighbourFields());
	}
	
	public int getBoardAtXYValue(int x, int y) {
		return board[x][y];
	}
	
	public int getPacmanX(){
		return pacmanX;
	}
	
	public int getPacmanY(){
		return pacmanY;
	}
	
	public int getPacmanDirection(){
		return pacmanDirection;
	}
	
	public int getPacmanFitness(){
		return pacman.getFitness();
	}
}