package abmsSimulation;

import java.awt.Color;
import java.util.Set;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;


public class GrowthSchedule {
	
	//SimulBuilder builder;
	
	public GrowthSchedule() {  //SimulBuilder builder) {
		//this.builder = builder;		
	}
	
	@ScheduledMethod(priority=1,interval=7,start=7)
	public void GrowthCheck() {
		
		System.out.println("GrowthCheck()");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		//SimulBuilder.network.getNodes();
		
		for ( Object oIn: SimulBuilder.context.getRandomObjects(Story.class, SimulBuilder.getStoryListCount())) {
			
			System.out.println(((Story)oIn).chkKeyPosition);
			
			double totalAddition = 0;
			int count = 0;
			Set<Agent> set = ((Story)oIn).tomato.keySet();
			for(Agent agent : set){
				if (count <= ((Story)oIn).chkKeyPosition){
					count++;
				}else {
					totalAddition += ((Story)oIn).tomato.get(agent);
				}
			}
			((Story)oIn).chkKeyPosition = set.size();
			SimulBuilder.ecoPoolMoney += totalAddition;
		}
		
//		if (r < Parameters.newConnectionsProbability) {
//			int random = RandomHelper.nextIntFromTo(1, 5);
//			
//			Object attached;
//			
//			switch (random) {
//				default:
//					PD c = new PD(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("P"));
//					SimulBuilder.networkGenerator.attachNode((Object)c);
//					SimulBuilder.pdList.add(c);
//					attached = c;
//					break;
////				case 1:networkGenerator
////					RD rd = new RD(context, network, nextId("R"));
////					networkGenerator.attachNode(rd);		
////					attached = rd;
////					break;
//				case 2:
//					ST e = new ST(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("S"));
//			//		e.generateGoal();
//					SimulBuilder.networkGenerator.attachNode(e);
//					SimulBuilder.stList.add(e);
//					attached = e;				
//					break;
//			}
//		}
	}
}
