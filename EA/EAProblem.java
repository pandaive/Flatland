import java.util.List;

/*
 * interface that each EA problem must implement
 */
public interface EAProblem {

	public List<Vector> createPopulation(int n);
	
	public List<Vector> getFitnessValues(List<Vector> population);
	
	public boolean foundSolution(List<Vector> population);
}