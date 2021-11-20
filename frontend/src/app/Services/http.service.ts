import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../domain/User";
import {firstValueFrom} from "rxjs";

@Injectable(
  {
    providedIn: "root"
  }
)
export class HttpService {

  baseUrl = "http://localhost:8080/v1/users"

  constructor(private http: HttpClient) {
  }

  async login(username: string, password: string): Promise<string> {
    return (await firstValueFrom(await this.http.post<{ jwtToken: string }>(`${this.baseUrl}/login`, {
      username,
      password
    }))).jwtToken
  }

  async register(username: string, password: string, firstName: string, lastName: string): Promise<User> {
    return firstValueFrom(await this.http.post<User>(`${this.baseUrl}/register`, {
      username,
      password,
      firstName,
      lastName
    }))
  }

  async addFriend(userId: string | null, friendId: string, jwtToken: string | null): Promise<void> {
    const headers = new HttpHeaders().set("Authorization", `Bearer ${jwtToken}`)
    return firstValueFrom(await this.http.post<void>(`${this.baseUrl}/${userId}/friends/${friendId}`, {}, {headers: headers}))
  }

  async getAllUsers(pageNumber: number, query: string): Promise<User[]> {
    return firstValueFrom(await this.http.get<User[]>(`${this.baseUrl}?pageNumber=${pageNumber}&query=${query}`))
  }

  async getUserFriends(userId: string | null): Promise<User[]> {
    const headers = new HttpHeaders().set("Authorization", `Bearer ${localStorage.getItem("jwtToken")}`)
    return firstValueFrom(await this.http.get<User[]>(`${this.baseUrl}/${userId}/friends`, {headers: headers}))
  }

  async removeFriend(userId: string | null, friendId: string, jwtToken: string | null): Promise<void> {
    const headers = new HttpHeaders().set("Authorization", `Bearer ${jwtToken}`)
    return firstValueFrom(await this.http.delete<void>(`${this.baseUrl}/${userId}/friends/${friendId}`, {headers: headers}))
  }

  async deleteAccount(userId: string | null, jwtToken: string | null): Promise<void> {
    const headers = new HttpHeaders().set("Authorization", `Bearer ${jwtToken}`)
    return firstValueFrom(await this.http.delete<void>(`${this.baseUrl}/${userId}`, {headers: headers}))
  }
}
