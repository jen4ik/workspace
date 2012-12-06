
import java.text.DecimalFormat;

public class PQWordFreq implements Comparable<PQWordFreq> {
	
	private String word;
	private int freq;

	DecimalFormat fmt = new DecimalFormat("00000");


	public PQWordFreq(String newWord)
	{
		word = newWord;
	    freq = 0;
	}
	
	public void inc()
	{
	    freq++;
	}
	
	public void dec () {
		freq--;
	}
	
	public int compareTo(PQWordFreq other)
	{
	    return this.word.compareTo(other.word); 
	}
	
	public boolean equals(PQWordFreq other)
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
	
	public int freqIs()
	{
	    return freq;
	}

}
