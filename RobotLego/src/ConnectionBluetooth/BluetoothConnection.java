package ConnectionBluetooth;

import java.awt.Dimension;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;

import lejos.pc.comm.NXTCommException;
import parcours.Graph;
import parcours.MainParcours;
import parcours.Node;
import parcours.TypeCase;

public class BluetoothConnection {
	  public static void main (String[] args) throws NXTCommException, InterruptedException, UnsupportedEncodingException, IOException {
	    Node nodeStart = new Node("Empty", -1, -1, TypeCase.LIGNE, false, false);
		  
		Node nodeA = new Node("A", 0, 0, TypeCase.VIRAGE, false, false);
		Node nodeB = new Node("B", 0, 1, TypeCase.SLIP, false, false);
		Node nodeC = new Node("C", 0, 2, TypeCase.SLIP, false, false);
		Node nodeO = new Node("O", 0, 3, TypeCase.VIRAGE, true, false);
		Node nodeD = new Node("D", 1, 0, TypeCase.LIGNE, false, false); 
		Node nodeE = new Node("E", 1, 1, TypeCase.LIGNE, false, false);
		Node nodeF = new Node("F", 1, 2, TypeCase.VIRAGE, false, false);
		Node nodeG = new Node("G", 1, 3, TypeCase.SLIP, false, true);
		Node nodeH = new Node("H", 2, 0, TypeCase.VIRAGE, true, false);
		Node nodeI = new Node("I", 2, 1, TypeCase.SLIP, false, true);
		Node nodeJ = new Node("J", 2, 2, TypeCase.LIGNE, false, false);
		Node nodeK = new Node("K", 2, 3, TypeCase.LIGNE, false, false);
		Node nodeL = new Node("L", 3, 1, TypeCase.VIRAGE, false, false);
		Node nodeM = new Node("M", 3, 2, TypeCase.SLIP, false, false);
		Node nodeN = new Node("N", 3, 3, TypeCase.VIRAGE, true, false);
			
		nodeB.setJointures(1, 1);
		nodeC.setJointures(1, 2);
		nodeG.setJointures(1, 2);
		nodeI.setJointures(2, 0);
		nodeM.setJointures(2, 2);

		nodeA.addDestination(nodeB, 1);
		nodeA.addDestination(nodeD, 1);
		
		nodeB.addDestination(nodeC, 1);
		nodeB.addDestination(nodeE, 1);
		nodeC.addDestination(nodeO, 1);
		nodeC.addDestination(nodeF, 1);
		
		nodeO.addDestination(nodeG, 1);
		
		nodeD.addDestination(nodeH, 1);
		
		nodeE.addDestination(nodeI, 1);
		
		nodeF.addDestination(nodeG, 1);
		
		nodeG.addDestination(nodeK, 1);
		
		nodeH.addDestination(nodeI, 1);
		
		nodeI.addDestination(nodeL, 1);
			
		nodeJ.addDestination(nodeM, 1);
			
		nodeK.addDestination(nodeN, 1);
			
		nodeL.addDestination(nodeM, 1);
			
		nodeM.addDestination(nodeN, 1);

		Graph graph = new Graph();

		graph.addNode(nodeA);
		graph.addNode(nodeB);
		graph.addNode(nodeC);
		graph.addNode(nodeD);
		graph.addNode(nodeE);
		graph.addNode(nodeF);
		graph.addNode(nodeG);
		graph.addNode(nodeH);
		graph.addNode(nodeI);
		graph.addNode(nodeJ);
		graph.addNode(nodeK);
		graph.addNode(nodeL);
		graph.addNode(nodeM);
		graph.addNode(nodeN);
		graph.addNode(nodeO);
			
		Node caseDepartPlayer = nodeK;
		Node caseDerrierePlayer = nodeN;
		
		Node caseDepartIA = nodeJ;
		Node caseDerriereIA = nodeF;
		  
	    Robot robotPlayer = new Robot(caseDepartPlayer, caseDerrierePlayer, "OwOmega");
	    Robot robotIA = new Robot(caseDepartIA, caseDerriereIA, "FUBUKI");
	    
	    JFrame f = new JFrame();
	    f.addKeyListener(new Deplacements(robotPlayer));
	    f.setVisible(true);
	    f.setSize(new Dimension(500, 500)); 
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    MainParcours.finish(graph, robotIA, robotPlayer);
	    
	    f.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent wE) {
	            System.exit(1);
	        }
	    });
	  }
	  
	  
	}