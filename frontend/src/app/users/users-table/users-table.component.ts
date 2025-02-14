import {AfterViewInit, Component, EventEmitter, Inject, OnInit, Output, ViewChild} from '@angular/core';
import { FullUserDto } from 'src/services/dtos/full-user.dto';
import { UserService } from 'src/services/user.service';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatTable, MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

export const sortUsers = (users: FullUserDto[]) => {
  console.log("Sorting users")
  users.sort((a, b) => {
    if (a.profile.lastName.toLowerCase() < b.profile.lastName.toLowerCase()) return -1;
    if (a.profile.lastName.toLowerCase() > b.profile.lastName.toLowerCase()) return 1;

    if (a.profile.firstName.toLowerCase() < b.profile.firstName.toLowerCase()) return -1;
    if (a.profile.firstName.toLowerCase() > b.profile.firstName.toLowerCase()) return 1;
    return 0;
  });
  const sortCopy = [...users]
  console.log("Sorted users", sortCopy);
}

@Component({
  selector: 'app-users-table',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule
  ],
  templateUrl: './users-table.component.html',
  styleUrl: './users-table.component.css'
})
export class UsersTableComponent implements OnInit, AfterViewInit {
  // allCompanyUsers: FullUserDto[] = [];
  @Output()
  shareUsers = new EventEmitter<MatTableDataSource<FullUserDto>>();


  private subscription: Subscription = new Subscription();
  displayedColumns = ['name', 'email', 'active', 'admin', 'status'];
  dataSource: MatTableDataSource<FullUserDto> = new MatTableDataSource();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatTable) table!: MatTable<FullUserDto>;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    const usersSubscription = this.userService.getCompanyUsers(this.userService.selectedCompany).subscribe({
      next: (data: FullUserDto[]) => {
        // this.allCompanyUsers = data
        sortUsers(data);
        console.log("Setting datasource")
        this.dataSource.data = data
        this.shareUsers.emit(this.dataSource)
      },
      error: (error) => {
        console.log('Error fetching all users from a company', error)
      }
    })
    this.subscription.add(usersSubscription)
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
    console.log('table data source', this.table.dataSource)
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}


