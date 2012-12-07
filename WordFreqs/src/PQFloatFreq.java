//
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PQFloatFreq implements Comparable<PQFloatFreq> {
	
	private String word;
	private Double freq;

	NumberFormat fmt = new DecimalFormat("00.00");


	public PQFloatFreq(String newWord, Double f)
	{
		word = newWord;
	    freq = f;
	}
	
	public int compareTo(PQFloatFreq other)
	{
	    return this.word.compareTo(other.word); 
	}
	
	public boolean equals(PQFloatFreq other)
	{
	    return this.word.equals(other.word); 
	}
	
	public String toString()
	{
	    return(fmt.format(freq) + " " + word);
	}
	
	public String wordIs()
	{
	    return word;
	}
	
	public Double freqIs()
	{
	    return freq;
	}

}
