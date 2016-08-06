import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * implementation of adult selection algorithms
 * Modes:
 * 0 - full generation replacement
 * 1 - overproduction replacement
 * 2 - generational mixing
 */
public class AdultSelection 
{
	int mode;
	int maxAdults = 0;

	public AdultSelection(int mode, int maxAdults) 
	{
		this.mode = mode;
		this.maxAdults = maxAdults;
	}
	
	public  List<Vector> selectAdults(List<Vector> population)
	{
		switch(mode) 
		{
		case 0: return fullGenerationalReplacement(population);
		case 1: return overProductionReplacement(population);
		case 2: return generationalMixing(population);
		default: return fullGenerationalReplacement(population);
		}
	}
	
	/*
	 * remove all adults
	 */
	private List<Vector> fullGenerationalReplacement(List<Vector> population)
	{
		List<Vector> newPopulation = new ArrayList<Vector>();
		for (Vector v : population)
			if (v.generation == 0)
				newPopulation.add(v);
		
		return newPopulation;
	}
	
	/*
	 * remove all adults then remove weakest children
	 */
	private List<Vector> overProductionReplacement(List<Vector> population) 
	{
		List<Vector> newPopulation = fullGenerationalReplacement(population);
		Collections.sort(newPopulation);
		for (int i = 0; i < newPopulation.size() - maxAdults; i++) {
			newPopulation.remove(0);
		}
		
		return newPopulation;
	}
	
	/*
	 * remove weakest creatures from entire population
	 */
	private List<Vector> generationalMixing(List<Vector> population) 
	{
		Collections.sort(population);
		for (int i = 0; i < population.size() - maxAdults; i++)
			population.remove(0);
		
		return population;
	}	
}