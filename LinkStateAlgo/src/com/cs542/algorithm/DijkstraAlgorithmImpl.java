package com.cs542.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.cs542.object.Edge;
import com.cs542.object.Graph;
import com.cs542.object.Node;

/**
 * @author Brinda Rao
 *
 */
public class DijkstraAlgorithmImpl {

	/* Global declarations */
	private final List<Node> nodes;
	private final List<Edge> edges;
	public Set<Node> minimumNodes = null;
	public List<Node> allPath = new ArrayList<Node>();
	private Set<Node> visitedNodes;
	private Set<Node> unvisitedNodes;
	public Map<Node, List<Node>> adjacencyNodes = new HashMap<Node, List<Node>>();
	private Map<Node, Node> predecessors;
	private Map<Node, Integer> distance;
	public static StringBuilder demoString = new StringBuilder();

	public DijkstraAlgorithmImpl(Graph graph) {
		// create a copy of the array so that we can operate on this array
		new ArrayList<Node>(graph.getNodes());
		this.nodes = new ArrayList<Node>(graph.getNodes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	/**
	 * Function to find the adjacent minimal distance between the given source and destination routers
	 * @param source
	 * @param demoFlag
	 */
	public void algoRun(Node source,int demoFlag) {
		visitedNodes = new HashSet<Node>(); //set of routers that are visited during the due course of finding the shortest path
		unvisitedNodes = new HashSet<Node>(); //set of unvisited during the due course of finding the shortest path.
		distance = new HashMap<Node, Integer>(); // Mapping of the respective node and its shortest distance
		predecessors = new HashMap<Node, Node>();
		distance.put(source, 0); //initialing the source router to zero distance.
		unvisitedNodes.add(source); //add the source router to the set of unvisited set of nodes.

		minimumNodes = new HashSet<Node>();
		while (unvisitedNodes.size() > 0) {
			Node node = getMinimum(unvisitedNodes,demoFlag); //call to the function getMinimum that finds the minimum distance
			visitedNodes.add(node); // making the node as visited
			unvisitedNodes.remove(node); //removing the node from unvisited
			findMinimalDistances(node,demoFlag); //call to the function findMinimalDistance 
			adjacencyNodes.put(node, getNeighbors(node)); // Mapping of the respective node and its respective neighbours list
		}

	}

	
	/**
	 * Function to find the minimal distance
	 * @param node
	 * @param demoFlag
	 */
	private void findMinimalDistances(Node node,int demoFlag) {
		List<Node> adjacentNodes = getNeighbors(node);
		
		if(demoFlag==1 && adjacentNodes.size()>0)
		demoString.append("\n \n The adjacent nodes of:"+ node.getName()+"are:\n"); //displaying the adjacent nodes of the node that was sent as the parameter to this function.
		
		for (int i = 0; i < adjacentNodes.size(); i++){
			if(demoFlag==1)
			demoString.append("\n \n "+adjacentNodes.get(i)+"\n");
		}
		
		for (Node target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				int sum = getShortestDistance(node) + getDistance(node, target); // adding the shortestDistance of the node that is sent as the parameter to the function and the distance between the node and the chosen target.
				
				if(demoFlag==1)
				demoString.append("\n \n The value of shortest path via"+target+" is :"+ sum+"\n");
				
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));//adding the distance of the node and the node in to the Mapping set - distance
				predecessors.put(target, node); //adding the node that is chosen as the interface to the node that is sent as the parameter.
				unvisitedNodes.add(target); //adding the target as one of the node in the unvisited nodes.
			}
		}
	}

	/**
	 * Function call to find the distance between the node and the target
	 * @param node
	 * @param target
	 * @return cost of the edge between the node and target
	 */
	private int getDistance(Node node, Node target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getCost(); //return the edge cost of the node and the target that is sent to the function call.
			}
		}
		throw new RuntimeException("Should not happen");
	}

	/**
	 * @param node
	 * @return List of nodes that are neighbours to the node that is sent as input to the function call
	 */
	public List<Node> getNeighbors(Node node) {
		List<Node> neighbors = new ArrayList<Node>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) { //check if the source list has the specified node and if its nearest neighbour that is unvisited.
				neighbors.add(edge.getDestination());// Add the particular node as the neighbour 
			}
		}
		return neighbors;
	}

	/**
	 * Function call to get the minimum node 
	 * @param vertexes
	 * @param demoFlag
	 * @return Minimum node
	 */
	private Node getMinimum(Set<Node> vertexes,int demoFlag) {
		Node minimum = null;
		for (Node vertex : vertexes) {
			//System.out.println(": node is " + vertex.getName());
			if (minimum == null) {
				minimum = vertex; // at first make the vertex as the minimum
				minimumNodes.add(minimum); // add the node to the set of nodes
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) { //compare the distances between the vertex and the minimum node
					minimum = vertex;// make the vertex as the minimum
					minimumNodes.add(minimum); // add this vertex to the set of minimum nodes
				} else if (getShortestDistance(vertex) == getShortestDistance(minimum)) { //if both the vertex and the minimum node's distance is equal
					minimumNodes.add(vertex); //add the vertex to the set of minimum nodes
					if(demoFlag==1)
					demoString.append("\n The minimum path next node is: "+minimum+","+vertex); // display the minimum node
				}
			}
		}
		if(demoFlag==1)
		demoString.append("\n The minimum path next node is: "+minimum);
		return minimum;
	}

	/**
	 * Function that returns boolean value to check if the node that is  passed as input to the function is a visited node
	 * @param vertex
	 * @return boolean
	 */
	private boolean isSettled(Node vertex) {
		return visitedNodes.contains(vertex);
	}

	/**
	 * Function that return the shortest distance to the destination node
	 * @param destination
	 * @return distance of the node that is passed as the parameter to the function call
	 */
	private int getShortestDistance(Node destination) {
		Integer d = distance.get(destination); //find the distance of the node that is passed as destination
		if (d == null) {
			return Integer.MAX_VALUE; //return max value of integer if the distance found is null else return the distance
		} else {
			return d;
		}
	}

	
	/**
	 * Function that returns the path to the destination router in the given topology
	 * @param target
	 * @return LinkedList of nodes to the target that is passed as parameter
	 */
	public LinkedList<Node> getPath(Node target) {
		LinkedList<Node> path = new LinkedList<Node>(); //declaration of LinkedList of nodes
		Node step = target; //assigning the parameter i.e. target node as the step node
		// check if a path exists
		if (predecessors.get(step) == null) { // find if the target node is in the predecessors list; if not found found return null
			return null;
		}
		path.add(step); // add the target node as one of the node to the declared LinkedList
		while (predecessors.get(step) != null) { //recursive call to find the predecessors of the node that is sent as parameter and adding them to the declared LinkedList 
			step = predecessors.get(step);
			path.add(step);
		}
		// Put the linkedList into the correct order
		Collections.reverse(path);
		return path; //return the path that was found.
	}

	/**
	 * Function to find all the paths from the source router to the destination router
	 * @param source
	 * @param destination
	 * @return List of all the paths that will be stored as LinkedList
	 */
	public List<LinkedList<Node>> getAllPaths(Node source, Node destination) {
		List<LinkedList<Node>> paths = new ArrayList<LinkedList<Node>>();
		buildPath(source, destination, paths, new LinkedHashSet<Node>());
		Map<LinkedList<Node>, Integer> costMap = new HashMap<LinkedList<Node>, Integer>();
		int totalCost = 0;
		for (int i = 0; i < paths.size(); i++) {
			totalCost = calculateTotalCost(paths.get(i));//function call to find the total cost to each of the path calculated
			costMap.put(paths.get(i), totalCost); //Mapping that contains the respective total cost for each of the link
		}
		List<Integer> costList = new ArrayList<Integer>(costMap.values()); //saving the costMap values into the list of integers
		Collections.sort(costList); // sorting the list of the costMap values in order

		List<LinkedList<Node>> finalPath = new ArrayList<LinkedList<Node>>();
		int minimumCost = costList.get(0); // the first value of costList is considered to be the minimum

		for (Entry<LinkedList<Node>, Integer> entry : costMap.entrySet()) {
			if (Objects.equals(minimumCost, entry.getValue())) { // check if for every entry list the cost is equal to minimum cost. If minimum add the path to the final list 
				finalPath.add(entry.getKey());
			}
		}
		return finalPath; //return the finalPath
	}

	/**
	 * Function that visits all the nodes that have direct connection to the node
	 * @param current
	 * @param destination
	 * @param paths
	 * @param path
	 */
	public void buildPath(Node current, Node destination,
			List<LinkedList<Node>> paths, LinkedHashSet<Node> path) {
		path.add(current); // add the current node to the path which is of the type LinkedHashSet

		if (current.equals(destination)) {
			paths.add(new LinkedList<Node>(path));
			path.remove(current);
			return;
		}

		List<Node> edges = adjacencyNodes.get(current);

		for (Node t : edges) {
			if (!path.contains(t)) {
				buildPath(t, destination, paths, path); // recursive call to the same function to visit all the nodes that is directly connected to the node
			}
		}

		path.remove(current);//remove the current node from the path
	}

	/**
	 * Function call to find the total cost of the path.
	 * @param path
	 * @return total cost of the path that is given as input to the function call
	 */
	public int calculateTotalCost(LinkedList<Node> path) {
		String source = null, destination = null;
		int cost = 0;
		int i = 1;
		if (path == null) { // if the input path is null , return total cost -1
			return -1;
		}
		for (Node vertex : path) {
			if (i == 1) {
				source = vertex.getName(); // initialise the source node
				destination = vertex.getName(); //initialise the destination node
				i++;
			} else {
				source = destination; // make the destination node as source node
				destination = vertex.getName(); //initialise the destination node
				i++;
				for (Edge edge : edges) {
					if (edge.getSource().getName().equals(source)
							&& edge.getDestination().getName()
									.equals(destination)) { //validate if the source and destination nodes are part of the list's of source and destination routers
						cost = cost + edge.getCost(); // summation the cost with the edge costs
					}
				}

			}

		}

		return cost; // return the computed total cost of the path
	}
}
