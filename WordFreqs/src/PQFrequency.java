import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Iterator;
import java.util.Scanner;
import java.util.PriorityQueue;

//----------------------------------------------------------------------------
// PQFrequency.java            by Jennie Steshenko              CS187 Fall 2012 Project #5
// Relies on FrequencyList by DJW
//
// Displays a word frequency list of the words listed in the input file.
// Prompts user for minSize and minFreq AND filename.
// Does not process words less than minSize in length.
// Does not output words unless their frequency is at least minFreq.
//----------------------------------------------------------------------------


public class PQFrequency {
	
	

	public static void freqEnqueue (PQWordFreq pqw, PriorityQueue<PQWordFreq> pq) {
		Iterator<PQWordFreq> itr = pq.iterator();
		PQWordFreq tmp = new PQWordFreq(null);
		
		if (pq.peek() == null) {
			pqw.inc();
			pq.add(pqw);
		} else {
			tmp = (PQWordFreq)itr.next();
			while (itr.hasNext() && !(tmp.compareTo(pqw) == 0)) {
				tmp = (PQWordFreq)itr.next();						
			}
			
			if (tmp.compareTo(pqw) == 0) {
				itr.remove();
				tmp.inc();
				pq.add(tmp);
			} else {
				pqw.inc();
				pq.add(pqw);
			}
		}
	}
	
	public static void wordEnqueue (PriorityQueue<PQWordFreq> f, PriorityQueue<PQWordFreq> w) {
		
	}
	
	public static void main(String[] args) {
		
	    String word;
	    PQWordFreq wordToTry;
	    PQWordFreq wordInPQ;
	    PQWordFreq wordFromPQ;

	    PriorityQueue<PQWordFreq> fpq = new PriorityQueue<PQWordFreq>(10, new FreqComparator());
		
		String skip;        // skip end of line after reading integer
		String fileName;	// filename to be scanned
		
		int numWords = 0;
	    int numValidWords = 0;
	    int numValidFreqs = 0;
	    int minSize = 0;
	    int minFreq = 0;
		
		// Checking that args have the correct amount
		if (args.length == 0) {
			
		    Scanner conIn = new Scanner(System.in);
	
		    //Get word and frequency limits from user
		    System.out.print("Minimum word size: ");
		    minSize = conIn.nextInt();
		    skip = conIn.nextLine();      
		    System.out.print("Minimum word frequency: ");
		    minFreq = conIn.nextInt();
		    skip = conIn.nextLine();
		    System.out.print("Filename to scan: ");
		    fileName = conIn.next();
		    
		    conIn.close();
		} else if (args.length == 3) {
			try {
				minSize = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Argument" + " must be an integer");
		        System.exit(1);
			}
			
			try {
				minFreq = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Argument" + " must be an integer");
		        System.exit(1);
			}
			
				fileName = args[2];
		} else {
			System.out.println("Incorrect number of arguments! Aborting...");
	        System.exit(1);
		}
		
		// Set up file reading
	    FileReader fin = null;
		try {
			fin = new FileReader("Federalists.dat");
		} catch (FileNotFoundException e) {
			System.out.println("Could not read from file! Aborting...");
	        System.exit(1);
		}
	    Scanner wordsIn = new Scanner(fin);
	    wordsIn.useDelimiter("[^a-zA-Z0-9]");  // delimiters are nonletters-digits
		
		while (wordsIn.hasNext())      // while more words to process
	    {
	      word = wordsIn.next();          
	      numWords++;
	      if (word.length() >= minSize)
	      {
	        numValidWords++;
	        word = word.toLowerCase();
	        wordToTry = new PQWordFreq(word);
	        freqEnqueue(wordToTry, fpq);
	      }
	      if (numWords % 20 == 0) {
	    	  System.out.println(numWords);
	      }
	    }
		
		System.out.println("----- -----------------");
		System.out.println("size of fpq is: " + fpq.size());
		System.out.println("----- -----------------");
		
	  
	    System.out.println("The words of length " + minSize + " and above,");
	    System.out.println("with frequency counts of " + minFreq + " and above:");
	    System.out.println();
	    System.out.println("Freq  Word");
	    System.out.println("----- -----------------");
	    
	    //PriorityQueue<PQWordFreq> wpq = new PriorityQueue<PQWordFreq>(fpq.size(), new WordComparator());
	    for (int count = 1; count <= fpq.size(); count++)
	    {
	    	wordFromPQ = fpq.poll();
	      if (wordFromPQ.freqIs() >= minFreq)
	      {
	        numValidFreqs++;
	        System.out.println(wordFromPQ.toString());
	      }
	    }
	
	    System.out.println();  
	    System.out.println(numWords + " words in the input file.  ");
	    System.out.println(numValidWords + " of them are at least " + minSize + " characters.");
	    System.out.println(numValidFreqs + " of these occur at least " + minFreq + " times.");
	    System.out.println("Program completed.");
	    
	    wordsIn.close();

	}

}
