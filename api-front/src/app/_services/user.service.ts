import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/test/';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.http.get(API_URL + 'all', { responseType: 'text' });
  }

  getUserBoard(): Observable<any> {
    return this.http.get(API_URL + 'user', { responseType: 'text' });
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(API_URL + 'mod', { responseType: 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(API_URL + 'admin', { responseType: 'text' });
  }

  getBooks(): Observable<any> {
    return this.http.get('http://localhost:8080/api/books');
  }

  getUsersWithOrders(): Observable<any> {
    return this.http.get('http://localhost:8080/api/users-with-orders');
  }

  addBook(book: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/books', book);
  }

  updateBook(id: number, book: any): Observable<any> {
    return this.http.put(`http://localhost:8080/api/books/${id}`, book);
  }

  deleteBook(id: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/api/books/${id}`);
  }

  addOrder(userId: number, bookId: number): Observable<any> {
    return this.http.post(`http://localhost:8080/api/orders/for-user?userId=${userId}&bookId=${bookId}`, {});
  }

  updateOrder(orderId: number, bookId: number): Observable<any> {
    return this.http.put(`http://localhost:8080/api/orders/${orderId}`, { bookId });
  }

  deleteOrder(orderId: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/api/orders/${orderId}`);
  }

  createUser(user: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/users-with-orders', user);
  }

  updateUser(id: number, user: any): Observable<any> {
    return this.http.put(`http://localhost:8080/api/users-with-orders/${id}`, user);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/api/users-with-orders/${id}`);
  }
}
