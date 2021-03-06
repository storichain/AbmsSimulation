package abmsSimulation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;


public class ST extends Agent {
	
	public List<Story> storyList;
	//public GrowthIndex myGIrate;


	public ST(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		
		storyList = new ArrayList<Story>();
	
	}

	
	public List<Story> getStoryList() {
		return storyList;
	}

	public void setStoryList(List<Story> storyList) {
		this.storyList = storyList;
	}
	
	public void startStory(Context<Object> context, Network<Object> network, String storyTitle, int deadlineWeek ) {
		
			Story story = new Story(context,network,storyTitle, deadlineWeek);
			storyList.add(story);
			//SimulBuilder.getNetworkGenerator().attachNode(story);
			context.add(story);
			network.addEdge(this, story);
	}
	
	// for starting new story and continuing stories
	//@ScheduledMethod(start=5,priority=4,interval=7)
	public void doWriting() {
		
		System.out.println("doWriting()");
		double coworkingProb = RandomHelper.nextDoubleFromTo(0, 100);
		int week = RandomHelper.nextIntFromTo(4, 16);
		
		if (!isNegotiating()) {

			if(coworkingProb < 10) {
				ST c;
				c = (ST) meetCoworker( this);
				
				if (c!=null && c instanceof ST) {
					
					Story story = new Story(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("T"), week);
					storyList.add(story);
					context.add(story);
					network.addEdge(this, story);
					
					c.storyList.add(story);
					network.addEdge(this, c);
					network.addEdge(c, story);
					
					for (RepastEdge<Object> edge: SimulBuilder.network.getEdges(c)) {
						((CustomNetworkEdge) edge).setColor(Color.blue);
						((CustomNetworkEdge) edge).setThickness(3.0); 
					}
				}
			}else if(getStoryList().isEmpty()) {
				startStory(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("T"), week);
				//attachNode()
				System.out.println("generate a story");
				
			}else if(getStoryList().size() >= Parameters.limitOfMaxStory ){   // Max Story list 10 
				
				Collections.shuffle(storyList);
				for(int i=0; i < storyList.size(); i++) {
					if(!storyList.get(i).chkStoryCompleted()) {
						storyList.get(i).aggregateProductVector();
						break;
					}
				}
			}else {
				startStory(SimulBuilder.context, SimulBuilder.network, SimulBuilder.nextId("T"), week);
				//attachNode()
				System.out.println("generate a story until Max Story List");
			}
			
		}
		
	}
	
	public void doTyping() {
		if(storyList.size() == 0)
			return;
	
		int typingStory = RandomHelper.nextIntFromTo(0, storyList.size()-1);
		if(typingStory != 0) {
			//storyList.get(typingStory).gi.typingMetrics += 1;
			int additionMetric = 0;
			if(storyList.get(typingStory).gi.sceneMetrics.containsKey(this)) {
				additionMetric = storyList.get(typingStory).gi.sceneMetrics.get(this) + 1;
				storyList.get(typingStory).gi.sceneMetrics.put(this, additionMetric);
			}else
				storyList.get(typingStory).gi.sceneMetrics.put(this, 1);
		}
	}
	
	public void doCreateScene() {
		if(storyList.size() == 0)
			return;
	
		int sceneCreation = RandomHelper.nextIntFromTo(0, storyList.size()-1);
		
		if(sceneCreation != 0) {
			int additionMetric = 0;
			if(storyList.get(sceneCreation).gi.sceneMetrics.containsKey(this)) {
				additionMetric = storyList.get(sceneCreation).gi.sceneMetrics.get(this) + 1;
				storyList.get(sceneCreation).gi.sceneMetrics.put(this, additionMetric);
			}else
				storyList.get(sceneCreation).gi.sceneMetrics.put(this, 1);
		}
	}
	
	public void doSTaction() {
		int random = RandomHelper.nextIntFromTo(1, 5);
		
		switch(random) {
			case 1:
				doWriting();
				break;
			case 2:
				doTyping();
				break;
			case 3:
				doCreateScene();
				break;
			default:
				break;
			
		}
	}
	
}
