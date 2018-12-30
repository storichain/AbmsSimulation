package abmsSimulation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.dataLoader.ContextBuilder;



public class SimulBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {


		public static Context<Object> context;
		public static Context<Object> contextSub;
		public static Network<Object> network;
		//public static Network<Object> effectuationNetwork;	
		public static MainNetworkGenerator networkGenerator;
		//public static boolean allEntrepreneursOffering;   // to check to stop simulation
		public static ArrayList<ST> stList;
		public static ArrayList<RD> rdList;
		public static ArrayList<PD> pdList;
		//public static ArrayList<Story> storyList;
		

		public static double ecoPoolMoney;
		private static HashMap<String, Integer> lastIds;
		//public static int staticDemandSteps;
		//public static double[]  productElementCost;   
		
		public TempSchedule randomEvolve;

		
		@Override
		public Context<Object> build(Context<Object> context) {
			Parameters.initialize();
			ecoPoolMoney = 1000;
			
			stList= new ArrayList<ST>();
		//	oldDemand = new ArrayList<int[]>();
			rdList = new ArrayList<RD>();
			pdList = new ArrayList<PD>();
			//storyList = new ArrayList<Story>();
			//staticDemandSteps = 0;
			
			
		//	generateProductElementCosts();
			
			lastIds = new HashMap<String, Integer>();
				
			context.setId("AbmsSimulation");
			
			SimulBuilder.context = context;
			
			//make Edges setting and connect with context and network
			buildNetworks();
			
			
			//Network generation
			if (Parameters.networkGenerator.equals("BarabasiAlbert")) {
				networkGenerator = new BarabasiAlbertNetworkGenerator(context);
			} else if (Parameters.networkGenerator.equals("ZombieNetwork")) {
				networkGenerator = new ZombieNetworkGenerator(context);
			} else if (Parameters.networkGenerator.equals("RandomNetwork")) {
				networkGenerator = new RandomNetworkGenerator(context, Parameters.randomNetworkDensity);
			}
			
			networkGenerator.setTotalST(Parameters.numberOfST);
			networkGenerator.setTotalPD(Parameters.numberOfPD);
			networkGenerator.setTotalRD(Parameters.numberOfRD);
			networkGenerator.setTotalStory(Parameters.numberOfStory);
			networkGenerator.seEdgesPerStep(Parameters.edgesPerStep);
			networkGenerator.setEdgeProbability(Parameters.edgeProbability);
			
			network = networkGenerator.createNetwork(network);
			
	//		initializeDemandVectors();
	//		aggregateProductVectors();
			
			calculateBetweennesCentralities();
			
			//allEntrepreneursOffering = false;
			
			scheduleActions();
			
	
			
	//		context.add(randomEvolve = new TempSchedule(this));
			//context.addProjection(network);
			context.add(networkGenerator);
			context.add(new GrowthSchedule());
			
			//context.addSubContext(contextSub);
			//contextSub.add(networkGenerator);
			
			return context;
		}
		
		public static int getStoryListCount() {
			int count = 0;
			for(Object o : context.getObjects(Story.class)){
				count++;
			}
			return count;
		}
		
		public double getEcoPoolMoney() {
			return ecoPoolMoney;
		}
		
	
		public static MainNetworkGenerator getNetworkGenerator() {
			return networkGenerator;
		}
		
		/**
		 * Recursively get all PD acquaintances of a node using a specified network depth.
		 * @param n node
		 * @param depth
		 * @param List of PD acquaintances
		 */
		public static void getPDAcquiantances(Object n, int depth, List<PD> rpdList) {		
			for (Object o: network.getAdjacent(n)) {
				if (o instanceof PD) {
					rpdList.add((PD)o);
				}
				if (depth > 1) {
					getPDAcquiantances(o, depth-1, rpdList);
				}
			}
		}
		
		

		// default setting such as color, thickness,  //make Edges setting
		private void buildNetworks() {
			EdgeCreator<CustomNetworkEdge, Object> edgeCreator = new CustomEdgeCreator();		
			
			//Build Entrepreneurial network
			NetworkBuilder<Object> netBuilder2 = new NetworkBuilder<Object>("full network",context, false);
			netBuilder2.setEdgeCreator(edgeCreator);
			network = netBuilder2.buildNetwork();
			
			//Build Effectuation network
//			NetworkBuilder<Object> netBuilder3 = new NetworkBuilder<Object>("effectual network",context, false);
//			netBuilder3.setEdgeCreator(edgeCreator);	
//			effectuationNetwork = netBuilder3.buildNetwork();
		}
		
		/**
		 *  Initialize All users' demand vectors using the define "Market split", i.e
		 *  the percentage of customers that "like" a certain product element
		 */
		private void initializeDemandVectors() {
			
//			for (Object c: context.getObjects(PD.class)) {
//				((PD)c).setDemandVector();
//			}
			
			ArrayList<PD> demand = new ArrayList<PD>();
			
			for (Object c: context.getObjects(PD.class)) {
				demand.add((PD)c);
			}
			
			int shouldLikeProductElement = (int)Math.ceil(((double)Parameters.marketSplit / 100) * demand.size());
			
			for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
				ArrayList<PD> copy = new ArrayList<PD>(demand);
				
				for (int j = 0; j < shouldLikeProductElement; j++) {
					PD c = copy.remove(RandomHelper.nextIntFromTo(0, copy.size() - 1));
					c.getDemandVector()[i] = 1;
				}
			}
		}

		
		/**
		 * Calculates the betweenness centrality for each node, using the JUNG implemented
		 * betweenness centrality calculator algorithm 
		 */	
		public static void calculateBetweennesCentralities() {			
			
			ContextJungNetwork<Object> N = (ContextJungNetwork<Object>)network;
			
			Graph<Object, RepastEdge<Object>> G = N.getGraph();
			
			BetweennessCentrality<Object, RepastEdge<Object>> ranker = new BetweennessCentrality<Object, RepastEdge<Object>>(G);
			
			ranker.setRemoveRankScoresOnFinalize(false);
			ranker.evaluate();
			
			for (Object n: network.getNodes()) {
				//System.out.println("check Object n : " + n.getClass());
				if(n instanceof MainNetworkGenerator || n instanceof GrowthSchedule)
					continue;
				
				Agent a = (Agent)n;
				// Normalize by (n-1)(n-2)/2
				a.setBetweennessCentrality(ranker.getVertexRankScore(n) / (((N.size()-1) * (N.size()-2)) / 2.0) );
			}
		}
		
		/**
		 * Returns the "network density"
		 * density = 2 * number of edges / N * (N-1)
		 * @return networkDensity
		 */
		public double getNetworkDensity() {
			
			return ( 2.0 * network.numEdges() ) / ( network.size() * (network.size()-1) );
		}
		
		/**
		 * Returns the next Id of a node, also by specifying a certain prefix
		 * @param prefix
		 * @return nextId
		 */
		public static String nextId(String prefix) {
			int nextId;
			
			if (lastIds.containsKey(prefix)) {
				nextId = lastIds.get(prefix) + 1;
				lastIds.put(prefix, nextId);
			} else {
				nextId = 1;
				lastIds.put(prefix, nextId);
			}
			return prefix + String.valueOf(nextId);
		}

		
		
		
		/**
		 * Returns the "Hamming distance" between two equal length 0-1 vectors
		 * @param p1
		 * @param p2
		 * @return int
		 */
		public static int hammingDistance(int[] p1, int[] p2) {
			int d = 0;
			
			for (int i = 0; i < p1.length; i++) {
				d += p1[i] ^ p2[i];
			}
			
			return d;		
		}
		
		public static void printMessage(String m) {
			ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance()
	        .getCurrentSchedule();
			
			System.out.println(m + " - " + String.valueOf(schedule.getTickCount()));
		}
		
		/**
		 * Returns the the bits that are different in two equal length 0-1 vectors
		 * @param p1
		 * @param p2
		 * @return s
		 */
		public static int[] diff(int[] p1, int[] p2) {
			int[] diff = new int[p1.length];
			
			for (int i = 0; i < p1.length; i++) {
				diff[i] = p1[i] ^ p2[i];
			}
			
			return diff;
		}
		
		@ScheduledMethod(start=1,interval=2)
		public void scheduleMethodTest() {
			
			System.out.println("scheduleMethodTest()  in SimulBuilder");
			
		}
		
		
		/**
		 *  Evolves network (if set) during the simulation (adding new nodes randomly)
		 */
		//@ScheduledMethod(start=10,interval=10,priority=10)
		//@Watch(watcheeClassName="abmsSimulation.Agent",
		//		watcheeFieldNames="negotiating",whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
		
		
		/**
		 * Checks if all entrepreneurs are offering and update the relevant flag
		  
		@Watch(watcheeClassName="EffectuationCausation.Entrepreneur",
				watcheeFieldNames="offering",whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
		public void checkAllEntrepreneursOffering() {
			for (Object o: context.getObjects(Entrepreneur.class)) {
				if (!((Entrepreneur)o).isOffering()) {
					allEntrepreneursOffering = false;
				}
				return;
			}
			allEntrepreneursOffering = true;
		}
		*/
		
		public void scheduleActions() {
			ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance().getCurrentSchedule();
			
//			ArrayList<PD> pd = new ArrayList<PD>();
//			for (Object c: context.getObjects(PD.class)) {
//				pd.add((PD)c);
//			}
															// createRepeating(start,interval,priority)
			//ScheduleParameters parameters = ScheduleParameters.createRepeating(1, 6 - Parameters.adaptationSpeed, 1);		
			//schedule.scheduleIterable(parameters, pd, "adaptProductVector", true);
			ScheduleParameters parameters = ScheduleParameters.createRepeating(10, Parameters.adaptationSpeed+3, 9);
			schedule.scheduleIterable(parameters, pdList, "doProducing", true);
			
													
			parameters = ScheduleParameters.createRepeating(2, Parameters.adaptationSpeed+17, 8);
			schedule.scheduleIterable(parameters, stList, "doWriting", true);
			parameters = ScheduleParameters.createRepeating(2, Parameters.adaptationSpeed+9, 8);
			schedule.scheduleIterable(parameters, rdList, "doReaction", true);
			
		}
		
		/**
		 * Refine the product vector of the PD based on the
		 * connected (randomly)
		 */
		public void aggregateProductVectors() {
			
			if (!Parameters.aggregateProductVector) {
				return;
			}
			
			double prob = RandomHelper.nextDoubleFromTo(0, 1);
			
			for (Object o: context.getObjects(PD.class)) {
				
				//Skip causator and effectuator
				
//				if (o instanceof Causator || o instanceof Effectuator) {
//					continue;
//				}
				
				double r = RandomHelper.nextDoubleFromTo(0, 1);
				
				if (r >= prob) {
					//PD e = (PD)o;
					Story e = (Story)o;
					e.aggregateProductVector();
				}
			}		
		}
		
		/**
		 * Returns the average clustering coefficient of the network 
		 * (as calculated by Watts and Strogatz)
		 * @return C
		 */
//		public double getClusteringCoefficient() {
//			double C = 0;
//			
//			
//			Map<Object, Double> cc = Metrics.clusteringCoefficients(((ContextJungNetwork<Object>)network).getGraph());
//			
//			for (Object n: network.getNodes()) {
//				C += cc.get(n) / network.size();
//			}
//			
//			return C;
//		}
//		
//		
//		/**
//		 * Returns the effectuator's betweenness centrality in the network
//		 * @return betweennessCentrality
//		 */
//		public double getEffectuatorsBetweennessCentrality() {		 
//			return effectuator.getBetweennessCentrality();
//		}
//
//		/**
//		 * Returns the effectuator's degree centrality in the entrepreneurial network
//		 * @return degreeCentrality
//		 */	
//		public double getEffectuatorsDegreeCentrality() {
//			//Return the normalized effectuators degree centrality
//			return network.getDegree(effectuator) / (double)(network.size() - 1);		
//		}	
//
//		public String getEffectuatorsGoal() {
//			return effectuator.getGoal().printProductVector();
//		}
//		
//		public String getCausatorsGoal() {
//			return causator.getGoal().printProductVector();
//		}
//		
//		public double getEffectuatorsMarketFit() {
//			int meetDemand = 0;		
//			
//			for (int i = 0; i < customers.size(); i++) {
//				if (hammingDistance(effectuator.getGoal().getProductVector(), 
//						customers.get(i).getDemandVector()) < Math.ceil(Parameters.vectorSpaceSize / 4.0)) {
//					meetDemand++;
//				}
//			}
//			
//			return (meetDemand / (double)customers.size()) * 100.0;
//		}
//		
//		public double getCausatorsMarketFit() {
//			int meetDemand = 0;		
//			
//			for (int i = 0; i < customers.size(); i++) {
//				if (hammingDistance(causator.getGoal().getProductVector(), 
//						customers.get(i).getDemandVector()) < Math.ceil(Parameters.vectorSpaceSize / 4.0)) {
//					meetDemand++;
//				}
//			}
//			
//			return (meetDemand / (double)customers.size()) * 100.0;
//		}*/
		

}
