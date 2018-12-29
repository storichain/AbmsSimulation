package abmsSimulation;

import java.awt.Color;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;


public class BarabasiAlbertNetworkGenerator extends MainNetworkGenerator implements NetworkGenerator<Object> {

	
	public BarabasiAlbertNetworkGenerator(Context<Object> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see repast.simphony.context.space.graph.RandomDensityGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {		

		this.network = network;

		initializeNetwork(getEdgeProbability());	

		// Evolve network using preferential attachment
		//InitEvolveNetwork();
		
//		scheduleMethodTest();

		return network;
	}
	
	/**
	 * Preferential attachment  // 주변에 클러스터가 큰 그룹에 붙는다.
	 * @param n Node to be attached
	 */
	public void attachNode(Object n) {	
		context.add(n);

	//	for (int i = 0; i < edgesPerStep; i++) {
			//Get the total degree (number of edges) for the graph.
			double totalDegree = network.getDegree();
			boolean attached = false;
	
			int chk = 0;
		
			while (!attached) {
				
				Object o = null;
				
			//	System.out.println(" getRandomeObjects() : " + context.getRandomObjects(Story.class, SimulBuilder.storyList.size()));
				for ( Object oIn: context.getRandomObjects(Story.class, SimulBuilder.storyList.size())) {
			//		System.out.println(" check Object oINn : " + oIn);
				
					for (Object o1: network.getAdjacent(oIn)) {
						if(n.equals(o1)) {
							System.out.println("---------------------- already connected ---------------");
							break;
						}
					}
					o = oIn;
				}
					
				//System.out.println(" check Object : " + o);
				double prob = (network.getDegree(o) + 1) / (totalDegree + network.size());
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) <= prob) {
					network.addEdge(n, o);
					
					attached = true;
					for (RepastEdge<Object> edge: SimulBuilder.network.getEdges(n)) {
						//RepastEdge<Object> edge = (RepastEdge<Object>) SimulBuilder.network.getEdges(n);
						((CustomNetworkEdge) edge).setColor(Color.BLUE);
						((CustomNetworkEdge) edge).setThickness(3.0); 
						//System.out.println("((CustomNetworkEdge) edge).getRed() : " +((CustomNetworkEdge) edge).getRed());
						//System.out.println("((CustomNetworkEdge) edge).getBlue() : " +((CustomNetworkEdge) edge).getBlue());
					}
				}
			}			
		//}
	}
	
	/*
	public void attachNode(Object n) {
//		System.out.println("attachNode() in Barabasi");
		
//		System.out.println("check Object in attachNode : " + n.getClass());
		
		context.add(n);
		//When checking the network degree, look only at the "entreprenurial network",
		// i.e at the network without means and goals
		for (int i = 0; i < edgesPerStep; i++) {
			
			double totalDegree = network.getDegree();
			boolean attached = false;
			
			while (!attached) {
				
				Object o = null;
				do {
					o = context.getRandomObject();
//					if(o instanceof TempSchedule)
//						System.out.println("check instanceof -------------------------");
					
				} while(o instanceof TempSchedule);
				
				double prob = (network.getDegree(o) + 1) / (totalDegree + network.size());
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) <= prob) {
					network.addEdge(n, o);
					
					attached = true;
					for (RepastEdge<Object> edge: SimulBuilder.network.getEdges(n)) {
						//RepastEdge<Object> edge = (RepastEdge<Object>) SimulBuilder.network.getEdges(n);
						((CustomNetworkEdge) edge).setColor(Color.BLUE);
						((CustomNetworkEdge) edge).setThickness(3.0); 
						//System.out.println("((CustomNetworkEdge) edge).getRed() : " +((CustomNetworkEdge) edge).getRed());
						//System.out.println("((CustomNetworkEdge) edge).getBlue() : " +((CustomNetworkEdge) edge).getBlue());
					}
				}			
			}
		}
	}*/

}

