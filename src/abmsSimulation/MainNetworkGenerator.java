package abmsSimulation;

import java.awt.Color;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;


public abstract class MainNetworkGenerator implements NetworkGenerator<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	
	protected double edgeProbability;
	protected int edgesPerStep;
	protected int totalST;
	protected int totalPD;
	protected int totalRD;
	protected int totalStory;
	
	
	public MainNetworkGenerator(Context<Object> context) {

		this.context = context;
		edgesPerStep = 0;
		totalST = 0;
		totalPD = 0;
		totalRD = 0;
		totalStory = 0;
	}
	
	public void setTotalStory(int totalStory) {
		this.totalStory = totalStory;
	}
	
	public int getTotalStory() {
		return totalStory;
	}
	
	public void setTotalST(int totalST) {
		this.totalST = totalST;
	}
	
	public int getTotalST() {
		return totalST;
	}
	
	public void setTotalRD(int totalRD) {
		this.totalRD = totalRD;
	}
	
	public int getTotalRD() {
		return totalRD;
	}
	
	public void setTotalPD(int totalPD) {
		this.totalPD = totalPD;
	}
	
	public int getTotalPD() {
		return totalPD;
	}

	
	public Context<Object> getContext() {
		return context;
	}


	public void setContext(Context<Object> context) {
		this.context = context;
	}


	public Network<Object> getNetwork() {
		return network;
	}


	public void setNetwork(Network<Object> network) {
		this.network = network;
	}
	
	/**
	 * @return the edgesPerStep
	 */
	public int getEdgesPerStep() {
		return edgesPerStep;
	}

	/**
	 * @param edgesPerStep the edgesPerStep to set
	 */
	public void seEdgesPerStep(int edgesPerStep) {
		this.edgesPerStep = edgesPerStep;
	}

	
	/**
	 * @return the edgeProbability
	 */
	public double getEdgeProbability() {
		return edgeProbability;
	}

	/**
	 * @param edgeProbability the edgeProbability to set
	 */
	public void setEdgeProbability(double edgeProbability) {
		this.edgeProbability = edgeProbability;
	}
	
	/**
	 * Initializes the network
	 * @param p initial wiring probability
	 */
	protected void initializeNetwork(double pp) {
		System.out.println("totalPD : " + totalPD); 
		System.out.println("totalST : " + totalST); 
		System.out.println("totalRD : " + totalRD); 
		System.out.println("totalStory : " + totalStory); 
		
		for (int i = 0;  i < totalPD; i++) {

			PD p1 = new PD(context, network, SimulBuilder.nextId("P"));
			context.add(p1);
			SimulBuilder.pdList.add(p1);
			//totalPD--;
			System.out.println(" p1.getLabel() : " + p1.getLabel());
		}
		
		for (int i = 0;  i < totalST; i++) {

			ST p1 = new ST(context, network, SimulBuilder.nextId("S"));
			context.add(p1);
			SimulBuilder.stList.add(p1);
			
		}
		
		for (int i = 0;  i < totalRD; i++) {

			RD p1 = new RD(context, network, SimulBuilder.nextId("R"));
			context.add(p1);
			SimulBuilder.rdList.add(p1);
			
		}
		
		for (int i = 0; i < totalStory; i++) {
			int deadlineWeek = 16;
			Story to = new Story(context, network, SimulBuilder.nextId("T"), deadlineWeek);
			context.add(to);
			//SimulBuilder.storyList.add(to);
		}
		
		defaultRandomWire(pp);
	}
	
	public void defaultRandomWire(double pp) {
		
		//Initial wiring using a random network
		for (Object i: network.getNodes()) {
			if(!(i instanceof Story))
				continue;
			for (Object j: network.getNodes()) {
				if(!(j instanceof ST))
					continue;
				double random = RandomHelper.nextDoubleFromTo(0, 1);
				// Only Storytellor can connect with Storeis
				if (random <= pp && !i.equals(j)  ){
					network.addEdge(i, j);
				}
			}
		}
	}
	
		
	@ScheduledMethod(priority=5,interval=3,start=10)
	public void myEvolveNetwork() {
		
		System.out.println("myEvolveNetwork");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r < Parameters.newConnectionsProbability) {
			int random = RandomHelper.nextIntFromTo(1, 5);
			
			Object attached;
			
			switch (random) {
				default:
					RD rd = new RD(context, network, SimulBuilder.nextId("R"));
					context.add(rd);
					//SimulBuilder.networkGenerator.attachNode(rd);
					SimulBuilder.rdList.add(rd);
					attached = rd;
					break;
				case 1:
					PD c = new PD(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("P"));
					context.add(c);
					//SimulBuilder.networkGenerator.attachNode(c);
					SimulBuilder.pdList.add(c);
					attached = c;
					break;
				case 2:
					ST e = new ST(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("S"));
					context.add(e);
					//SimulBuilder.networkGenerator.attachNode(e);
					SimulBuilder.stList.add(e);
					attached = e;				
					break;
			}
//			
//			for (RepastEdge<Object> edge: SimulBuilder.network.getEdges(attached)) {
//				System.out.println("before chk getEdges(attached) : " + ((CustomNetworkEdge) edge).getColor());
//				System.out.println("before chk getEdges(attached) : " + ((CustomNetworkEdge) edge).getThickness());
//				((CustomNetworkEdge) edge).setColor(Color.BLUE);
//				((CustomNetworkEdge) edge).setThickness(3.0); 
//				System.out.println("((CustomNetworkEdge) edge).getRed() : " +((CustomNetworkEdge) edge).getRed());
//				System.out.println("((CustomNetworkEdge) edge).getBlue() : " +((CustomNetworkEdge) edge).getBlue());
//			}
//			SimulBuilder.calculateBetweennesCentralities();
		}
	}
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		// TODO Auto-generated method stub
		return network;
	}
	
	public void attachNode(Object n) {
		
	}

}
