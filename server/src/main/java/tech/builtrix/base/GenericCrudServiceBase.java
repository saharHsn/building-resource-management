package tech.builtrix.base;


import org.springframework.data.repository.CrudRepository;
import tech.builtrix.exceptions.NotFoundException;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public abstract class GenericCrudServiceBase<TModel extends EntityBase<TModel>, TRepository extends CrudRepository<TModel, String>> extends ServiceBase {

    protected TRepository repository;

    private Class<TModel> modelType;

    protected GenericCrudServiceBase(TRepository repository) {
        this.modelType = (Class<TModel>)
                ((ParameterizedType) getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];

        this.repository = repository;
    }

    public TModel getById(String id) throws NotFoundException {
        Optional<TModel> entity = repository.findById(id);
        if (!entity.isPresent()) {
            throw new NotFoundException(modelType.getSimpleName(), "Id", id);
        }
        return entity.get();
    }

    public Long getAllCount() {
        return this.repository.count();
    }

    public boolean existById(String id) {
        return this.repository.existsById(id);
    }

    public TModel setActiveness(String id, boolean active) throws NotFoundException {
        TModel model = this.getById(id);
        model.setActive(active);
        model = this.repository.save(model);
        return model;
    }

    public TModel safeDelete(String id) throws NotFoundException {
        TModel model = this.getById(id);
        model.setActive(false);
        model = this.repository.save(model);
        return model;
    }

    public TModel setActive(TModel model, boolean active){
        model.setActive(active);
        return model;
    }
}
