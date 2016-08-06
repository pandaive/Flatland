import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crossover {

	/*
	 * randomly cross two parents' genes
	 */
	public List<Vector> cross(int[] a, int[] b, double threshold){
		List<Vector> children = new ArrayList<Vector>();
		int[] crossed_a = new int[a.length];
		int[] crossed_b = new int[b.length];
		Random random = new Random();
		
		for (int i = 0; i < a.length; i++)
		{
			if (random.nextDouble() < threshold)
				crossed_a[i] = a[i];
			else
				crossed_a[i] = b[i];
		}
		
		for (int i = 0; i < b.length; i++)
		{
			if (random.nextDouble() < threshold)
				crossed_b[i] = b[i];
			else
				crossed_b[i] = a[i];
		}
		children.add(new Vector(crossed_a));
		children.add(new Vector(crossed_b));
		
		return children;
	}
}