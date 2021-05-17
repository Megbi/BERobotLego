package ConnectionBluetooth;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import lejos.nxt.remote.AsciizCodec;
import lejos.pc.comm.NXTComm;
import parcours.Node;
import parcours.TypeCase;

public class Deplacements implements KeyListener{

    NXTComm commRobot;
    Robot robot;
    byte[] request;
    byte[] requeste;
    byte[] avancer;
    byte[] demitour;
    byte[] slipgauche;
    byte[] slipdroit;
    
    public byte[] getAvancer() {
		return avancer;
	}

	public byte[] getDemitour() {
		return demitour;
	}

	public byte[] getSlipgauche() {
		return slipgauche;
	}

	public byte[] getSlipdroit() {
		return slipdroit;
	}

	public Deplacements(Robot robot) {
		this.robot = robot;
        this.commRobot = robot.getComm();
        try{
            init();
            }
            catch(Exception e){
            	
            }
        
    }
    
    private void init() throws UnsupportedEncodingException{
    	this.avancer = new byte[22];
    	avancer[0]=(byte)0x00;
    	avancer[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("av.rxe"), 0, this.avancer, 2, AsciizCodec.encode("av.rxe").length);
        
        this.demitour = new byte[22];
        demitour[0]=(byte)0x00;
        demitour[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("dt.rxe"), 0, this.demitour, 2, AsciizCodec.encode("dt.rxe").length);
        
        this.slipgauche = new byte[22];
        slipgauche[0]=(byte)0x00;
        slipgauche[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("sg.rxe"), 0, this.slipgauche, 2, AsciizCodec.encode("sg.rxe").length);
        
        this.slipdroit = new byte[22];
        slipdroit[0]=(byte)0x00;
        slipdroit[1]=(byte)0x00;
        System.arraycopy(AsciizCodec.encode("sd.rxe"), 0, this.slipdroit, 2, AsciizCodec.encode("sd.rxe").length);
        
    }

	@Override
    public void keyPressed(KeyEvent arg0) {
        int keyCode = arg0.getKeyCode();
        try{
            // ajouter ZQSD pour ajuster manuellement
            switch(keyCode) {
            case KeyEvent.VK_UP:
            	if(((robot.getCaseActuelle().getType() == TypeCase.VIRAGE)||(robot.getCaseActuelle().getType() == TypeCase.LIGNE)) && avancerPlayer(robot)){
            		commRobot.sendRequest(avancer, 3);
            		System.out.println("From : "+robot.getCaseDerriere().getName()+" | To : "+robot.getCaseActuelle().getName()+" | Move on : "+robot.getCaseDerriere().getType());
                }
            	break;
            case KeyEvent.VK_DOWN:
            	if(demitourPlayer(robot)){
            		commRobot.sendRequest(demitour, 3);
            		System.out.println("From : "+robot.getCaseDerriere().getName()+" | To : "+robot.getCaseActuelle().getName()+" | Move on : "+robot.getCaseDerriere().getType());
            	}
            	break;
            case KeyEvent.VK_RIGHT:
            	if((robot.getCaseActuelle().getType() == TypeCase.SLIP) && slipDroitPlayer(robot)){
            		commRobot.sendRequest(slipdroit, 3);
            		System.out.println("From : "+robot.getCaseDerriere().getName()+" | To : "+robot.getCaseActuelle().getName()+" | Move on : "+robot.getCaseDerriere().getType());
                }
            	break;
            case KeyEvent.VK_LEFT:
            	if((robot.getCaseActuelle().getType() == TypeCase.SLIP) && slipGauchePlayer(robot)){
            		commRobot.sendRequest(slipgauche, 3);
            		System.out.println("From : "+robot.getCaseDerriere().getName()+" | To : "+robot.getCaseActuelle().getName()+" | Move on : "+robot.getCaseDerriere().getType());
                }
            	break;
            default:
                break;
        }
        }catch(Exception e){
            System.out.println("OwOmega a des problemes");
            System.out.println(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        
    }
    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }
    
    public boolean avancerPlayer(Robot robot){
    	Map<Node,Integer> liste = robot.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	boolean temp = false;
    	for(Node n : nodeAdjacentes){
    		if(!n.equals(robot.getCaseActuelle()) && !n.equals(robot.getCaseDerriere())){
    			temp = true;
    			destination = n;
    		}// retirer plutot de la liste les cases actuelles et derriere et regarder la taille de la liste 0/1/2
    	}
    	if(temp){
    		robot.setCaseDerriere(robot.getCaseActuelle());
        	robot.setCaseActuelle(destination);
    	}
    	return temp;
    }
    
    public boolean demitourPlayer(Robot robot){
    	Map<Node,Integer> liste = robot.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	boolean test = false;
    	
    	for(Node n : nodeAdjacentes){
    		if(n.equals(robot.getCaseDerriere())){
    			test = true;
    		}
    	}
    	if(test){
    		Node destination = robot.getCaseDerriere();
    		robot.setCaseDerriere(robot.getCaseActuelle());
        	robot.setCaseActuelle(destination);
    	}
    	return test;
    }
    
    public boolean slipDroitPlayer(Robot robot){
    	Map<Node,Integer> liste = robot.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	boolean test = false;
    	
    	for(Node n : nodeAdjacentes){
    		if(IARobot.isSlipDroite(robot, n) && !(n.equals(robot.getCaseDerriere()))){
    			destination = n;
    			test = true;
    		}
    	}
    	if(test){
    		robot.setCaseDerriere(robot.getCaseActuelle());
        	robot.setCaseActuelle(destination);
    	}
    	return test;
    }
    
    public boolean slipGauchePlayer(Robot robot){
    	Map<Node,Integer> liste = robot.getCaseActuelle().getAdjacentNodes();
    	Set<Node> nodeAdjacentes = liste.keySet();
    	Node destination = null;
    	boolean test = false;
    	
    	for(Node n : nodeAdjacentes){
    		if(!IARobot.isSlipDroite(robot, n) && !(n.equals(robot.getCaseDerriere()))){
    			destination = n;
    			test = true;
    		}
    	}
    	if(test){
    		robot.setCaseDerriere(robot.getCaseActuelle());
        	robot.setCaseActuelle(destination);
    	}
    	return test;
    }
}
