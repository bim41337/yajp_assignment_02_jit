package de.oth.jit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import de.oth.jit.JitObject.Type;

class JitFile extends JitObject {

	private static final long serialVersionUID = 1L;
	private Path completeFilePath;

	JitFile(Path completePath) {
		// Stays empty
		entries = new ArrayList<JitObject>(0);
		completeFilePath = completePath;
		pathString = completePath.toString();
	}

	private void writeObject(ObjectOutputStream out) throws Exception {

	}

	private void readObject(ObjectInputStream in) throws Exception {

	}

	@Override
	byte[] getCommitContent() {
		// how to read file content while preserving structure???
		return null;
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
