import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Product } from '../models/product.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = `${environment.apiUrl}/api/products`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseUrl)
      .pipe(
        catchError(err => this.handleError(err, 'Error al obtener la lista de productos'))
      );
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`)
      .pipe(
        catchError(err => this.handleError(err, `Error al obtener el producto con id ${id}`))
      );
  }

  create(product: Product): Observable<Product> {
    // Validaciones de frontend (ejemplo mínimo)
    if (!product.name || product.price == null || product.stock == null) {
      return throwError(() => new Error('Campos obligatorios incompletos: nombre, precio y stock.'));
    }
    if (product.price < 0) {
      return throwError(() => new Error('El precio no puede ser negativo.'));
    }
    if (product.stock < 0) {
      return throwError(() => new Error('El stock no puede ser negativo.'));
    }

    return this.http.post<Product>(this.baseUrl, product)
      .pipe(
        catchError(err => this.handleError(err, 'Error al crear el producto'))
      );
  }

  update(id: number, product: Product): Observable<Product> {
    if (product.price != null && product.price < 0) {
      return throwError(() => new Error('El precio no puede ser negativo.'));
    }
    if (product.stock != null && product.stock < 0) {
      return throwError(() => new Error('El stock no puede ser negativo.'));
    }

    return this.http.put<Product>(`${this.baseUrl}/${id}`, product)
      .pipe(
        catchError(err => this.handleError(err, `Error al actualizar el producto con id ${id}`))
      );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`)
      .pipe(
        catchError(err => this.handleError(err, `Error al eliminar el producto con id ${id}`))
      );
  }

  // Ejemplo: búsqueda con params (paginación / filtro)
  search(paramsObj: { page?: number; size?: number; q?: string } = {}): Observable<{ items: Product[], total?: number }> {
    let params = new HttpParams();
    if (paramsObj.page != null) params = params.set('page', paramsObj.page.toString());
    if (paramsObj.size != null) params = params.set('size', paramsObj.size.toString());
    if (paramsObj.q) params = params.set('q', paramsObj.q);

    return this.http.get<{ items: Product[], total?: number }>(this.baseUrl, { params })
      .pipe(
        catchError(err => this.handleError(err, 'Error al buscar productos'))
      );
  }

  private handleError(error: any, userMessage: string) {
    // Puedes inspeccionar error.status, error.error, etc.
    console.error(userMessage, error);
    const serverMessage = error?.error?.message || error?.message || '';
    const message = serverMessage ? `${userMessage}: ${serverMessage}` : userMessage;
    return throwError(() => new Error(message));
  }
}
