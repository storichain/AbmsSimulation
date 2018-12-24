package abmsSimulation;

import java.awt.Color;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.RepastEdge;

public class TempSchedule {

	SimulBuilder builder;
	
	public TempSchedule(SimulBuilder builder) {
		this.builder = builder;
	}
	
	
	@ScheduledMethod(priority=5,interval=2,start=10)
	public void myEvolveNetwork() {
		
		System.out.println("myEvolveNetwork");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r < Parameters.newConnectionsProbability) {
			int random = RandomHelper.nextIntFromTo(1, 5);
			
			Object attached;
			
			switch (random) {
				default:
					PD c = new PD(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("P"));
					SimulBuilder.networkGenerator.attachNode(c);
					attached = c;
					break;
//				case 1:
//					RD rd = new RD(context, network, nextId("R"));
//					networkGenerator.attachNode(rd);		
//					attached = rd;
//					break;
				case 2:
					ST e = new ST(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("S"));
			//		e.generateGoal();
					SimulBuilder.networkGenerator.attachNode(e);					
					attached = e;				
					break;
			}
			
			for (RepastEdge<Object> edge: SimulBuilder.network.getEdges(attached)) {
				((CustomNetworkEdge) edge).setThickness(5.0); 
				((CustomNetworkEdge) edge).setColor(Color.red);
			}
			
			SimulBuilder.calculateBetweennesCentralities();
		}
	}

}
