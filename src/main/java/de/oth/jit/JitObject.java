package de.oth.jit;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

// hash method here
// getHashKey liefert Hashwerte eines Objekts
// getCommitContent liefert Inhalt der Datei im Commit
// Wie Ordnerstruktur aufbauen? JitCommit mit Kindliste anstatt StageController?
//	Kindliste eines Commits wären dann alle Ordner, die direkt im Workspace-Dir. liegen
//	Einfach durchgehen und Match suchen? => equals für JitDir / JitFile anhand Pfadstring?
// JitCommit erstellt über Constructor anhand eines StagingControllers die Struktur per Iterator<Path> ...
abstract class JitObject implements Serializable {

	private static final long serialVersionUID = 1L;
	ArrayList<JitObject> entries;
	String pathString;

	abstract byte[] getCommitContent() throws JitException;

	abstract Type getType();

	void writeRecursive(Path path) {
		path = path.resolve(this.getDirectPath());
		System.out.println(this + ": " + path);
		for (JitObject entry : entries) {
			entry.writeRecursive(path);
		}
		// Create and write own content
		// Seems to work, now write that stuff
		// DEBUG
		try {
			System.out.println(new String(getCommitContent()));
		} catch (JitException e) {};
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
			// System.out.println(path);
			// System.out.println(entry.getDirectPath());
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
