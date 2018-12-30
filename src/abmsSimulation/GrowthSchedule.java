package abmsSimulation;


import java.util.Set;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;


public class GrowthSchedule {
	
	//SimulBuilder builder;
	
	public GrowthSchedule() {  //SimulBuilder builder) {
		//this.builder = builder;		
	}
	
	public double getEcoPoolMoney() {
		return SimulBuilder.ecoPoolMoney;
	}
	
	@ScheduledMethod(priority=1,interval=7,start=7)
	public void GrowthCheck() {
		
		System.out.println("GrowthCheck()");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		//SimulBuilder.network.getNodes();
		
		for ( Object oIn: SimulBuilder.context.getRandomObjects(Story.class, SimulBuilder.getStoryListCount())) {
			
			//System.out.println(((Story)oIn).chkKeyPosition);
			//double totalAddition = 0;
			if(((Story)oIn).chkStoryCompleted()) {
				Set<PD> set = ((Story)oIn).tomato.keySet();
				for(PD agent : set){
					agent.availableMoney += ((Story)oIn).tomato.get(agent) * 2;  // 2 times
					SimulBuilder.ecoPoolMoney += ((Story)oIn).tomato.get(agent);
				}
			}else {
				((Story)oIn).tentativeMoney = 0;
				int count = 0;
				Set<PD> set = ((Story)oIn).tomato.keySet();
				for(PD agent : set){
//					if (count <= ((Story)oIn).chkKeyPosition){
//						count++;
//					}else {
//						totalAddition += ((Story)oIn).tomato.get(agent);
//					}
					((Story)oIn).tentativeMoney += ((Story)oIn).tomato.get(agent);
				}
				//((Story)oIn).chkKeyPosition = set.size();
				//SimulBuilder.ecoPoolMoney += totalAddition;
			}
			
		}
		
	}
}
