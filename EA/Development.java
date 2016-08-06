
public class Development 
{
	int vectorSize;
	
	public Development(int vectorSize){
		this.vectorSize = vectorSize;
	}
	public double[] getPhenotypes(Vector v){
		double[] weights = new double[vectorSize];
		for (int i = 0; i < vectorSize; i++)
			weights[i] = (double) v.vector[i] / 10.0d;
		return weights;
	}
	
	public int[] getGenotypes(double[] phenotypes){
		int[] weights = new int[vectorSize];
		for (int i = 0; i < vectorSize; i++)
			weights[i] = (int) phenotypes[i] * 10;
		return weights;
	}	
}