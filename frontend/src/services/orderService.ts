import axios from 'axios';
import { Order, Address, ApiResponse, PaginatedResponse } from '../types';

const API_URL = '/api/orders';

// Get user's orders with pagination
export const getOrders = async (
  page = 0,
  size = 10
): Promise<PaginatedResponse<Order>> => {
  try {
    const response = await axios.get<PaginatedResponse<Order>>(
      `${API_URL}?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Get order by ID
export const getOrderById = async (id: string): Promise<Order> => {
  try {
    const response = await axios.get<ApiResponse<Order>>(`${API_URL}/${id}`);
    return response.data.data;
  } catch (error) {
    throw error;
  }
};

// Create a new order
export const createOrder = async (
  shippingAddress: Address,
  billingAddress: Address
): Promise<Order> => {
  try {
    const response = await axios.post<ApiResponse<Order>>(API_URL, {
      shippingAddress,
      billingAddress
    });
    return response.data.data;
  } catch (error) {
    throw error;
  }
};

// Cancel an order
export const cancelOrder = async (id: string): Promise<Order> => {
  try {
    const response = await axios.post<ApiResponse<Order>>(
      `${API_URL}/${id}/cancel`
    );
    return response.data.data;
  } catch (error) {
    throw error;
  }
}; 