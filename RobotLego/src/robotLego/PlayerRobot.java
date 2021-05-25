package robotLego;

import java.awt.Dimension;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import lejos.pc.comm.NXTCommException;
import parcours.Graph;
import parcours.Node;

public class PlayerRobot extends Robot {

	public PlayerRobot(Graph graph, Node caseDepart, Node caseDerriere, String nomRobot) throws NXTCommException, UnsupportedEncodingException {
		super(graph,caseDepart, caseDerriere, nomRobot);
		
		this.getCaseActuelle().setOccupe(true);
		this.getCaseDerriere().setOccupe(true);
		
		JFrame f = new JFrame();
		f.addKeyListener(new InterfaceBluetooth(this));
		f.setVisible(true);
	    f.setSize(new Dimension(500, 500)); 
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    
	    f.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(java.awt.event.WindowEvent wE) {
	            System.exit(1);
	        }

	    });
	}

	public Node canDemitourPlayer() {
    	Map<Node,Integer> liste = this.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	
    	for(Node n : nodeAdjacentes){
    		if(n.equals(this.getCaseDerriere())){
    			destination = this.getCaseDerriere();
    		}
    	}
		return destination;
	}

	@Override
	public void updatePositionAndWait(Node destination) throws IOException,
			InterruptedException {
		this.getCaseDerriere().setOccupe(false);
		super.updatePositionAndWait(destination);
		this.getCaseActuelle().setOccupe(true);
	}

	public Node canAvancerPlayer() {
    	Map<Node,Integer> liste = this.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	
    	for(Node n : nodeAdjacentes){
    		if(!n.equals(this.getCaseActuelle()) && !n.equals(this.getCaseDerriere())){
    			destination = n;
    		}// retirer plutot de la liste les cases actuelles et derriere et regarder la taille de la liste 0/1/2
    	}
		return destination;
	}

	public Node canSlipDroitPlayer() {
    	Map<Node,Integer> liste = this.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	
    	for(Node n : nodeAdjacentes){
    		if(this.isSlipDroite(n) && !(n.equals(this.getCaseDerriere()))){
    			destination = n;
    		}
    	}
		return destination;
	}

	public Node canSlipGauchePlayer() {
    	Map<Node,Integer> liste = this.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	
    	for(Node n : nodeAdjacentes){
    		if(!this.isSlipDroite(n) && !(n.equals(this.getCaseDerriere()))){
    			destination = n;
    		}
    	}
    	return destination;
	}
	

}
