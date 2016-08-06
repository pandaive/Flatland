import java.util.Random;

public class Vector implements Comparable<Vector> 
{

	int[] vector;
	int fitness;
	int generation;
	
	public Vector(int n)
	{
		vector = new int[n];
		initBinary(n);
		
		fitness = 0;
		generation = 0;
	}
	
	public Vector(int[] v) {
		vector = v;
		fitness = 0;
		generation = 0;
	}
	
	public Vector(int n, int symbols) {
		vector = new int[n];
		init(n, symbols);
		
		fitness = 0;
		generation = 0;
	}
	
	private void initBinary(int n)
	{
		Random random = new Random();
		for (int i = 0; i < n; i++) 
		{
			vector[i] = random.nextInt(2);
		}
	}
	
	private void init(int n, int symbols)
	{
		Random random = new Random();
		for (int i = 0; i < n; i++)
		{
			vector[i] = random.nextInt(symbols*2)-symbols;
		}
	}
	
	@Override
	public int compareTo(Vector o) 
	{
		// TODO Auto-generated method stub
		return this.fitness - o.fitness;
	}
}