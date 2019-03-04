package assignment3;

public class Node {
	private String data;	// word
	private Node parent;	// neighbor
	
	/**
	 * @constructor default
	 */
	Node() {
		data = "";
		parent = null;
	}
	
	/**
	 * @constructor takes data as string, 
	 * sets parent to null
	 */
	Node(String s) {
		data = s;
		parent = null;
	}
	
	/** sets node data
	 * @param Strind data
	 * @return none
	 */
	public void setData(String word) {
		data = word;
	}
	
	/** returns node dta
	 * @param none
	 * @return data in node
	 */
	public String getData() {
		return data;
	}
	
	/** sets parent of node
	 * @param parent of node
	 * @return none
	 */
	public void setParent(Node p) {
		parent = p;
	}
	
	/** returns node parent
	 * @param none
	 * @return node parent
	 */
	public Node getParent() {
		return(parent);
	}
	
	
}
