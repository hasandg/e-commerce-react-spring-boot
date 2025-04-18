import axios from 'axios';
import { Product, Category, Review, ApiResponse, PaginatedResponse, ProductResponse } from '../types';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:9090/api';

// Flag to use mock data if backend is not available
const USE_MOCK_DATA = false;

// Mock data for development or fallback
const MOCK_CATEGORIES: Category[] = [
  { id: 1, name: 'Electronics', description: 'Electronic gadgets and devices' },
  { id: 2, name: 'Clothing', description: 'Fashion and apparel' },
  { id: 3, name: 'Home & Kitchen', description: 'Home appliances and kitchen essentials' },
  { id: 4, name: 'Books', description: 'Books, magazines and literature' },
  { id: 5, name: 'Sports', description: 'Sports equipment and accessories' }
];

const MOCK_PRODUCTS: Product[] = [
  {
    id: 1,
    name: 'Smartphone XYZ',
    description: 'Latest smartphone with advanced features',
    price: 699.99,
    stockQuantity: 50,
    stock: 50,
    imageUrls: [
      'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1565849904461-04a58ad377e0?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[0],
    averageRating: 4.5,
    reviewCount: 120,
    createdAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 2,
    name: 'Laptop Ultra',
    description: 'Powerful laptop for work and gaming',
    price: 1299.99,
    stockQuantity: 30,
    stock: 30,
    imageUrls: [
      'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[0],
    averageRating: 4.8,
    reviewCount: 85,
    createdAt: new Date(Date.now() - 60 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 3,
    name: 'Wireless Headphones',
    description: 'Noise-cancelling wireless headphones',
    price: 159.99,
    stockQuantity: 100,
    stock: 100,
    imageUrls: [
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1599669454699-248893623440?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1578319439584-104c94d37305?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[0],
    averageRating: 4.2,
    reviewCount: 210,
    createdAt: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 4,
    name: 'Men\'s Casual Shirt',
    description: 'Comfortable cotton casual shirt',
    price: 39.99,
    stockQuantity: 200,
    stock: 200,
    imageUrls: [
      'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1598032895397-b9472444bf93?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[1],
    averageRating: 4.0,
    reviewCount: 95,
    createdAt: new Date(Date.now() - 45 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 5,
    name: 'Women\'s Running Shoes',
    description: 'Comfortable shoes for running and jogging',
    price: 89.99,
    stockQuantity: 150,
    stock: 150,
    imageUrls: [
      'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1584735175315-9d5df23be620?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[4],
    averageRating: 4.7,
    reviewCount: 180,
    createdAt: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 6,
    name: 'Coffee Maker',
    description: 'Automatic coffee maker with timer',
    price: 79.99,
    stockQuantity: 60,
    stock: 60,
    imageUrls: [
      'https://images.unsplash.com/photo-1608354580875-30fd4224c3e6?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1610889556528-9a770e32642f?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1572119865084-43c285814d63?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1608354580875-30fd4224c3e6?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[2],
    averageRating: 4.3,
    reviewCount: 75,
    createdAt: new Date(Date.now() - 20 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 7,
    name: 'Bestselling Novel',
    description: 'Latest bestselling fiction novel',
    price: 24.99,
    stockQuantity: 300,
    stock: 300,
    imageUrls: [
      'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[3],
    averageRating: 4.9,
    reviewCount: 320,
    createdAt: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 8,
    name: 'Smart Watch',
    description: 'Fitness and health tracking smartwatch',
    price: 199.99,
    stockQuantity: 80,
    stock: 80,
    imageUrls: [
      'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&auto=format&fit=crop&q=60',
      'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=500&auto=format&fit=crop&q=60'
    ],
    imageUrl: 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=500&auto=format&fit=crop&q=60',
    category: MOCK_CATEGORIES[0],
    averageRating: 4.4,
    reviewCount: 150,
    createdAt: new Date(Date.now() - 25 * 24 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date().toISOString()
  }
];

/**
 * Get products with pagination
 * @param page Page number (0-based)
 * @param size Page size
 * @param sort Optional sort parameter
 * @param categoryId Optional category ID filter
 * @param search Optional search query
 * @returns Promise with paginated product response
 */
export const getProducts = async (
  page: number = 0, 
  size: number = 10,
  sort: string = 'createdAt,desc',
  categoryId?: number,
  search?: string
): Promise<ProductResponse> => {
  try {
    if (USE_MOCK_DATA) {
      console.log('Using mock product data');
      // Filter products by category if specified
      let filteredProducts = [...MOCK_PRODUCTS];
      
      if (categoryId) {
        filteredProducts = filteredProducts.filter(product => {
          if (typeof product.category === 'object' && product.category !== null) {
            return product.category.id === categoryId;
          }
          return false;
        });
      }
      
      // Filter products by search term if specified
      if (search) {
        const searchLower = search.toLowerCase();
        filteredProducts = filteredProducts.filter(product => 
          product.name.toLowerCase().includes(searchLower) || 
          product.description.toLowerCase().includes(searchLower)
        );
      }
      
      // Sort products
      if (sort) {
        const [field, direction] = sort.split(',');
        filteredProducts.sort((a, b) => {
          let comparison = 0;
          
          if (field === 'price') {
            comparison = a.price - b.price;
          } else if (field === 'name') {
            comparison = a.name.localeCompare(b.name);
          } else if (field === 'createdAt') {
            const aTime = a.createdAt ? new Date(a.createdAt).getTime() : 0;
            const bTime = b.createdAt ? new Date(b.createdAt).getTime() : 0;
            comparison = aTime - bTime;
          }
          
          return direction === 'asc' ? comparison : -comparison;
        });
      }
      
      // Paginate products
      const totalElements = filteredProducts.length;
      const totalPages = Math.ceil(totalElements / size);
      const start = page * size;
      const end = start + size;
      const paginatedProducts = filteredProducts.slice(start, end);
      
      // Create response object
      const mockResponse: ProductResponse = {
        content: paginatedProducts,
        pageable: {
          pageNumber: page,
          pageSize: size,
          sort: {
            sorted: true,
            unsorted: false,
            empty: false
          },
          offset: page * size,
          paged: true,
          unpaged: false
        },
        last: page >= totalPages - 1,
        totalElements,
        totalPages,
        size,
        number: page,
        sort: {
          sorted: true,
          unsorted: false,
          empty: false
        },
        first: page === 0,
        numberOfElements: paginatedProducts.length,
        empty: paginatedProducts.length === 0
      };
      
      return mockResponse;
    }
    
    // Use the real API
    let url = `${API_URL}/products?page=${page}&size=${size}&sort=${sort}`;
    if (categoryId) url += `&categoryId=${categoryId}`;
    if (search) url += `&search=${encodeURIComponent(search)}`;
    
    const response = await axios.get<ApiResponse<ProductResponse>>(url);
    return response.data.data;
  } catch (error) {
    console.error('Error fetching products:', error);
    
    // If API fails, fall back to mock data
    return getProductsMock(page, size, sort, categoryId, search);
  }
};

// Private mock data function for fallback
const getProductsMock = (
  page: number = 0, 
  size: number = 10,
  sort: string = 'createdAt,desc',
  categoryId?: number,
  search?: string
): ProductResponse => {
  // Filter products by category if specified
  let filteredProducts = [...MOCK_PRODUCTS];
  
  if (categoryId) {
    filteredProducts = filteredProducts.filter(product => {
      if (typeof product.category === 'object' && product.category !== null) {
        return product.category.id === categoryId;
      }
      return false;
    });
  }
  
  // Filter products by search term if specified
  if (search) {
    const searchLower = search.toLowerCase();
    filteredProducts = filteredProducts.filter(product => 
      product.name.toLowerCase().includes(searchLower) || 
      product.description.toLowerCase().includes(searchLower)
    );
  }
  
  // Sort products
  if (sort) {
    const [field, direction] = sort.split(',');
    filteredProducts.sort((a, b) => {
      let comparison = 0;
      
      if (field === 'price') {
        comparison = a.price - b.price;
      } else if (field === 'name') {
        comparison = a.name.localeCompare(b.name);
      } else if (field === 'createdAt') {
        const aTime = a.createdAt ? new Date(a.createdAt).getTime() : 0;
        const bTime = b.createdAt ? new Date(b.createdAt).getTime() : 0;
        comparison = aTime - bTime;
      }
      
      return direction === 'asc' ? comparison : -comparison;
    });
  }
  
  // Paginate products
  const totalElements = filteredProducts.length;
  const totalPages = Math.ceil(totalElements / size);
  const start = page * size;
  const end = start + size;
  const paginatedProducts = filteredProducts.slice(start, end);
  
  // Create response object
  return {
    content: paginatedProducts,
    pageable: {
      pageNumber: page,
      pageSize: size,
      sort: {
        sorted: true,
        unsorted: false,
        empty: false
      },
      offset: page * size,
      paged: true,
      unpaged: false
    },
    last: page >= totalPages - 1,
    totalElements,
    totalPages,
    size,
    number: page,
    sort: {
      sorted: true,
      unsorted: false,
      empty: false
    },
    first: page === 0,
    numberOfElements: paginatedProducts.length,
    empty: paginatedProducts.length === 0
  };
};

/**
 * Get a single product by ID
 * @param id Product ID
 * @returns Promise with product data
 */
export const getProductById = async (id: number): Promise<Product> => {
  try {
    // For development, return mock data
    // In production, this would call the actual API
    // const response = await axios.get(`${API_URL}/products/${id}`);
    // return response.data;
    
    const product = MOCK_PRODUCTS.find(p => p.id === id);
    
    if (!product) {
      throw new Error(`Product with ID ${id} not found`);
    }
    
    return product;
  } catch (error) {
    console.error(`Error fetching product with ID ${id}:`, error);
    throw error;
  }
};

/**
 * Get all product categories
 * @returns Promise with array of categories
 */
export const getCategories = async (): Promise<Category[]> => {
  try {
    // For development, return mock data
    // In production, this would call the actual API
    // const response = await axios.get(`${API_URL}/categories`);
    // return response.data;
    
    return MOCK_CATEGORIES;
  } catch (error) {
    console.error('Error fetching categories:', error);
    throw error;
  }
};

/**
 * Get related products for a specific product
 * @param productId Product ID
 * @param limit Number of related products to fetch
 * @returns Promise with array of related products
 */
export const getRelatedProducts = async (
  productId: number, 
  limit: number = 4
): Promise<Product[]> => {
  try {
    // For development, return mock data
    // In production, this would call the actual API
    // const response = await axios.get(`${API_URL}/products/${productId}/related?limit=${limit}`);
    // return response.data;
    
    const product = MOCK_PRODUCTS.find(p => p.id === productId);
    
    if (!product) {
      return [];
    }
    
    // Get products in the same category except the current product
    let relatedProducts = MOCK_PRODUCTS.filter(p => {
      if (typeof p.category === 'object' && p.category !== null && 
          typeof product.category === 'object' && product.category !== null) {
        return p.category.id === product.category.id && p.id !== productId;
      }
      return false;
    });
    
    // If not enough products in the same category, add some random products
    if (relatedProducts.length < limit) {
      const otherProducts = MOCK_PRODUCTS
        .filter(p => {
          if (typeof p.category === 'object' && p.category !== null && 
              typeof product.category === 'object' && product.category !== null) {
            return p.category.id !== product.category.id && p.id !== productId;
          }
          return p.id !== productId;
        })
        .sort(() => 0.5 - Math.random()); // Shuffle
      
      relatedProducts = [
        ...relatedProducts,
        ...otherProducts.slice(0, limit - relatedProducts.length)
      ];
    }
    
    return relatedProducts.slice(0, limit);
  } catch (error) {
    console.error(`Error fetching related products for product ID ${productId}:`, error);
    throw error;
  }
};

/**
 * Get featured products
 * @param limit Number of featured products to fetch
 * @returns Promise with array of featured products
 */
export const getFeaturedProducts = async (limit: number = 8): Promise<Product[]> => {
  try {
    // For development, return mock data
    // In production, this would call the actual API
    // const response = await axios.get(`${API_URL}/products/featured?limit=${limit}`);
    // return response.data;
    
    // Sort products by rating and return the top ones
    const featuredProducts = [...MOCK_PRODUCTS]
      .sort((a, b) => {
        const aRating = a.averageRating || 0;
        const bRating = b.averageRating || 0;
        return bRating - aRating;
      })
      .slice(0, limit);
    
    return featuredProducts;
  } catch (error) {
    console.error('Error fetching featured products:', error);
    throw error;
  }
};

// Get products by category
export const getProductsByCategory = async (
  categoryId: string,
  page = 0,
  size = 10
): Promise<PaginatedResponse<Product>> => {
  try {
    const response = await axios.get<PaginatedResponse<Product>>(
      `${API_URL}/category/${categoryId}?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Search products
export const searchProducts = async (
  query: string,
  page = 0,
  size = 10
): Promise<PaginatedResponse<Product>> => {
  try {
    const response = await axios.get<PaginatedResponse<Product>>(
      `${API_URL}/search?query=${encodeURIComponent(query)}&page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Get product reviews
export const getProductReviews = async (
  productId: string,
  page = 0,
  size = 10
): Promise<PaginatedResponse<Review>> => {
  try {
    const response = await axios.get<PaginatedResponse<Review>>(
      `${API_URL}/${productId}/reviews?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// Add product review
export const addProductReview = async (
  productId: string,
  rating: number,
  comment: string
): Promise<Review> => {
  try {
    const response = await axios.post<ApiResponse<Review>>(
      `${API_URL}/${productId}/reviews`,
      { rating, comment }
    );
    return response.data.data;
  } catch (error) {
    throw error;
  }
};

// Create a new product
export const createProduct = async (productData: any): Promise<Product> => {
  try {
    // For development, return mock data
    // const response = await axios.post<ApiResponse<Product>>(`${API_URL}/products`, productData);
    // return response.data.data;
    
    // Mock implementation to simulate product creation
    const newProduct = {
      id: Math.max(...MOCK_PRODUCTS.map(p => p.id)) + 1,
      ...productData,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      averageRating: 0,
      reviewCount: 0,
      stock: productData.stockQuantity,
    };
    
    // For development, just return the new product without actually adding it to MOCK_PRODUCTS
    return newProduct as Product;
  } catch (error) {
    console.error('Error creating product:', error);
    throw error;
  }
};

// Update an existing product
export const updateProduct = async (id: string, productData: any): Promise<Product> => {
  try {
    // For development, return mock data
    // const response = await axios.put<ApiResponse<Product>>(`${API_URL}/products/${id}`, productData);
    // return response.data.data;
    
    // Find the product to update
    const existingProductIndex = MOCK_PRODUCTS.findIndex(p => p.id.toString() === id);
    
    if (existingProductIndex === -1) {
      throw new Error(`Product with ID ${id} not found`);
    }
    
    // Create updated product
    const updatedProduct = {
      ...MOCK_PRODUCTS[existingProductIndex],
      ...productData,
      stock: productData.stockQuantity,
      updatedAt: new Date().toISOString()
    };
    
    // For development, just return the updated product without actually updating MOCK_PRODUCTS
    return updatedProduct as Product;
  } catch (error) {
    console.error(`Error updating product with ID ${id}:`, error);
    throw error;
  }
}; 