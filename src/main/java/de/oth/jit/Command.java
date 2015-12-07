package de.oth.jit;

class Command {
	
	private Action action;
	private String parameter;
	
	private Command(Action action, String parameter) {
		this.action = action;
		this.parameter = parameter;
	}
	
	// Factory method to take care of errors and get the right Action-Param-Pair
	static Command get(String[] args) throws JitException {
		Action selectedAction = Action.get(args[0]);
		String param;
		if (selectedAction.getTotalParametersNeeded() != args.length) {
			throw new JitException("Unexpected number of arguments supplied!");
		}
		param = (selectedAction == Action.INIT) ? "" : args[1];
		return new Command(selectedAction, param);
	}

	Action getAction() {
		return action;
	}

	String getParameter() {
		return parameter;
	}
		
}
