package abmsSimulation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

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
		InitEvolveNetwork();

		return network;
	}
	
	/**
	 * Preferential attachment  // 주변에 클러스터가 큰 그룹에 붙는다.
	 * @param n Node to be attached
	 */
	public void attachNode(Object n) {
		System.out.println("attachNode() in Barabasi");
		
		System.out.println("check Object in attachNode : " + n.getClass());
		
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
				}			
			}
		}
	}

}

