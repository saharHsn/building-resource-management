package tech.builtrix.dto;

import lombok.Data;
import tech.builtrix.base.EntityBase;

/**
 * Created By sahar-hoseini at 11. Jul 2019 5:53 PM
 **/

@Data
public abstract class EntityDtoBase {

    protected String id;

    public EntityDtoBase(EntityBase modelBase) {
        this.id = modelBase.getId();
    }

    public EntityDtoBase() {
    }
}
