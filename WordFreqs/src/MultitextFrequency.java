import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;


public class MultitextFrequency {
	
	public class tfidfTuple {
		StringBuffer text;
		public PriorityQueue<PQWordFreq> fpq; // ordered by frequency
		public PriorityQueue<PQWordFreq> wpqf; // ordered by alphabet for frequency
		//public PriorityQueue<PQFloatFreq> wpqt; // ordered by alphabet for tfidf
		public PriorityQueue<PQFloatFreq> tpq; // ordered by tfidf
		
		public tfidfTuple(StringBuffer txt) {
			fpq = new PriorityQueue<PQWordFreq>(10, new FreqComparator());
			wpqf = new PriorityQueue<PQWordFreq>(10, new WordComparator());
			//wpqt = new PriorityQueue<PQFloatFreq>(10, new WordComparator());
			tpq = new PriorityQueue<PQFloatFreq>(10, new FloatComparator());
			
			text = new StringBuffer(txt);
		}
		
		public void freqEnqueue (PQWordFreq pqw, PriorityQueue<PQWordFreq> pq) {
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
		
		public void scanItem (Scanner words) {
			//Scanner words = new Scanner (fr);
			PQWordFreq wordToTry;
			String word;
			
			while (words.hasNext())      // while more words to process
		    {
				word = words.next();
				if (word.length() >= corpus.minSize) {
					word = word.toLowerCase();
					wordToTry = new PQWordFreq(word);
					// Adding word to Priority Queue according to frequency
					freqEnqueue(wordToTry, fpq);
				}
		    }		
		}
		
		public void toWPQ () {
			Iterator<PQWordFreq> itr = fpq.iterator();
			while (itr.hasNext()) {
				wpqf.add(itr.next());
			}
		}
		
		/*public void toTPQ () {
			Iterator<PQFloatFreq> itr = tpq.iterator();
			while (itr.hasNext()) {
				wpqt.add((PQFloatFreq)itr);
				itr.next();
			}
		}*/
	}
	
	public String separator;
	public String rptName = "MultitextReport.txt";
	public String inputFile;
	
	//public int minLen = 6;
	public int sCount = 0; // number of separator occurences, indicating number of items
	
	public PQFrequency corpus;
	
	public ArrayList <tfidfTuple> items;
	public ArrayList <Iterator> itrs;
	
	public MultitextFrequency (String sprtr, String fileName) {
		separator = sprtr.toLowerCase();//+ " ";
		inputFile = fileName;
		corpus  = new PQFrequency();
		corpus.minFreq = 6;
		corpus.minSize = 7;
		items = new ArrayList<tfidfTuple>();
		itrs = new ArrayList<Iterator>();
	}
	
	public double tfidf(int wFreq, int iCount) {
		/*
		 * tfidf(w) = freqInItem(w) * ln(items in corpus / items containing w)
		 */
		
		return Math.log(sCount / iCount) * wFreq;
	}
	
	public void docStats (PrintWriter pw) throws IOException {
		/*
		 * item number: commonword, lesscommonword, lesslesscommonword; hightfidf, nexttfidf, nextnexttfidf
		 */
		
		/*PQWordFreq freqWords[] = new PQWordFreq[3];
		PQFloatFreq tfWords[] = new PQFloatFreq[3];*/
		DecimalFormat fmt = new DecimalFormat("00000");
		
		Iterator<PQWordFreq> wItr;
		Iterator<PQFloatFreq> tItr;
		
		int i;
		int j;
		//int itemNum;
		
		String str = "";
		
		/*FileWriter fout = new FileWriter(rptName, false);
		PrintWriter fileout = new PrintWriter(fout,false);		*/
		
		for (i = 0; i < sCount; i++) {
			str = "Item #" + fmt.format(i) + ": "; //13
			wItr = items.get(i).wpqf.iterator();
			tItr = items.get(i).tpq.iterator();
			
			for (j = 0; j < 3; j++){
				str.concat(wItr.next().wordIs());
				if (j < 2) {
					str.concat(", ");
				}
			}
			str.concat("; ");
			
			for (j = 0; j < 3; j++){
				str.concat(tItr.next().wordIs());
				if (j < 2) {
					str.concat(", ");
				}
			}
			str.concat("\n");
		}
		writeOut(pw, str);
		
		//fileout.close();
		
	}
	
	public void scanCorpus (Scanner words) {
		//Scanner words = new Scanner (fr);
		PQWordFreq wordToTry;
		String word;
		StringBuffer sb = new StringBuffer();
		sb.append("");
		
		while (words.hasNext())      // while more words to process
	    {
			word = words.next();
			corpus.numWords++;			

 			if (word.length() >= corpus.minSize) {
				corpus.numValidWords++;
				word = word.toLowerCase();
				wordToTry = new PQWordFreq(word);
				// Adding word to Priority Queue according to frequency
				corpus.freqEnqueue(wordToTry, corpus.fpq);
			}
 			if (word.compareTo(separator.toString()) == 0) {
				sCount++;
				items.add(new tfidfTuple(sb));
				sb.delete(0, sb.length());
			} else {
				sb.append((String)word + " ");
			}
	    }		
	}
	
	public void scanItems(){
		// Setting up information for each of the items
		for (int i = 0; i < sCount; i++) {
			// Creating the item's priority queue by frequency
			items.get(i).scanItem(new Scanner(items.get(i).text.toString()));
			// Creating the item's priority queue by alphabet
			items.get(i).toWPQ();
		}
	}
	
	public void calcTFIDF () {
		
		Iterator<PQWordFreq> cItr = corpus.wpq.iterator();
		Iterator<PQWordFreq> iItr = null;
		
		int itemCount = 0;
		int i;
		
		boolean first = true;
		
		int inItem[] = new int [sCount];
		
		String cWord;
		//String iWord;
		PQWordFreq tmp = null;
		
		for (i = 0; i < sCount; i++) {
			itrs.add(items.get(i).wpqf.iterator());			
		}
		
		while (cItr.hasNext()) {
			cWord = cItr.next().wordIs();
			itemCount = 0;
			
			for (i = 0; i < sCount; i++) {
				// Count how many items contains the word
				iItr = (Iterator<PQWordFreq>)itrs.get(i);
				if (iItr != null) {
					if (first) {
						tmp = iItr.next();
					}
					if (tmp.wordIs().equals(cWord)) {
						itemCount++;
						inItem[i] = tmp.freqIs();
						iItr.next();
					} else {
						inItem[i] = 0;
					}
				}
			}
			
			for (i = 0; i < sCount; i++) {
				// Calculate tfidf for word and insert into queue
				if (inItem[i] > 0) {
					// Create new PQFloat tuple with tfidf score and add to tfidf queue
					//PQFloatFreq tfWord = ;
					//items.get(i).tpq.add(tfWord);
					items.get(i).tpq.add(new PQFloatFreq(cWord, tfidf(inItem[i], itemCount)));
					//items.get(i).wpqt.add(tfWord);
				}
			}
			first = false;
		}		
	}
	
	public static void writeOut (PrintWriter fo, String str) {
		System.out.print(str);
		fo.print(str);
		
	}
	
	public static void main (String[] args) throws IOException {
		MultitextFrequency mf = new MultitextFrequency("federalist", "Federalists.dat");
		PQWordFreq pqWord;
		
		
		
		FileReader fin = null;
		try {
			fin = new FileReader(mf.inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Could not read from file! Aborting...");
	        System.exit(1);
		}
	    Scanner wordsIn = new Scanner(fin);
	    wordsIn.useDelimiter("[^a-zA-Z0-9]");  // delimiters are nonletters-digits
	    
	    // Setting up for writing output to file
 		FileWriter fout = new FileWriter(mf.rptName, false);
 		PrintWriter fileout = new PrintWriter(fout,false);		
 		
 		mf.scanCorpus(wordsIn);
 		mf.corpus.validFrequencies();
 		mf.scanItems(); 		
 		mf.calcTFIDF();
 		mf.docStats(fileout);
 	    
 		/*
 	    // Writing output in DJW format
 	    writeOut(fileout, "----- -----------------\n");
 	    writeOut(fileout, "size of Priority Queue is: " + mf.corpus.wpq.size() + "\n");
 	    writeOut(fileout, "----- -----------------\n");
 	    
 	    writeOut(fileout, "\n");
 	    writeOut(fileout, "Freq  Word\n");
 	    writeOut(fileout, "----- -----------------\n");
 	    
 	    // Reporting results in alphabetical order
 	    while (mf.corpus.wpq.size() != 0)
 	    {
 	    	pqWord = (PQWordFreq)mf.corpus.wpq.poll();
 	    	mf.writeOut(fileout, pqWord.toString() + "\n");
 	    }
 	
 	    writeOut(fileout, "\n");  
 	    writeOut(fileout, mf.corpus.numWords + " words in the input file.  \n");
 	    writeOut(fileout, mf.corpus.numValidWords + " of them are at least " + mf.corpus.minSize + " characters.\n");
 	    writeOut(fileout, mf.corpus.numValidFreqs + " of these occur at least " + mf.corpus.minFreq + " times.\n");
 	    
 	    writeOut(fileout, "Program completed.\n");*/
 	    
 	    // Avoiding memory leaks
 	    fileout.close();
 	    wordsIn.close();
	}

}
