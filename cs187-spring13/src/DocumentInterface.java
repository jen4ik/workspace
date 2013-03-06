
public interface DocumentInterface {

	// expected constructor: Document( String docid, String title, String text );
	// expected constructor: Document( String completeLine );
	
	// Accessor functions
	public String getTitle();
	public String getDocid();
	public WordList getWords();
	public int numWords();
	
	// Optional: public String toString();
}
