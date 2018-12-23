package abmsSimulation;

import java.util.HashMap;

import EffectuationCausation.SimulationBuilder;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;



public class PD extends Agent {
	
	protected int[] demandVector;		// PD's Demand. when demand matches with ProductDemand on Story, PD does invest.
	
	protected boolean isOperating;	    // for checking staking on one time per a story

	public PD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		demandVector = new int[Parameters.vectorSpaceSize];
		initializeDemandVector();
		isOperating = false;
	}
	
	/**
	 * Initialize demand vector with all element set as 0s
	 */
	public void initializeDemandVector() {
		double marketSplit = Parameters.marketSplit / 100.0;
		for (int i = 0; i < demandVector.length; i++) {
			double r = RandomHelper.nextDoubleFromTo(0, 1);
			
			demandVector[i] = r < marketSplit ? 1 : 0;
		}
	}
	
	public boolean isOperating() {
		return isOperating;
	}
	
	public void setOperating(boolean isOperating) {
		this.isOperating = isOperating;
		if (isOperating) {
			//SimulationBuilder.staticDemandSteps = 0;
		}
	}

	public int[] getDemandVector() {
		return demandVector;
	}
	
	public void setDemandVector(int[] demandVector) {
		this.demandVector = demandVector;
	}
	
	/**  // adaptable variation on PD's demand by neighbours demand
	 *  Adapts product vector based on neighbours demand and simulation parameters 
	 */
	public void adaptProductVector() {
		
		//System.out.println("PD adaptProductVector()");
		
		HashMap<Integer, Integer> neighboursAdaption = new HashMap<Integer, Integer>();
		
		double threshold = Parameters.adaptationThreshold / 100.0;
		int neighbours = 0;
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			neighboursAdaption.put(i, 0);
		}
		
		for (Object o: network.getAdjacent(this)) {
			if (o instanceof PD) {
				PD c = (PD)o;
				
				for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
					int count = neighboursAdaption.get(i);
					if (c.getDemandVector()[i] == 1) {
						count++;
					}
					neighboursAdaption.put(i, count);
				}
			}
			neighbours++;
		}
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			if ((double)neighboursAdaption.get(i) / neighbours >= threshold ) {
				demandVector[i] = 1;
			}
		}
	}
	
	// demand comparison
	//public void processOffer(int[] productVector) {
	public void processDemandComparison(int[] productVector) {

		setNegotiating(true);
		
		int d = ContextBuilder.hammingDistance(demandVector, productVector);
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (d>0 && d <= Math.ceil(Parameters.vectorSpaceSize / 2.0) && r < (Parameters.customersPersuadability / 100.0)) {
			demandVector = productVector;
		}
		
		setNegotiating(false);
	}
	
	/**
	 * Offers the product to customers
	 */
	@ScheduledMethod(start=1,priority=2,interval=3)
	public void doProducing() {
		if (!isOperating()) {
			Story c;
			
			c = (Story) meet(Story.class);
			
			if (c!=null && c instanceof Story) {			
				//c.processOffer(goal.getProductVector());
				// 여기서 투자를 할지 말지 체크하는 기능 넣기  12월23일 24:00
			}
		}
	}
}
