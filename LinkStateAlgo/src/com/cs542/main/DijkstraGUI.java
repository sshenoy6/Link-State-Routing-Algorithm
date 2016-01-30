package com.cs542.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.cs542.algorithm.DijkstraAlgorithmImpl;
import com.cs542.algorithm.InputParser;
import java.awt.Font;
import java.awt.Color;

public class DijkstraGUI {

	public static JFrame frame;
	public static JPanel panel_1 = new JPanel();
	private final JButton btnModifyCost = new JButton("Modify Cost");
	public static JButton btnImport = new JButton("Import File");
	public static JTextField textField = new JTextField();
	public static JTextArea jta = new JTextArea();
	public static JScrollPane scrollPane = new JScrollPane();
	public String originalPath;
	DijkstraAlgorithmImpl dijkstraAlgorithm = null; 
	int choice;
	public static String[] temp = { " ", " " };
	InputParser inputParser = new InputParser();
	int[][] originalMatrix = null;
	public static String sourceRouter, destinationRouter;
	public static JTextField sourceTextField;
	public static JTextField destinationTextField;
	private JTextField router_1;
	private JTextField router_2;
	private JTextField cost_modify;
	private JTextField disablerouter1;
	private JTextField disablerouter2;
	private JTextField delete_router;

	
	/**
	 * Function that starts the execution of the program and makes the GUI visible.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DijkstraGUI window = new DijkstraGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * Create the application by invoking the initialize function
	 */
	public DijkstraGUI() {
		initialize();
	}

	
	/**
	 * Function that is invoked to set all the frame, panel , button and textArea details 
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setEnabled(false);
		frame.getContentPane().setBackground(new Color(153, 153, 204));
		frame.getContentPane().setName("mainContent.contentPane");
		frame.setVisible(true);
		JPanel panel = new JPanel();
		panel.setBounds(87, 86, 200, 640);
		frame.setSize(1500, 850);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(panel);

		JLabel lblNewLabel = new JLabel("Enter the path");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblNewLabel);
		textField.setToolTipText("Enter the path");

		panel.add(textField);
		textField.setColumns(10);

		//Actions to be performed on clicking the import file button
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputFile = null;
				inputFile = inputParser.getFile(); //function to get the fileName from the text box 
				originalPath = inputFile;
				textField.setText("");
				jta.setText("");
				if (null != inputFile) {
					originalMatrix = inputParser.loadFile(inputFile); //function call to loadFile that accepts the given input file as parameter and generates the cost matrix for the topology
					if (InputParser.counter >= 8
							&& InputParser.counter == InputParser.totalrouters) {
						//jta.append("\n\tCOST MATRIX" + "\n");
						jta.setCaretPosition(0);
						dijkstraAlgorithm = inputParser.findOptimalPath(
								originalMatrix, -1); //function call to find the optimal path with the given source and destination routers
					}
				}
			}
		});
		panel.add(btnImport);

		final JButton btnbuildConnectionTable = new JButton("Build Connection Table");
		//actions that are performed when the Build Connection Table button is clicked
		btnbuildConnectionTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jta.setText("");
				textField.setText("");
				for (int i = 1; i <= originalMatrix.length; i++) {
					inputParser.buildRoutingTable(originalMatrix,
							dijkstraAlgorithm, String.valueOf(i)); //function call to the build routing table using the cost matrix that was previously generated for the given network topology.
				}
			}
		});
		panel.add(btnbuildConnectionTable);
		// Modification to the buttons by providing some properties.
		JLabel lblDestinationRouter = new JLabel("Enter Source Router");
		lblDestinationRouter.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblDestinationRouter);

		sourceTextField = new JTextField();
		sourceTextField.setToolTipText("Enter Source Router");
		panel.add(sourceTextField);
		sourceTextField.setColumns(10);

		JLabel lblEnterDestinationRouter = new JLabel(
				"Enter Destination Router");
		lblEnterDestinationRouter.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblEnterDestinationRouter);

		destinationTextField = new JTextField();
		destinationTextField.setToolTipText("Enter Destination Router");
		panel.add(destinationTextField);
		destinationTextField.setColumns(10);

		JButton btnShortestPath = new JButton("Shortest Path");
		//actions that are performed on click of shortest path 
		btnShortestPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jta.setText("");
				sourceRouter = inputParser.getSourceRouter(); // line that gets the source router from the textfield
				temp[0] = sourceRouter;
				destinationRouter = inputParser.getDestinationRouter(); //line that gets the destination router from the textfield
				temp[1] = destinationRouter;
				if (sourceRouter.length()==0 || destinationRouter.length()==0) {
					JOptionPane.showMessageDialog(null,
							"Enter a valid source/destination node\n"); //validation to check if the source and destination routers are entered correctly
				}
				int temp1 = Integer.parseInt(temp[1]);
				if (temp[0].equals(temp[1])) {
					JOptionPane
					.showMessageDialog(
							null,
							"\nThe source and destination entered are same. The path and cost cannot be computed."); //validation to check if the entered source and destination routers are the same
				} else if (temp[0].equals("0") || temp[1].equals("0")) {
					JOptionPane
					.showMessageDialog(null,
							"\nInvalid input. One of the router entered is 0. Please re-enter."); //validation to check if the entered routers are zero ,that doesnt belong to the topology matrix given.
				} else if (temp1 > originalMatrix.length) {
					JOptionPane
					.showMessageDialog(null,
							"\nThe destination router entered is not in the network.\n"); //validation to find if the entered destination is not in the network.
				} else {
					inputParser.computePath(dijkstraAlgorithm, temp,1); // if all validations are passed, call the computePath function that calculates the path
				}
				jta.append("\n"+DijkstraAlgorithmImpl.demoString.toString());
				sourceTextField.setText("");
				destinationTextField.setText("");
			}
		});
		panel.add(btnShortestPath);

		JButton btnAddRouter = new JButton("Add Router");
		//action listener when the add router button is clicked
		btnAddRouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputFile = null;
				inputFile = inputParser.getFile(); //function that retrieves the file in the given text field.
				if(inputFile ==null){
					JOptionPane.showMessageDialog(null, "Enter the path of updated topology file\n"); //validation to check if the new file path is inserted correctly
				}
				textField.setText("");
				jta.setText("");
				if (null != inputFile) {
					originalMatrix = inputParser.loadFile(inputFile); //function that creates the cost matrix for the given topology.
					if (InputParser.counter >= 8
							&& InputParser.counter == InputParser.totalrouters) {
						dijkstraAlgorithm = inputParser.findOptimalPath(
								originalMatrix, -1); // function call to find the optimal path between the source and destination routers
						// Display Routing table
						for (int i = 1; i <= originalMatrix.length; i++) {
							inputParser.buildRoutingTable(originalMatrix,
									dijkstraAlgorithm, String.valueOf(i)); // function call to display the routing table for the given cost matrix of the given topology.
						}

						inputParser.computePath(dijkstraAlgorithm, temp,-1); //function call to find the path between the source and destination routers

					}
				}
			}
		});
		panel.add(btnAddRouter);

		//action listener that is called when the Modify Cost button is called
		btnModifyCost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				jta.setText("");
				if(router_1.getText() == ""||router_2.getText() == "" ||cost_modify.getText() == "")
				{
					JOptionPane.showMessageDialog(null, "Please enter Edge Router 1/ Edge Router 2 / Modified Cost \n"); //validation that validates if the edge router 1 and edge router 2 and cost between them is given.
				}
				int router1 = Integer.parseInt(router_1.getText());
				int router2 = Integer.parseInt(router_2.getText());
				int modifiedCost = Integer.parseInt(cost_modify.getText());
				
					originalMatrix[router1 - 1][router2 - 1] = modifiedCost;
					jta.append("\n\tCOST MATRIX" + "\n");
					//line of code that prints the modified cost matrix
					for (int i = 0; i < originalMatrix.length; i++) {
						jta.append("\t\t|");
						for (int j = 0; j < originalMatrix.length; j++) {
							jta.append(originalMatrix[i][j] + "  ");
						}
						jta.append("|\n");
					}


					dijkstraAlgorithm = inputParser.findOptimalPath(
							originalMatrix, -1); //function call to find the optimal path between the source and destination router in the network topology

					for (int i = 1; i <= originalMatrix.length; i++) {
						inputParser.buildRoutingTable(originalMatrix,
								dijkstraAlgorithm, String.valueOf(i)); // function call to display the routing table of all the routers that are present in the given network topology.
					}
				inputParser.computePath(dijkstraAlgorithm, temp,-1); //function call to find the path between the source and destination routers.
				router_1.setText("");
				router_2.setText("");
				cost_modify.setText("");
			}
		});

		JLabel lblEnterTheSource = new JLabel("Enter the router 1");
		lblEnterTheSource.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblEnterTheSource);

		router_1 = new JTextField();
		router_1.setToolTipText("Enter Router 1");
		panel.add(router_1);
		router_1.setColumns(10);

		JLabel lblEnterTheDestination = new JLabel("Enter the router 2");
		lblEnterTheDestination.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblEnterTheDestination);

		router_2 = new JTextField();
		router_2.setToolTipText("Enter Router 2");
		panel.add(router_2);
		router_2.setColumns(10);

		JLabel lblNewCost = new JLabel("Enter the new cost");
		lblNewCost.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblNewCost);

		cost_modify = new JTextField();
		cost_modify.setToolTipText("Enter new cost");
		panel.add(cost_modify);
		cost_modify.setColumns(10);

		panel.add(btnModifyCost);

		JLabel lblDisableLink = new JLabel("Enter the Edge Router 1");
		lblDisableLink.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblDisableLink);

		disablerouter1 = new JTextField();
		disablerouter1.setToolTipText("Enter source router");
		panel.add(disablerouter1);
		disablerouter1.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Enter the Edge Router 2");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblNewLabel_1);

		disablerouter2 = new JTextField();
		disablerouter2.setToolTipText("Enter destination router");
		panel.add(disablerouter2);
		disablerouter2.setColumns(10);

		JButton btnDisableLink = new JButton("Disable Link");
		//action listener that is called when the Disable link is clicked
		btnDisableLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(disablerouter1.getText() == "" || disablerouter2.getText() == ""){
					JOptionPane.showMessageDialog(null, "The routers in the path to be disabled are not entered\n"); //validation to make sure that the router to be disabled is entered
				}
				textField.setText("");
				jta.setText("");
				//jta.append("\n\tCOST MATRIX" + "\n");
				int router1 = Integer.parseInt(disablerouter1.getText());
				int router2 = Integer.parseInt(disablerouter2.getText());
				int modifiedCost = -1;
				originalMatrix[router1 - 1][router2 - 1] = modifiedCost;
				jta.append("\n\tMODIFIED COST MATRIX\n");
				for (int i = 0; i < InputParser.costMatrix.length; i++) {
					jta.append("\t\t|");
					for (int j = 0; j < originalMatrix.length; j++) {
						jta.append(originalMatrix[i][j] + "  ");
					}
					jta.append("|\n");
				}
				dijkstraAlgorithm = inputParser.findOptimalPath(originalMatrix,
						-1); //function to find the optimal path between the source and destination router
				for (int i = 1; i <= originalMatrix.length; i++) {
					inputParser.buildRoutingTable(originalMatrix,
							dijkstraAlgorithm, String.valueOf(i)); //function that builds the routing table for each of the routers in the given topology.
				}
				inputParser.computePath(dijkstraAlgorithm, temp,-1); //function that builds the path between the source and destination routers
				disablerouter1.setText("");
				disablerouter2.setText("");
			}
		});
		panel.add(btnDisableLink);

		JLabel lblEnterTheRouter = new JLabel("Enter the router to be deleted");
		lblEnterTheRouter.setFont(new Font("Arial", Font.BOLD, 13));
		panel.add(lblEnterTheRouter);

		delete_router = new JTextField();
		delete_router.setToolTipText("Enter router to be deleted");
		panel.add(delete_router);
		delete_router.setColumns(10);

		JButton btnDeleteRouter = new JButton("Delete Router");
		//action listener when the delete router button is clicked
		btnDeleteRouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(delete_router.getText()== ""){
					JOptionPane.showMessageDialog(null, "Please enter the router to be deleted."); //validation to check if the router to be deleted is entered in the textfield or not
				}
				int deleteRouter = Integer.parseInt(delete_router.getText());
				textField.setText("");
				jta.setText("");
				
					if(originalPath.length()==0){
						JOptionPane.showMessageDialog(null, "Please provide the initial file path");
					}
					jta.append("\n\tCOST MATRIX" + "\n");
					originalMatrix = inputParser.deleteNode(originalPath,deleteRouter); // function call to deleteNode where the particular router is marked unreachable from the rest of the routers in the given network topology

					dijkstraAlgorithm = inputParser
							.findOptimalPath(originalMatrix,deleteRouter); //function call to find the optimal path between the given source and destination routers
					for (int i = 1; i <= originalMatrix.length; i++) {
						inputParser.buildRoutingTable(originalMatrix,
								dijkstraAlgorithm, String.valueOf(i)); //function call that builds the routing table for each routers in the routing table
					}
					inputParser.computePath(dijkstraAlgorithm, temp,-1);
					delete_router.setText("");
				}
		});
		panel.add(btnDeleteRouter);
		// following lines of code are used to design the GUI
		JLabel lblCsLink = new JLabel(
				"CS 542 Link State Algorithm Implementation");
		lblCsLink.setToolTipText("Link State Algorithm Implementation");
		lblCsLink.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 28));
		lblCsLink.setBounds(495, 26, 556, 50);
		frame.getContentPane().add(lblCsLink);

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(1158, 755, 89, 23);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		frame.getContentPane().add(btnExit);

		scrollPane.setBounds(335, 86, 912, 640);
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(jta);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);

		jta.setColumns(32);
		jta.setRows(100);

		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(551, 126, 200, 50);
		frame.getContentPane().add(scrollBar);

	}
}
