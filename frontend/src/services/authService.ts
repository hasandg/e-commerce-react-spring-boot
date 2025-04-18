import axios from 'axios';
import { LoginRequest, RegisterRequest, AuthResponse } from '@/types';

// API URL for backend services
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:9090/api';

// Mock user for development fallback
const MOCK_USER = {
  id: 1,
  username: 'testuser',
  email: 'test@example.com',
  firstName: 'Test',
  lastName: 'User',
  role: 'USER',
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString()
};

// Mock token for development fallback
const MOCK_TOKEN = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTUxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

// Flag to use mock data if backend is not available
const USE_MOCK_DATA = false;

/**
 * Register a new user
 * @param data User registration data
 * @returns Promise with auth response containing token and user data
 */
export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
  try {
    if (USE_MOCK_DATA) {
      console.log('Using mock register data');
      // Mock successful registration
      const mockResponse: AuthResponse = {
        token: MOCK_TOKEN,
        user: {
          ...MOCK_USER,
          username: data.username,
          email: data.email,
          firstName: data.firstName || MOCK_USER.firstName,
          lastName: data.lastName || MOCK_USER.lastName
        }
      };
      
      // Store token in local storage
      localStorage.setItem('token', mockResponse.token);
      localStorage.setItem('user', JSON.stringify(mockResponse.user));
      
      return mockResponse;
    }
    
    // Real API call
    const response = await axios.post(`${API_URL}/auth/register`, data);
    const authResponse = response.data.data as AuthResponse;
    
    // Store token in local storage
    localStorage.setItem('token', authResponse.token);
    localStorage.setItem('user', JSON.stringify(authResponse.user));
    
    return authResponse;
  } catch (error) {
    console.error('Registration error:', error);
    throw error;
  }
};

/**
 * Log in an existing user
 * @param credentials User login credentials
 * @returns Promise with auth response containing token and user data
 */
export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  try {
    if (USE_MOCK_DATA) {
      console.log('Using mock login data');
      // Simple validation
      if (credentials.username !== 'testuser' && credentials.username !== 'admin') {
        throw new Error('Invalid credentials');
      }
      
      // Mock successful login
      const mockResponse: AuthResponse = {
        token: MOCK_TOKEN,
        user: {
          ...MOCK_USER,
          username: credentials.username,
          role: credentials.username === 'admin' ? 'ADMIN' : 'USER'
        }
      };
      
      // Store token in local storage
      localStorage.setItem('token', mockResponse.token);
      localStorage.setItem('user', JSON.stringify(mockResponse.user));
      
      // Set authorization header for future requests
      axios.defaults.headers.common['Authorization'] = `Bearer ${mockResponse.token}`;
      
      return mockResponse;
    }
    
    // Real API call
    const response = await axios.post(`${API_URL}/auth/login`, credentials);
    const authResponse = response.data.data as AuthResponse;
    
    // Store token in local storage
    localStorage.setItem('token', authResponse.token);
    localStorage.setItem('user', JSON.stringify(authResponse.user));
    
    // Set authorization header for future requests
    axios.defaults.headers.common['Authorization'] = `Bearer ${authResponse.token}`;
    
    return authResponse;
  } catch (error) {
    console.error('Login error:', error);
    throw error;
  }
};

/**
 * Log in an existing user (alias for login function)
 * @param credentials User login credentials
 * @returns Promise with auth response containing token and user data
 */
export const loginUser = login;

/**
 * Log out the current user
 */
export const logout = (): void => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  delete axios.defaults.headers.common['Authorization'];
};

/**
 * Log out the current user (alias for logout function)
 */
export const logoutUser = logout;

/**
 * Get the current logged-in user from local storage
 * @returns User object or null if not logged in
 */
export const getCurrentUser = () => {
  const userJson = localStorage.getItem('user');
  if (userJson) {
    return JSON.parse(userJson);
  }
  return null;
};

/**
 * Check if a user is logged in
 * @returns boolean indicating if user is logged in
 */
export const isAuthenticated = (): boolean => {
  return !!localStorage.getItem('token');
};

/**
 * Initialize the auth service
 * Sets up axios interceptors and restores session if token exists
 */
export const initAuthService = (): void => {
  // Set up request interceptor to add the auth token to all requests
  axios.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
  
  // Set up response interceptor to handle auth errors
  axios.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && error.response.status === 401) {
        // Auto logout if 401 response returned from api
        logout();
        window.location.href = '/login';
      }
      return Promise.reject(error);
    }
  );
};

// Initialize the auth service on module load
initAuthService(); 