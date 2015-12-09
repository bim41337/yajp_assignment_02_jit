package de.oth.jit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class JitFile implements JitObject {
	
	private static final long serialVersionUID = 1L;
	private String pathString;
	private byte[] content;
	
	JitFile(Path path) throws JitException {
		try {
			pathString = path.toString();
			this.content = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new JitException("Could not read file at " + path + "!");
		}
	}
	
	private void writeObject(ObjectOutputStream out) throws Exception {
		
	}
	
	private void readObject(ObjectInputStream in) throws Exception {
		
	}
	
	byte[] getContent() {
		return content;
	}
	
}
