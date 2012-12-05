import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
	
	
	/*
	 * The method is used to add words to the priority queue, when their frequency is the priority value
	 * First search the queue to check if the word is there
	 * If yes - increment freq by one and reinsert
	 * Else - increment freq by one and just insert
	 */
	public static void freqEnqueue (PQWordFreq pqw, PriorityQueue<PQWordFreq> pq) {
		Iterator<PQWordFreq> itr = pq.iterator();
		PQWordFreq tmp = new PQWordFreq(null);
		
		if (pq.peek() == null) {
			// The queue is empty so we just add the word after incrementing its freq to 1
			pqw.inc();
			pq.add(pqw);
		} else {
			// The queue is not empty, so we check if the word already in the queue
			// Take the first thing in the queue so we have something to start from
			tmp = (PQWordFreq)itr.next();
			while (itr.hasNext() && !(tmp.compareTo(pqw) == 0)) {
				// We'll stop iterating if we reach the end of the queue or we found a match to the word
				tmp = (PQWordFreq)itr.next();						
			}
			
			if (tmp.compareTo(pqw) == 0) {
				// We stopped looking for things because we found a match so:
				// 1. Remove from queue
				// 2. Increase frequency
				// 3. Add to queue with update frequency
				itr.remove();
				tmp.inc();
				pq.add(tmp);
			} else {
				// We stopped searching at the end of the queue and didn't find a match so:
				// Increment freq to 1 and add to queue
				pqw.inc();
				pq.add(pqw);
			}
		}
	}
	
	public static void wordEnqueue (PQWordFreq pqw, PriorityQueue<PQWordFreq> wq) {
		Iterator<PQWordFreq> itr = wq.iterator();
		PQWordFreq tmp = new PQWordFreq(null);
		
		if (wq.peek() == null) {
			// The queue is empty so we just add the word after incrementing its freq to 1
			pqw.inc();
			wq.add(pqw);
		} else {
			// The queue is not empty, so we check if the word already in the queue
			// Take the first thing in the queue so we have something to start from
			tmp = (PQWordFreq)itr.next();
			while (itr.hasNext() && !(tmp.compareTo(pqw) == 0)) {
				// We'll stop iterating if we reach the end of the queue or we found a match to the word
				tmp = (PQWordFreq)itr.next();						
			}
			
			if (tmp.compareTo(pqw) == 0) {
				// We stopped looking for things because we found a match so:
				// 1. Remove from queue
				// 2. Increase frequency
				// 3. Add to queue with update frequency
				itr.remove();
				tmp.inc();
				wq.add(tmp);
			} else {
				// We stopped searching at the end of the queue and didn't find a match so:
				// Increment freq to 1 and add to queue
				pqw.inc();
				wq.add(pqw);
			}
		}
	}
	
	/*
	 * This method is used to write the same output both to the console and to a file
	 */
	public static void writeOut (PrintWriter fo, String str) {
		System.out.print(str);
		fo.print(str);
		
	}
	public static void main(String[] args) throws IOException {
		
	    String word;
	    PQWordFreq wordToTry;
	    PQWordFreq wordFromPQ = null;

	    PriorityQueue<PQWordFreq> fpq = new PriorityQueue<PQWordFreq>(10, new FreqComparator());
		
		String skip;        // skip end of line after reading integer
		String fileName = null;	// filename to be scanned
		
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
			// Trying to get input from console
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
			fin = new FileReader(fileName);
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
	        // Adding word to Priority Queue according to frequency
	        freqEnqueue(wordToTry, fpq);
	      }
	    }
		
		// Setting up for writing output to file
		FileWriter fout = new FileWriter("report.dat", false);
		PrintWriter fileout = new PrintWriter(fout,false);		
	    
		// Helper queue to store words tuples in alphabetical order
		PriorityQueue<PQWordFreq> wpq = new PriorityQueue<PQWordFreq>(fpq.size(), new WordComparator());	    
	    
	    for (int count = 1; count <= fpq.size(); count++)
	    {
	    	wordFromPQ = (PQWordFreq)fpq.poll();
	    	
	    	if (wordFromPQ.freqIs() >= minFreq)
	    	{
	    		numValidFreqs++;
	    		// If the word matches our criteria, we want to insert it into the helper queue but 
	    		// we don't want to increase its frequency count again
	    		wordFromPQ.dec();
	    		freqEnqueue(wordFromPQ, wpq);
	    	}
	    }
	    
	    // Writing output in DJW format
	    writeOut(fileout, "----- -----------------\n");
	    writeOut(fileout, "size of Priority Queue is: " + wpq.size() + "\n");
	    writeOut(fileout, "----- -----------------\n");
	    
	    writeOut(fileout, "\n");
	    writeOut(fileout, "Freq  Word\n");
	    writeOut(fileout, "----- -----------------\n");
	    
	    int laps = wpq.size();
	    
	    for (int count = 1; count <= laps; count++)
	    {
	    	wordFromPQ = (PQWordFreq)wpq.poll();
	        writeOut(fileout, wordFromPQ.toString() + "\n");
	    }
	
	    writeOut(fileout, "\n");  
	    writeOut(fileout, numWords + " words in the input file.  \n");
	    writeOut(fileout, numValidWords + " of them are at least " + minSize + " characters.\n");
	    writeOut(fileout, numValidFreqs + " of these occur at least " + minFreq + " times.\n");
	    writeOut(fileout, "Program completed.\n");
	    
	    // Avoiding memory leaks
	    fileout.close();
	    wordsIn.close();

	}

}
