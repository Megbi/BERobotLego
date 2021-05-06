package parcours;

import java.io.IOException;
import java.util.Set;

import lejos.pc.comm.NXTCommException;
import ConnectionBluetooth.IARobot;
import ConnectionBluetooth.Robot;

public class MainParcours {

	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
		Node nodeStart = new Node("Empty", -1, -1, TypeCase.LIGNE, false, false);
		
		Node nodeA = new Node("A", 0, 0, TypeCase.LIGNE, true, false);
		Node nodeB = new Node("B", 0, 1, TypeCase.LIGNE, false, true);
		Node nodeC = new Node("C", 0, 2, TypeCase.SLIP, false, false);
		Node nodeD = new Node("D", 1, 2, TypeCase.SLIP, false, false); 
		Node nodeE = new Node("E", 1, 3, TypeCase.LIGNE, false, false);
		Node nodeF = new Node("F", 2, 0, TypeCase.LIGNE, false, true);
		Node nodeG = new Node("G", 2, 1, TypeCase.LIGNE, true, false);
		Node nodeH = new Node("H", 2, 2, TypeCase.SLIP, false, false);
		
		nodeC.setJointures(1, 2);
		nodeD.setJointures(1, 3);
		nodeH.setJointures(1, 2);

		nodeA.addDestination(nodeB, 1);
		
		nodeB.addDestination(nodeC, 1);
		
		nodeC.addDestination(nodeD, 1);
		
		nodeD.addDestination(nodeE, 1);
		nodeD.addDestination(nodeH, 1);
		
		nodeH.addDestination(nodeG, 1);
		
		nodeG.addDestination(nodeF, 1);

		Graph graph = new Graph();

		graph.addNode(nodeA);
		graph.addNode(nodeB);
		graph.addNode(nodeC);
		graph.addNode(nodeD);
		graph.addNode(nodeE);
		graph.addNode(nodeF);
		graph.addNode(nodeG);
		graph.addNode(nodeH);
		
		Node nodeDepart = nodeE;
		Node previousNode = nodeStart;
		
		Robot robot = new Robot(nodeDepart, previousNode, "OwOmega");
		
		IARobot.init();
		
		finish(graph, nodeDepart, robot);

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
			
			System.out.println("- On part de la case "+robot.getCaseActuelle().getName()+" et on va" +
						" sur la case "+n.getName());
			IARobot.deplacement(robot,n);
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
	
	public static void finish(Graph graph, Node nodeDepart, Robot robot) throws IOException, InterruptedException{
		while(!graph.listeVictimes().isEmpty()){
			Node nodeArrivee = closestVictim(graph, nodeDepart, robot);
			parcours(graph,nodeDepart,nodeArrivee,robot);
			nodeDepart = nodeArrivee;
			if(robot.getNombreVictimes() == Robot.getNombrevictimesmax()){
				Node nodeHopital = closestHospital(graph, nodeDepart, robot);
				parcours(graph, nodeDepart, nodeHopital, robot);
				nodeDepart = nodeHopital;
			}
		}
		if(robot.getNombreVictimes() > 0){
			Node nodeHopital = closestHospital(graph, nodeDepart, robot);
			parcours(graph, nodeDepart, nodeHopital, robot);
			nodeDepart = nodeHopital;
		}
	}
}
