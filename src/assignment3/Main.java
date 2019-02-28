/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2019
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static ArrayList<String> userInput; 
	static ArrayList<String> ladder;
	static ArrayList <String> bfsLadder;														//
	static Set<String> dict;																	// remove?
	static String[] arrayDict;																	//
	static ArrayList<LinkedList<Node>> graph;											  	    //
	static LinkedList<Node> dictionaryLL; 
	
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
		// TODO methods to read in words, output ladder
		userInput = parse(kb);
		
		if (!userInput.contains("/quit")) { // doesn't contain /quit command
			ps.println(userInput); 
			ladder.add("Ana");
			ladder.add("Megan");
			printLadder(ladder);
			bfsLadder = getWordLadderBFS(userInput.get(0), userInput.get(1));					//
		}
		
		//end program 
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		userInput = new ArrayList<String>(); 
		ladder = new ArrayList<String>();
		bfsLadder = new ArrayList<String>();													//
		dictionaryLL = new LinkedList<Node>();
		graph = new ArrayList<LinkedList<Node>>();
		
		
		dictionaryLL = makeLinkedListDict();
		graph = makeGraph();																    //		
																							
	}
	
	public static LinkedList<Node> makeLinkedListDict() {
		dict = makeDictionary();
		Iterator<String> i = dict.iterator(); 
		
		while(i.hasNext()) {
			Node newNode = new Node(i.next()); 
			dictionaryLL.add(newNode); 
		}
//		arrayDict = dict.toArray(new String[dict.size()]); 										//

		for (Node step : dictionaryLL) {
			System.out.println(step.getData());
		}
		
		return dictionaryLL; 
		
	}
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		//get start word
		String start = keyboard.next().toLowerCase();
		userInput.add(start);
		
		// check for /quit command
		if (!userInput.get(0).equals("/quit")) { 			// isn't /quit command
			String end = keyboard.next().toLowerCase();		// get end word
			userInput.add(end);
		}
		/*
		String delims = "[\\s\\t]+";
		String[] tokens = inputLine.split(delims);
		userInput.add(tokens[0]);
		if(tokens.length == 2) {
			userInput.add(tokens[1]);
		}
		while(userInput.size() != 2) {
			inputLine = keyboard.next();
			tokens = inputLine.split(delims); 
			userInput.add(tokens[0]);
		}
		*/
		
		return userInput;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	
//  
//		Queue <Node> myQ = new LinkedList<>(); 
//		
//		int idx = find(start);						
//		if (idx != -1) {						
//			
//			myQ.add(graph.get(idx).element());				
//			graph.get(idx).getFirst().setVisited(true);		
//		
//			//check if visited!
//			
//			while (!myQ.isEmpty()) {
//				
//				Node curr = myQ.remove();				
//				
//				if (arrayDict[curr.getData()].equals(end)) {
//					bfsLadder.add(end);
//					return bfsLadder;
//				}
//				
//				bfsLadder.add(arrayDict[curr.getData()]);
//				
//				curr = curr.getNext();
//				while (curr != null) {
//					curr.setVisited(true);
//					myQ.add(graph.get(curr.getData()).element());
//					curr = curr.getNext();
//				}
//				
//			}
//		}
		return bfsLadder; 
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		
		for (String step : ladder) {
			System.out.println(step);
		}
	}

	/**
	 * Finds index of start word
	 * @param String start word
	 * @return -1 if not found, index of start 
	 * word if found
	 */																						//
	public static int find(String start) {
		
		for (int i = 0; i < arrayDict.length; i++) {
			if (arrayDict[i].equals(start)) {
				
			}
		}
		return -1; // not found
	}
																							//
	public static ArrayList<LinkedList<Node>> makeGraph(){
		String word = userInput.get(0);
		char[] startArray = userInput.get(0).toCharArray(); 
		int idx = 0; 
		//add first linked list
		graph.add(0, new LinkedList<Node>()); 
		
		//add start to as first node 
		graph.get(0).add(new Node(word));
		
		for(int i = 0; i < userInput.get(0).length(); i++) {
			for(char j = 'a'; j < 'z'; j++) {
				startArray[i] = j;
				word = startArray.toString();
				
				//add word to graph if it exists in dictionary
				for(int k = 0; k < dictionaryLL.size(); k++) {
					if(dictionaryLL.get(k).equals(word)) {
						graph.get(k).add(new Node(word));
					}
				}
								
			}
			idx = idx + 1; 
			//fix how to get next word in graph to make new node
			word = graph.get(i).get(graph.get(idx));
			graph.add(i, new LinkedList<Node>());
			
		}
		
		return graph;
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
