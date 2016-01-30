package com.cs542.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import com.cs542.main.DijkstraGUI;
import com.cs542.object.Edge;
import com.cs542.object.Graph;
import com.cs542.object.Node;

public class InputParser {

	private List<Node> nodes;
	private List<Edge> edges;
	public static int totalrouters = 0;
	public static int counter = 0;
	public static int[][] costMatrix;

	
	/**
	 * Function that gets the file Name
	 * @return a string that contains the fileName
	 */
	public String getFile() {
		String buffer = null;

		buffer = DijkstraGUI.textField.getText();

		if (buffer.isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"Empty!!! Enter a valid file path !!");
		}
		if (buffer != null && !buffer.trim().equals("")) {

		} else {
			return null;

		}

		return buffer;

	}

	/**
	 * This  method reads the source router from the text field and returns it
	 * @return source router name
	 */
	public String getSourceRouter() {
		String buffer = null;

		buffer = DijkstraGUI.sourceTextField.getText();

		return buffer;

	}

	/**
	 * This method returns the destination router read from the text field in GUI
	 * @return destination router name
	 */
	public String getDestinationRouter() {
		String buffer = null;

		buffer = DijkstraGUI.destinationTextField.getText();

		return buffer;

	}

	
	/**
	 * This reads the topology file and returns a matrix form of cost
	 * @param filepath
	 * @return cost matrix of the topology that was given as the input file
	 */
	public int[][] loadFile(String filepath) {
		BufferedReader buffer1 = null, buffer2 = null, buffer3 = null;
		int[][] routingMatrix = null;
		int columnCount = 0, rowIndex = 0;
		try {

			String line;
			counter = 0;
			buffer1 = new BufferedReader(new FileReader(filepath));
			while ((line = buffer1.readLine()) != null) {
				totalrouters = line.split("\\ ").length;//calculate number of new lines in the file to find total number of routers
				break;
			}
			if (totalrouters < 8) {//validate for initial number of routers = 8
				JOptionPane.showMessageDialog(null,
						"At least 8 routers are required !!");
			} else {
				routingMatrix = new int[totalrouters][totalrouters];
				costMatrix = new int[totalrouters][totalrouters];
				buffer2 = new BufferedReader(new FileReader(filepath));
				DijkstraGUI.jta.append("\n COST MATRIX:");
				while ((line = buffer2.readLine()) != null) {//read each line in the topology file and form the cost matrix
					String[] strArray = line.split("\\ ");
					columnCount = strArray.length;
					for (int j = 0; j < strArray.length; j++) {
						routingMatrix[counter][j] = Integer //store the value row wise into the matrix
								.parseInt(strArray[j]);
					}
					counter++;
				}
				if (counter != columnCount) {//not a square matrix
					JOptionPane.showMessageDialog(null,
							"The input matrix is malformed !!");
				} else {
					buffer3 = new BufferedReader(new FileReader(filepath));
					while ((line = buffer3.readLine()) != null) {
						DijkstraGUI.jta.append("\t\t|" + line + "|" + "\n");//append  the matrix to GUI
						String[] strArray = line.split("\\ ");
						for (int j = 0; j < strArray.length; j++) {
							routingMatrix[rowIndex][j] = Integer
									.parseInt(strArray[j]);
						}
						rowIndex++;
					}
				}
			}

			for (int i = 0; i < totalrouters; i++) {
				for (int j = 0; j < totalrouters; j++) {

					costMatrix[i][j] = routingMatrix[i][j];//save the cost into global matrix file

				}

			}
		} catch (IOException e) {
			JOptionPane
					.showMessageDialog(null,
							"Invalid File Path! Please re-enter the File Path to continue.");
			e.printStackTrace();
		} finally {
			try {
				/*close all the open file streams*/
				if (buffer1 != null)
					buffer1.close();
				if (buffer2 != null)
					buffer2.close();
				if (buffer3 != null)
					buffer3.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return routingMatrix;
	}

	/**
	 * Function that returns the cost matrix after deleting the node
	 * @param filePath
	 * @param nodeToDelete
	 * @return cost matrix
	 */
	public int[][] deleteNode(String filePath, int nodeToDelete) {
		BufferedReader buffer1 = null, buffer2 = null, buffer3 = null;
		int[][] routingMatrix = null;
		try {

			String line;
			counter = 0;

			buffer1 = new BufferedReader(new FileReader(filePath));
			while ((line = buffer1.readLine()) != null) {
				totalrouters = line.split("\\ ").length;
				break;
			}
			routingMatrix = new int[totalrouters][totalrouters];

			for (int i = 0; i < costMatrix.length; i++) {//store the values of matrix to local variable
				for (int j = 0; j < costMatrix.length; j++) {
					routingMatrix[i][j] = costMatrix[i][j];
					// System.out.print("\t " + routingMatrix[i][j]);
				}
				// System.out.println("\n");
			}

			for (int j = 0; j < routingMatrix.length; j++) {//make the deleted router row and column to -1 in the matrix to appear as deleted
				routingMatrix[nodeToDelete - 1][j] = -1;
				routingMatrix[j][nodeToDelete - 1] = -1;
			}
			// code to remove the desired row and column

			for (int i = 0; i < totalrouters; i++) {
				DijkstraGUI.jta.append("\t\t|");
				for (int j = 0; j < totalrouters; j++) {
					DijkstraGUI.jta.append(" " + routingMatrix[i][j]);//append new matrix to the text area and display to user
				}

				DijkstraGUI.jta.append("|\n");
			}

			// columnIndex=0;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				/*close all the open file streams*/
				if (buffer1 != null)
					buffer1.close();
				if (buffer2 != null)
					buffer2.close();
				if (buffer3 != null)
					buffer3.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return routingMatrix;
	}

	/**
	 * Function that returns the optimal path
	 * @param initialRoutingMatrix
	 * @param nodeToIgnore
	 * @return
	 */
	public DijkstraAlgorithmImpl findOptimalPath(int[][] initialRoutingMatrix,
			int nodeToIgnore) {

		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		int edgeCounter = 0, vertextCounter = 0;
		for (int i = 0; i < initialRoutingMatrix.length; i++) {
			Node location = new Node("R_" + (i + 1), "R" + (i + 1));
			nodes.add(location);//add each node in topology to nodes list
		}
		for (int i = 0; i < initialRoutingMatrix.length; i++) {
			for (int j = 0; j < initialRoutingMatrix.length; j++) {
				if (initialRoutingMatrix[i][j] != -1) {
					addLane("Edge_" + edgeCounter, i, j,
							initialRoutingMatrix[i][j]);
					edgeCounter++;//add an edge to each link in the topology
				}
			}
		}
		Graph graph = new Graph(nodes, edges);//form the graph object with nodes and edges
		DijkstraAlgorithmImpl dijkstraAlgorithm = new DijkstraAlgorithmImpl(
				graph);//make the object with created graph and return it
		return dijkstraAlgorithm;

	}

	
	/**
	 * Function that adds the edge in the graph
	 * @param lane
	 * @param source
	 * @param destination
	 * @param duration
	 */
	private void addLane(String lane, int source, int destination, int duration) {
		Edge lane1 = new Edge(lane, nodes.get(source), nodes.get(destination),
				duration);
		edges.add(lane1);//add an edge for given source and destination router
	}

	
	/**
	 * Function that displays the routing table for the given input cost matrix of the topology
	 * @param initialRoutingMatrix
	 * @param dijkstraAlgorithm
	 * @param sourceRouter
	 */
	public void buildRoutingTable(int[][] initialRoutingMatrix,
			DijkstraAlgorithmImpl dijkstraAlgorithm, String sourceRouter) {
		List<Integer> routingTable = new ArrayList<Integer>();
		int temp = Integer.parseInt(sourceRouter);
		dijkstraAlgorithm.algoRun(nodes.get(temp - 1), -1);//run the Dijkstra's algorithm for given source router

		for (int i = 0; i < initialRoutingMatrix.length; i++) {
			if (i != (temp - 1)) {

				LinkedList<Node> path = dijkstraAlgorithm.getPath(nodes.get(i));//get the path for each node

				int totalCost = dijkstraAlgorithm.calculateTotalCost(path);//calculate the cost of the shortest path
				routingTable.add(new Integer(totalCost));//add cost to the routing table
			} else {
				routingTable.add(0);
			}
		}
		/*Display the routing table to text area*/
		DijkstraGUI.jta.append("\tRouting Table for Router" + sourceRouter
				+ "\n");
		DijkstraGUI.jta.append("\t-------------------------------------\n");
		DijkstraGUI.jta.append("\tDestination              Interface\n");
		for (int j = 0; j < initialRoutingMatrix.length; j++) {

			LinkedList<Node> route = dijkstraAlgorithm.getPath(nodes.get(j));
			if (route == null)
				DijkstraGUI.jta.append("\t      " + (j + 1)
						+ "     |            --\n");
			else {
				String cost = route.toString();
				// System.out.println("The value of cost is"+cost);
				String cost1[] = cost.split(", R_");
				String costTemp = cost1[1];
				String cost2[] = costTemp.split("]");
				DijkstraGUI.jta.append("\t      " + (j + 1)
						+ "     |            " + cost2[0] + "\n");//append the routing table to the jta

			}
			DijkstraGUI.jta.append("\t-------------------------------------\n");
			DijkstraGUI.jta.setCaretPosition(0);//scroll bar should be in beginning of text area
		}
	}

	/**
	 * Function that computes the path between the source and the destination routers.
	 * @param dijkstraAlgorithm
	 * @param srcDest
	 * @param demoFlag
	 */
	public void computePath(DijkstraAlgorithmImpl dijkstraAlgorithm,
			String[] srcDest, int demoFlag) {
		dijkstraAlgorithm.algoRun(nodes.get(Integer.parseInt(srcDest[0]) - 1),
				demoFlag);//run the algorithm for source router

		List<LinkedList<Node>> finalPath = new ArrayList<LinkedList<Node>>();
		int totalCost = 0;
		try {
			finalPath = dijkstraAlgorithm.getAllPaths(
					nodes.get(Integer.parseInt(srcDest[0]) - 1),
					nodes.get(Integer.parseInt(srcDest[1]) - 1));//gets all the shortest path between given source and destination
			if (finalPath == null) {
				JOptionPane
						.showMessageDialog(null,
								"There is no path between given Source and Destination Router\n");
			}

			totalCost = dijkstraAlgorithm.calculateTotalCost(finalPath.get(0));//the total cost of the shortest path

		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		
		/*Append the shortest path and explanation to text area*/
		DijkstraGUI.jta.append("\nThe shortest path from Router " + srcDest[0]
				+ " to Router " + srcDest[1] + " is: \n");
		for (LinkedList<Node> totalPath : finalPath) {
			DijkstraGUI.jta.append(" " + totalPath + "\n");
		}
		DijkstraGUI.jta.append("\nThe Total cost is: " + totalCost);

		LinkedList<Node> iterativePath = new LinkedList<Node>();
		StringBuilder iterations = new StringBuilder();
		int i = 0;
		for (LinkedList<Node> totalPath : finalPath) {
			i++;
			DijkstraGUI.jta.append("\nPath " + i);
			for (int j = 0; j < totalPath.size(); j++) {
				iterativePath.add(totalPath.get(j));
				iterations.append(totalPath.get(j).toString() + "  ");
				DijkstraGUI.jta.append("\nThe path chosen is " + iterations
						+ " cost is:"
						+ dijkstraAlgorithm.calculateTotalCost(iterativePath)
						+ "\n");
			}
			iterativePath.clear();
			iterations.setLength(0);
		}
		DijkstraGUI.jta.setCaretPosition(0);
	}
}
