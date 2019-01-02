package abmsSimulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GrowthIndex {

	// RD metrics
	public HashMap<RD,Integer> reactionMetrics;
	public HashMap<RD,Integer> readingMetrics;
	public HashMap<RD,Integer> shareTipMetrics;
	// ST metrics
	public HashMap<ST,Integer> typingMetrics;
	public HashMap<ST,Integer> sceneMetrics;
	// PD metrics
	public HashMap<PD,Integer> verifyMetrics;
	public HashMap<PD,Integer> branchingMetrics;
	
	public GrowthIndex() {
		reactionMetrics = new HashMap<RD, Integer>();
		readingMetrics = new HashMap<RD, Integer>();
		shareTipMetrics = new HashMap<RD, Integer>();
		typingMetrics = new HashMap<ST, Integer>();
		sceneMetrics = new HashMap<ST, Integer>();
		verifyMetrics = new HashMap<PD, Integer>();
		branchingMetrics = new HashMap<PD, Integer>();
	}
	
	public int getTotalGIsumResult() {
		return getReactionMetrics() + getReadingMetrics() + getShareTipMetrics() + getTotalTypingMetrics() + getTotalsceneMetrics()
		+ getTotalVerifyMetrics() + getTotalBranchingMetrics();
	}
	
	public int getReactionMetrics() {
		int totalAddition = 0;
		Set<RD> set = reactionMetrics.keySet();
		for(RD at : set){
			totalAddition += reactionMetrics.get(at);
		}
		return totalAddition;
	}
	
	public int getReadingMetrics() {
		int totalAddition = 0;
		Set<RD> set = readingMetrics.keySet();
		for(RD at : set){
			totalAddition += readingMetrics.get(at);
		}
		return totalAddition;
	}
	
	public int getShareTipMetrics() {
		int totalAddition = 0;
		Set<RD> set = shareTipMetrics.keySet();
		for(RD at : set){
			totalAddition += shareTipMetrics.get(at);
		}
		return totalAddition;
	}
	
	public int getTotalTypingMetrics() {
		int totalAddition = 0;
		Set<ST> set = typingMetrics.keySet();
		for(ST at : set){
			totalAddition += typingMetrics.get(at);
		}
		return totalAddition;
	}
	
	public int getTotalsceneMetrics() {
		int totalAddition = 0;
		Set<ST> set = sceneMetrics.keySet();
		for(ST at : set){
			totalAddition += sceneMetrics.get(at);
		}
		return totalAddition;
	}
	
	public int getTotalVerifyMetrics() {
		int totalAddition = 0;
		Set<PD> set = verifyMetrics.keySet();
		for(PD at : set){
			totalAddition += verifyMetrics.get(at);
		}
		return totalAddition;
	}
	
	public int getTotalBranchingMetrics() {
		int totalAddition = 0;
		Set<PD> set = branchingMetrics.keySet();
		for(PD at : set){
			totalAddition += branchingMetrics.get(at);
		}
		return totalAddition;
	}
}
