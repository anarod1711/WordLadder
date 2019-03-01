package assignment3;

public class Node {
	private String data;
	private boolean visited;
	private Node next;
	
	Node(String s) {
		data = s.toLowerCase();
		visited = false;
		next = null;
	}
	
	public String getData() {
		return data;
	}
	
	public void setVisited(boolean discovered) {
		visited = discovered;
	}
	
	public boolean hasVisited() {
		return visited;
	}
	
	public void setNext(Node nxt) {
		next = nxt;
	}
	
	public Node getNext() {
		return next;
	}
	
}
