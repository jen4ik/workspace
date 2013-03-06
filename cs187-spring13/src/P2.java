
public class P2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Welcome to CS187, Spring2013");
		
		NextCommand nc = new NextCommand();
		Command cmd;
		
		do {
			cmd = nc.get();
			System.out.println(cmd);
		} while (cmd.getCommand() != Command.CMD_END);

	}

}
