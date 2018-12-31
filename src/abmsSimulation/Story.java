package abmsSimulation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Story extends Agent {


	public HashMap<ST, Double> potato;		// copyright
	public HashMap<PD, Double> tomato;		// earning rate
	public int chkKeyPosition;
	public boolean storyCompleted;
	public boolean settleCompleted;
	private int[] productVector;   // Story's score as Quantity and Quality
	public int deadlineWeek;
	public GrowthIndex gi;
	
	public double tentativeMoney;
	
	
	public Story(Context<Object> context, Network<Object> network, String label, int week) {
		super(context, network, label);	
		productVector = new int[Parameters.vectorSpaceSize];
		generateRandomProductVector();
		storyCompleted = false;
		settleCompleted = false;
		//tomato = new HashMap<PD, Double>();
		tomato = new HashMap<PD,Double>();
		potato = new HashMap<ST,Double>();

		chkKeyPosition = 0;
		tentativeMoney = 0;
		deadlineWeek = week;
		gi = new GrowthIndex();
	}
	
	// 스토리완성기준
	// 1. 작가가 완성기간을 설정
	// 2. 연장싶으면 지금까지 스테이킹한 금액에 1% 스테이킹을 마스터작가 해야 함.
	
	
	public boolean chkStoryCompleted() {
		double tickCount = repast.simphony.engine.environment.RunEnvironment.getInstance().getCurrentSchedule().getTickCount();		
		if(tickCount >= (deadlineWeek * 7) ) {
			storyCompleted = true;
		}
		return storyCompleted;
	}
	
	public double totalTomato() {
		double totalAddition = 0;
		Set<PD> set = tomato.keySet();
		for(PD agent : set){
			totalAddition += tomato.get(agent);
		}
		return totalAddition;
	}
	
	/**
	 * Story scenario:
	 * Aggregate product vector based on the demand of the surrounding PD
	 */
	public void aggregateProductVector() {
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		List<PD> pds = new ArrayList<PD>();
		
		SimulBuilder.getPDAcquiantances(this, depth, pds);		
		
		if (pds.size() > 0) {
			Collections.shuffle(pds);
			
			int sampleTotal = RandomHelper.nextIntFromTo(1, pds.size());
			
			//Survey random connected customers sample
			
			int[] surveyResults = new int[Parameters.vectorSpaceSize];
			
			for (int i = 0; i < surveyResults.length; i++) {
				surveyResults[i] = 0;
			}
			
			for (int i = 0; i < sampleTotal; i++) {
				int[] demandVector = pds.get(i).demandVector;
				
				for (int j = 0; j < demandVector.length; j++) {
					surveyResults[j] += demandVector[j];
				}
			}
			
			//int[] productVector = goal.getProductVector();
			
			for (int i = 0; i < surveyResults.length; i++) {				
				if ( ((double)surveyResults[i] / (double)sampleTotal) * 100 >= Parameters.productElementChangeThreshold) {					
					productVector[i] = (productVector[i] + 1) % 2;
				}
			}
		}
	}
	
	public int[] getProductVector() {
		return productVector;
	}
	
	public void setProductVector(int[] productVector) {
		this.productVector = productVector;
		//this.generateRequiredMeans();
	}
	
	public String printProductVector() {
		String s = "";
		
		for (int i = 0; i < productVector.length; i++) {
			s += String.valueOf(productVector[i]);
		}
		return s;
	}
	
	public void generateRandomProductVector() {
		for (int i = 0; i < productVector.length; i++) {
			productVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}
	
	/**
	 * Process offer made by an entrepreneur
	 * @param productVector
	 */
	//public int[] processStaking(int[] demandVector) {
	public double processStaking(PD agent, int[] demandVector, double availableMoney) {

		//int[] returnValue = demandVector;
		boolean chk = false;
		
		int d = SimulBuilder.hammingDistance(demandVector, productVector);
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (d>0 && d <= Math.ceil(Parameters.vectorSpaceSize / 2.0) && r < (Parameters.customersPersuadability / 100.0)) {
			if(checkAvailableStakingReaction(demandVector)) {
				//tomato += availableMoney;
				tomato.put(agent, availableMoney);
				chk = true;
			}
		}
		
		if(!chk) {
			availableMoney = 0;
		}
		
		return availableMoney;
	}
	

	public boolean checkAvailableStakingReaction(int demandVector[]) {
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r <= 0.5 && SimulBuilder.hammingDistance(demandVector, productVector) < 3) {
			return true;
		}
		return false;
	}
	
	public boolean processReaction(int[] demandVector) {
		boolean chk = false;
		
		int d = SimulBuilder.hammingDistance(demandVector, productVector);
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (d>0 && d <= Math.ceil(Parameters.vectorSpaceSize / 2.0) && r < (Parameters.customersPersuadability / 100.0)) {
			if(checkAvailableStakingReaction(demandVector)) {
				gi.reactionMetric += 1;
				chk = true;
			}
		}
		
		return chk;
	}
	
	public boolean processReading(int[] demandVector) {
		boolean chk = false;
		
		int d = SimulBuilder.hammingDistance(demandVector, productVector);
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (d>0 && d <= Math.ceil(Parameters.vectorSpaceSize / 2.0) && r < (Parameters.customersPersuadability / 100.0)) {
			if(checkAvailableStakingReaction(demandVector)) {
				gi.readingMetric += 1;
				chk = true;
			}
		}
		
		return chk;
	}
}
