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
		public PriorityQueue<PQFloatFreq> tpq; // ordered by tfidf
		
		public tfidfTuple(StringBuffer txt) {
			fpq = new PriorityQueue<PQWordFreq>(10, new FreqComparator());
			wpqf = new PriorityQueue<PQWordFreq>(10, new WordComparator());
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
	}
	
	public String separator;
	public String rptName = "MultitextReport.txt";
	public String inputFile;
	
	public int sCount = 0; // number of separator occurences, indicating number of items
	
	public PQFrequency corpus;
	PriorityQueue<PQWordFreq> fullFreqs;
	PriorityQueue<PQWordFreq> fullWords;
	
	
	public ArrayList <tfidfTuple> items;
	
	public MultitextFrequency (String sprtr, String fileName) {
		separator = sprtr.toLowerCase();//+ " ";
		inputFile = fileName;
		corpus  = new PQFrequency();
		corpus.minFreq = 6;
		corpus.minSize = 7;
		items = new ArrayList<tfidfTuple>();
		fullFreqs = new PriorityQueue<PQWordFreq>(10, new FreqComparator());
		fullFreqs = new PriorityQueue<PQWordFreq>(10, new WordComparator());
	}
	
	public void docStats (PrintWriter pw) throws IOException {
		/*
		 * item number: commonword, lesscommonword, lesslesscommonword; hightfidf, nexttfidf, nextnexttfidf
		 */
		
		DecimalFormat fmt = new DecimalFormat("00000");
		
		int i;
		int j;
		int fSize;
		
		String str = "These are the most common words in each of the items:\n";
		writeOut(pw, str);
		
		for (i = 0; i < sCount; i++) {
			// For each item in the corpus, create 2 iterators and check for size
			fSize = items.get(i).fpq.size();
			
			str = "Item #" + fmt.format(i) + ": "; //13 chars
			
			if (fSize > 0) {
				// If the size of the alphabetical queue is bigger than 0, stats should be printed
				for (j = 0; (j < 3) && (j < fSize); j++){
					if (items.get(i).fpq.size() > 0) {
						str = str + items.get(i).fpq.poll().wordIs();
					}
					if (j < 2) {
						str = str + ", ";
					}
				}
				str = str + "; ";
				
				for (j = 0; (j < 3) && (j < fSize); j++){
					// Get at most top 3 words from tfidf queue 
					if (items.get(i).tpq.size() > 0) {
						str = str + items.get(i).tpq.poll().wordIs();
					}
					if (j < 2) {
						str = str + ", ";
					}
				}
			} else {
				// The particular item didn't have any words that matched the criteria, so no stats for it
				str = str + "This item didn't have any words that matched the paramters";
			}
			writeOut(pw, str + "\n");
		}		
	}
	
	public void scanCorpus (Scanner words) {
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
	
	public double tfidf(int wFreq, int iCount) {
		/*
		 * tfidf(w) = freqInItem(w) * ln(items in corpus / items containing w)
		 */
		
		return Math.log((sCount*1.0) / (iCount*1.0)) * (wFreq*1.0);
	}
	
	public void calcTFIDF () {				
		int itemCount = 0;
		int i;
		int wpqSize = fullWords.size(); // Size of PQ of corpus
		
		int inItem[] = new int [sCount]; // Array to hold frequencies of word in each item
		
		PQWordFreq wArr[] = new PQWordFreq[sCount]; // Array to hold the top of each alpha queue
		PQWordFreq corpusArr[] = new PQWordFreq[wpqSize]; // Array to hold corpus in alpha order
		
		//PriorityQueue<PQWordFreq> cWords = new PriorityQueue<PQWordFreq>(10, new WordComparator());
		//Iterator<PQWordFreq> cItr = corpus.fpq.iterator();
		
		/*while (cItr.hasNext()) {
			cWords.add(cItr.next());
		}*/
		
		//String cWord;
		
		/*
		 * Transfer all corpus words into array because the iterator is inconcistent
		 */
		for (i = 0; i < wpqSize; i++) {
			corpusArr[i] = fullWords.poll();
		}
		
		/*
		 * Load top word of each item's alpha queue because iterator is inconcistent
		 */
		for (i = 0; i < sCount; i++) {
			if (items.get(i).wpqf.size() != 0) { //make sure there's something to poll for
				wArr[i] = items.get(i).wpqf.poll();
			} else {
				wArr[i] = null;
			}
		}
		
		/*
		 * Focus on a word in the corpus
		 * Reset the counter of items containing that word
		 */
		for (int j = 0; j < wpqSize; j++) {
			
			itemCount = 0;
			
			for (i = 0; i < sCount; i++) {
				// Count how many items contain the word
				
				if (wArr[i] != null) {
					if (wArr[i].wordIs().equals(corpusArr[j].wordIs())) {
						itemCount++;
						inItem[i] = wArr[i].freqIs();
						if (items.get(i).wpqf.size() != 0) { // Poll for a new word from a matching item
							wArr[i] = items.get(i).wpqf.poll();
						}
					} else {
						inItem[i] = 0;
					}
				}
			}			
			System.out.println("bla");
			for (i = 0; i < sCount; i++) { // Calculate tfidf for word and insert into queue
				if (inItem[i] > 0) {
					// Create new PQFloat tuple with tfidf score and add to tfidf queue
					Double tfidf = tfidf(inItem[i], itemCount);
					items.get(i).tpq.add(new PQFloatFreq(corpusArr[j].wordIs(), tfidf));
				} 
			}
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
 		
 		Iterator<PQWordFreq> fitr = mf.corpus.fpq.iterator();
 		while (fitr.hasNext()) {
 			mf.fullFreqs.add(fitr.next());
 		}
 		
 		Iterator<PQWordFreq> witr = mf.fullFreqs.iterator();
 		while (witr.hasNext()) {
 			mf.fullWords.add(witr.next());
 		}
 		
 		mf.corpus.validFrequencies();
 		mf.scanItems(); 		
 		mf.calcTFIDF();
 		mf.docStats(fileout);
 		
 		writeOut(fileout, "Program completed\n");
 	    
 	    // Avoiding memory leaks
 	    fileout.close();
 	    wordsIn.close();
	}

}
