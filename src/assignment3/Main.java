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
	static ArrayList <String> bfsLadder;
	static ArrayList <String> dfsLadder;
	static Set<String> dict;																	// remove?
	static String[] arrayDict;																	//
	static ArrayList<LinkedList<String>> graph;											  	    //
	static LinkedList<Node> dictionaryLL; 
	static ArrayList<String> wordsAdded;
	
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
		graph = makeGraph();
		if (!userInput.contains("/QUIT")) { // doesn't contain /quit command
			ps.println(userInput); 
			bfsLadder = getWordLadderBFS(userInput.get(0), userInput.get(1));					//
			printLadder(bfsLadder);
		}
		
		//end program 
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		userInput = new ArrayList<String>(); 
		ladder = new ArrayList<String>();
		bfsLadder = new ArrayList<String>();
		dfsLadder = new ArrayList<String>();
		dictionaryLL = new LinkedList<Node>();
		graph = new ArrayList<LinkedList<String>>();
		
		wordsAdded = new ArrayList<String>();
		dictionaryLL = makeLinkedListDict();
																		    //		
																							
	}
	
	public static LinkedList<Node> makeLinkedListDict() {
		dict = makeDictionary();
		Iterator<String> i = dict.iterator(); 
		
		while(i.hasNext()) {
			Node newNode = new Node(i.next()); 
			dictionaryLL.add(newNode); 
		}

		arrayDict = dict.toArray(new String[dict.size()]); 										//

		
		return dictionaryLL; 
		
	}
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		//get start word
		String start = keyboard.next().toUpperCase();
		userInput.add(start);
		
		// check for /quit command
		if (!userInput.get(0).equals("/QUIT")) { 			// isn't /quit command
			String end = keyboard.next().toUpperCase();	// get end word 
			userInput.add(end);
		}

		
		return userInput;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.


		Set <String> discovered = new HashSet<>();
		
		if (getWordLadderDFSHelper(start, end, discovered, dfsLadder)) {
			return dfsLadder;
		}
		
		dfsLadder.add(start);
		dfsLadder.add(end);
		return dfsLadder; // replace this line later with real return
	}
	
	public static boolean getWordLadderDFSHelper(String current, String end, Set<String> discovered, ArrayList<String> path){
		
		discovered.add(current);
		path.add(current);
		
		if(current.equals(end)) {
			return true;
		}
		
		else {
			for(String word : graph.get(wordsAdded.indexOf(current))) {
				if (!discovered.contains(word)) {
					if (getWordLadderDFSHelper(word, end, discovered, path)) {
						return true;
					}
				}
			}
			path.remove(current);
			return false;
		}
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	
		Queue <String> myQ = new LinkedList<>(); 
		Set <String> discovered = new HashSet<>();
										
		ArrayList<ArrayList<String>> paths;
		
		myQ.add(graph.get(0).getFirst());	
		discovered.add(graph.get(0).getFirst());

		
		while (!myQ.isEmpty()) {
			
			String curr = myQ.remove();				
			
			
			bfsLadder.add(curr);
			
			boolean useful = false;
			for (String word : graph.get(wordsAdded.indexOf(curr))) {
				if (!discovered.contains(word)) {
					
					if (word.equals(end)) {
						bfsLadder.add(end);
						return bfsLadder;
					}
					
					discovered.add(word);
					myQ.add(word);
					useful = true;
				}
			}
			
			if (!useful) bfsLadder.remove(curr);
			
			
		}
		
		return bfsLadder; 
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		
		for (String step : ladder) {
			System.out.println(step);
		}
	}

																							//
	public static ArrayList<LinkedList<String>> makeGraph(){
		
			
		// get start word and convert to char array
		String word = userInput.get(0); 
		
		int idx = 0; // pos to add new linked list
		
		//create first linked list, add start word to it
		graph.add(0, new LinkedList<String>()); 
		graph.get(0).add(word);
		wordsAdded.add(word);
		Arrays.sort(arrayDict);
		
		
		for (int LL = 0; LL < graph.size(); LL++) {
			word = graph.get(LL).getFirst();
			for(String dictWord : arrayDict) {
				if(differByOne(word, dictWord)) {
					graph.get(LL).addLast(dictWord); // add word to current linked list (LL)
					if(!wordsAdded.contains(dictWord)) { //add dictWord as new head if not yet created
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
