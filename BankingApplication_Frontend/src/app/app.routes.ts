import { Routes } from '@angular/router';
import { AuthComponent } from './auth/auth.component';
import { CreateAccountComponent } from './create-account/create-account.component';
import { GuestViewResourcesComponent } from './guest-view-resources/guest-view-resources.component';
import { UserComponent } from './user/user.component';
import { LoanApplicationComponent } from './loan-application/loan-application.component';
import { AdminComponent } from './admin/admin.component';

export const routes: Routes = [
    {
        path:'',
        component:AuthComponent
    },{
        path:'create-account',
        component:CreateAccountComponent
    },
    {
         path: 'view-resources',
         component: GuestViewResourcesComponent
    },{
        path:'user',
        component:UserComponent
    },
    {
        path:'admin',
        component:AdminComponent
    },
    {
        path:'loan-application',
        component:LoanApplicationComponent
    }
    
];
