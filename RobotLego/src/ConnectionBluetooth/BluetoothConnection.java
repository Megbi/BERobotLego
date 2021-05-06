package ConnectionBluetooth;

import java.awt.Dimension;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class BluetoothConnection {
	  public static void main (String[] args) throws NXTCommException, InterruptedException, UnsupportedEncodingException, IOException {
	    NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
	    NXTInfo[] nxtInfo = nxtComm.search("OwOmega");
	    if (nxtInfo.length == 0) {
	        System.out.println("No nxt found");
	        System.exit(1);
	    }
	    nxtComm.open(nxtInfo[0]);
	    
	    System.out.println("Connecté à "+ nxtInfo[0].name + " à l'adresse : "  + nxtInfo[0].deviceAddress);
	    JFrame f = new JFrame();
	    f.addKeyListener(new Deplacements(nxtComm));
	    f.setVisible(true);
	    f.setSize(new Dimension(500, 500)); 
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    
	    f.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent wE) {
	            System.exit(1);
	        }
	    });
	  }
	  
	  
	}