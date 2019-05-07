export interface Project {
  id: number;
  name: string;
  dateStrart: string;
  dateEnd: string;
  sizeId: SizeId;
  techId?: any;
  workItemIds: any[];
  etracEffortIds: any[];
  projecTypeId: ProjecTypeId;
}

interface ProjecTypeId {
  id: number;
  decription: string;
  technogory: string;
  name: string;
  projects?: any;
  nfrs?: any;
}

interface SizeId {
  id: number;
  nameSize: string;
}


export interface EditProject {
  id: string;
  name: string;
  category?: any;
  size: string;
  techName: string;
  techDescripson: string;
  techContact: string;
  startDate: string;
  endDate: string;
}
