package abmsSimulation;

import java.awt.Color;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class AdjacentNetworkGenerator extends MainNetworkGenerator implements NetworkGenerator<Object> {

	public AdjacentNetworkGenerator(Context<Object> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Network<Object> createNetwork(Network<Object> network) {		

		this.network = network;

		initializeNetwork(getEdgeProbability());

		return network;
	}
	
	
	// This doesn't use 
	public void attachNode(Object n) {	
		context.add(n);

		//double totalDegree = network.getDegree();
		boolean attached = false;

		int chk = 0;
	
		while (!attached) {
			
			Object o = null;
			
		//	System.out.println(" getRandomeObjects() : " + context.getRandomObjects(Story.class, SimulBuilder.storyList.size()));
			for ( Object oIn: context.getRandomObjects(Story.class, SimulBuilder.getStoryListCount())) {
			
				for (Object o1: network.getAdjacent(oIn)) {
					if(!n.equals(o1)) {
						break;
					}
				}
				o = oIn;
			}
				
			network.addEdge(n, o);
			attached = true;
			
		}			
	}

}
