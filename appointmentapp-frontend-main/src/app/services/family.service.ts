import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SimpleEntity } from '../models/simple.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FamilyService {
  private baseUrl = `${environment.apiUrl}/api/families`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<SimpleEntity[]> {
    return this.http.get<SimpleEntity[]>(this.baseUrl)
      .pipe(catchError(err => this.handleError(err, 'Error al obtener familias')));
  }

  getById(id: number): Observable<SimpleEntity> {
    return this.http.get<SimpleEntity>(`${this.baseUrl}/${id}`)
      .pipe(catchError(err => this.handleError(err, `Error al obtener la familia ${id}`)));
  }

  create(entity: SimpleEntity): Observable<SimpleEntity> {
    if (!entity.name) return throwError(() => new Error('El nombre de la familia es obligatorio.'));
    return this.http.post<SimpleEntity>(this.baseUrl, entity)
      .pipe(catchError(err => this.handleError(err, 'Error al crear familia')));
  }

  update(id: number, entity: SimpleEntity): Observable<SimpleEntity> {
    if (!entity.name) return throwError(() => new Error('El nombre de la familia es obligatorio.'));
    return this.http.put<SimpleEntity>(`${this.baseUrl}/${id}`, entity)
      .pipe(catchError(err => this.handleError(err, `Error al actualizar la familia ${id}`)));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(err => this.handleError(err, `Error al eliminar la familia ${id}`)));
  }

  private handleError(err: any, userMessage: string) {
    console.error(userMessage, err);
    const server = err?.error?.message || err?.message || '';
    return throwError(() => new Error(server ? `${userMessage}: ${server}` : userMessage));
  }
}
