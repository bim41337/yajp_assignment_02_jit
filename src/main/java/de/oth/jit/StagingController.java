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
	
	void save() throws JitException {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(stagingFilePath.toFile()));
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			throw new JitException("Could not write staging file!");
		}
	}
	
	private static StagingController readStagingFile() throws JitException {
		StagingController instance;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(stagingFilePath.toFile()));
			instance = (StagingController) in.readObject();
			in.close();
		} catch (Exception e) {
			throw new JitException("Could not read staging file!");
		}
		return instance;
	}

	void addFile(Path path) {
		if (!alreadyStaged(path)) {
			entries.add(path.toString());
		}
	}
	
	void removeFile(Path path) {
		String pathString = path.toString();
		for (String memberString : entries) {
			if (pathString.equals(memberString)) {
				entries.remove(entries.indexOf(memberString));
			}
		}
	}
	
	private boolean alreadyStaged(Path path) {
		String pathString = path.toString();
		for (String memberString : entries) {
			if (pathString.equals(memberString)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Current staging status:\n");
		for (String pathString : entries) {
			builder.append(pathString);
		}
		return builder.toString();
	}

}
