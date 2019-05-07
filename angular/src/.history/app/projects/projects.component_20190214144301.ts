
import { CreateProjectComponent } from 'app/projects/create-project/create-project.component';
import { EditProjectComponent } from 'app/projects/edit-project/edit-project.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { PaginationModule } from 'ngx-bootstrap';

import { Project } from '../../models/project';

import { ProjectService } from '../../shared/service-proxies/service-proxies';
import { Observable } from 'rxjs';
import { map, flatMap } from 'rxjs/operators';
@Component({
    selector: 'app-projects',
    templateUrl: './projects.component.html',
    styleUrls: ['./projects.component.css'],
    animations: [appModuleAnimation()]
})
export class ProjectsComponent implements OnInit {
    @ViewChild('createProjectModal') createProjectModal: CreateProjectComponent;
    @ViewChild('editProjectModal') editProjectModal: EditProjectComponent;

    projectList: Observable<any>;
    pages: Array<number>;
    totalItem: number;
    p = 1;
    itemsPerPage = 5;
    rotate = true;

    constructor(
        private projectService: ProjectService
    ) { }

    setPage (i) {
        this.p = i;
        this.getProjectsbyPages(this.p - 1);
    }

    ngOnInit() {
        this.setPage(this.p);
    }

    getProjectsbyPages(i: number): void {
        this.projectList =  this.projectService.getProjectsbyPage(i).pipe(
            map(data => {
                this.totalItem = data.totalElements;
                return data.content;
            })
        );
    }

    createProject(): void {
        this.createProjectModal.show();
    }

    editProject(id: number): void {
        this.editProjectModal.show(id);
    }

    // deleteProject(id: number, name: string): void {
    //     abp.message.confirm(
    //         'Delete Project \'' + name + '\'?',
    //         (result: boolean) => {
    //             if (result) {
    //                 this.projectService.deleteProject(id)
    //                     .subscribe(() => {
    //                         this.projectList = this.projectList.filter(u => u.id !== id);
    //                         abp.notify.info('Deleted');
    //                         this.getValue();
    //                     });
    //             }
    //         }
    //     );
    // }



    // // renew data affter edit or create
    // getValue() {
    //     this.projectService.getProjects()
    //     .subscribe(data => {
    //         this.projectList = data['content'];
    //         this.pages = new Array(data['totalPages']);
    //         this.totalItem = data['totalElements'];
    //     });
    // }
}
