import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { TokenStorageService } from '../_services/token-storage.service';
import { OrderService } from '../_services/order.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  books: any[] = [];

  constructor(
    private userService: UserService,
    private tokenStorage: TokenStorageService,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    this.userService.getBooks().subscribe({
      next: data => {
        this.books = data;
      },
      error: err => {
        console.error('Error fetching books:', err);
      }
    });
  }
  
  orderBook(book: any): void {
    if (!this.tokenStorage.getToken()) {
      alert('Для оформления заказа необходимо авторизоваться!');
      return;
    }
    this.orderService.createOrder(book.id).subscribe({
      next: () => {
        alert('Заказ успешно оформлен!');
      },
      error: err => {
        alert('Ошибка при оформлении заказа: ' + (err.error?.message || err.message));
      }
    });
  }
}
