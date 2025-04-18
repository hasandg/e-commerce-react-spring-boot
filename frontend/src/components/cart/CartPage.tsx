import React, { useState, useEffect } from 'react';
import { 
  Container, 
  Typography, 
  Paper, 
  Table, 
  TableBody, 
  TableCell, 
  TableContainer, 
  TableHead, 
  TableRow,
  Button,
  IconButton,
  Box,
  Divider,
  Grid,
  TextField,
  CircularProgress,
  Alert
} from '@mui/material';
import { 
  Add as AddIcon, 
  Remove as RemoveIcon, 
  Delete as DeleteIcon,
  ShoppingCart as CartIcon
} from '@mui/icons-material';
import { Link as RouterLink } from 'react-router-dom';
import { getCart, updateCartItem, removeFromCart, clearCart } from '../../services/cartService';
import { Cart } from '../../types';

const CartPage: React.FC = () => {
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [quantities, setQuantities] = useState<Record<string, number>>({});

  // Load cart data on component mount
  useEffect(() => {
    fetchCart();
  }, []);

  // Fetch cart data from API
  const fetchCart = async () => {
    try {
      setLoading(true);
      setError(null);
      const cartData = await getCart();
      setCart(cartData);
      
      // Initialize quantities state from cart items
      const initialQuantities: Record<string, number> = {};
      cartData.items.forEach(item => {
        initialQuantities[String(item.id)] = item.quantity;
      });
      setQuantities(initialQuantities);
    } catch (err) {
      console.error('Error fetching cart:', err);
      setError('Failed to load cart. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Handle quantity change
  const handleQuantityChange = (itemId: string, newQuantity: number) => {
    if (newQuantity < 1) return; // Don't allow quantities less than 1
    
    setQuantities(prev => ({
      ...prev,
      [itemId]: newQuantity
    }));
  };

  // Update item quantity in cart
  const handleUpdateQuantity = async (itemId: string) => {
    try {
      if (quantities[itemId] !== undefined) {
        const updatedCart = await updateCartItem(itemId, quantities[itemId]);
        setCart(updatedCart);
      }
    } catch (err) {
      console.error('Error updating item:', err);
      setError('Failed to update item. Please try again.');
    }
  };

  // Remove item from cart
  const handleRemoveItem = async (itemId: string) => {
    try {
      const updatedCart = await removeFromCart(itemId);
      setCart(updatedCart);
    } catch (err) {
      console.error('Error removing item:', err);
      setError('Failed to remove item. Please try again.');
    }
  };

  // Clear all items from cart
  const handleClearCart = async () => {
    try {
      await clearCart();
      fetchCart(); // Refresh cart after clearing
    } catch (err) {
      console.error('Error clearing cart:', err);
      setError('Failed to clear cart. Please try again.');
    }
  };

  // Get quantity for an item with fallback to cart item quantity
  const getItemQuantity = (itemId: string, defaultQuantity: number): number => {
    return quantities[itemId] !== undefined ? quantities[itemId] : defaultQuantity;
  };

  if (loading) {
    return (
      <Container sx={{ py: 4, textAlign: 'center' }}>
        <CircularProgress />
        <Typography variant="body1" sx={{ mt: 2 }}>
          Loading your cart...
        </Typography>
      </Container>
    );
  }

  if (error) {
    return (
      <Container sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button 
          variant="contained" 
          onClick={fetchCart}
        >
          Try Again
        </Button>
      </Container>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <Container sx={{ py: 4 }}>
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <CartIcon sx={{ fontSize: 60, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h5" gutterBottom>
            Your cart is empty
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            Looks like you haven't added any products to your cart yet.
          </Typography>
          <Button 
            variant="contained" 
            component={RouterLink} 
            to="/"
            size="large"
          >
            Start e-commerce
          </Button>
        </Paper>
      </Container>
    );
  }

  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h4" gutterBottom sx={{ fontWeight: 'bold' }}>
        Your e-commerce Cart
      </Typography>
      <Typography variant="body1" color="text.secondary" paragraph>
        You have {cart.totalItems} {cart.totalItems === 1 ? 'item' : 'items'} in your cart
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Grid container spacing={4}>
        <Grid item xs={12} md={8}>
          <TableContainer component={Paper} sx={{ mb: 4 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Product</TableCell>
                  <TableCell align="center">Price</TableCell>
                  <TableCell align="center">Quantity</TableCell>
                  <TableCell align="right">Subtotal</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {cart.items.map((item) => {
                  const itemIdStr = String(item.id);
                  const currentQuantity = getItemQuantity(itemIdStr, item.quantity);
                  
                  return (
                    <TableRow key={itemIdStr}>
                      <TableCell component="th" scope="row">
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <Box
                            component="img"
                            src={item.imageUrl}
                            alt={item.productName}
                            sx={{ 
                              width: 60, 
                              height: 60, 
                              borderRadius: 1,
                              mr: 2,
                              objectFit: 'cover' 
                            }}
                          />
                          <Box>
                            <Typography variant="body1">
                              {item.productName}
                            </Typography>
                            <Typography 
                              component={RouterLink} 
                              to={`/products/${item.productId}`}
                              variant="body2" 
                              color="primary"
                              sx={{ textDecoration: 'none' }}
                            >
                              View details
                            </Typography>
                          </Box>
                        </Box>
                      </TableCell>
                      <TableCell align="center">
                        ${item.price.toFixed(2)}
                      </TableCell>
                      <TableCell align="center">
                        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                          <IconButton 
                            size="small"
                            onClick={() => handleQuantityChange(itemIdStr, Math.max(1, currentQuantity - 1))}
                          >
                            <RemoveIcon fontSize="small" />
                          </IconButton>
                          <TextField
                            value={currentQuantity}
                            onChange={(e) => {
                              const val = parseInt(e.target.value);
                              if (!isNaN(val) && val > 0) {
                                handleQuantityChange(itemIdStr, val);
                              }
                            }}
                            inputProps={{
                              min: 1,
                              style: { textAlign: 'center' }
                            }}
                            variant="outlined"
                            size="small"
                            sx={{ width: '60px', mx: 1 }}
                          />
                          <IconButton 
                            size="small"
                            onClick={() => handleQuantityChange(itemIdStr, currentQuantity + 1)}
                          >
                            <AddIcon fontSize="small" />
                          </IconButton>
                        </Box>
                        {currentQuantity !== item.quantity && (
                          <Button 
                            size="small" 
                            onClick={() => handleUpdateQuantity(itemIdStr)}
                            sx={{ mt: 1 }}
                          >
                            Update
                          </Button>
                        )}
                      </TableCell>
                      <TableCell align="right">
                        ${(item.price * item.quantity).toFixed(2)}
                      </TableCell>
                      <TableCell align="right">
                        <IconButton 
                          color="error"
                          onClick={() => handleRemoveItem(itemIdStr)}
                        >
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </TableContainer>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Button 
              variant="outlined" 
              component={RouterLink} 
              to="/"
            >
              Continue e-commerce
            </Button>
            <Button 
              variant="outlined" 
              color="error"
              onClick={handleClearCart}
            >
              Clear Cart
            </Button>
          </Box>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Order Summary
            </Typography>
            <Divider sx={{ my: 2 }} />
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
              <Typography variant="body1">Subtotal</Typography>
              <Typography variant="body1">${cart.totalPrice.toFixed(2)}</Typography>
            </Box>
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
              <Typography variant="body1">Shipping</Typography>
              <Typography variant="body1">Free</Typography>
            </Box>
            
            <Divider sx={{ my: 2 }} />
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
              <Typography variant="h6">Total</Typography>
              <Typography variant="h6">${cart.totalPrice.toFixed(2)}</Typography>
            </Box>
            
            <Button 
              variant="contained" 
              fullWidth 
              size="large"
              component={RouterLink}
              to="/checkout"
            >
              Proceed to Checkout
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default CartPage; 