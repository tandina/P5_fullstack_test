import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { of, throwError } from 'rxjs';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockRouter: Partial<Router>;
  let mockSessionService: Partial<SessionService>;
  let mockAuthService: Partial<AuthService>;

  const mockSessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: '',
    firstName: '',
    lastName: '',
    admin: true,
  };

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
    };
    mockAuthService = {
      login: jest.fn().mockReturnValue(of(mockSessionInformation)),
    };
    mockSessionService = {
      logIn: jest.fn().mockReturnValue(of(mockSessionInformation)),
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('is created', () => {
    expect(component).toBeTruthy();
  });

  it('should on submit call AuthService.login ', () => {
    const loginSpy = jest.spyOn(mockAuthService, 'login');
    component.submit();
    expect(loginSpy).toHaveBeenCalled();
  });

  it('should on submit call sessionService.logIn', () => {
    const logInSpy = jest.spyOn(mockSessionService, 'logIn');
    component.submit();
    expect(logInSpy).toHaveBeenCalled();
  });

  it('should go to /sessions on submit', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component.submit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should be an error if login fails', () => {
    const error = new Error();
    jest.spyOn(mockAuthService, 'login').mockReturnValueOnce(throwError(error));
    component.submit();
    expect(component.onError).toBeTruthy();
  });
});