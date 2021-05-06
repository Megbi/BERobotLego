package parcours;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Node {

	private String name;
	private int x;
	private int y;
	private int slipJoinX = -1;
	private int slipJoinY = -1;
    private TypeCase type;
    private boolean victime;
    private boolean hopital;
    
    private List<Node> shortestPath = new LinkedList<Node>();
    
    private Integer distance = Integer.MAX_VALUE;
    
    Map<Node, Integer> adjacentNodes = new HashMap<Node, Integer>();

    public void addDestination(Node destination, int distance) {
    	if(!adjacentNodes.containsKey(destination)){
    		adjacentNodes.put(destination, distance);
    		destination.addDestination(this, distance);
    	}
    }
 
    public Node(String name, int x, int y, TypeCase type, boolean v, boolean h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
        this.victime = v;
        this.hopital = h;
    }
    
    public void setJointures(int x, int y){
    	this.slipJoinX = x;
    	this.slipJoinY = y;
    }
    
    public void djikstraResetNode(){
    	this.shortestPath =  new LinkedList<Node>();
    	this.distance = Integer.MAX_VALUE;
    }
    
    public int getSlipJoinX(){
    	return slipJoinX;
    }
    
    public int getSlipJoinY(){
    	return slipJoinY;
    }
    
	@Override
	public String toString() {
		return "Node [name=" + name + ", type=" + type + ", victime=" + victime
				+ ", hopital=" + hopital
				+ "]";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isVictime() {
		return victime;
	}

	public void setVictime(boolean victime) {
		this.victime = victime;
	}

	public boolean isHopital() {
		return hopital;
	}

	public void setHopital(boolean hopital) {
		this.hopital = hopital;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Map<Node, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public TypeCase getType() {
		return type;
	}

	public void setType(TypeCase type) {
		this.type = type;
	}
    
    // getters and setters

}
