package tech.builtrix.repository.building;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.building.Building;

@EnableScan
public interface BuildingRepository extends RepositoryBase<Building> {

}
