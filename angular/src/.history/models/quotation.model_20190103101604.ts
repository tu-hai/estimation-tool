
export class Quotation {
      id: number;
      statDate: string;
      endDate: string;
      customerId: {
            id: number;
            name: string;
            country: string;
      }
      projectID: {
            id: number;
            name: string;
            dateStrart: string;
            dateEnd: string;
            sizeId: string;
            techId: string;
            workItemIds: [
                  {
                        id: number;
                        note: string;
                        actualeffort: string;
                        taskname: string;
                        codingEffort: number;
                        indates: number;
                  }
            ]
            etracEffortIds: string;
            projecTypeId: string;
      }
}
