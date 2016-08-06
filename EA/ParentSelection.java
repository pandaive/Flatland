import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
 * implementation of parent selection algorithms
 * Modes:
 * 0 - best fitness
 * 1 - fitness proportionate
 * 2 - sigma scaling
 * 3 - tournament
 */
public class ParentSelection 
{

	int mode;
	
	public ParentSelection(int mode) 
	{
		this.mode = mode;
	}
	
	public List<Vector> selectParents(List<Vector> population, int maxParents)
	{
		//just in case given max parents is bigger (never happened, but who knows?)
		if (maxParents > population.size())
			maxParents = population.size();
		
		switch(mode)
		{
		case 0: return bestFitness(population, maxParents);
		case 1: return fitnessProportionate(population, maxParents);
		case 2: return sigmaScaling(population, maxParents);
		case 3: return tournament(population, maxParents);
		default: return bestFitness(population, maxParents); 
		}
	}
	
	private List<Vector> bestFitness( List<Vector> population, int maxParents) 
	{
		List<Vector> selectedParents = new ArrayList<Vector>();
		Collections.sort(population);
		for (int i = 0; i < maxParents; i++)
			selectedParents.add(population.get(population.size()-1-i));
		
		return selectedParents;
	}
	
	private List<Vector> fitnessProportionate(List<Vector> population, int maxParents)
	{
		int fit = 0;
		List<Double> normFitness = new ArrayList<Double>();
		
		//count sum of fitness values
		for (Vector v : population)
			fit += v.fitness;
		
		Collections.sort(population);
		//get normalized fitness values
		int k = 0;
		for (Vector v : population)
		{
			normFitness.add(k == 0 ? ((double)v.fitness/fit) : ((double) v.fitness/fit + normFitness.get(k-1)));
			//System.out.println(normFitness.get(k));
			k++;
		}
		
		List<Integer> parents = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 0; i < maxParents; i++) 
		{
			double choice = random.nextDouble();
			int counter = 0;
			for (int j = 0; j < population.size(); j++)
			{
				if (choice <= normFitness.get(j))
				{
					if (parents.contains(j) && counter < Math.pow(10, 4))
					{
						choice = random.nextDouble();
						counter++;
						j = -1;
					}
					else 
					{
						if (counter == Math.pow(10, 4))
							parents.add(population.size()-1);
						else {
							parents.add(j);
							counter = 0;
							break;
						}
					}
				}
			}
		}
		List<Vector> selectedParents = new ArrayList<Vector>();
		for (int i : parents) {
			selectedParents.add(population.get(i));
		}
		Collections.sort(selectedParents);
		return selectedParents;
	}
	
	private List<Vector> sigmaScaling(List<Vector> population, int maxParents) {
		List<Double> normFitness = new ArrayList<Double>();
		//count average of fitness values
		int sumFitness = 0;
		for (Vector v : population)
			sumFitness += v.fitness;
		double avgFitness = (double)sumFitness / population.size();

		//count variance
		double variance = 0;
		for (Vector v : population)
			variance += Math.pow((avgFitness - (double) v.fitness), 2);
		variance /= population.size();
		
		//count standard deviation
		double sigma = Math.sqrt(variance);
		
		int k = 0;
		double newSumFitness = 0.0;
		//sigma scaling of each fitness
		for (Vector v : population)
		{
			double temp = 1.0+(((double) v.fitness - avgFitness)/(2*sigma));
			normFitness.add(k == 0 ? temp : temp + normFitness.get(k-1));
			k++;
			newSumFitness += temp;
			
		}
						
		for (int i = 0; i < normFitness.size(); i++) {
			normFitness.set(i, normFitness.get(i)/newSumFitness);
		}
		
		List<Integer> parents = new ArrayList<Integer>();
		Random random = new Random();
		for (int i = 0; i < maxParents; i++) 
		{
			double choice = random.nextDouble();
			int counter = 0;
			for (int j = 0; j < population.size(); j++)
			{
				if (choice <= normFitness.get(j))
				{
					
					if (parents.contains(j) && counter < Math.pow(10, 4))
					{
						choice = random.nextDouble();
						counter++;
						j = -1;
						
					}
					else 
					{
						counter=0;
						parents.add(j);
						break;
					}
				}
			}
		}
		
		List<Vector> selectedParents = new ArrayList<Vector>();
		for (int i : parents) {
			selectedParents.add(population.get(i));
		}
		return selectedParents;
	}
	
	private List<Vector> tournament(List<Vector> population, int maxParents) {
		List<Vector> selectedParents = new ArrayList<Vector>();
		List<Integer> parents_temp;
		List<Vector> parents;
		int k = population.size()/4;
		Random random = new Random();
		for (int i = 0; i < maxParents; i++)
		{
			parents_temp = new ArrayList<Integer>();
			parents = new ArrayList<Vector>();
			for (int j = 0; j < k; j++)
			{
				int p;
				do {
				p = random.nextInt(population.size());
				} while (parents_temp.contains(p));
				parents_temp.add(p);
				parents.add(population.get(p));
			}
			Collections.sort(parents);
			double choice = random.nextDouble();
			if (choice < 0.9)
				selectedParents.add(parents.get(k-1));
			else
				selectedParents.add(parents.get(random.nextInt(k)));
				
		}
		
		return selectedParents;
	}
}