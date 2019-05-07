
export interface Customer  {
    id: number;
    name: string ;
    country: string ;
    company: string;
    tel: string;
    email: string;
    billaddress: string;
    skypeid: string;
}
export interface CustomerbyPage {
    content: Content[];
    pageable: Pageable;
    totalElements: number;
    totalPages: number;
    last: boolean;
    size: number;
    number: number;
    sort: Sort;
    numberOfElements: number;
    first: boolean;
  }

  interface Pageable {
    sort: Sort;
    offset: number;
    pageSize: number;
    pageNumber: number;
    paged: boolean;
    unpaged: boolean;
  }

  interface Sort {
    sorted: boolean;
    unsorted: boolean;
  }

  interface Content {
    id: number;
    name: string;
    country: string;
    company: string;
    tel: string;
    email: string;
    billaddress: string;
    skypeid: string;
  }
