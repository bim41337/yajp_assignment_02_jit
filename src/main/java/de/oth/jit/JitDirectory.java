package de.oth.jit;

import java.nio.file.Path;
import java.util.ArrayList;

public class JitDirectory extends JitObject {

	JitDirectory(Path path) {
		entries = new ArrayList<JitObject>();
		pathString = path.toString();
	}

	@Override
	byte[] getCommitContent() throws JitException {
		String lineSeparator = System.lineSeparator(), entryName;
		StringBuilder builder = new StringBuilder(getType() + lineSeparator);
		for (JitObject entry : entries) {
			entryName = entry.getDirectPath().getFileName().toString();
			builder.append(entry.getType() + " " + entry.getHashString() + " " + entryName + lineSeparator);
		}
		return builder.toString().trim().getBytes();
	}

	@Override
	ObjectType getType() {
		return ObjectType.DIRECTORY;
	}

}
