package de.oth.jit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class JitFile extends JitObject {

	private Path completeFilePath;

	JitFile(Path completePath) {
		entries = new ArrayList<JitObject>(0);
		completeFilePath = completePath;
		pathString = completePath.toString();
	}

	@Override
	byte[] getCommitContent() throws JitException {
		byte[] byteContent = {};
		String commitContent;
		try {
			byteContent = Files.readAllBytes(completeFilePath);
		} catch (IOException e) {
			throw new JitException("Could not commit file \"" + completeFilePath + "\"!");
		}
		commitContent = getType() + System.lineSeparator() + new String(byteContent);
		return commitContent.getBytes();
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
	ObjectType getType() {
		return ObjectType.FILE;
	}

}
