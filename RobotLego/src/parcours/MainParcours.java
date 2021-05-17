package parcours;

import java.io.IOException;
import java.util.Set;

import lejos.pc.comm.NXTCommException;
import ConnectionBluetooth.IARobot;
import ConnectionBluetooth.Robot;

public class MainParcours {

	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
		Node nodeStart = new Node("Empty", -1, -1, TypeCase.LIGNE, false, false);
		
		Node nodeA = new Node("A", 0, 0, TypeCase.VIRAGE, true, false);
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
		
		Node nodeDepart = nodeJ;
		Node previousNode = nodeStart;
		
		Robot robot = new Robot(nodeDepart, previousNode, "OwOmega");
		
		IARobot.init();
		
		finish(graph, robot);

		System.out.println("");
		System.out.println("Merci d'avoir fait confiance à OwOmega pour votre prise en charge santé");
		System.out.println("");
	}
	
	public static void parcours(Graph graph, Node nodeDepart, Node nodeArrivee, Robot robot) throws IOException, InterruptedException{
		graph.djikstraReset();
		
		graph = Dijkstra.calculateShortestPathFromSource(graph, nodeDepart);
		
		System.out.println("");
		System.out.println("*** Parcours de "+nodeDepart.getName()+" vers "+nodeArrivee.getName()+" ***");
		
		for(Node n : nodeArrivee.getShortestPath()){
			if(!n.equals(robot.getCaseActuelle())){
				System.out.println("- On part de la case "+robot.getCaseActuelle().getName()+" et on va" +
						" sur la case "+n.getName());
				IARobot.deplacement(robot,n);
			}
		}
		
		System.out.println("- On touche au but, de la case "+robot.getCaseActuelle().getName()+" à la case "+nodeArrivee.getName());
		IARobot.deplacement(robot, nodeArrivee);
		System.out.println("Vous êtes arrivé sur la case "+nodeArrivee.getName());
		System.out.println("");
	}

	public static int poidsChemin(Graph graph, Node nodeDepart, Node nodeArrivee, Robot robot){
		graph.djikstraReset();
		
		graph = Dijkstra.calculateShortestPathFromSource(graph, nodeDepart);
		
		return nodeArrivee.getDistance();
	}
	
	public static Node closestVictim(Graph graph, Node nodeDepart, Robot robot){
		Set<Node> listeVictimes = graph.listeVictimes();
		Node resultat = null;
		int i = Integer.MAX_VALUE; 
		for(Node n : listeVictimes){
			if(poidsChemin(graph, nodeDepart, n, robot) <= i){
				resultat = n;
				i = poidsChemin(graph, nodeDepart, n, robot);
			}
		}
		return resultat;
	}
	
	public static Node closestHospital(Graph graph, Node nodeDepart, Robot robot){
		Set<Node> listeHopitaux = graph.listeHopitaux();
		Node resultat = null;
		int i = Integer.MAX_VALUE; 
		for(Node n : listeHopitaux){
			if(poidsChemin(graph, nodeDepart, n, robot) <= i){
				resultat = n;
				i = poidsChemin(graph, nodeDepart, n, robot);
			}
		}
		return resultat;
	}
	
	public static void finish(Graph graph, Robot robot) throws IOException, InterruptedException{
		while(!graph.listeVictimes().isEmpty()){
			Node nodeArrivee = closestVictim(graph, robot.getCaseActuelle(), robot);
			parcours(graph, robot.getCaseActuelle(), nodeArrivee, robot);
			if(robot.getNombreVictimes() == Robot.getNombrevictimesmax()){
				Node nodeHopital = closestHospital(graph, robot.getCaseActuelle(), robot);
				parcours(graph, robot.getCaseActuelle(), nodeHopital, robot);
			}
		}
		if(robot.getNombreVictimes() > 0){
			Node nodeHopital = closestHospital(graph, robot.getCaseActuelle(), robot);
			parcours(graph, robot.getCaseActuelle(), nodeHopital, robot);
		}
		// parcours(graph, robot.getCaseActuelle(), robot.getCaseDerriere(), robot);
		// fait un demi-tour pour considérer que le robot est passé par la case
	}
}
