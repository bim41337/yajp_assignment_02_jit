package de.oth.jit;

public class Jit {

	public static void main(String[] args) {
		try {
			new Operator(Command.get(args)).execute();
		} catch (JitException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.exit(1);
		}
	}

}
