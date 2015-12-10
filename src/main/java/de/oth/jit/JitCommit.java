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
			JitObject predecessor, current, newEntry;
			Iterator<Path> iterator = stagePath.iterator();
			Path next;
			predecessor = current = this;
			// Iterator goes through path elements one by one
			while (iterator.hasNext()) {
				next = iterator.next();
				if (current.contains(next)) {
					predecessor = current;
					current = current.getEntryByPath(next);
				} else {
					// staged paths only end with files
					newEntry = (stagePath.endsWith(next)) ? new JitFile(stagePath) : new JitDirectory(next);
					predecessor.addEntry(newEntry);
				}
			}
		}
	}
	
	@Override
	byte[] getCommitContent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	void setCommitMessage(String message) {
		commitMessage = message;
	}

}
