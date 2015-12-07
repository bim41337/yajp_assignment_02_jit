package de.oth.jit;

public class Jit {
	
	public static void main(String[] args) {
		try {
			new JitOrder(Command.get(args)).execute();
		} catch (JitException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}
