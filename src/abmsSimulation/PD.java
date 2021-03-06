package abmsSimulation;

import java.util.HashMap;


import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;


public class PD extends Agent {
	
	protected int[] demandVector;		// PD's Demand. when demand matches with ProductDemand on Story, PD does invest.
	
	protected boolean isProducing;	    // for checking staking on one time per an activity
	public double availableMoney;
	//public GrowthIndex myGIrate;

	public PD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		demandVector = new int[Parameters.vectorSpaceSize];
		initializeDemandVector();
		initializeAvailableMoney();
		isProducing = false;
		 
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
	
	public void initializeAvailableMoney() {
		//availableMoney = RandomHelper.nextDoubleFromTo(0, 100);
		availableMoney =  RandomHelper.nextIntFromTo(5, 10);
		SimulBuilder.ecoPoolMoney -= availableMoney;
	}
	
	public double getAvailableMoney() {
		return  availableMoney;
	}
	
	public boolean isProducing() {
		return isProducing;
	}
	
	public void setIsProducing(boolean isProducing) {
		this.isProducing = isProducing;
		if (isProducing) {
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

	
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doProducing() {
		System.out.println("doProducing");
		//setNegotiating(!isNegotiating());
		//System.out.println("Nego : " + isNegotiating());
		if (!isNegotiating()) {
			
			setNegotiating(true);
			Story c;
			
			//c = (Story) meet(Story.class);
			c = (Story) meet( this);
			
			if (c!=null && c instanceof Story) {
				//c.processOffer(goal.getProductVector());
	
				double chkAvailableMoney = c.processStaking(this, demandVector, (availableMoney * 0.1));
				availableMoney -= chkAvailableMoney;
				
				network.addEdge(this, c);
			}
			
			setNegotiating(false);
		}
	}
	
	public void doVerify() {
		
		int additionMetric = 0;
		for(Object ot : network.getAdjacent(this)) {
			if(!(ot instanceof Story))
				continue;
			
			//((Story)ot).gi.verifyMetrics. += 1;
			
			if(((Story)ot).gi.verifyMetrics.containsKey(this)) {
				additionMetric = ((Story)ot).gi.verifyMetrics.get(this) + 1;
				((Story)ot).gi.verifyMetrics.put(this, additionMetric);
			}else
				((Story)ot).gi.verifyMetrics.put(this, 1);
			
			break;
		}
	}
	
	public void doBranching() {
		int additionMetric = 0;
		for(Object ot : network.getAdjacent(this)) {
			if(!(ot instanceof Story))
				continue;
			
			if(((Story)ot).gi.branchingMetrics.containsKey(this)) {
				additionMetric = ((Story)ot).gi.branchingMetrics.get(this) + 1;
				((Story)ot).gi.branchingMetrics.put(this, additionMetric);
			}else
				((Story)ot).gi.branchingMetrics.put(this, 1);
			
			break;
		}
	}
	
	
	public void doPDaction() {
		int random = RandomHelper.nextIntFromTo(1, 5);
		
		switch(random) {
			default:
				doProducing();
				break;
			case 1:
				doVerify();
				break;
			case 2:
				doBranching();
				break;
		}
	}
	
}
