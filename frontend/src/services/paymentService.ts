import axios from 'axios';
import { Payment, PaymentMethod, ApiResponse } from '../types';

const API_URL = '/api/payments';

// Process payment for an order
export const processPayment = async (
  orderId: string,
  method: PaymentMethod,
  paymentDetails: any // This would vary based on payment method
): Promise<Payment> => {
  try {
    const response = await axios.post<ApiResponse<Payment>>(
      API_URL,
      {
        orderId,
        method,
        paymentDetails
      }
    );
    return response.data.data;
  } catch (error) {
    throw error;
  }
};

// Get payment by ID
export const getPaymentById = async (id: string): Promise<Payment> => {
  try {
    const response = await axios.get<ApiResponse<Payment>>(`${API_URL}/${id}`);
    return response.data.data;
  } catch (error) {
    throw error;
  }
};

// Get payment by order ID
export const getPaymentByOrderId = async (orderId: string): Promise<Payment> => {
  try {
    const response = await axios.get<ApiResponse<Payment>>(
      `${API_URL}/order/${orderId}`
    );
    return response.data.data;
  } catch (error) {
    throw error;
  }
};

// Request a refund
export const requestRefund = async (
  paymentId: string,
  reason: string
): Promise<Payment> => {
  try {
    const response = await axios.post<ApiResponse<Payment>>(
      `${API_URL}/${paymentId}/refund`,
      { reason }
    );
    return response.data.data;
  } catch (error) {
    throw error;
  }
}; 