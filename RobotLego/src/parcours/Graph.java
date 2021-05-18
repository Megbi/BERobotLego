package parcours;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    private Set<Node> nodes = new HashSet<Node>();
    
    public void addNode(Node case0) {
    	nodes.add(case0);
    }

	public Set<Node> getCases() {
		return nodes;
	}
	
	public Node getCase(String name){
		Node res = null;
		for(Node n : this.getCases()){
			if(n.getName() == name){
				return n;
			}
		}
		return res;
	}

	public void djikstraReset() {
		for (Node n : this.nodes) {
			n.djikstraResetNode();
		}
	}

	public void setCases(Set<Node> cases) {
		this.nodes = cases;
	}

	public Set<Node> listeVictimes(){
		Set<Node> listeVictimes = new HashSet<Node>();
		for(Node n : nodes){
			if(n.isVictime()){
				listeVictimes.add(n);
			}
		}
		return listeVictimes;
	}
	
	public Set<Node> listeHopitaux(){
		Set<Node> listeHopitaux = new HashSet<Node>();
		for(Node n : nodes){
			if(n.isHopital()){
				listeHopitaux.add(n);
			}
		}
		return listeHopitaux;
	}
	// getters and setters 
}