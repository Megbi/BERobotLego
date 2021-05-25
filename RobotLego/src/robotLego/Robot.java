package robotLego;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lejos.nxt.remote.AsciizCodec;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import parcours.Graph;
import parcours.Node;
import parcours.TypeCase;

public class Robot {
	private static final int nombreVictimesMax = 2;
	private byte[] avancer;
	private byte[] demitour;
	private byte[] slipgauche;
	private byte[] slipdroit;
	private byte[] wait;
	private Node caseDerriere;
	private Node caseActuelle;
	private int nombreVictimes = 0;
	private NXTComm Comm;
	private String name;
	private Graph parcours;
	
	public Robot(Graph graph, Node caseDepart, Node caseDerriere, String nomRobot) throws NXTCommException, UnsupportedEncodingException{
		this.caseActuelle = caseDepart;
		this.caseDerriere = caseDerriere;
		this.parcours = graph;
		this.name = nomRobot;
		this.Comm = linkToRobot();
		init();
	}
	//Varargs???
	public void deplacement(Robot robotPlayer, Node destination) throws IOException, InterruptedException{
		if(robotPlayer.getCaseActuelle().equals(destination) || robotPlayer.getCaseDerriere().equals(destination)){
			attendre(robotPlayer, destination);
		}
		
		if(this.getCaseDerriere().equals(destination)){
			demiTour(destination);
		}
		else{
			if(this.getCaseActuelle().getType() == TypeCase.SLIP){
				if(isSlipDroite(destination)){
					slipDroit(destination);
				}
				else{
					slipGauche(destination);
				}
			}
			else{

				avancer(destination);
			}
		}
	}
	public void avancer(Node destination) throws IOException, InterruptedException {
		this.getComm().sendRequest(avancer,3);
		//System.out.println("Le robot effectue AvancerF");
		updatePositionAndWait(destination);
	}
	public void slipGauche(Node destination) throws IOException, InterruptedException {
		this.getComm().sendRequest(slipgauche,3);
		//System.out.println("Le robot effectue SlipGauche");
		updatePositionAndWait(destination);
	}
	public void slipDroit(Node destination) throws IOException, InterruptedException {
		this.getComm().sendRequest(slipdroit,3);
		//System.out.println("Le robot effectue SlipDroit");
		updatePositionAndWait(destination);
	}
	public void demiTour(Node destination) throws IOException, InterruptedException {
		this.getComm().sendRequest(demitour,3);
		//System.out.println("Le robot effectue Demi-tour");
		updatePositionAndWait(destination);
	}
	public void updatePositionAndWait(Node destination) throws IOException, InterruptedException {
//		this.caseDerriere.setOccupe(false);
		
		this.setCaseDerriere(this.getCaseActuelle());
		this.setCaseActuelle(destination);
		
//		this.caseActuelle.setOccupe(true);
		
		while(isRunning()){
			TimeUnit.MICROSECONDS.sleep(100);
		}
		this.recuperationVictime(destination);
		this.depotVictimes(destination);
		TimeUnit.SECONDS.sleep(1);
	}

	private void attendre(Robot robotPlayer, Node destination) throws InterruptedException {
		System.out.println(this.getName()+" : la case "+destination.getName()+" est occupée");

		while(robotPlayer.getCaseActuelle().equals(destination) || robotPlayer.getCaseDerriere().equals(destination) ){
			TimeUnit.SECONDS.sleep(1);
		}
	}
	
	public boolean isSlipDroite(Node destination){
		int xDir = this.getCaseActuelle().getX()-this.getCaseDerriere().getX();
		int yDir = this.getCaseActuelle().getY()-this.getCaseDerriere().getY();
		int xDest = destination.getX()-this.getCaseActuelle().getX();
		int yDest = destination.getY()-this.getCaseActuelle().getY();
		int xVir = this.getCaseActuelle().getX()-this.getCaseActuelle().getSlipJoinX();
		int yVir = this.getCaseActuelle().getY()-this.getCaseActuelle().getSlipJoinY();
		
		if(yDir == -1){ // vient de la droite
			if(xDest == -1){
				return true;
			}
			else if(xDir == xDest && yDir == yDest){
				if(xVir == -1){
					return true;
				}
			}
		}
		else if(yDir == 1){ // vient de la gauche
			if(xDest == 1){
				return true;
			}
			else if(xDir == xDest && yDir == yDest){
				if(xVir == 1){
					return true;
				}
			}
		}
		else if(xDir == -1){ // vient du bas
			if(yDest == 1){
				return true;
			}
			else if(xDir == xDest && yDir == yDest){
				if(yVir == 1){
					return true;
				}
			}
		}
		else{ // vient du haut
			if(yDest == -1) {
				return true;
			}
			else if(xDir == xDest && yDir == yDest){
				if(yVir == -1){
					return true;
				}
			}
		}
		return false;
	}
    
    public Boolean isRunning() throws IOException{
    	byte[] answer = new byte[22];
    	answer = this.getComm().sendRequest(wait,22);
    	if (answer[2]==(byte)0x00) {
    		return true;
		}
        return false;
    }
	
	private void init() throws UnsupportedEncodingException{
    	avancer = new byte[22];
    	avancer[0]=(byte)0x00;
    	avancer[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("av.rxe"), 0, avancer, 2, AsciizCodec.encode("av.rxe").length);
        
        demitour = new byte[22];
        demitour[0]=(byte)0x00;
        demitour[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("dt.rxe"), 0, demitour, 2, AsciizCodec.encode("dt.rxe").length);
        
        slipgauche = new byte[22];
        slipgauche[0]=(byte)0x00;
        slipgauche[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("sg.rxe"), 0, slipgauche, 2, AsciizCodec.encode("sg.rxe").length);
        
        slipdroit = new byte[22];
        slipdroit[0]=(byte)0x00;
        slipdroit[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("sd.rxe"), 0, slipdroit, 2, AsciizCodec.encode("sd.rxe").length);
        
        wait = new byte[2];
        wait[0]=(byte)0x00;
        wait[1]=(byte)0x11;
        
    }
	
	public String getName(){
		return name;
	}
	
	
	public NXTComm getComm() {
		return Comm;
	}


	public void setComm(NXTComm Comm) {
		this.Comm = Comm;
	}


	public NXTComm linkToRobot() throws NXTCommException{
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
	    NXTInfo[] nxtInfo = nxtComm.search(this.name);
	    if (nxtInfo.length == 0) {
	        System.out.println("No nxt found");
	        System.exit(1);
	    }
	    nxtComm.open(nxtInfo[0]);
	    
	    System.out.println("Connecté à "+ nxtInfo[0].name + " à l'adresse : "  + nxtInfo[0].deviceAddress);
	    return nxtComm;
	}
	
	public void recuperationVictime(Node node){
		if(node.isVictime() && (this.nombreVictimes < nombreVictimesMax)){
			this.nombreVictimes++;
			node.setVictime(false);
			System.out.println("-> Victime récupérée par "+this.name+" <-");
		}
		else if(node.isVictime() && (this.nombreVictimes == nombreVictimesMax)){
			System.out.println("-> "+this.name+" ne peut pas prendre une victime supplémentaire <-");
		}
	}
	
	public void depotVictimes(Node node){
		if((nombreVictimes >= 1) && node.isHopital()){
			System.out.println("-> Victime(s) déposée(s) par "+this.name+" <-");
			this.nombreVictimes = 0;
		}
	}

	public Node getCaseDerriere() {
		return caseDerriere;
	}

	public void setCaseDerriere(Node caseDerriere) {
		this.caseDerriere = caseDerriere;
	}

	public Node getCaseActuelle() {
		return caseActuelle;
	}

	public void setCaseActuelle(Node caseActuelle) {
		this.caseActuelle = caseActuelle;
	}

	public int getNombreVictimes() {
		return nombreVictimes;
	}

	public void setNombreVictimes(int nombreVictimes) {
		this.nombreVictimes = nombreVictimes;
	}


	public static int getNombrevictimesmax() {
		return nombreVictimesMax;
	}
	

	//Robotplayer necessaire pour connaitre la position de lautre root, used in attendre, should be varargs as they me be 0 or many robot.
	public void finish(Robot robotPlayer) throws IOException, InterruptedException{
		
		while(!this.parcours.listeVictimes().isEmpty()){
			Node nodeArrivee = closestVictim();
			parcours(this.getCaseActuelle(), nodeArrivee, robotPlayer);
			if(this.getNombreVictimes() == Robot.getNombrevictimesmax()){
				Node nodeHopital = closestHospital();
				parcours(this.getCaseActuelle(), nodeHopital, robotPlayer);
			}
		}
		if(this.getNombreVictimes() > 0){
			Node nodeHopital = closestHospital();
			parcours(this.getCaseActuelle(), nodeHopital, robotPlayer);
		}
		
		System.out.println(this.getName()+" a fini son service");
		// parcours(graph, robot.getCaseActuelle(), robot.getCaseDerriere(), robot);
		// fait un demi-tour pour considérer que le robot est passé par la case
		
		System.out.println("");
		System.out.println("Merci d'avoir fait confiance à OwOmega pour votre prise en charge santé");
		System.out.println("");
	}
	

	
	public Node closestVictim(){
		Set<Node> listeVictimes = this.parcours.listeVictimes();
		Node resultat = null;
		int i = Integer.MAX_VALUE; 
		for(Node n : listeVictimes){
			if(poidsChemin(this.getCaseActuelle(), n) <= i){
				resultat = n;
				i = poidsChemin(this.getCaseActuelle(), n);
			}
		}
		return resultat;
	}
	
	public Node closestHospital(){
		Set<Node> listeHopitaux = this.parcours.listeHopitaux();
		Node resultat = null;
		int i = Integer.MAX_VALUE; 
		for(Node n : listeHopitaux){
			if(poidsChemin(this.getCaseActuelle(), n) <= i){
				resultat = n;
				i = poidsChemin(this.getCaseActuelle(), n);
			}
			System.out.println(n.getName()+" a un poids de "+poidsChemin(this.getCaseActuelle(), n)+" !!");
		}
		return resultat;
	}
	

	//TODO a enlever
	public int poidsChemin(Node nodeDepart, Node nodeArrivee){
		this.parcours.djikstraReset();
		
		calculateShortestPathFromSource(nodeDepart);
		
		return nodeArrivee.getDistance();
	}

	
	public void parcours(Node nodeDepart, Node nodeArrivee, Robot robotPlayer) throws IOException, InterruptedException{
		this.parcours.djikstraReset();
		
		calculateShortestPathFromSource(nodeDepart);
		
		System.out.println("*** "+this.getName()+" fait le parcours de "+nodeDepart.getName()+" vers "+nodeArrivee.getName()+" ***");
		
		for(Node n : nodeArrivee.getShortestPath()){
			if(nodeArrivee.isVictime() || nodeArrivee.isHopital()){
				if(!n.equals(caseActuelle)){
					System.out.println(this.name+" : "+this.caseActuelle.getName()+" -> "+nodeArrivee.getName()+" ("+this.caseActuelle.getType()+")");
					deplacement( robotPlayer, n);
				}
			}
		}
		
		if(nodeArrivee.isVictime() || nodeArrivee.isHopital()){
			System.out.println(this.name+" : "+this.caseActuelle.getName()+" -> "+nodeArrivee.getName()+" ("+this.caseActuelle.getType()+")");
			deplacement(robotPlayer, nodeArrivee);
		}
	}
	

	private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
		Integer sourceDistance = sourceNode.getDistance();
		if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
			evaluationNode.setDistance(sourceDistance + edgeWeigh);
			if (evaluationNode.isOccupe()) {
				evaluationNode.setDistance(evaluationNode.getDistance()+10);
			}
			LinkedList<Node> shortestPath = new LinkedList<Node>(sourceNode.getShortestPath());
			shortestPath.add(sourceNode);
			evaluationNode.setShortestPath(shortestPath);
		}
	}
	
	private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
	    Node lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Node node: unsettledNodes) {
	        int nodeDistance = node.getDistance();
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }
	    return lowestDistanceNode;
	}
	
	public void calculateShortestPathFromSource(Node source) {
		this.parcours.djikstraReset();
	    source.setDistance(0);

	    Set<Node> settledNodes = new HashSet<Node>();
	    Set<Node> unsettledNodes = new HashSet<Node>();

	    unsettledNodes.add(source);

	    while (unsettledNodes.size() != 0) {
	        Node currentNode = getLowestDistanceNode(unsettledNodes);
	        unsettledNodes.remove(currentNode);
	        for (Entry<Node, Integer> adjacencyPair: 
	          currentNode.getAdjacentNodes().entrySet()) {
	            Node adjacentNode = adjacencyPair.getKey();
	            Integer edgeWeight = adjacencyPair.getValue();
	            if (!settledNodes.contains(adjacentNode)) {
	                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }
	        }
	        settledNodes.add(currentNode);
	    }
	}
	
}
