
export interface Quotation {
      id: number;
      projectId: ProjectId;
      customerId: CustomerId;
      total: number;
    }

    interface CustomerId {
      id: number;
      name: string;
      country: string;
      company: string;
      tel: string;
      email: string;
      billaddress: string;
      skypeid: string;
    }

    interface ProjectId {
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
      note: string;
      actualeffort: number;
      taskname: string;
      codingEffort: number;
      indates: number;
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


export interface RootObject {
  id: number;
  projectId: ProjectId;
  customerId: CustomerId;
  total: number;
}

interface CustomerId {
  id: number;
  name: string;
  country: string;
  company: string;
  tel: string;
  email: string;
  billaddress: string;
  skypeid: string;
}

interface ProjectId {
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
  note: string;
  actualeffort: number;
  taskname: string;
  codingEffort: number;
  indates: number;
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
export interface CreateQuotation {
      projectID: number;
      customerID: number;
      listworkItem: ListworkItem[];
      effortList: any[];
      assumptionList: AssumptionList[];
    }
    interface ListworkItem {
      taskname: string;
      codingEffort: number;
    }
    interface AssumptionList {
      content: string;
      note: string;
    }
