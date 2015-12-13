package de.oth.jit;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

abstract class JitObject {

	ArrayList<JitObject> entries;
	String pathString;

	abstract byte[] getCommitContent() throws JitException;

	abstract Type getType();

	void writeRecursive(Path commitPath) throws JitException {
		for (JitObject entry : entries) {
			entry.writeRecursive(commitPath);
		}
		File outputFile = commitPath.resolve(getHashString()).toFile();
		try (FileOutputStream out = new FileOutputStream(outputFile)) {
			outputFile.createNewFile();
			out.write(getCommitContent());
			out.flush();
		} catch (Exception e) {
			throw new JitException("Could not write commit file.\nCAUTION: Commit incomplete!");
		}
	}

	void addEntry(JitObject entry) {
		entries.add(entry);
	}

	JitObject getEntryByPath(Path path) {
		JitObject member = null;
		for (JitObject entry : entries) {
			if (entry.getDirectPath().equals(path)) {
				member = entry;
			}
		}
		return member;
	}

	boolean contains(Path path) {
		for (JitObject entry : entries) {
			if (entry.getDirectPath().equals(path)) {
				return true;
			}
		}
		return false;
	}

	Path getDirectPath() {
		return Paths.get(pathString);
	}
	
	String getHashString() throws JitException {
		return byteArrayToHexString(getCommitContent());
	}

	static String byteArrayToHexString(byte[] contents) throws JitException {
		StringBuilder s = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digested = md.digest(contents);

			for (byte b : digested) {
				int value = b & 0xFF;
				s.append(Integer.toHexString(value & 0x0F));
				s.append(Integer.toHexString(value >>> 4));
			}
		} catch (NoSuchAlgorithmException ex) {
			throw new JitException("Could not compute a hash string!");
		}
		return s.toString();
	}

	enum Type {
		COMMIT, DIRECTORY, FILE
	}

	// DEBUG
	@Override
	public String toString() {
		return getType().name() + " " + pathString;
	}

}
