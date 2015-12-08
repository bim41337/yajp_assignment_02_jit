package de.oth.jit;

@SuppressWarnings("serial")
class JitException extends Exception {

	JitException(String msg) {
		super(msg);
	}

	JitException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
