import {Role} from '../building/enums/Role';

export class User {
  id: string;
  emailAddress: string;
  password: string;
  confirmPassword: string;
  fullName: string;
  firstName: string;
  lastName: string;
  token: string;
  job: string;
  role: Role;
  jobTitle: string;
  interest: string;
  subscribe: boolean;
}
