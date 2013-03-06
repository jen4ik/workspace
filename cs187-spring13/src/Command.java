
public class Command implements CommandInterface{
	public static final int 
		CMD_COMMENT = 1,
		CMD_DO = 2,
		CMD_DOC = 3,
		CMD_END = 4,
		CMD_HISTORY = 5,
		CMD_INVALID = 6,
		CMD_LET = 7,
		CMD_PRINT = 8;
	
	private int cmd;
	private String cmdStr;
	private String args;
	
	public Command(String str) {		
		
		if (str == null) {
			cmd = CMD_INVALID;
			cmdStr = "INVALID";
			args = "null argument";
			return;
		}
		// Blank line is a comment
		if (str.equals("")) {
			cmd = CMD_COMMENT;
			cmdStr = "COMMENT";
			args = "";
			return;
		}
		
		String[] line = str.split(" ", 2);
		
		if (line.length == 1) {
			if (line[0].toLowerCase().equals("history")) {
				cmd = CMD_HISTORY;
				cmdStr = "HISTORY";
				args = "";
			} else if (line[0].toLowerCase().equals("end")) {
				cmd = CMD_END;
				cmdStr = "END";
				args = "";
			} else {
				cmd = CMD_INVALID;
				cmdStr = "INVALID";
				args = str;
			}
		} else {
			args = line[1];
			if (line[0].toLowerCase().equals("let")) {
				cmd = CMD_LET;
				cmdStr = "LET";				
			} else if (line[0].toLowerCase().equals("doc")) {
				cmd = CMD_DOC;
				cmdStr = "DOC";		
			} else if (line[0].toLowerCase().equals("print")) {
				cmd = CMD_PRINT;
				cmdStr = "PRINT";		
			} else if (line[0].toLowerCase().equals("do")) {
				cmd = CMD_DO;
				cmdStr = "DO";		
			} else if (line[0].toLowerCase().equals("#") || 
					line[0].toLowerCase().equals("comment")) {
				cmd = CMD_COMMENT;
				cmdStr = "COMMENT";		
			} else {
				cmd = CMD_INVALID;
				cmdStr = "INVALID";		
				args = str;
			}
		}		
	}
	
	public int getCommand() {
		return cmd;
	}
	
	public String getArg() {
		return args;
	}
	
	public String toString() {
		if (args == null || args.equals("")) {
			return "<" + cmdStr + ">";
		} else {
			return "<" + cmdStr + "> <" + args + ">"; 
		}
	}
}
