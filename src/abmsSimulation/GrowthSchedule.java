package abmsSimulation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
	
	@ScheduledMethod(priority=1,interval=(7*2),start=7)
	public void GrowthCheck() {
		
		System.out.println("GrowthCheck()");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		int totalStoryNum = SimulBuilder.getStoryListCount();
		//double arrGrowth[] = totalStoryNum * 0.1;
		//HashMap<Story,Integer> thisWeekGI = new HashMap<Story,Integer>();
		ArrayList<Story> thisWeekGI = new ArrayList<Story>();
		
		for ( Object oIn: SimulBuilder.context.getRandomObjects(Story.class, totalStoryNum)) {
			
			// Story completed and settlement completed
			if(((Story)oIn).chkStoryCompleted() && ((Story)oIn).settleCompleted) 
				continue;
	
			thisWeekGI.add((Story)oIn);
		}
		
		Collections.sort(thisWeekGI, new Comparator<Story>() {
			@Override
			public int compare(Story t1, Story t2) {
				if(t1.gi.getTotalGIsumResult() > t2.gi.getTotalGIsumResult()) {
					return 1;
				}else if(t1.gi.getTotalGIsumResult() < t2.gi.getTotalGIsumResult()) {
					return -1;
				}else
					return 0;
			}
		});
		
		//int chknum = (int) (totalStoryNum * 0.8);
		System.out.println("thisWeekGI.size()   : " + thisWeekGI.size());
		//System.out.println("chkNum 8  : " + chknum);
		//System.out.println("chkNum 2  : " + totalStoryNum * 0.2);
		for(int i = thisWeekGI.size()-1 ; i >= thisWeekGI.size() * 0.8 ; i--) {
			System.out.println(thisWeekGI.get(i).gi.getTotalGIsumResult());
			// Story completed but settlement is not done
			if(thisWeekGI.get(i).chkStoryCompleted() && !thisWeekGI.get(i).settleCompleted ) {
				Set<PD> set = thisWeekGI.get(i).tomato.keySet();
				for(PD at : set){
					at.availableMoney += thisWeekGI.get(i).tomato.get(at) * 0.1;  // give 10% of the amount of what they stake
					SimulBuilder.ecoPoolMoney += thisWeekGI.get(i).tomato.get(at);  // give EcoPool same amount of what they stake
				}
				thisWeekGI.get(i).settleCompleted = true;
			}else { // Story is not done, settlement is not done 
				thisWeekGI.get(i).tentativeMoney = 0;
				Set<PD> set = thisWeekGI.get(i).tomato.keySet();
				for(PD at : set){
					thisWeekGI.get(i).tentativeMoney += thisWeekGI.get(i).tomato.get(at);
				}
			}
		}
	}
}
