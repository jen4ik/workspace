import java.io.*;


public class NextCommand implements NextCommandInterface{
	
	private BufferedReader inStream;
	private boolean promptUser;
	
	public NextCommand() {
		inStream = new BufferedReader(new InputStreamReader(System.in));
		promptUser = true;
	}
	
	public Command get(){
		String inLine;
		
		// no more input to process, returning end
		if (inStream == null) {
			return new Command("end");
		}
		
		// working with the user
		if (promptUser) {
			System.out.println("What is your next command?");
		}
		
		// trying to get user input, might not always work
		try {
			if (null == (inLine = inStream.readLine()))
				return new Command ("end");
		} catch (IOException e) {
			inLine = "";
		}
		
		return new Command(inLine);
		
	}

}
