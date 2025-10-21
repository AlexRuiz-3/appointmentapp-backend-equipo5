import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SimpleEntity } from '../models/simple.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LaboratoryService {
  private baseUrl = `${environment.apiUrl}/api/laboratories`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<SimpleEntity[]> {
    return this.http.get<SimpleEntity[]>(this.baseUrl)
      .pipe(catchError(err => this.handleError(err, 'Error al obtener laboratorios')));
  }

  getById(id: number): Observable<SimpleEntity> {
    return this.http.get<SimpleEntity>(`${this.baseUrl}/${id}`)
      .pipe(catchError(err => this.handleError(err, `Error al obtener el laboratorio ${id}`)));
  }

  create(entity: SimpleEntity): Observable<SimpleEntity> {
    if (!entity.name) return throwError(() => new Error('El nombre del laboratorio es obligatorio.'));
    return this.http.post<SimpleEntity>(this.baseUrl, entity)
      .pipe(catchError(err => this.handleError(err, 'Error al crear laboratorio')));
  }

  update(id: number, entity: SimpleEntity): Observable<SimpleEntity> {
    if (!entity.name) return throwError(() => new Error('El nombre del laboratorio es obligatorio.'));
    return this.http.put<SimpleEntity>(`${this.baseUrl}/${id}`, entity)
      .pipe(catchError(err => this.handleError(err, `Error al actualizar el laboratorio ${id}`)));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(err => this.handleError(err, `Error al eliminar el laboratorio ${id}`)));
  }

  private handleError(err: any, userMessage: string) {
    console.error(userMessage, err);
    const server = err?.error?.message || err?.message || '';
    return throwError(() => new Error(server ? `${userMessage}: ${server}` : userMessage));
  }
}
