package abmsSimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.OpenLongToDoubleHashMap.Iterator;


public class ST extends Agent {
	
	//protected Story goal;
	public List<Story> storyList;
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
		//SimulBuilder.getNetworkGenerator().attachNode(story);
		context.add(story);
		network.addEdge(this, story);
	}
	
	// for starting new story and continuing stories
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doWriting() {
		
		System.out.println("doWriting()");
		
		if (!isNegotiating()) {

			if(getStoryList().isEmpty()) {
				startStory(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("T"));
				//attachNode()
				System.out.println("generate a story");
				
			}else if(getStoryList().size() > 10 ){   // Max Story list 10 
				
				Collections.shuffle(storyList);
				for(int i=0; i < storyList.size(); i++) {
					if(!storyList.get(i).chkStoryCompleted()) {
						storyList.get(i).aggregateProductVector();
						break;
					}
				}
			}else {
				startStory(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("T"));
				//attachNode()
				System.out.println("generate a story until Max Story List");
			}
		
		}
		
	}
	
}
