export class Project {
    id: number;
    name: string;
    dateStrart: Date;
    dateEnd: Date;
    sizeId: {
        id: number;
        nameSize: string;
    }
    techId: {
        id: number;
        name: string;
        description: string;
        techContact: string;
    }
    projecTypeId: {
        id: number;
        decription: string;
        technogory: string;
    }
}
