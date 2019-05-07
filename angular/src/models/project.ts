export interface Project {
  content: Content[];
  pageable: Pageable;
  totalElements: number;
  last: boolean;
  totalPages: number;
  size: number;
  number: number;
  sort: Sort;
  first: boolean;
  numberOfElements: number;
}

interface Pageable {
  sort: Sort;
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  unpaged: boolean;
}

interface Sort {
  unsorted: boolean;
  sorted: boolean;
}

interface Content {
  id: number;
  name: string;
  dateStrart: string;
  dateEnd: string;
  sizeId: SizeId;
  techId: TechId;
  workItemIds: WorkItemId[];
  etracEffortIds: EtracEffortId[];
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

interface EtracEffortId {
  id: number;
  name: string;
  decription: string;
  percentEfforts: number;
}

interface WorkItemId {
  id: number;
  note?: any;
  actualeffort?: any;
  taskname: string;
  codingEffort: number;
  indates?: any;
  assumptions?: any;
}

interface TechId {
  id: number;
  name: string;
  description: string;
  techContact: string;
}

interface SizeId {
  id: number;
  nameSize: string;
}
