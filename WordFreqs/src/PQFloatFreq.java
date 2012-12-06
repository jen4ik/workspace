
import java.text.DecimalFormat;

public class PQFloatFreq implements Comparable<PQFloatFreq> {
	
	private String word;
	private float freq;

	DecimalFormat fmt = new DecimalFormat("00000");


	public PQFloatFreq(String newWord)
	{
		word = newWord;
	    freq = 0;
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
	
	public float freqIs()
	{
	    return freq;
	}

}
