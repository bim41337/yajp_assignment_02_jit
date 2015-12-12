package de.oth.jit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class JitFile extends JitObject {

	private static final long serialVersionUID = 1L;
	private Path completeFilePath;

	JitFile(Path completePath) {
		// Stays empty
		entries = new ArrayList<JitObject>(0);
		completeFilePath = completePath;
		pathString = completePath.toString();
	}

	@Override
	byte[] getCommitContent() throws JitException {
		byte[] content = {};
		try {
			content = Files.readAllBytes(completeFilePath);
		} catch (IOException e) {
			throw new JitException("Could not commit file \"" + completeFilePath + "\"!");
		}
		return content;
	}

	@Override
	void addEntry(JitObject entry) {
		return;
	}

	@Override
	JitObject getEntryByPath(Path path) {
		// Will never be called since entries are always empty
		return this;
	}

	@Override
	Path getDirectPath() {
		// File needs to know its full path since it has to read the byte content
		return completeFilePath.getFileName();
	}

	@Override
	boolean contains(Path path) {
		return false;
	}

	@Override
	Type getType() {
		return Type.FILE;
	}

}
