package de.oth.jit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
class JitOrder {

	private final static Path jitPath = Paths.get(".jit");
	private final static Path objectsPath = Paths.get(".jit/objects");
	private final static Path stagingPath = Paths.get(".jit/staging");
	private final static Path stagingFilePath = Paths.get(".jit/staging/stage.ser");
	private Command command;

	JitOrder(Command command) {
		this.command = command;
	}

	void execute() throws JitException {
		System.out.println("Calling " + command.getAction().getCommandString() + " with " + command.getParameter());
		if (command.getAction() == Action.INIT) {
			init();
		} else {
			// All commands expect for init require initialization
			if (!isInitialized()) {
				throw new JitException(command.getAction().getCommandString() + " requires a call to \"init\" first");
			}
			prepareAction();
			try {
				JitOrder.class.getDeclaredMethod(command.getAction().getCommandString()).invoke(this);
			} catch (Exception e) {
				throw new JitException("Internal error occured (JitOrder) [" + e.getMessage() + "]");
			}
		}
	}

	private void init() throws JitException {
		if (isInitialized()) {
			return; // Nothing to do here
		}
		try {
			Files.createDirectory(jitPath);
			Files.createDirectory(objectsPath);
			Files.createDirectory(stagingPath);
		} catch (IOException e) {
			throw new JitException("Could not initialize .jit directory!");
		}
	}

	private void prepareAction() throws JitException {
		if (command.getAction() == Action.CHECKOUT) {
			// needs to deserialize object files
		} else {
			// need to create or deserialize stage.ser
			if (!Files.exists(stagingFilePath)) {
				try {
					Files.createFile(stagingFilePath);
				} catch (IOException e) {
					throw new JitException("Could not create staging file!");
				}
			} else {
				// Deserialize stage file
			}
		}
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
