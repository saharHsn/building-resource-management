package tech.builtrix.commands;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

public abstract class CommandBase {
	private String type;

	CommandBase(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
