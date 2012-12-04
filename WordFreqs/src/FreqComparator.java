import java.util.Comparator;

public class FreqComparator implements Comparator {
	
	public int compare(Object pqw1, Object pqw2) {
		int freq1 = ((PQWordFreq)pqw1).freqIs();
		int freq2 = ((PQWordFreq)pqw2).freqIs();
		
		if (freq1 < freq2) {
			return -1;
		} else if (freq1 == freq2) {
			return 0;			
		} else {
			return 1;
		}
	}	
}
