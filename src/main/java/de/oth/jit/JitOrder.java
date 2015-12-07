package de.oth.jit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class JitOrder {

	private final static Path jitPath = Paths.get(".jit");
	private Command command;

	JitOrder(Command command) {
		this.command = command;
	}

	void execute() throws JitException {
		// All commands expect for init require initialization
		if (command.getAction() != Action.INIT && isInitialized() == false) {
			throw new JitException(command.getAction().getCommandString() + " requires a call to \"init\" first");
		}
		try {
			System.out.println("Calling " + command.getAction().getCommandString() + " with " + command.getParameter());
			JitOrder.class.getMethod(command.getAction().getCommandString()).invoke(this);
		} catch (Exception e) {
			throw new JitException("Internal error occured (JitOrder):\n[" + e.getMessage() + "]");
		}
	}
	
	private void init() throws Exception {
		if (isInitialized()) {
			return; // Nothing to do here
		}
		Files.createDirectory(jitPath);
	}
	
	private void add() {
		
	}
	
	private void remove() {
		
	}
	
	private void commit() {
		
	}
	
	private void checkout() {
		
	}
	
	private boolean isInitialized() {
		return Files.exists(jitPath) && Files.isDirectory(jitPath);
	}

}
