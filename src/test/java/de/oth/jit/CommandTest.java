package de.oth.jit;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CommandTest {

	@Test
	public void getInitTest() {
		Command cmd = null;
		try {
			cmd = Command.get(new String[] { "init" });
		} catch (JitException e) {
			System.out.println(e.getMessage());
		}
		assertEquals(Action.INIT, cmd.getAction());
		assertEquals("", cmd.getParameter());
	}

	@Test
	public void getInitFailTest() {
		Command cmd = null;
		try {
			cmd = Command.get(new String[] { "init", "sthUnnecessary" });
		} catch (JitException e) {
			System.out.println(e.getMessage());
		}
		assertNull(cmd);
	}

	@Test
	public void getOtherTest() {
		Command cmd = null;
		try {
			cmd = Command.get(new String[] { "ReMovE", "src/some/source/path/abc.java" });
		} catch (JitException e) {
		}
		assertEquals(Action.REMOVE, cmd.getAction());
		assertEquals("src/some/source/path/abc.java", cmd.getParameter());
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void commandUnknownTest() throws JitException {
		thrown.expect(JitException.class);
		thrown.expectMessage("Issued command not defined!");
		Command.get(new String[] { "UndefinedCommand", "src/some/source/path/abc.java" });
	}

}
