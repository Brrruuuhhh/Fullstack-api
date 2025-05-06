import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-board-moderator',
  templateUrl: './board-moderator.component.html',
  styleUrls: ['./board-moderator.component.css']
})
export class BoardModeratorComponent implements OnInit {
  content?: string;
  usersWithOrders: any[] = [];
  books: any[] = [];
  selectedBook: any = null;
  newBook: any = { title: '', author: '', price: null };
  isEditing: boolean = false;
  orderEdit: { [key: number]: any } = {};
  orderAdd: { [userId: number]: { bookId: number|null } } = {};
  isLoggedIn = false;
  isModerator = false;

  constructor(private userService: UserService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    const token = this.tokenStorage.getToken();
    this.isLoggedIn = !!token;
    const user = this.tokenStorage.getUser();
    this.isModerator = user && Array.isArray(user.roles) && (user.roles.includes('ROLE_MODERATOR') || user.roles.includes('ROLE_ADMIN'));
    if (!this.isLoggedIn || !this.isModerator) {
      this.content = 'Доступ запрещён. Требуется авторизация модератора.';
      return;
    }
    this.userService.getModeratorBoard().subscribe({
      next: data => {
        this.content = data;
      },
      error: err => {
        this.content = JSON.parse(err.error).message;
      }
    });
    this.userService.getUsersWithOrders().subscribe({
      next: data => {
        this.usersWithOrders = data;
      }
    });
    this.userService.getBooks().subscribe({
      next: data => {
        this.books = data;
      }
    });
  }

  addBook() {
    this.userService.addBook(this.newBook).subscribe({
      next: book => {
        this.books.push(book);
        this.newBook = { title: '', author: '', price: null };
      }
    });
  }

  editBook(book: any) {
    this.selectedBook = { ...book };
    this.isEditing = true;
  }

  updateBook() {
    this.userService.updateBook(this.selectedBook.id, this.selectedBook).subscribe({
      next: updatedBook => {
        const idx = this.books.findIndex(b => b.id === updatedBook.id);
        if (idx !== -1) this.books[idx] = updatedBook;
        this.selectedBook = null;
        this.isEditing = false;
      }
    });
  }

  deleteBook(id: number) {
    this.userService.deleteBook(id).subscribe({
      next: () => {
        this.books = this.books.filter(b => b.id !== id);
      }
    });
  }

  cancelEdit() {
    this.selectedBook = null;
    this.isEditing = false;
  }

  startEditOrder(order: any) {
    this.orderEdit[order.id] = { ...order, bookId: order.book?.id };
  }

  saveEditOrder(order: any, user: any) {
    this.userService.updateOrder(order.id, this.orderEdit[order.id].bookId).subscribe({
      next: updated => {
        const idx = user.orders.findIndex((o: any) => o.id === order.id);
        if (idx !== -1) user.orders[idx] = { ...order, book: this.books.find(b => b.id === this.orderEdit[order.id].bookId) };
        delete this.orderEdit[order.id];
      }
    });
  }

  cancelEditOrder(orderId: number) {
    delete this.orderEdit[orderId];
  }

  deleteOrder(orderId: number, user: any) {
    this.userService.deleteOrder(orderId).subscribe({
      next: () => {
        user.orders = user.orders.filter((o: any) => o.id !== orderId);
      }
    });
  }

  startAddOrder(userId: number) {
    this.orderAdd[userId] = { bookId: null };
  }

  saveAddOrder(user: any) {
    const bookId = this.orderAdd[user.id].bookId;
    if (bookId != null) {
      this.userService.addOrder(user.id, bookId).subscribe({
        next: order => {
          user.orders.push(order);
          delete this.orderAdd[user.id];
        }
      });
    }
  }

  cancelAddOrder(userId: number) {
    delete this.orderAdd[userId];
  }
}
