import axios from 'axios';
import { Cart, ApiResponse } from '../types';

// Use mock data in development, real API in production
const USE_MOCK_DATA = false; // Set to true to use mock data if backend is not available
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:9090/api';

// Mock cart data for development
const MOCK_CART: Cart = {
  id: "cart-123",
  userId: "user-456",
  items: [
    {
      id: "item-1",
      productId: "1",
      productName: "Smartphone XYZ",
      quantity: 1,
      price: 699.99,
      imageUrl: "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500&auto=format&fit=crop&q=60"
    },
    {
      id: "item-2",
      productId: "3",
      productName: "Wireless Headphones",
      quantity: 2,
      price: 159.99,
      imageUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60"
    }
  ],
  totalItems: 3,
  totalPrice: 1019.97,
  createdAt: new Date().toISOString(),
  updatedAt: new Date().toISOString()
};

// Get current user's cart
export const getCart = async (): Promise<Cart> => {
  try {
    if (USE_MOCK_DATA) {
      console.log("Using mock cart data");
      return MOCK_CART;
    }
    
    const response = await axios.get<ApiResponse<Cart>>(`${API_URL}/cart`);
    return response.data.data;
  } catch (error) {
    console.error("Error fetching cart:", error);
    return MOCK_CART; // Fallback to mock data on error
  }
};

// Add item to cart
export const addToCart = async (
  productId: string,
  quantity: number
): Promise<Cart> => {
  try {
    if (USE_MOCK_DATA) {
      console.log(`Added to cart: ${productId}, quantity: ${quantity}`);
      
      // Create a copy of the mock cart
      const updatedCart = { ...MOCK_CART };
      
      // Check if the product is already in the cart
      const existingItemIndex = updatedCart.items.findIndex(
        item => item.productId === productId
      );
      
      if (existingItemIndex >= 0) {
        // Update existing item quantity
        updatedCart.items[existingItemIndex].quantity += quantity;
      } else {
        // Add new item to cart
        updatedCart.items.push({
          id: `item-${updatedCart.items.length + 1}`,
          productId: productId,
          productName: `Product ${productId}`,
          quantity: quantity,
          price: 99.99,
          imageUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60"
        });
      }
      
      // Update totals
      updatedCart.totalItems = updatedCart.items.reduce(
        (total, item) => total + item.quantity, 0
      );
      updatedCart.totalPrice = updatedCart.items.reduce(
        (total, item) => total + (item.price * item.quantity), 0
      );
      updatedCart.updatedAt = new Date().toISOString();
      
      // Update the global mock cart
      Object.assign(MOCK_CART, updatedCart);
      
      return updatedCart;
    }
    
    const response = await axios.post<ApiResponse<Cart>>(
      `${API_URL}/cart/items`,
      { productId, quantity }
    );
    return response.data.data;
  } catch (error) {
    console.error('Error adding to cart:', error);
    throw error;
  }
};

// Update cart item quantity
export const updateCartItem = async (
  itemId: string,
  quantity: number
): Promise<Cart> => {
  try {
    if (USE_MOCK_DATA) {
      console.log(`Updated cart item: ${itemId}, quantity: ${quantity}`);
      
      // Create a copy of the mock cart
      const updatedCart = { ...MOCK_CART };
      
      // Find the item to update
      const itemIndex = updatedCart.items.findIndex(item => item.id === itemId);
      
      if (itemIndex >= 0) {
        // Update item quantity
        updatedCart.items[itemIndex].quantity = quantity;
        
        // Update totals
        updatedCart.totalItems = updatedCart.items.reduce(
          (total, item) => total + item.quantity, 0
        );
        updatedCart.totalPrice = updatedCart.items.reduce(
          (total, item) => total + (item.price * item.quantity), 0
        );
        updatedCart.updatedAt = new Date().toISOString();
        
        // Update the global mock cart
        Object.assign(MOCK_CART, updatedCart);
      }
      
      return updatedCart;
    }
    
    const response = await axios.put<ApiResponse<Cart>>(
      `${API_URL}/cart/items/${itemId}`,
      { quantity }
    );
    return response.data.data;
  } catch (error) {
    console.error('Error updating cart item:', error);
    throw error;
  }
};

// Remove item from cart
export const removeFromCart = async (itemId: string): Promise<Cart> => {
  try {
    if (USE_MOCK_DATA) {
      console.log(`Removed from cart: ${itemId}`);
      
      // Create a copy of the mock cart
      const updatedCart = { ...MOCK_CART };
      
      // Remove the item
      updatedCart.items = updatedCart.items.filter(item => item.id !== itemId);
      
      // Update totals
      updatedCart.totalItems = updatedCart.items.reduce(
        (total, item) => total + item.quantity, 0
      );
      updatedCart.totalPrice = updatedCart.items.reduce(
        (total, item) => total + (item.price * item.quantity), 0
      );
      updatedCart.updatedAt = new Date().toISOString();
      
      // Update the global mock cart
      Object.assign(MOCK_CART, updatedCart);
      
      return updatedCart;
    }
    
    const response = await axios.delete<ApiResponse<Cart>>(
      `${API_URL}/cart/items/${itemId}`
    );
    return response.data.data;
  } catch (error) {
    console.error('Error removing from cart:', error);
    throw error;
  }
};

// Clear cart
export const clearCart = async (): Promise<void> => {
  try {
    if (USE_MOCK_DATA) {
      console.log("Cleared cart");
      
      // Reset the cart
      MOCK_CART.items = [];
      MOCK_CART.totalItems = 0;
      MOCK_CART.totalPrice = 0;
      MOCK_CART.updatedAt = new Date().toISOString();
      
      return;
    }
    
    await axios.delete(`${API_URL}/cart`);
  } catch (error) {
    console.error('Error clearing cart:', error);
    throw error;
  }
};

// Get cart item count (for header display)
export const getCartItemCount = async (): Promise<number> => {
  try {
    if (USE_MOCK_DATA) {
      return MOCK_CART.totalItems;
    }
    
    const response = await axios.get<ApiResponse<Cart>>(`${API_URL}/cart`);
    return response.data.data.totalItems;
  } catch (error) {
    console.error('Error getting cart item count:', error);
    // If there's an error (like no cart), return 0
    return MOCK_CART.totalItems;
  }
}; 