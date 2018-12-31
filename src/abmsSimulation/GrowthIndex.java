package abmsSimulation;

public class GrowthIndex {

	public int reactionMetric;
	public int readingMetric;
	public int shareTipMetric;
	public int typingMetric;
	public int verifyMetrics;
	public int branchingMetrics;
	
	public GrowthIndex() {
		reactionMetric = 0;
		readingMetric = 0;
		shareTipMetric = 0;
		typingMetric = 0;
		verifyMetrics = 0;
		branchingMetrics = 0;
	}
	
	public int getTotalGIsumResult() {
		return reactionMetric + readingMetric + shareTipMetric + typingMetric + verifyMetrics + branchingMetrics;
	}
	
}
