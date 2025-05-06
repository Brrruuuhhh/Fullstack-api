import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {
  content?: string;
  users: any[] = [];
  editingUserId: number | null = null;
  newUser: any = { username: '', email: '', password: '', roles: [] };
  editUser: any = { username: '', email: '', password: '', roles: [] };
  isCreating: boolean = false;
  isLoggedIn = false;
  isAdmin = false;

  constructor(private userService: UserService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    const token = this.tokenStorage.getToken();
    this.isLoggedIn = !!token;
    const user = this.tokenStorage.getUser();
    this.isAdmin = user && Array.isArray(user.roles) && user.roles.includes('ROLE_ADMIN');
    if (!this.isLoggedIn || !this.isAdmin) {
      this.content = 'Доступ запрещён. Требуется авторизация администратора.';
      return;
    }
    this.userService.getAdminBoard().subscribe({
      next: data => {
        this.content = data;
      },
      error: err => {
        this.content = JSON.parse(err.error).message;
      }
    });
    this.userService.getUsersWithOrders().subscribe({
      next: data => {
        this.users = data;
      }
    });
  }

  startEditUser(user: any) {
    this.editingUserId = user.id;
    this.editUser = {
      username: user.username,
      email: user.email,
      password: '',
      roles: (user.roles || []).map((r: any) => typeof r === 'string' ? r : r.name || String(r))
    };
  }

  cancelEditUser() {
    this.editingUserId = null;
    this.editUser = { username: '', email: '', password: '', roles: [] };
  }

  saveEditUser(user: any) {
    this.userService.updateUser(user.id, this.editUser).subscribe({
      next: updated => {
        if (updated.roles && Array.isArray(updated.roles)) {
          updated.roles = updated.roles.map((r: any) => typeof r === 'string' ? r : r.name || String(r));
        }
        const idx = this.users.findIndex(u => u.id === user.id);
        if (idx !== -1) this.users[idx] = updated;
        this.cancelEditUser();
      }
    });
  }

  startCreateUser() {
    this.isCreating = true;
    this.newUser = { username: '', email: '', password: '', roles: [] };
  }

  cancelCreateUser() {
    this.isCreating = false;
    this.newUser = { username: '', email: '', password: '', roles: [] };
  }

  createUser() {
    this.userService.createUser(this.newUser).subscribe({
      next: created => {
        this.users.push(created);
        this.cancelCreateUser();
      }
    });
  }

  deleteUser(userId: number) {
    if (confirm('Удалить пользователя?')) {
      this.userService.deleteUser(userId).subscribe({
        next: () => {
          this.users = this.users.filter(u => u.id !== userId);
        }
      });
    }
  }
}
