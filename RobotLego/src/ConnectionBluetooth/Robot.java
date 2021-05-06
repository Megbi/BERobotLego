package ConnectionBluetooth;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import parcours.Node;

public class Robot {
	private static final int nombreVictimesMax = 2;
	private Node caseDerriere;
	private Node caseActuelle;
	private int nombreVictimes = 0;
	private NXTComm Comm;
	
	public Robot(Node caseDepart, Node caseDerriere, String nomRobot) throws NXTCommException{
		this.caseActuelle = caseDepart;
		this.caseDerriere = caseDerriere;
		this.Comm = linkToRobot(nomRobot);
	}
	
	
	public NXTComm getComm() {
		return Comm;
	}


	public void setRobot(NXTComm Comm) {
		this.Comm = Comm;
	}


	public NXTComm linkToRobot(String nom) throws NXTCommException{
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
	    NXTInfo[] nxtInfo = nxtComm.search(nom);
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
			System.out.println("Victime récupérée");
		}
		else if(node.isVictime() && (this.nombreVictimes == nombreVictimesMax)){
			System.out.println("Impossible de prendre une victime supplémentaire");
		}
	}
	
	public void depotVictimes(Node node){
		if((nombreVictimes >= 1) && node.isHopital()){
			System.out.println("Victime(s) déposée(s)");
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
	
}
