package de.oth.jit;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class StagingController implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient List<Path> entries;

	StagingController() {
		entries = new ArrayList<Path>();
	}

	private void writeObject(ObjectOutputStream out) throws Exception {

	}

	private void readObject(ObjectInputStream in) throws Exception {

	}

}
