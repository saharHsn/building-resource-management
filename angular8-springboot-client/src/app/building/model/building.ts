import {BuildingAge} from '../enums/buildingAge';
import {BuildingUsage} from '../enums/buildingUsage';
import {EnergyCertificate} from '../enums/energyCertificate';
import {User} from '../../user/user';


export class Building {
  id: string;
  name: string;
  postalAddress: string;
  postalCode: string;
  addressId: number;
  usage: BuildingUsage;
  age: BuildingAge;
  energyCertificate: EnergyCertificate;
  area: number;
  numberOfPeople: number;
  gasBill: File;
  waterBill: File;
  electricityBill: File;
  owner: User;
}
