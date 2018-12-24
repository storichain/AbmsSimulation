package abmsSimulation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
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
			//SimulBuilder.staticDemandSteps = 0;
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
	
	public void startStory(Context<Object> context, Network<Object> network, String storyTitle ) {
		
		Story story = new Story(context,network,storyTitle);
		storyList.add(story);
		SimulBuilder.getNetworkGenerator().attachNode(story);
	}
	
	// for starting new story and continuing stories
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doWriting() {
		
		System.out.println("doWriting()");
		
		if (!isNegotiating()) {
//			Story c;
//			c = (Story) meet(Story.class);
//			
//			if (c!=null && c instanceof Story) {
//				//c.processOffer(goal.getProductVector());
//				c.processStaking(demandVector, (availableMoney * 0.1));
//			}
			if(getStoryList().isEmpty()) {
				startStory(SimulBuilder.context, SimulBuilder.network, "story title");
				//attachNode()
				System.out.println("generate a story");
				
			}
		}
	}
	
}
