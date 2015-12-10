package de.oth.jit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class JitFile extends JitObject {
	
	private static final long serialVersionUID = 1L;
	private Path completeFilePath;
	
	JitFile(Path path) {
		// Stays empty
		entries = new ArrayList<JitObject>(0);
		completeFilePath = path;
		pathString = path.toString();
	}
	
	private void writeObject(ObjectOutputStream out) throws Exception {
		
	}
	
	private void readObject(ObjectInputStream in) throws Exception {
		
	}

	@Override
	byte[] getCommitContent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	void addEntry(JitObject entry) {
		return;
	}
	
	@Override
	JitObject getEntryByPath(Path path) {
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
	
}
