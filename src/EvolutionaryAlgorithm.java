import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EvolutionaryAlgorithm implements EAProblem {

	//adult selection
	static final int FULLGENERATIONREPLACEMENT = 0;
	static final int OVERPRODUCTIONREPLACEMENT = 1;
	static final int GENERATIANOALMIXING = 2;

	//parent selection
	static final int BESTFITNESS = 0;
	static final int FITNESSPROPORTIONATE = 1;
	static final int SIGMASCALING = 2;
	static final int TOURNAMENT = 3;
	
	static final int SCENARIO_1 = 1;
	static final int SCENARIO_2 = 2;
	static final int SCENARIO_3 = 3;
	static final int SCENARIO_4 = 4;
	
	int scenario;
	
	static double mutationThreshold = 0.99;
	static double crossoverThreshold = 0.5;
		
	int vectorSize = 4;
	int maxWeight = 20;
	boolean dynamic = false;
	
	int populationNumber = 300;
	int generationsNumber = 30;
	int generation;
	
	AdultSelection adultSelection;
	ParentSelection parentSelection;
	Crossover crossover;
	Mutation mutation;
	Development development;
	
	List<Vector> parents;
	
	Flatland flatland;
	int gridSize;
	int maxSteps;
	
	int PacmanPosX = 5;
	int PacmanPosY = 5;
	int[][][] boards;
	
	public double FPD_f = 1.0/3.0;
	public double FPD_p = FPD_f + 1.0/3.0;
	
	int[] fitnessResults;
	double[][] weightResults;
	
	public EvolutionaryAlgorithm(boolean dynamic, int gridSize, int maxSteps, int scenario) throws FileNotFoundException, UnsupportedEncodingException {
		this.gridSize = gridSize;
		this.maxSteps = maxSteps;
		
		this.scenario = scenario;
		
		adultSelection = new AdultSelection(GENERATIANOALMIXING, populationNumber);
		parentSelection = new ParentSelection(SIGMASCALING);
		crossover = new Crossover();
		mutation = new Mutation();
		development = new Development(vectorSize);
		
		boards = generateMaps(5);
		
		fitnessResults = new int[generationsNumber];
		weightResults = new double[generationsNumber][vectorSize];
		
		List<Vector> population = createPopulation(populationNumber);
		generation = 0;
		for (int k = 0; k < generationsNumber; k++) {
			System.out.println("Generation " + (k+1));
			population = getFitnessValues(population);
			Collections.sort(population);
			population = adultSelection.selectAdults(population);
			parents = parentSelection.selectParents(population, populationNumber);
			
			for (Vector v : population) {
				v.generation += 1;
			}

			for (int i = 0; i < parents.size()-1; i += 2) 
			{
				population.addAll(mutation.mutateAll(crossover.cross(parents.get(i).vector, parents.get(i+1).vector, crossoverThreshold), mutationThreshold));
			}
			if (scenario == SCENARIO_3 || scenario == SCENARIO_4)
				boards = generateMaps(5);
			generation++;
		}
		saveResults();
	}
	
	public int[][] getMap(){
		return boards[0];
	}

	private int[][] generateMap() {
		int[][] board = new int[gridSize][gridSize];
		board[PacmanPosX][PacmanPosY] = Flatland.PACMAN;
		Random random = new Random();
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++) {
				if (board[i][j] == Flatland.PACMAN)
					continue;
				double object = random.nextDouble();
				if (object < FPD_f)
					board[i][j] = Flatland.FOOD;
				else if (object < FPD_p)
					board[i][j] = Flatland.POISON;
				else
					board[i][j] = 0;
			}
		return board;
	}
	
	private int[][][] generateMaps(int n) {
		int[][][] tempBoards = new int[n][gridSize][gridSize];
		for (int i = 0; i < n; i++) {
			tempBoards[i] = generateMap();
		}
		return tempBoards;
	}
	
	@Override
	public List<Vector> createPopulation(int n) {
		List<Vector> population = new ArrayList<Vector>();
		for (int i = 0; i < n; i++) {
			Vector v = new Vector(vectorSize, maxWeight);
			population.add(v);
		}
		return population;
	}

	@Override
	public List<Vector> getFitnessValues(List<Vector> population) {
		int count = 0;
		double[] bestWeights = {0.0, 0.0, 0.0, 0.0};
		int bestFitness = Integer.MIN_VALUE;
		int nbScenarios;
		if (scenario == SCENARIO_2 || scenario == SCENARIO_4)
			nbScenarios = 5;
		else
			nbScenarios = 1;
		for (int i = 0; i < nbScenarios; i++) {
			for (Vector v : population) {
				double[] weights = development.getPhenotypes(v);

				flatland = new Flatland(gridSize, weights[0], weights[1], weights[2], weights[3], boards[i]);
				flatland.prepare();
				
				int stepsLeft = maxSteps;
				while(stepsLeft > 0) {
					stepsLeft--;
					flatland.updatePacmanDirection();
					flatland.pacmanMove();
					flatland.refresh();
				}
				population.get(count).fitness = flatland.getPacmanFitness();
				//System.out.println(flatland.getPacmanFitness());
				if (flatland.getPacmanFitness() > bestFitness) {
					bestFitness = flatland.getPacmanFitness();
					bestWeights = weights;
				}

				count++;

			}
			count=0;
			System.out.println(bestFitness);
			
		}
		fitnessResults[generation] = bestFitness;
		weightResults[generation] = bestWeights;
		return population;
	}

	@Override
	public boolean foundSolution(List<Vector> population) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void saveResults() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("results_scenario" + scenario + ".txt", "UTF-8");
		for (int i = 0; i < generationsNumber; i++) {
			writer.println(fitnessResults[i]);
		}
		writer.close();
		
		writer = new PrintWriter("params_scenario" + scenario + ".txt", "UTF-8");
		for (int i = 0; i < generationsNumber; i++) {
			writer.println(Arrays.toString(weightResults[i]));
		}
		writer.close();
	}
	
	public double[] getBestParams() {
		return weightResults[weightResults.length-1];
	}
}