package de.oth.jit;

// Indicates the jit action to be performed
enum Action {

	INIT("init", 1), ADD("add"), REMOVE("remove"), COMMIT("commit"), CHECKOUT("checkout");

	// Factory method
	static Action get(String commandString) throws JitException {
		if (commandString != null) {
			for (Action action : Action.values()) {
				if (commandString.toLowerCase().equals(action.getCommandString())) {
					return action;
				}
			}
		}
		throw new JitException("Issued command not defined!");
	}

	private String commandString;
	private int totalParametersNeeded; // Used to check length of args array

	private Action(String commandString) {
		this.commandString = commandString;
		totalParametersNeeded = 2;
	}

	private Action(String commandString, int parametersNeeded) {
		this(commandString);
		totalParametersNeeded = parametersNeeded;
	}

	String getCommandString() {
		return commandString;
	}

	int getTotalParametersNeeded() {
		return totalParametersNeeded;
	}

}