package abmsSimulation;


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
	
	public void doRDaction() {
		int random = RandomHelper.nextIntFromTo(1, 5);
		
		switch(random) {
			default:
				doReading();
				break;
			case 1:
				doReaction();
				break;
			case 2:
				doShareTip();
				break;
		}
	}
	
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doReaction() {
		
		System.out.println("doReaction()");
		
		setNegotiating(true);
		
		if (isNegotiating()) {
			Story c;
			
			//c = (Story) meet(Story.class);
			c = (Story) meet( this);
			//System.out.println("getLabel() : " + c);
			
			if (c!=null && c instanceof Story) {
				if(c.processReaction(demandVector))
					network.addEdge(this, c);
			}
		}
		setNegotiating(false);
	}
	
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doReading() {
			
		System.out.println("doReading()");
		
		setNegotiating(true);
		
		if (isNegotiating()) {
			Story c;
			
			//c = (Story) meet(Story.class);
			c = (Story) meet( this);
			//System.out.println("getLabel() : " + c);
			
			if (c!=null && c instanceof Story) {
				if(c.processReading(demandVector))
					network.addEdge(this, c);
			}
		}
		setNegotiating(false);
	}
	
	public void doShareTip() {
		
		System.out.println("doShareTip()");
		
		setNegotiating(true);
		
		if (isNegotiating()) {
			Story c;
			
			//c = (Story) meet(Story.class);
			c = (Story) meet( this);
			//System.out.println("getLabel() : " + c);
			
			if (c!=null && c instanceof Story) {
				if(c.processShareTip(demandVector))
					network.addEdge(this, c);
			}
		}
		setNegotiating(false);
	}

}
