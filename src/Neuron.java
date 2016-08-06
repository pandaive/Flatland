import java.util.ArrayList;

public class Neuron {
	private ArrayList<Neuron> input;
	private double threshold;
	private double weight;
	private boolean isFired;
	public double output = 0.0d;
	
	//normal neuron
	public Neuron(double thr, double w, ArrayList<Neuron> input){
		this.input = input;
		this.threshold = thr;
		this.weight = w;
		this.isFired = false;
	}
	
	//bias neuron or input
	public Neuron(double w, int output) {
		this.weight = w;
		this.output = output;
		this.isFired = true;
		
	}
	
	public void addInput(Neuron n) {
		this.input.add(n);
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public double getOutput() {
		return this.output;
	}
	
	public double sigmoid(double t){
		return 1.0/(1.0+Math.pow(Math.E, -t));
	}
	
	public void fire(){
		double total = 0.0d;
		if (!input.isEmpty()) {
			for (Neuron n : input) {
				if (n.isFired)
					total += n.getWeight()*n.getOutput();
			}
			total = sigmoid(total);
			if (total >= threshold) {
				this.isFired = true;
				output = total;
			}
		}
		else
			if (weight >= threshold) {
				isFired = true;
			}
	}
}