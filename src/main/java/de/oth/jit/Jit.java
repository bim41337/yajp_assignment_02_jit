package de.oth.jit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class Jit {

	public static void main(String[] args) {
		// args = new String[] {"init"};
		// args = new String[] { "add", "src/test/java/de/oth/jit/testFiles/SomeMarker.java" };
		args = new String[] { "commit", "trying to commit something ..." };
		try {
			new Operator(Command.get(args)).execute();
		} catch (JitException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		Path path = Paths.get("some/path/to/sth.mep"), next;
		Iterator<Path> it = path.iterator();
		while (it.hasNext()) {
			next = it.next();
			System.out.println(next + " | " + Files.isDirectory(next));
		}
	}

}
