package de.oth.jit;

public class Jit {

	public static void main(String[] args) {
		// args = new String[] {"init"};
		// args = new String[] { "add", "src/test/java/de/oth/jit/CommandTest.java" };
		// args = new String[] { "commit", "trying to commit something ..." };
		args = new String[] { "checkout", "730c8c2fe75e31255da453e63efe4e429dcb6d1d" };
		try {
			new Operator(Command.get(args)).execute();
		} catch (JitException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
