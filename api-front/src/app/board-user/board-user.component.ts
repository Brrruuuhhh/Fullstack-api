import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { OrderService } from '../_services/order.service';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css']
})
export class BoardUserComponent implements OnInit {
  content?: string;
  orders: any[] = [];
  isLoggedIn = false;
  isUser = false;

  constructor(private userService: UserService, private orderService: OrderService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    const token = this.tokenStorage.getToken();
    this.isLoggedIn = !!token;
    const user = this.tokenStorage.getUser();
    this.isUser = user && Array.isArray(user.roles) && (user.roles.includes('ROLE_USER') || user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_MODERATOR'));
    if (!this.isLoggedIn || !this.isUser) {
      this.content = 'Доступ запрещён. Требуется авторизация пользователя.';
      return;
    }
    this.userService.getUserBoard().subscribe({
      next: data => {
        this.content = data;
      },
      error: err => {
        this.content = JSON.parse(err.error).message;
      }
    });
    this.orderService.getMyOrders().subscribe({
      next: data => {
        this.orders = data;
      },
      error: err => {
      }
    });
  }
}
