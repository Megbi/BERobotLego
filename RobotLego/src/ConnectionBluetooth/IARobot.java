package ConnectionBluetooth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import lejos.nxt.remote.AsciizCodec;
import parcours.Node;
import parcours.TypeCase;

public class IARobot {
	static byte[] avancer;
    static byte[] demitour;
    static byte[] slipgauche;
    static byte[] slipdroit;
    static byte[] wait;
	
	public static void deplacement(Robot robotIA, Robot robotPlayer, Node destination) throws IOException, InterruptedException{
		if(robotPlayer.getCaseActuelle().equals(destination) || robotPlayer.getCaseDerriere().equals(destination)){
			System.out.println(robotIA.getName()+" : la case "+destination.getName()+" est occupée");
		}
		while(robotPlayer.getCaseActuelle().equals(destination) || robotPlayer.getCaseDerriere().equals(destination) ){
			TimeUnit.SECONDS.sleep(1);
		}
		
		if(robotIA.getCaseDerriere().equals(destination)){
			robotIA.getComm().sendRequest(demitour,3);
		}
		else{
			if(robotIA.getCaseActuelle().getType() == TypeCase.SLIP){
				if(isSlipDroite(robotIA,destination)){
					robotIA.getComm().sendRequest(slipdroit,3);
				}
				else{
					robotIA.getComm().sendRequest(slipgauche,3);
				}
			}
			else{
				robotIA.getComm().sendRequest(avancer,3);
			}
		}
		robotIA.setCaseDerriere(robotIA.getCaseActuelle());
		robotIA.setCaseActuelle(destination);
		while(isRunning(robotIA)){
			TimeUnit.MICROSECONDS.sleep(100);
		}
		robotIA.recuperationVictime(destination);
		robotIA.depotVictimes(destination);
		TimeUnit.SECONDS.sleep(1);
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
    
    public static Boolean isRunning(Robot robot) throws IOException{
    	byte[] answer = new byte[22];
    	answer = robot.getComm().sendRequest(wait,22);
    	if (answer[2]==(byte)0x00) {
    		return true;
		}
        return false;
    }
}
