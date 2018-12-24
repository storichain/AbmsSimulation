package abmsSimulation;


import java.util.HashMap;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Story extends Agent {

	protected double tomato;
	protected double potato;
	//public HashMap<PD, Double> tomato;
	public String title;
	public boolean storyCompleted;
	private int[] productVector;   // Story's score as Quantity and Quality
	
	public Story(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);	
		productVector = new int[Parameters.vectorSpaceSize];
		generateRandomProductVector();
		storyCompleted = false;
		//tomato = new HashMap<PD, Double>();
		tomato = 0;
		potato = 0;
	}

	String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int[] getProductVector() {
		return productVector;
	}
	
	public void setProductVector(int[] productVector) {
		this.productVector = productVector;
		//this.generateRequiredMeans();
	}
	
	public String printProductVector() {
		String s = "";
		
		for (int i = 0; i < productVector.length; i++) {
			s += String.valueOf(productVector[i]);
		}
		return s;
	}
	
	public void generateRandomProductVector() {
		for (int i = 0; i < productVector.length; i++) {
			productVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}
	
	/**
	 * Process offer made by an entrepreneur
	 * @param productVector
	 */
	//public int[] processStaking(int[] demandVector) {
	public double processStaking(int[] demandVector, double availableMoney) {

		setNegotiating(true);
		//int[] returnValue = demandVector;
		boolean chk = false;
		
		int d = SimulBuilder.hammingDistance(demandVector, productVector);
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (d>0 && d <= Math.ceil(Parameters.vectorSpaceSize / 2.0) && r < (Parameters.customersPersuadability / 100.0)) {
			if(checkAvailableStaking(demandVector)) {
				tomato += availableMoney;
				chk = true;
				//availableMoney = 0;
				
				/// 임시 처
				//storyCompleted = true;
			}
		}
		
		if(!chk) {
			availableMoney = 0;
		}
		
		setNegotiating(false);
		
		return availableMoney;
	}
	

	public boolean checkAvailableStaking(int demandVector[]) {
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r <= 0.5 && SimulBuilder.hammingDistance(demandVector, productVector) < 3) {
			return true;
		}
		return false;
	}
}
