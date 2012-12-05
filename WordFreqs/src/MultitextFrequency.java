import java.util.PriorityQueue;


public class MultitextFrequency {
	
	public String separator;
	public String rptName = "MultitextReport.txt";
	public String inputFile;
	
	public int minLen = 6;
	public int sCount = 0; // number of separator occurences, indicating number of items
	
	public PriorityQueue<PQWordFreq> corpus = new PriorityQueue<PQWordFreq>(10, new FreqComparator());
	
	public MultitextFrequency (String sprtr, String fileName) {
		separator = sprtr + " ";
		inputFile = fileName;
	}
	
	public double tfidf() {
		/*
		 * tfidf(w) = freqInItem(w) * ln(words in corpus / items containing w)
		 */
		return 0.0;
	}
	
	public String docStats () {
		/*
		 * item number: commonword, lesscommonword, lesslesscommonword; hightfidf, nexttfidf, nextnexttfidf
		 */
		return "nothing";
	}
	
	public void scanCorpus () {
		
	}

}
