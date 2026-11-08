export interface User {
  id: number;
  name: string;
  surname: string;
  email: string;
  role: string;
}

export interface UserRegistration {
  name: string;
  surname: string;
  email: string;
  password: string;
}
