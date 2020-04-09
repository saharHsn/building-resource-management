package tech.builtrix.services.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BuildingMetaData {
    String name;
    String type;
    Float area;
    Integer averageNumberOfPeople;
    String address;
    Float contractedPower;

     /*Name of the Building
        Type of the Building
        Area
        Average Number of People
        Location
        Contracted Power
        and other building info*/
}
