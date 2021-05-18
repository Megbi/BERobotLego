package robotLego;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import parcours.TypeCase;

public class InterfaceBluetooth implements KeyListener {

    PlayerRobot robot;

	public InterfaceBluetooth(PlayerRobot robot) {
		this.robot = robot;
        
    }
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void keyPressed(KeyEvent arg0) {
        int keyCode = arg0.getKeyCode();
        try{
            // ajouter ZQSD pour ajuster manuellement
            switch(keyCode) {
            case KeyEvent.VK_UP:
            	if(((robot.getCaseActuelle().getType() == TypeCase.VIRAGE)||(robot.getCaseActuelle().getType() == TypeCase.LIGNE)) && robot.canAvancerPlayer()!=null){
            		robot.avancer(robot.canAvancerPlayer());
    				affichage();
                }
            	break;
            case KeyEvent.VK_DOWN:
            	if(robot.canDemitourPlayer()!=null){
            		robot.demiTour(robot.canDemitourPlayer());
    				affichage();
            	}
            	break;
            case KeyEvent.VK_RIGHT:
            	if((robot.getCaseActuelle().getType() == TypeCase.SLIP) && robot.canSlipDroitPlayer()!=null){
            		robot.slipDroit(robot.canSlipDroitPlayer());
    				affichage();
                }
            	break;
            case KeyEvent.VK_LEFT:
            	if((robot.getCaseActuelle().getType() == TypeCase.SLIP) && robot.canSlipGauchePlayer()!=null){
            		robot.slipGauche(robot.canSlipGauchePlayer());
    				affichage();
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
	private void affichage() {
		System.out.println(robot.getName()+" : "+robot.getCaseDerriere().getName()+" -> "+robot.getCaseActuelle().getName()+" ("+robot.getCaseDerriere().getType()+")");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
