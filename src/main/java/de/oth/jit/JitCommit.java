package de.oth.jit;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

class JitCommit extends JitObject {

	private static final long serialVersionUID = 1L;
	private String commitMessage;

	JitCommit(StagingController controller) {
		entries = new ArrayList<JitObject>();
		commitMessage = "";
		pathString = "";
		gatherEntries(controller.getPathEntries());
	}

	private void gatherEntries(ArrayList<Path> stagingEntries) {
		for (Path stagePath : stagingEntries) {
			// Walk the commit tree and try to find a match
			JitObject predecessor, newEntry;
			Iterator<Path> iterator = stagePath.iterator();
			Path next;
			predecessor = this;
			// Iterator goes through path elements one by one
			while (iterator.hasNext()) {
				next = iterator.next();
				if (predecessor.contains(next)) {
					predecessor = predecessor.getEntryByPath(next);
				} else {
					// staged paths only end with files
					newEntry = (stagePath.endsWith(next)) ? new JitFile(stagePath) : new JitDirectory(next);
					predecessor.addEntry(newEntry);
					predecessor = newEntry;
				}
			}
		}
	}

	void writeCommitFiles(Path commitPath) {
		commitPath = commitPath.resolve(this.getDirectPath());
		writeRecursive(commitPath);
	}

	@Override
	byte[] getCommitContent() throws JitException {
		String lineSeparator = System.lineSeparator(), entryName;
		StringBuilder builder = new StringBuilder(getType() + " " + commitMessage + lineSeparator);
		for (JitObject entry : entries) {
			entryName = entry.getDirectPath().getFileName().toString();
			builder.append(entry.getType() + " " + entry.getHashString() + " " + entryName + lineSeparator);
		}
		return builder.toString().getBytes();
	}

	@Override
	Type getType() {
		return Type.COMMIT;
	}

	void setCommitMessage(String message) {
		commitMessage = message;
	}

}
