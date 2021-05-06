package ConnectionBluetooth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import lejos.nxt.remote.AsciizCodec;
import lejos.pc.comm.NXTComm;
import parcours.Node;
import parcours.TypeCase;

public class IARobot {
	static byte[] avancer;
    static byte[] demitour;
    static byte[] slipgauche;
    static byte[] slipdroit;
    static byte[] wait;
	
	public static void deplacement(Robot robot, Node destination) throws IOException, InterruptedException{
		boolean care = true;
		if(robot.getCaseActuelle().equals(destination)){
			care = false;
			System.out.println("Aucune action effectuée");
		}
		else if(robot.getCaseDerriere().equals(destination)){
			robot.getComm().sendRequest(demitour,3);
			System.out.println("Le robot effectue Demi-tour");
		}
		else{
			if(robot.getCaseActuelle().getType() == TypeCase.SLIP){
				if(isSlipDroite(robot,destination)){
					robot.getComm().sendRequest(slipdroit,3);
					System.out.println("Le robot effectue SlipDroit");
				}
				else{
					robot.getComm().sendRequest(slipgauche,3);
					System.out.println("Le robot effectue SlipGauche");
				}
			}
			else{
				robot.getComm().sendRequest(avancer,3);
				System.out.println("Le robot effectue AvancerF");
			}
		}
		if(care){
			robot.setCaseDerriere(robot.getCaseActuelle());
			robot.setCaseActuelle(destination);
		}
		
		robot.recuperationVictime(destination);
		robot.depotVictimes(destination);
		TimeUnit.SECONDS.sleep(7);
	}
	
	public static boolean isSlipDroite(Robot robot, Node destination){
		int xDir = robot.getCaseActuelle().getX()-robot.getCaseDerriere().getX();
		int yDir = robot.getCaseActuelle().getY()-robot.getCaseDerriere().getY();
		int xDest = destination.getX()-robot.getCaseActuelle().getX();
		int yDest = destination.getY()-robot.getCaseActuelle().getY();
		int xVir = robot.getCaseActuelle().getX()-robot.getCaseActuelle().getSlipJoinX();
		int yVir = robot.getCaseActuelle().getY()-robot.getCaseActuelle().getSlipJoinY();
		
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
	
	public static void init() throws UnsupportedEncodingException{
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
    
    public Boolean isRunning(Robot robot) throws IOException{
    	byte[] answer = new byte[22];
    	answer = robot.getComm().sendRequest(slipdroit,22);
    	if (answer[2]==(byte)0x00) {
    		return true;
		}
        return false;
    }
}
