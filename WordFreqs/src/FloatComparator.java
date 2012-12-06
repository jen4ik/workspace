import java.util.Comparator;


public class FloatComparator implements Comparator{
	
	public int compare(Object pqw1, Object pqw2) {
		
		return ((PQFloatFreq)pqw1).compareTo(((PQFloatFreq)pqw2));
	}
}
