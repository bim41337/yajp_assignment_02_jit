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

	abstract byte[] getCommitContent();

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

	String byteArrayToHexString() throws JitException {
		StringBuilder s = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digested = md.digest(getCommitContent());

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
	
	// DEBUG
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(pathString + "\n");
		for (JitObject entry : entries) {
			builder.append(entries.indexOf(entry) + ": " + entry.toString() + "\n");
		}
		return builder.toString();
	}

}
