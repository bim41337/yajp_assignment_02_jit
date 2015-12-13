package de.oth.jit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class StagingController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Path stagingFilePath = Paths.get(".jit/staging/stage.ser");
	// Paths are saved as their string representation to be serializable
	private ArrayList<String> entries;

	private StagingController() {
		entries = new ArrayList<String>();
	}

	static StagingController get() throws JitException {
		if (!Files.exists(stagingFilePath)) {
			return new StagingController();
		}
		return readStagingFile();
	}

	// Deserializing method
	private static StagingController readStagingFile() throws JitException {
		StagingController instance;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(stagingFilePath.toFile()))) {
			instance = (StagingController) in.readObject();
		} catch (Exception e) {
			throw new JitException("Could not read staging file!");
		}
		return instance;
	}

	// Serializing method
	void save() throws JitException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(stagingFilePath.toFile()))) {
			out.writeObject(this);
		} catch (Exception e) {
			throw new JitException("Could not write staging file!");
		}
	}

	void addFile(Path path) {
		if (!alreadyStaged(path)) {
			entries.add(path.toString());
		}
	}

	void removeFile(Path path) {
		String pathString = path.toString();
		int removeIndex = -1;
		for (String memberString : entries) {
			if (pathString.equals(memberString)) {
				removeIndex = entries.indexOf(memberString);
				break;
			}
		}
		if (removeIndex >= 0) {
			entries.remove(removeIndex);
		}
	}
	
	// Helper methods

	private boolean alreadyStaged(Path path) {
		String pathString = path.toString();
		for (String memberString : entries) {
			if (pathString.equals(memberString)) {
				return true;
			}
		}
		return false;
	}

	ArrayList<Path> getPathEntries() {
		ArrayList<Path> pathEntries = new ArrayList<>();
		entries.forEach(pathString -> pathEntries.add(Paths.get(pathString)));
		return pathEntries;
	}

	ArrayList<String> getEntries() {
		return entries;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Current staging status:\n");
		for (String pathString : entries) {
			builder.append(pathString + "\n");
		}
		return builder.toString();
	}

}
