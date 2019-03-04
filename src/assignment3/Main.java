/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <16225>
 * <Analaura Rodriguez>
 * <ar55665>
 * <16225>
 * Slip days used: <0>
 * Git URL: 
 * Spring 2019
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static ArrayList<String> userInput; 		// contains start & end word
	static ArrayList <String> bfsLadder;	
	static ArrayList <String> dfsLadder;
	static Set<String> dict;					// instructor made dict														
	static String[] arrayDict;					// easily accessible dict							
	static ArrayList<LinkedList<String>> graph;	// graph based on start word										  	    
	static ArrayList<String> wordsAdded;		// words in graph (easily accessible)
	
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		
		initialize();							
		userInput = parse(kb);
		
		// IF not quit command	
		if (!userInput.isEmpty()) { 				
			printLadder(getWordLadderBFS(userInput.get(0), userInput.get(1)));
			//printLadder(getWordLadderDFS(userInput.get(0), userInput.get(1)));
		}
		
		//end program 
	}
	
	/* Initializes static variables*/
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		userInput = new ArrayList<String>(); 
		bfsLadder = new ArrayList<String>();
		dfsLadder = new ArrayList<String>();
		graph = new ArrayList<LinkedList<String>>();
		wordsAdded = new ArrayList<String>();	
		dict = makeDictionary();							
																							
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		String start = keyboard.next().toUpperCase();	// get start word
		 
		if (start == "/QUIT") return userInput;			// return empty ArrayList
		
		String end = keyboard.next().toUpperCase();		// get end word 
		
		userInput.add(start);
		userInput.add(end);

		
		return userInput;								// contains start & end word
	}
	
	
	/** Looks for word ladder b/w start and end word using depth 
	 * first search, recursively.
	 * @param String start and end word
	 * @return if ladder exists, return ordered ladder from start to end (inclusive)
	 * If ladder doesn't exist, return list with just start and end.
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		
		// IF both words in dictionary, begin search
		if (dict.contains(start) && dict.contains(end)) {
			graph = makeGraph(start);					// make graph based on start word
			sortGraph(graph, end);
			
			Set <String> discovered = new HashSet<>();	
			
			if (getWordLadderDFSHelper(start, end, discovered, dfsLadder)) { // populate ladder
				return dfsLadder;
			}
		}
		
		// return w only start and end word
		dfsLadder.clear();
		dfsLadder.add(start);
		dfsLadder.add(end);
		
		return dfsLadder; 
		
	}
	
	/** Performs recursive dfs until a path is found or not found
	 * @param current word, end word, discovered set, ladder
	 * @return true if ladder connection exists, false otherwise
	 */
	public static boolean getWordLadderDFSHelper(String current, String end, Set<String> discovered, ArrayList<String> path){
		
		discovered.add(current); 
		path.add(current);
		
		if(current.equals(end)) {
			return true;
		}
		
		else {
			
			// backtracking
			for(String word : graph.get(wordsAdded.indexOf(current))) {
				
				if (!discovered.contains(word)) {
					if (getWordLadderDFSHelper(word, end, discovered, path)) {
						return true;
					}
				}
			}
			path.remove(current); // dead end, no path
			return false;
		}
	}
	
	/** Looks for word ladder b/w start and end word using breadth
	 * first search, doubly linked list.
	 * @param Start and end word
	 * @return IF word ladder exist, return ordered ladder from start to end (inclusive)
	 * If ladder doesn't exist, return list with just start and end.
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		
    	//check word existance
    	if (dict.contains(start) && dict.contains(end)) {
    		graph = makeGraph(start);
    		sortGraph(graph, end);
    		
	    	Queue <Node> myQ = new LinkedList<>(); 
			Set <String> discovered = new HashSet<>();
			
			// create start node and current node holder
			Node startNode = new Node(start);
			Node curr = new Node();
			
			
			myQ.add(startNode);								//add start node
			discovered.add(graph.get(0).getFirst());		//add to discovered
			boolean found = false;							//"break" out of loop when found
			
			while (!myQ.isEmpty() && !found) {	
				
				curr = myQ.remove();						
				
				if (curr.getData().equals(end)) {			
					found = true;
				}
				
				if (!found) {
					for (String word : graph.get(wordsAdded.indexOf(curr.getData()))) { // search neighbors
						if (!discovered.contains(word)) {		
							
							Node newNeighbor = new Node(word);		// create nodes for neighbors 
							newNeighbor.setParent(curr);			// set current node as parent
							discovered.add(word);
							myQ.add(newNeighbor);
						}
					}
				}
	
			}
			
			// IF found, backtrack to get path, "reverse" path
			if (found) {
				bfsLadder.clear();
				Node parent = curr.getParent();
				bfsLadder.add(curr.getData());
				int idx = 1; // # elements in list
				
				while (parent != null) {
					bfsLadder.add(bfsLadder.size() - idx, parent.getData());
					parent = parent.getParent();
					idx++;
				}
				
				return bfsLadder;
			}
    	}
    	
		//else (not found)
		bfsLadder.clear();
		bfsLadder.add(start);	// add start
		bfsLadder.add(end);		// add end
		return bfsLadder;
	}
    
	
    /** Prints ladder if exists,
     * OR if no ladder exists, print appropriate message
     * @param ladder
     */
	public static void printLadder(ArrayList<String> ladder) {
		
		// ladder exists
		if (ladder.size() > 2) { 
			System.out.println("a " + ladder.size() + "-rung word ladder exists between " + ladder.get(0).toLowerCase() + " and " + ladder.get(ladder.size() - 1).toLowerCase() + ".");
			for (String step : ladder) {
				System.out.println(step.toLowerCase());
			}
		}
		
		// ladder doesn't exist
		else {
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase() + ".");
		}
	}

	/** Populates a graph based on start word.Connections are 
	 * between words that differ only by one letter. 
	 * @param start word
	 * @return ArrayList of linked list graph
	 */																					
	public static ArrayList<LinkedList<String>> makeGraph(String start){
			
		// get start word and convert to char array
		String word = start;
		
		int idx = 0; // pos to add new linked list
		
		//create first linked list, add start word to it
		graph.add(0, new LinkedList<String>()); 
		graph.get(0).add(word);
		wordsAdded.add(word);
		
		// other stuff needed
		arrayDict = dict.toArray(new String[dict.size()]); 	// transform to array (easily accessible)
		Arrays.sort(arrayDict);									
		
		// populate
		for (int LL = 0; LL < graph.size(); LL++) {
			
			word = graph.get(LL).getFirst();				// get head
			for(String dictWord : arrayDict) {				// find neighbors
				if(differByOne(word, dictWord)) {
					graph.get(LL).addLast(dictWord); 		// add word to current linked list (LL)
					if(!wordsAdded.contains(dictWord)) { 	// add dictWord as new head if not in graph
						idx++;
						graph.add(idx, new LinkedList<String>());
						graph.get(idx).add(dictWord);
						wordsAdded.add(dictWord);
					}
				}
			}	
		}
		
		
		return graph;
	}
	
	
	/** checks if two words differ by one letter
	 * @param two words to compare
	 * @return true if words differ by one letter, false otherwise
	 */	
	public static boolean differByOne(String word, String dictWord) {
		char[] wordArray = word.toCharArray();
		char[] dictWordArray = dictWord.toCharArray();
		int sameLetters = 0;
		
		for(int i = 0; i < word.length(); i++) {
			if(wordArray[i] == dictWordArray[i]) {
				sameLetters++;
			}
			
		}
		if(sameLetters == word.length() - 1)
			return true;
		else
			return false;
	
	}
	
	/** Sorts graph by placing words that are closer 
	 * to end word at the front of LL
	 * @param graph
	 */	
	public static void sortGraph(ArrayList<LinkedList<String>> graph, String endWord) {
		
		
		for (int LL = 0; LL < graph.size(); LL++) {
			if (graph.get(LL).size() > 2) {
				String[] wordWeight = getWeight(graph.get(LL), endWord); // orders G to L similarities w end word
				
				String head = graph.get(LL).getFirst(); 		// preserve head
				graph.get(LL).clear(); 
				graph.get(LL).add(head); 						// add same head
				
				for (int i = 1; i < wordWeight.length; i ++) {
					graph.get(LL).addLast(wordWeight[i]);
				}
			}
			
		}
		
	}
	
	/** Orders linked list by placing words with more
	 * similarities to end word at the front
	 * @param linked list to order
	 */	
	public static String[] getWeight(LinkedList <String> LL, String endWord){
		
		// to order based on G to L similarities to end word
		String[] wordWeight = LL.toArray(new String[LL.size()]); 
		int[] weight = new int[LL.size()];
		
		
		for (int i = 1; i < weight.length; i ++) {
			weight[i] = countSimilarities(wordWeight[i], endWord);  // counts similarities
		}
		
		// re-ordering LL from G to L similarities to end Word
		for (int i = 2; i < weight.length; i++) {
			for (int j = 1; j < weight.length; j++) {
				if (weight[i] > weight[j]) {
					
					String temp = wordWeight[i];
					int tempI = weight[i];
					
					wordWeight[i] = wordWeight[j];
					wordWeight[j] = temp;
					
					weight[i] = weight[j];
					weight[j] = tempI;
					
					
				}
			}
		}
		
		
		return wordWeight;
	}
	
	
	/** counts similar letters & position  of 
	 * two words
	 * @param two words to compare
	 * @return # of in common letters
	 */	
	public static Integer countSimilarities(String word, String endWord) {
		
		char[] wordArray = word.toCharArray();
		char[] endWordArray = endWord.toCharArray();
		Integer count = 0;
		
		for(int i = 0; i < word.length(); i++) {
			
			if(wordArray[i] == endWordArray[i]) {
				count++;
			}
			
		}
		
		return count;
	}
	
	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
