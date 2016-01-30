package com.cs542.object;


public class Edge {

	private int cost;
	private Node source;
	private Node destination;
	private String id;

	// parameterized constructor for initializing the id, source, destination
	// and weight
	public Edge(String id, Node source, Node destination, int cost) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	//default method to convert prameter's to string to display
	@Override
	public String toString() {
		return source + " " + destination;
	}
}
