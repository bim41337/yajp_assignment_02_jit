package de.oth.jit;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

class Operator {

	private final static Path jitPath = Paths.get(".jit");
	private final static Path objectsPath = Paths.get(".jit/objects");
	private final static Path stagingPath = Paths.get(".jit/staging");
	private Command command;

	Operator(Command command) {
		this.command = command;
	}

	// Starting point for all operations
	void execute() throws JitException {
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
		} catch (Exception e) {
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
		// Prints the current staging status
		System.out.print(controller);
		controller.save();
	}

	private void commit() throws JitException {
		StagingController controller = StagingController.get();
		String message = command.getParameter();
		JitCommit commit = new JitCommit(controller);
		commit.setCommitMessage(message);
		try {
			FileUtils.cleanDirectory(objectsPath.toFile());
		} catch (Exception e) {
			throw new JitException("Could not clean commit directory.\nCAUTION: Commit incomplete!");
		}
		commit.writeRecursive(objectsPath);
		System.out.println("Commit complete!");
	}

	private void checkout() throws JitException {
		Map<String, List<String>> objectContents = readObjectsContents();
		Path startingPath = Paths.get("checkout");
		// Delete the substitute workspace expect .jit directory
		try {
			Files.walk(startingPath, 1).filter(path -> !path.equals(startingPath))
					.filter(path -> !path.equals(startingPath.resolve(jitPath)))
					.forEach(path -> FileUtils.deleteQuietly(path.toFile()));
		} catch (Exception e) {
			throw new JitException("Could not clean workspace.\nCAUTION: Checkout incomplete!");
		}
		// Now read find commit entry, start writing files and directories
		if (objectContents.get(command.getParameter()) != null) {
			writeContent(startingPath, command.getParameter(), objectContents);
		} else {
			throw new JitException("Could not find specified commit file " + command.getParameter());
		}
		System.out.println("Checkout complete!");
	}

	// Helper methods

	// Restores the workspace by writing all directories and files
	private void writeContent(Path writePath, String hashKey, Map<String, List<String>> objectContents)
			throws JitException {
		List<String> content = objectContents.get(hashKey);
		ObjectType type = ObjectType.valueOf(content.get(0).split(" ")[0].trim()); // First line always contains type
		if (type == ObjectType.FILE) {
			// File needs to put its content together before writing
			StringBuffer fullContent = new StringBuffer();
			for (int i = 1; i < content.size(); i++) {
				fullContent.append(content.get(i) + System.lineSeparator());
			}
			File outputFile = writePath.toFile();
			try (FileOutputStream out = new FileOutputStream(outputFile)) {
				outputFile.createNewFile();
				out.write(fullContent.toString().getBytes());
				out.flush();
			} catch (Exception e) {
				throw new JitException("Could not write file.\nCAUTION: Checkout incomplete!");
			}
		} else {
			// Create directory contents and recall routine for every entry to write
			String line[], entryKey, entryName;
			Path newPath;
			for (int i = 1; i < content.size(); i++) {
				line = content.get(i).split(" ");
				type = ObjectType.valueOf(line[0]);
				entryKey = line[1];
				entryName = line[2];
				newPath = writePath.resolve(entryName);
				if (type == ObjectType.DIRECTORY) {
					try {
						Files.createDirectory(newPath);
					} catch (Exception e) {
						throw new JitException("Could not create new directory.\nCAUTION: Checkout incomplete!");
					}
				}
				writeContent(newPath, entryKey, objectContents);
			}
		}
	}

	// Reads and returns a map holding all the information needed to recreate the workspace
	private Map<String, List<String>> readObjectsContents() throws JitException {
		HashMap<String, List<String>> objectContents = new HashMap<>();
		try {
			Iterator<Path> iterator;
			Path current;
			// Get all paths to the commit files and filter out the objects directory entry
			iterator = Files.walk(objectsPath, 1).filter(path -> !path.equals(objectsPath)).iterator();
			while (iterator.hasNext()) {
				current = iterator.next();
				// Put the file name, which equals the hash string, and all lines of current object file
				objectContents.put(current.getFileName().toString(), Files.readAllLines(current));
			}
		} catch (Exception e) {
			throw new JitException("Could not read object files.\nCheckout aborted!");
		}
		return objectContents;
	}

	private boolean isInitialized() {
		return Files.exists(jitPath) && Files.isDirectory(jitPath);
	}

}
