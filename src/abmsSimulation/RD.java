package abmsSimulation;

import java.util.Collections;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class RD extends Agent {
	
	protected int[] demandVector;		// RD's Demand. when demand matches with ProductDemand on Story

	public RD(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		demandVector = new int[Parameters.vectorSpaceSize];
		initializeDemandVector();
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
	
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doReaction() {
		
		System.out.println("doReaction()");
		
		setNegotiating(true);
		
		//if (!isProducing()) {
			Story c;
			
			//c = (Story) meet(Story.class);
			c = (Story) meet( this);
			System.out.println("getLabel() : " + c);
			
			if (c!=null && c instanceof Story) {
				//c.processOffer(goal.getProductVector());
	
				//double chkAvailableMoney = c.processStaking(demandVector, (availableMoney * 0.1));
				//availableMoney -= chkAvailableMoney;
				c.processReaction(demandVector);
				
				network.addEdge(this, c);
			}
		//}
		setNegotiating(false);
	}

}
