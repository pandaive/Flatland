import java.util.ArrayList;

public class NeuralNetwork {

	Neuron foodForward, foodLeft, foodRight;
	Neuron poisonForward, poisonLeft, poisonRight;
	Neuron bias;
	
	double foodWeight, poisonWeight, hiddenLayerWeight, biasWeight;
	int biasValue = 1;
	
	double threshold;
	
	double[] output;
	
	public NeuralNetwork(int forward, int left, int right, 
			double foodWeight, double poisonWeight, 
			double hiddenLayerWeight, double biasWeight,
			double threshold){
		this.threshold = threshold;
		this.foodWeight = foodWeight;
		this.poisonWeight = poisonWeight;
		this.hiddenLayerWeight = hiddenLayerWeight;
		this.biasWeight = biasWeight;
		foodForward = new Neuron(this.foodWeight, forward == Flatland.FOOD ? 1 : 0);
		foodLeft = new Neuron(this.foodWeight, left == Flatland.FOOD ? 1 : 0);
		foodRight = new Neuron(this.foodWeight, right == Flatland.FOOD ? 1 : 0);
		
		poisonForward = new Neuron(this.poisonWeight, forward == Flatland.POISON ? 1 : 0);
		poisonLeft = new Neuron(this.poisonWeight, left == Flatland.POISON ? 1 : 0);
		poisonRight = new Neuron(this.poisonWeight, right == Flatland.POISON ? 1 : 0);
		
		bias = new Neuron(biasWeight, biasValue);
		runNetwork();
	}
	
	private void runNetwork(){
		ArrayList<Neuron> inputForward = new ArrayList<Neuron>();
		inputForward.add(foodForward);
		inputForward.add(poisonForward);
		Neuron hiddenNeuronForward = activateNeuron(hiddenLayerWeight, inputForward);
		
		ArrayList<Neuron> inputLeft = new ArrayList<Neuron>();
		inputLeft.add(foodLeft);
		inputLeft.add(poisonLeft);
		Neuron hiddenNeuronLeft = activateNeuron(hiddenLayerWeight, inputLeft);
		
		ArrayList<Neuron> inputRight = new ArrayList<Neuron>();
		inputRight.add(foodRight);
		inputRight.add(poisonRight);
		Neuron hiddenNeuronRight = activateNeuron(hiddenLayerWeight, inputRight);
		
		ArrayList<Neuron> outputForward = new ArrayList<Neuron>();
		outputForward.add(bias);
		outputForward.add(hiddenNeuronForward);
		Neuron outputNeuronForward = activateNeuron(1.0, outputForward);
		
		ArrayList<Neuron> outputLeft = new ArrayList<Neuron>();
		outputLeft.add(bias);
		outputLeft.add(hiddenNeuronLeft);
		Neuron outputNeuronLeft = activateNeuron(1.0, outputLeft);
		
		ArrayList<Neuron> outputRight = new ArrayList<Neuron>();
		outputRight.add(bias);
		outputRight.add(hiddenNeuronRight);
		Neuron outputNeuronRight = activateNeuron(1.0, outputRight);
		
		output = getOutput(outputNeuronForward, outputNeuronLeft, outputNeuronRight);
	}
	
	private Neuron activateNeuron(double weight, ArrayList<Neuron> input){
		Neuron newNeuron = new Neuron(threshold, weight, input);
		newNeuron.fire();
		return newNeuron;
	}
	
	private double[] getOutput(Neuron forward, Neuron left, Neuron right){
		double[] outputs = new double[3];
		
		outputs[0] = forward.getOutput();
		outputs[1] = left.getOutput();
		outputs[2] = right.getOutput();
		return outputs;
	}
	
	public double[] getOutput() {
		return this.output;
	}
}