package tech.builtrix.commands;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Data
public class SetSessionCommand extends CommandBase {

	@NotBlank
	private String session;

	public SetSessionCommand(String session) {
		super("set_session");
		this.session = session;
	}
}
