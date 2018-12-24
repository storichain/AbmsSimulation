package abmsSimulation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;


public abstract class MainNetworkGenerator implements NetworkGenerator<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	
	protected double edgeProbability;
	protected int edgesPerStep;
	protected int totalST;
	protected int totalPD;
	protected int totalRD;
	
	
	public MainNetworkGenerator(Context<Object> context) {

		this.context = context;
		edgesPerStep = 0;
		totalST = 0;
		totalPD = 0;
		totalRD = 0;
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
		
		for (int i = 0; i < 10 && i < totalPD; i++) {

			PD p1 = new PD(context, network, SimulBuilder.nextId("P"));
			context.add(p1);
			SimulBuilder.pdList.add(p1);
			totalPD--;
		}
		//randomWire(pp);
		
		for (int i = 0; i < 10 && i < totalST; i++) {

			ST p1 = new ST(context, network, SimulBuilder.nextId("S"));
			context.add(p1);
			SimulBuilder.stList.add(p1);
			totalST--;
		}
		
		randomWire(pp);
	}
	
	public void randomWire(double p) {
		
		//Initial wiring using a random network
		for (Object i: network.getNodes()) {
			for (Object j: network.getNodes()) {
				double random = RandomHelper.nextDoubleFromTo(0, 1);
				if (random <= p && !i.equals(j)) {
					network.addEdge(i, j);
				}
			}
		}
	}
	

	
	public void InitEvolveNetwork() {
		System.out.println("InitEvolveNetwork()");
		System.out.println("totlaST : " + totalST);
		System.out.println("totalPD : " + totalPD);
		
		while (totalST > 0 || totalPD > 0) {
			
			double random = RandomHelper.nextDoubleFromTo(0, 1);
			
			//Enter ST with less probability than  PD
			if (totalST > 0 && random <= 0.33) {
				/*
				ST e = null;
				 * int type = RandomHelper.nextIntFromTo(1, 2);
				
				if (type == 1 && SimulBuilder.currentST != null && !context.contains(SimulBuilder.currentST)) {
					e = SimulBuilder.currentST;
				} else {
					e = new ST(context, network, SimulBuilder.nextId("S"));
					//e.generateGoal();
				}
				*/
				ST c = new ST(context, network, SimulBuilder.nextId("S"));
			
				SimulBuilder.stList.add(c);
				attachNode(c);
				totalST--;
			} else if (totalPD > 0) {        //  여기서 부터 작업 12월 17일 effectuator  방식으로 처
				PD c = new PD(context, network, SimulBuilder.nextId("P"));
				attachNode(c);
				SimulBuilder.pdList.add(c);
				totalPD--;
			}
		}
	/*
		//Assure the presence of the effectuator and/or causator
		if (SimulBuilder.currentST != null && !context.contains(SimulBuilder.currentST)) {
			Agent e = null;
			do {
				e = (ST)context.getRandomObjects(ST.class, 1).iterator().next();
				//System.out.println("xxxxxxxxxxxxxxx");
			} while (e instanceof RD || e instanceof PD);
			context.remove(e);
			attachNode(SimulBuilder.currentST);
		}
		
		if (SimulBuilder.currentPD != null && !context.contains(SimulBuilder.currentPD)) {
			Agent e = null;
			do {
				e = (PD)context.getRandomObjects(PD.class, 1).iterator().next();
				//System.out.println("yyyyyyyyyyyyy");
			} while (e instanceof ST || e instanceof RD);
			context.remove(e);
			attachNode(SimulBuilder.currentPD);
		}
		*/
	}
	
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		// TODO Auto-generated method stub
		return network;
	}
	
	public void attachNode(Object n) {
		
	}

}
