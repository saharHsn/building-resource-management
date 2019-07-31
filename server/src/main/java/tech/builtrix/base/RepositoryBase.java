package tech.builtrix.base;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RepositoryBase<TEntity extends EntityBase> extends CrudRepository<TEntity, String> {
    int DefaultPageSize = 10;
}
