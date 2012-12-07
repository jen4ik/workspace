import java.util.Comparator;


public class FloatComparator implements Comparator{
	
	public int compare(Object pqw1, Object pqw2) {
		double freq1 = ((PQFloatFreq)pqw1).freqIs();
		double freq2 = ((PQFloatFreq)pqw2).freqIs();
		
		if (freq1 > freq2) {
			// Revesed logical order so more frequent words are at head of queue
			return -1;
		} else if (freq1 == freq2) {
			return 0;			
		} else {
			return 1;
		}
	}
}
