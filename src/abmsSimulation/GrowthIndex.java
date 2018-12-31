package abmsSimulation;

public class GrowthIndex {

	public int reactionMetric;
	public int readingMetric;
	
	public GrowthIndex() {
		reactionMetric = 0;
		readingMetric = 0;
	}
	
	public int getTotalGIsumResult() {
		return reactionMetric + readingMetric;
	}

	
}
