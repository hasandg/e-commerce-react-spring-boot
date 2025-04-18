import axios from 'axios';
import { Category, ApiResponse } from '../types';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:9090/api';

// Mock categories for development
const MOCK_CATEGORIES: Category[] = [
  { id: "1", name: 'Electronics', description: 'Electronic gadgets and devices' },
  { id: "2", name: 'Clothing', description: 'Fashion and apparel' },
  { id: "3", name: 'Home & Kitchen', description: 'Home appliances and kitchen essentials' },
  { id: "4", name: 'Books', description: 'Books, magazines and literature' },
  { id: "5", name: 'Sports', description: 'Sports equipment and accessories' }
];

/**
 * Get all categories
 * @returns Promise with category data
 */
export const getAllCategories = async (): Promise<Category[]> => {
  try {
    // For development, return mock data
    // In production, this would call the actual API
    // const response = await axios.get<ApiResponse<Category[]>>(`${API_URL}/categories`);
    // return response.data.data;
    
    return MOCK_CATEGORIES;
  } catch (error) {
    console.error('Error fetching categories:', error);
    throw error;
  }
}; 