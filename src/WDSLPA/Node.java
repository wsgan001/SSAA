package WDSLPA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Represents the node of the undirected graph.
 * @author pejakalabhargava
 *
 */
public class Node {

	//Adjacenecy list holding all the neighbours of the given node ����ڽӵ�
	//private Set<Node> neighbhours;
	private Map<Node, Double[]> neighbhours;
	private Map<Node, Double[]> beneighbhours;
	//Id of the given node.
	private int nodeId;
	
	// Memory map used to hold the labelId and the count used for SLPA
	// algorithm.�洢<��ǩ����ǩ����>
	private Map<Integer, Integer> memoryMap;
	
	// This represents the total number of counts(or communities) present in the
	// memory map of this node.//�ܱ�ǩ��������������
	private int noOfCommunities;

	/**
	 * Constructor to create the node structure.
	 */
	public Node(Integer source) {
		nodeId = source;
		initializeDataStructure();
	}

	/**
	 * Helper method to initialize the required data strucutres. This also makes
	 * sure that the memory of each node is initialized with a unique label as
	 * part of SLPA algorithm.
	 */
	private void initializeDataStructure() {
		neighbhours = new HashMap();
		memoryMap = new LinkedHashMap <Integer, Integer>();
		noOfCommunities = 1;
		memoryMap.put(nodeId, 1);
	}
	
	
	/**
	 * This function implements the listen step of the SLPA algorithm. Each
	 * neighbor sends the selected label to the listener and the listener adds
	 * the most popular label received to its memory.
	 */
	public void listen() {
		//Map to hold the all the received labels from its neighbours in this iteration
		Map<Integer, Double> labelMap = new HashMap<Integer, Double>();//�洢�õ�ÿ���յ��ı�ǩ�Լ�����
		//Iterate through all the neighbors and callk speak on them as part of SLPA algorithm.
		for (Node node : neighbhours.keySet()) {
			//Speak method returns label to the listener
			int label = node.speak(); //�õ��ڽӵ�ı�ǩ
			//Add the label received to the temporary labelMap to hold the labelId and received count.
			if (labelMap.get(label) == null) {
				labelMap.put(label, neighbhours.get(node)[1]);
			} else {
				Double currentLabelCount = labelMap.get(label);
				currentLabelCount+=neighbhours.get(node)[1];
				labelMap.put(label, currentLabelCount);
			}
		}
		//After all neighbours sends the label, findout the most popular label
		int popularLabel = getMostPopularLabel(labelMap);//�õ���ǩ���ִ������ı�ǩ
		if(popularLabel==-1) popularLabel=nodeId;
		//add the popular label to the memory map of the node.
		//�����������ı�ǩ�ӵ�����ǩ���У����Ҽ�¼�������ֵĴ���
		if(memoryMap.get(popularLabel) == null) {
			memoryMap.put(popularLabel, 1);
		} else {
			int currentCount = memoryMap.get(popularLabel);
			currentCount++;
			memoryMap.put(popularLabel, currentCount);
		}
		//Increment the noOfCommunities
		noOfCommunities++;
		labelMap.clear();
	}
	private int getMostPopularLabel(Map<Integer, Double> labelMap) {
		Double maxLabelCount = 0.0;
		List<Integer> labelist=new ArrayList<>();
		for (Map.Entry<Integer, Double> entry : labelMap.entrySet()) {
			maxLabelCount=Math.max(maxLabelCount, entry.getValue());
		}
		for (Map.Entry<Integer, Double> entry : labelMap.entrySet()) {
			if(maxLabelCount.equals(entry.getValue()))
			labelist.add(entry.getKey());
		}
		if(labelist.size()==0) return -1;
//		if(labelist.size()==1) return labelist.get(0);
		double index=Math.random()*(double)labelist.size();
		return labelist.get((int)index);
	}

	/**
	 * Each neighbor of the selected node randomly selects a label with probability
	 * proportional to the occurrence frequency of this label in its memory and sends
	 * the selected label to the listener.
	 * @return label
	 */
	//���ѡ������и������ı�ǩ�ͳ�,���ո��ʷֲ����ȡ
	private int speak() {
		//generate a random double value
		Random random = new Random();
		double randomDoubleValue = random.nextDouble();
		double cumulativeSum = 0;
		// Randomly select a label with probability proportional to the
		// occurrence frequency of this label in its memory
		for (Map.Entry<Integer, Integer> entry : memoryMap.entrySet()) {
			Integer labelId = entry.getKey();
			Integer labelCount = entry.getValue();
			cumulativeSum = cumulativeSum + ((double)labelCount)/noOfCommunities;
			if(cumulativeSum >= randomDoubleValue) {
				return labelId;
			}
		}
		return nodeId;
	}

	//Getters and Setters
	/**
	 * Adds a neighbor to the node's adjacency list
	 * @param destNode
	 */
//	public void addNeighbour(Node destNode,Double value,Double nd) {
//		neighbhours.put(destNode,value);
//	}
	public void addNeighbour(Node destNode,Double value,Double nd) {
		Double[] d=neighbhours.get(destNode);
		if(d!=null){
			d[0]+=value;
			d[1]+=nd;
		}else {
			d=new Double[2];
			d[0]=value;
			d[1]=nd;
		}
			neighbhours.put(destNode,d);
	}
	public void addbeNeighbour(Node destNode,Double w,Double de) {
		Double[] d=beneighbhours.get(destNode);
		if(d!=null){
			d[0]+=w;
			d[1]+=de;
		}else {
			d=new Double[2];
			d[0]=w;
			d[1]=de;
		}
			beneighbhours.put(destNode,d);
	}
	/**
	 * Returns the neighbors of the node
	 * @return set of neighbors
	 */
//	public Set<Node> getNeighbhours() {
//		return neighbhours;
//	}
	public Map<Node,Double[]> getNeighbhours() {
		return neighbhours;
	}
	/**
	 * Returns the NodeId
	 * @return node ID
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Returns the memory map of the node at given time t
	 * @return map
	 */
	public Map<Integer, Integer> getMemoryMap() {
		return memoryMap;
	}

	/**
	 * Returns the total number of entries in the memory map's count
	 * @return int
	 */
	public int getNoOfCommunities() {
		return noOfCommunities;
	}
}