package tech.builtrix.services.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidateResult {
    boolean isValid;
    String reason;


}
