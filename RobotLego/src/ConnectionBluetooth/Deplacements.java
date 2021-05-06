package ConnectionBluetooth;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;

import lejos.nxt.remote.AsciizCodec;
import lejos.nxt.remote.NXTCommand;
import lejos.pc.comm.NXTComm;

public class Deplacements implements KeyListener{

    NXTComm robot;
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

	public Deplacements(NXTComm robot) {
        this.robot = robot;
        try{
            this.requeste = new byte[22];
            requeste[0]=(byte)0x00;
            requeste[1]=(byte)0x00;
            System.arraycopy(AsciizCodec.encode("Main.rxe"), 0, this.requeste, 2, AsciizCodec.encode("Main.rxe").length);
            
            init();
            this.request = new byte[] { (byte)0x00,(byte)0x09,(byte)0x01,(byte)0x02,AsciizCodec.encode("1")[0],(byte)0};
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
//    "Main.rxe"
//    77
//    97
//    105
//    110
//    46
//    114
//    120
//    101
//    0
    NXTCommand commander = new NXTCommand(robot);

	@Override
    public void keyPressed(KeyEvent arg0) {
        int keyCode = arg0.getKeyCode();
        try{
            // ajouter ZQSD pour ajuster manuellement
            //System.out.println(commander.getFirmwareVersion());
            switch(keyCode) {
            case KeyEvent.VK_UP:
            	robot.sendRequest(avancer, 3);
                break;
            case KeyEvent.VK_DOWN:
            	robot.sendRequest(demitour, 3);
            	break;
            case KeyEvent.VK_RIGHT:
            	robot.sendRequest(slipdroit, 3);
            	break;
            case KeyEvent.VK_LEFT:
            	robot.sendRequest(slipgauche, 3);
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
}
