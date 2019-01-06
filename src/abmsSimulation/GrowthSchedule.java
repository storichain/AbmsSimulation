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
	
	public double getTotalPDmoney() {
		double total = 0;
		for(int i=0; i < SimulBuilder.pdList.size(); i++) {
			total += SimulBuilder.pdList.get(i).availableMoney;
		}
		return total;
	}
	
	public double getTotalCirculation() {
		return getEcoPoolMoney() + getTotalPDmoney();
	}
	
	@ScheduledMethod(priority=1,interval=(7*2),start=7)
	public void GrowthCheck() {
		
		System.out.println("GrowthCheck()");
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		int totalStoryNum = SimulBuilder.getStoryListCount();
		
		ArrayList<Story> thisWeekGI = new ArrayList<Story>();
		thisWeekGI.clear();
		
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
		
		System.out.println("thisWeekGI.size()   : " + thisWeekGI.size());
		for(int i = thisWeekGI.size()-1 ; i >= thisWeekGI.size() * 0.8 ; i--) {
			System.out.println(thisWeekGI.get(i).gi.getTotalGIsumResult());
			// Story completed but settlement is not done
			if(thisWeekGI.get(i).chkStoryCompleted() && !thisWeekGI.get(i).settleCompleted ) {
				Set<PD> set = thisWeekGI.get(i).tomato.keySet();
				for(PD at : set){
					System.out.println("---------  before ------------- ");
					System.out.println("each availableMoney : " + at.availableMoney);
					System.out.println("same value of thisWeekGI : " + thisWeekGI.get(i).tomato.get(at));
					at.availableMoney += thisWeekGI.get(i).tomato.get(at) * 0.1;  // give 10% of the amount of what they stake
					System.out.println("---------  after ------------- ");
					System.out.println("each availableMoney : " + at.availableMoney);
					//System.out.println("same value of thisWeekGI : " + thisWeekGI.get(i).tomato.get(at));
					SimulBuilder.ecoPoolMoney += at.availableMoney;
					thisWeekGI.get(i).tomato.put(at, (double) 0);
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
