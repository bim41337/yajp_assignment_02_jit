package de.oth.jit;

public class Jit {

	public static void main(String[] args) {
		// args = new String[] {"init"};
		// args = new String[] { "add", "src/test/java/de/oth/jit/CommandTest.java" };
		args = new String[] { "commit", "trying to commit something ..." };
		try {
			new Operator(Command.get(args)).execute();
		} catch (JitException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
