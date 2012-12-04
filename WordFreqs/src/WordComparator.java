import java.util.Comparator;


public class WordComparator implements Comparator{
	
	public int compare(Object pqw1, Object pqw2) {
		
		return ((PQWordFreq)pqw1).compareTo(((PQWordFreq)pqw2));
	}
}
