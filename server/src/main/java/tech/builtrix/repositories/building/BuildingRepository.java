package tech.builtrix.repositories.building;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.building.Building;

@EnableScan
public interface BuildingRepository extends RepositoryBase<Building> {

	Building findByOwner(String id);
}
