package de.oth.jit;

import java.nio.file.Path;
import java.util.ArrayList;

public class JitDirectory extends JitObject {

	private static final long serialVersionUID = 1L;
	
	JitDirectory(Path path) {
		entries = new ArrayList<JitObject>();
		pathString = path.toString();
	}

	@Override
	byte[] getCommitContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
