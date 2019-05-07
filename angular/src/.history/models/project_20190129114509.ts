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
