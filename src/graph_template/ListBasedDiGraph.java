package graph_template;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

 
//LeedleeleedlleeLeeee

public class ListBasedDiGraph implements DiGraph {
	private List<GraphNode> nodeList = new ArrayList<>();

	@Override
	public Boolean addNode(GraphNode node) {
		if(!nodeList.contains(node)) {
			nodeList.add(node);			
			return true;
		}
		return false;
	}

	@Override
	public Boolean removeNode(GraphNode node) {
		if(nodeList.contains(node)) {
			nodeList.remove(node);
			for(GraphNode otherNode : nodeList) {
				removeEdge(node, otherNode);
			}
			return true;
		}
		return false;
	}

	@Override
	public Boolean setNodeValue(GraphNode node, String newNodeValue) {
		if(nodeList.contains(node)) { // fix logic
			node.setValue(newNodeValue);
			return true;
		}
		return false;
	}

	@Override
	public String getNodeValue(GraphNode node) {
		return node.getValue();
	}

	@Override
	public Boolean addEdge(GraphNode fromNode, GraphNode toNode, Integer weight) {

		// TODO
		// BAD
		// fromNode.addNeighbor(toNode, weight);
		
		// GOOD
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
	 	 
		targetFromNode.addNeighbor(targetToNode, weight);
	
		return true;
	}

	@Override
	public Boolean removeEdge(GraphNode fromNode, GraphNode toNode) {
		if(fromNode.getNeighbors().contains(toNode)) {
			fromNode.removeNeighbor(toNode);
			return true;
		}
		
		return false;
	}

	@Override
	public Boolean setEdgeValue(GraphNode fromNode, GraphNode toNode, Integer newWeight) {
		if(fromNode.getNeighbors().contains(toNode)) {
			fromNode.removeNeighbor(toNode);
			fromNode.addNeighbor(toNode, newWeight);
			return true;
		}
		
		return false;
	}

	@Override
	public Integer getEdgeValue(GraphNode fromNode, GraphNode toNode) {
		if(fromNode.getNeighbors().contains(toNode)) {
			return fromNode.getDistanceToNeighbor(toNode);
		}
		return null;
	}

	@Override
	public List<GraphNode> getAdjacentNodes(GraphNode node) {
		return node.getNeighbors();
	}

	@Override
	public Boolean nodesAreAdjacent(GraphNode fromNode, GraphNode toNode) {
		if(fromNode.getNeighbors().contains(toNode) || toNode.getNeighbors().contains(fromNode)) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean nodeIsReachable(GraphNode fromNode, GraphNode toNode) {
		// create queue to manage nodes
				Queue<GraphNode> queue = new LinkedList<GraphNode>();
				// create set to manage visitedNodes
				Set<GraphNode> visitedNodes = new HashSet<GraphNode>();
				
//				// (preliminary)
//				if(fromNode.equals(toNode)) // if can reach itself
//				{
//					return true;
//				}
				// obsolete; we can't say this is necessarily
				// true, because a node may very well not have
				// a directed route back to itself (dead ends).
				
				queue.add(fromNode);
				visitedNodes.add(fromNode);
				List<GraphNode> neighborList;

				//start from the targetFromNode
				while(!queue.isEmpty())
				{
					GraphNode current = queue.poll();
					neighborList = current.getNeighbors(); // TODO THIS LINE DOES NOT WORK CORRECTLY
					
					//for all neighbors:
					for(GraphNode neighbor : neighborList)
					{
						//check if visited.  If not, add to the queue.  
						if(!visitedNodes.contains(neighbor))
						{
							queue.add(neighbor); // linkedlist add => enqueue op
							visitedNodes.add(neighbor);
						}
						//if targetToNode has been visited, return true
						if(neighbor.getValue().equals(toNode.getValue()))
						{
							return true;
						}
					}	
				}
				return false; // unreached through all neighbors
	}

	@Override
	public Boolean hasCycles() {
		// TODO Auto-generated method stub
		for(GraphNode node : nodeList)
		{
			if(nodeIsReachable(node, node))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public List<GraphNode> getNodes() {
		return nodeList;
	}

	@Override
	 
	public GraphNode getNode(String nodeValue) {
		for (GraphNode thisNode : nodeList) {
			if (thisNode.getValue().equals(nodeValue))
				return thisNode;
		}
		return null;
	}

	@Override
	public int fewestHops(GraphNode fromNode, GraphNode toNode) {
		// TODO Auto-generated method stub
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
		Queue<GraphNode> queue = new LinkedList<>();
		Set<GraphNode> visitedNodes = new HashSet<>();
		Map<GraphNode, Integer> pathMap = new HashMap<>();	
		
		queue.add(targetFromNode);
		visitedNodes.add(targetFromNode);
		pathMap.put(targetFromNode, 0);
 		
		//TODO - traverse the graph BF.  
		while (queue.peek() != null) {  
 
			GraphNode current = queue.poll();
			List<GraphNode> neighborList = current.getNeighbors();
			
			//TODO - keep track of neighbors visited && increment hops if not
			//TODO - iterate over all the neighbors of this node
			for(GraphNode neighbor : neighborList)
			{
				if(!visitedNodes.contains(neighbor))
				{
					visitedNodes.add(neighbor);
					queue.add(neighbor);
					pathMap.put(neighbor, pathMap.get(current) + 1);
				}
				
				//TODO - IF target reached, done. Return number of hops
				if(neighbor.getValue().equals(targetToNode.getValue()))
				{
					return pathMap.get(neighbor);
				}
			}
 		}

		return 0;

	}

	@Override
	//I am using the algorithm from the link on slides. I am taking that structure and turning into Java
	public int shortestPath(GraphNode fromNode, GraphNode toNode) {
		// TODO Auto-generated method stub
		GraphNode targetFromNode = getNode(fromNode.getValue());
		GraphNode targetToNode = getNode(toNode.getValue());
		int distance=0;
		
		Map<GraphNode, Integer> nodeDistance = new HashMap<>();//will have distance from node
		PriorityQueue<GraphNode> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(nodeDistance::get));
		//Set<GraphNode> visitedNodes = new HashSet<>();
		 
		nodeDistance.put(targetFromNode, 0);
		priorityQueue.add(targetFromNode);
		
		while (priorityQueue.peek() != null) {  
			 
			GraphNode current = priorityQueue.poll();
			List<GraphNode> neighborList = current.getNeighbors();
			
			// originally used visitedNodes cause I was doing similar to fewest hops but i realizes it never influenced answer.
			// makes me think it might not actually be settling like dijkstra algorithm, but it does take the shortest path
			
//			if(!visitedNodes.contains(current)) {
//				visitedNodes.add(current);
//			}
			
			for(GraphNode neighbor : neighborList)
			{
				//if(!visitedNodes.contains(neighbor))
				{
					int weight = current.getDistanceToNeighbor(neighbor);
		            int newDistance = nodeDistance.get(current) + weight;
		            
					if(!nodeDistance.containsKey(neighbor) ||newDistance<nodeDistance.get(neighbor)) {
						nodeDistance.put(neighbor, newDistance);
						priorityQueue.add(neighbor);
					}
					
				}
				
				
				
			}
 		}
		distance += nodeDistance.get(targetToNode);
		// Found the issue! Assigned rather than added to distance var. Good stuff :)
		
		
		
		
		return distance;
	}

 
	 
	
}
