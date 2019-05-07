
export class Quotation {
      id: number;
      statDate: string;
      endDate: string;
      projectID: {
            id: number;
            name: string;
            dateStrart: string;
            dateEnd: string;
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
            workItemIds: [
                  {
                        id: number;
                        note: string;
                        actualeffort: string;
                        taskname: string;
                        codingEffort: number;
                        indates: number;
                        assumptions: string;
                  }
            ]
            etracEffortIds: string;
            projecTypeId: {
                  id: number;
                  decription: string;
                  technogory: string;
                  name: string;
                  projects: string;
                  nfrs: string;

            }
      }
      customerId: {
           id: number;
            name: string
            country: string;
            company: string;
            tel: string;
            email: string;
            billaddress: string;
            skypeid: string;
      }
}

export class CreateQuotation {
      projectID: number;
      customerID: number;
}
