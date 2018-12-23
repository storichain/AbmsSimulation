package abmsSimulation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;


public class ST extends Agent {
	
	//protected Story goal;
	protected List<Story> storyList;
	//protected boolean offering;
	//protected boolean writing;

	public ST(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		
		storyList = new ArrayList<Story>();
		//setWriting(false);
	}
/*	
	public void setWriting(boolean writing) {
		this.writing = writing;
		if (writing) {
			//ContextBuilder.staticDemandSteps = 0;
		}
	}
	
	public boolean isWriting() {
		return writing;
	}
	*/
	
	public List<Story> getStoryList() {
		return storyList;
	}

	public void setStoryList(List<Story> storyList) {
		this.storyList = storyList;
	}
	
	//public void generateGoal() {
	public void startStory(Context<Object> context, Network<Object> network, String label ) {
		//goal = new Goal();
		//goal.generateRequiredMeans();
		Story story = new Story(context,network,label);
		storyList.add(story);
	}
	
}
