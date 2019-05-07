
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

    projectList: Project[];


    constructor(
        private projectService: ProjectService
    ) { }

    ngOnInit() {
        this.getProjectsFromServices();
    }

    getProjectsFromServices(): void {
        this.projectService.getProjects()
        .subscribe(data => this.projectList = data);
    }

    createProject(): void {
        this.createProjectModal.show();
    }

    editProject(project: Project): void {
        abp.message.confirm(
            'Update new infomation for: \'' + project.name + ' co ID: ' + project.id + '\'?',
            (result: boolean) => {
                if (result === true) {
                    this.editProjectModal.show(project.id);
                }
            }
        );
    }

    deleteProject(projectId: Project): void {
        abp.message.confirm(
            'Delete Project \'' + projectId.name + '\'?',
            (result: boolean) => {
                if (result) {
                    this.projectService.deleteProject(projectId.id)
                        .subscribe(() => {
                            this.projectList = this.projectList.filter(u => u.id !== projectId.id);
                            abp.notify.info('Deleted: ' + projectId.name);
                        });
                }
            }
        );
        }

    getValue(data) {
        this.projectList.push(data);
    }

}
