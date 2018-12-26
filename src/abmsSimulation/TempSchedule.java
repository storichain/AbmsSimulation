package abmsSimulation;

import java.awt.Color;


import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.editedStyle.DefaultEditedEdgeStyleData2D;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyle2D;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;


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
					SimulBuilder.networkGenerator.attachNode((Object)c);
					SimulBuilder.pdList.add(c);
					attached = c;
					break;
//				case 1:networkGenerator
//					RD rd = new RD(context, network, nextId("R"));
//					networkGenerator.attachNode(rd);		
//					attached = rd;
//					break;
				case 2:
					ST e = new ST(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("S"));
			//		e.generateGoal();
					SimulBuilder.networkGenerator.attachNode(e);
					SimulBuilder.stList.add(e);
					attached = e;				
					break;
			}
			
//			do {
//				e = (Entrepreneur)context.getRandomObjects(Entrepreneur.class, 1).iterator().next();
//			} while (e instanceof Causator);
//			context.remove(e);
//			attachNode(SimulationBuilder.effectuator);
			
			
			for (RepastEdge<Object> edge: SimulBuilder.network.getEdges(attached)) {
				System.out.println("before chk getEdges(attached) : " + ((CustomNetworkEdge) edge).getColor());
				System.out.println("before chk getEdges(attached) : " + ((CustomNetworkEdge) edge).getThickness());
				((CustomNetworkEdge) edge).setThickness(5.0); 
				((CustomNetworkEdge) edge).setColor(Color.red);
				
				System.out.println(" chk getEdges(attached) : " + ((CustomNetworkEdge) edge).getColor());
				System.out.println(" chk getEdges(attached) : " + ((CustomNetworkEdge) edge).getThickness());
				
				
				//edge.notifyAll();
				
				//getStyleDirName();
				EditedEdgeStyleData<Object> aa = EditedStyleUtils.getEdgeStyle("fullnetwork.style_0.xml");
				
				System.out.println(" EdgeStyle check Color[0] : " + aa.getColor()[0] );
				
				float[] colorSet2 = {255,0,0}; 
				aa.setColor(colorSet2);
				
				System.out.println(" EdgeStyle check Color[0] : " + aa.getColor()[0] );
				
				//repast.simphony.visualization.editedStyle.EditedStyle2D
				//EditedStyle2D style2d = new EditedStyle2D("fullnetwork.style_0.xml");
				
				//DefaultEditedEdgeStyleData2D<EditedEdgeStyleData> mm = new DefaultEditedEdgeStyleData2D<EditedEdgeStyleData>();
				DefaultEditedEdgeStyleData2D<Object> mm = (DefaultEditedEdgeStyleData2D<Object>) aa;
				System.out.println(" check Color[0] : " + mm.getColor()[0] );
				System.out.println(" check Color[1] : " + mm.getColor()[1] );
				System.out.println(" check Color[2] : " + mm.getColor()[2] );
				
				
				float[] colorSet = {255,0,0}; 
				mm.setColor(colorSet);
				
				System.out.println(" check Color[0] : " + mm.getColor()[0] );
				System.out.println(" check Color[1] : " + mm.getColor()[1] );
				System.out.println(" check Color[2] : " + mm.getColor()[2] );
			}
			
			
			//network.addEdge(n, o);
			
			SimulBuilder.calculateBetweennesCentralities();
		}
	}

}
