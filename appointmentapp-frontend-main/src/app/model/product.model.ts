export interface Category {
  id?: number;
  name: string;
}

export interface Family {
  id?: number;
  name: string;
}

export interface Laboratory {
  id?: number;
  name: string;
}

export interface Product {
  id?: number;
  name: string;
  barcode?: string;
  price: number;
  stock: number;
  description?: string;
  category?: Category;
  family?: Family;
  laboratory?: Laboratory;
  createdAt?: string;
  updatedAt?: string;
}
