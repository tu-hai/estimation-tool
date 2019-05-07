
import { CreateProjectComponent } from 'app/projects/create-project/create-project.component';
import { EditProjectComponent } from 'app/projects/edit-project/edit-project.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';

import { Project } from '../../models/project';

import { ProjectService } from '../../shared/service-proxies/service-proxies';
@Component({
    selector: 'app-projects',
    templateUrl: './projects.component.html',
    styleUrls: ['./projects.component.css'],
    animations: [appModuleAnimation()]
})
export class ProjectsComponent implements OnInit {
    @ViewChild('createProjectModal') createProjectModal: CreateProjectComponent;
    @ViewChild('editProjectModal') editProjectModal: EditProjectComponent;

    projectList: Array<any>;
    pages: Array<number>;


    constructor(
        private projectService: ProjectService
    ) { }

    setPage (i) {
        // this.pages = i;
        this.getProjectsbyPages(i);
        this.pages = i;
    }

    ngOnInit() {
        this.getProjectsFromServices();
    }

    getProjectsFromServices(): void {
        this.projectService.getProjects().subscribe(
            data => {
                this.projectList = data['content'];
                this.pages = new Array(data['totalPages']);
            });
    }
    getProjectsbyPages(i: number): void {
        this.projectService.getProjectsbyPage(i).subscribe(
            data => {
                this.projectList = data['content'];
            });
    }

    createProject(): void {
        this.createProjectModal.show();
    }

    editProject(id: number): void {
        this.editProjectModal.show(id);
    }

    deleteProject(id: number): void {
        abp.message.confirm(
            'Delete Project',
            (result: boolean) => {
                if (result) {
                    this.projectService.deleteProject(id)
                        .subscribe(() => {
                            this.projectList = this.projectList.filter(u => u.id !== id);
                            abp.notify.info('Deleted: ');
                        });
                }
            }
        );
    }

    getValue(data) {
        this.projectService.getProjects()
        .subscribe(datas => this.projectList = datas);
    }
    getValueEdit (data) {
        this.projectService.getProjects()
            .subscribe(datas => this.projectList = datas);
    }
}
