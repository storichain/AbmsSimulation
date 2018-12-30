package abmsSimulation;


import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.ShortestPath;

public class Agent implements Comparable<Object> {

	protected Context<Object> context;
	protected Network<Object> network;
	protected boolean negotiating;
	protected String label;
	protected double graphicsSize;
	protected double betweennessCentrality;

	public Agent(Context<Object> context, Network<Object> network, String label) {		

		this.context = context;
		this.network = network;
		this.label = label;
		this.negotiating = false;		
		this.graphicsSize = 0;
		this.betweennessCentrality = 0;
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
	
	public boolean isNegotiating() {
		return negotiating;
	}


	/**
	 * @param negotiating the negotiating to set
	 */
	public void setNegotiating(boolean negotiating) {
		this.negotiating = negotiating;
	}


	/**
	 * @return the graphicsSize
	 */
	public double getGraphicsSize() {
		return graphicsSize;
	}

	/**
	 * @param graphicsSize the graphicsSize to set
	 */
	public void setGraphicsSize(double graphicsSize) {
		this.graphicsSize = graphicsSize;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setBetweennessCentrality(double betweennessCentrality) {
		this.betweennessCentrality = betweennessCentrality;
	}
	
	public double getBetweennessCentrality() {
		return betweennessCentrality;
	}
	
	/**
	 * Returns the "degree centrality" of the agent
	 * @return centrality
	 */
	public double getDegreeCentralityUtility() {	
		return network.getDegree(this);
	}
	
	/**
	 * Returns the "connections utility" as defined by "Jackson and Wolinsky, 1996"
	 * @return utility
	 */
	public double getConnectionsUtility() {
		double sigma = 0.5;
		double utility = 0;
		
		ShortestPath<Object> sp = new ShortestPath<Object>(network);
				
		for(Object o: network.getNodes()) {
			if (!this.equals(o)) {
				utility += Math.pow(sigma, (double)sp.getPath(this, o).size());
			}
		}
		return utility;
	}
	
	@Override
	public int compareTo(Object a) {
		if (!(a instanceof PD)) {
			return 0;
		}else if (!(a instanceof RD)) {
			return 0;
		}else if (!(a instanceof ST)) {
			return 0;
		}
		
		double ownUtility, othersUtility;
		
		if (Parameters.utilityFunction.equals("ConnectionsUtility")) {
			ownUtility = getConnectionsUtility();
			othersUtility = ((Agent)a).getConnectionsUtility();
		} else if (Parameters.utilityFunction.equals("DegreeCentrality")) {
			ownUtility = getDegreeCentralityUtility();
			othersUtility = ((Agent)a).getDegreeCentralityUtility();
		} else if (Parameters.utilityFunction.equals("BetweennessCentrality")) {
			ownUtility = getBetweennessCentrality();
			othersUtility = ((Agent)a).getBetweennessCentrality();
		} else {
			ownUtility = RandomHelper.nextDouble();
			othersUtility = RandomHelper.nextDouble();
		}
		
		return ((Double)ownUtility).compareTo((Double)othersUtility);
	}
	
	/**
	 * Meet an entity of an specified type (all agents such as PD, ST, RD, Story)
	 * @param Class<?> Entity type 
	 * @return Object An acquaintance
	 */
	/*
	public Object meet(Class<?> type) {		
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		int i = 0;
		Object o = this, acquaintance = this;
		
		while (i < depth && o != null) {
			o = network..getRandomAdjacent(acquaintance);
			//network.
			// for ( Object oIn: context.getRandomObjects(Story.class, SimulBuilder.storyList.size())) {

			if (o != null && o.getClass() == type && !((Agent)o).isNegotiating()) {
				acquaintance = o;
			}
			i++;
		}
		
		if (acquaintance == this) {
			acquaintance = null;
		}
		
		return acquaintance;
	}
	*/
	
	// meet no connection before
	public Object meet( Object at) {		
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		Object o = null;

		for ( Object oIn: context.getRandomObjects(Story.class, SimulBuilder.getStoryListCount())) {
				
			if(network.isAdjacent(at, oIn)) {
				//System.out.println(" is Adjacent() ");
				continue;
			}
			
			o = oIn;
		}
/*		
		for(Object ot : network.getNodes()) {
			if(!(ot instanceof Story))
				continue;
			if(network.isAdjacent(ot, at))
				continue;
			System.out.println(ot.toString());
			o = ot;
			break;
		}
*/		
		return o;
	}
}