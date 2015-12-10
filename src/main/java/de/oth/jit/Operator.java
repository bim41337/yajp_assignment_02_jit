package de.oth.jit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Operator {

	private final static Path jitPath = Paths.get(".jit");
	private final static Path objectsPath = Paths.get(".jit/objects");
	private final static Path stagingPath = Paths.get(".jit/staging");
	private Command command;

	Operator(Command command) {
		this.command = command;
	}

	void execute() throws JitException {
		// DEBUG
		System.out.println("Calling " + command.getAction().getCommandString() + " with " + command.getParameter());
		if (command.getAction() == Action.INIT) {
			init();
		} else {
			if (!isInitialized()) {
				throw new JitException(command.getAction().getCommandString() + " requires a call to \"init\" first");
			}
			switch (command.getAction()) {
				case ADD:
				case REMOVE:
					stagingOperation();
					break;
				case COMMIT:
					commit();
					break;
				case CHECKOUT:
					checkout();
					break;
				default:
					; // null statement
					break;
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

	// Performs ADD and REMOVE operations
	private void stagingOperation() throws JitException {
		StagingController controller = StagingController.get();
		Path filePath = Paths.get(command.getParameter());
		if (Files.exists(filePath)) {
			if (command.getAction() == Action.ADD) {
				controller.addFile(filePath);
			} else if (command.getAction() == Action.REMOVE) {
				controller.removeFile(filePath);
			}
		}
		// DEBUG
		System.out.print(controller);
		controller.save();
	}

	private void commit() throws JitException {
		// NOPE
		StagingController controller = StagingController.get();
		String message = command.getParameter();
		JitCommit commit = new JitCommit(controller);
		commit.setCommitMessage(message);
		System.out.println(commit);
	}

	private void checkout() {
		// No clue ...
	}

	private boolean isInitialized() {
		return Files.exists(jitPath) && Files.isDirectory(jitPath);
	}

}
