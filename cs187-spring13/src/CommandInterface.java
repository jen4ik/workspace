
public interface CommandInterface {
	
	// Constants that indicate what command was given
	// You can access these as CommandInterface.CMD_COMMENT or Command.CMD_COMMENT
	public static final int
		CMD_COMMENT = 1,
		CMD_DO = 2,
		CMD_DOC = 3,
		CMD_END = 4,
		CMD_HISTORY = 5,
		CMD_INVALID = 6,
		CMD_LET = 7,
		CMD_PRINT = 8;
	
	// Expected constructor: public Command( String commandLine );
	
	// Accessor methods
	public int getCommand();
	public String getArg();
	
	// Optional: public String toString();
}
