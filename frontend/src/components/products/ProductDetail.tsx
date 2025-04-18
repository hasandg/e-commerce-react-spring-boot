import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  Grid,
  Typography,
  Paper,
  Rating,
  Divider,
  Chip,
  CircularProgress,
  IconButton,
  Snackbar,
  Alert,
  Card,
  CardMedia
} from '@mui/material';
import {
  AddShoppingCart as AddCartIcon,
  Favorite as FavoriteIcon,
  FavoriteBorder as FavoriteBorderIcon,
  ArrowForward as ArrowForwardIcon,
  ArrowBack as ArrowBackIcon
} from '@mui/icons-material';
import { getProductById } from '../../services/productService';
import { addToCart } from '../../services/cartService';
import { Product } from '../../types';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [quantity, setQuantity] = useState<number>(1);
  const [isFavorite, setIsFavorite] = useState<boolean>(false);
  const [notification, setNotification] = useState<{ open: boolean; message: string; type: 'success' | 'error' }>({
    open: false,
    message: '',
    type: 'success'
  });
  
  // For image gallery
  const [currentImageIndex, setCurrentImageIndex] = useState<number>(0);
  
  useEffect(() => {
    const fetchProduct = async () => {
      try {
        if (!id) return;
        
        const productData = await getProductById(Number(id));
        setProduct(productData);
      } catch (err) {
        setError('Failed to load product details');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    
    fetchProduct();
  }, [id]);
  
  const handleAddToCart = async () => {
    if (!product) return;
    
    try {
      // Convert number id to string for the cart API
      await addToCart(product.id.toString(), quantity);
      setNotification({
        open: true,
        message: 'Product added to cart successfully',
        type: 'success'
      });
    } catch (err) {
      setNotification({
        open: true,
        message: 'Failed to add product to cart',
        type: 'error'
      });
      console.error(err);
    }
  };
  
  const handleToggleFavorite = () => {
    setIsFavorite(!isFavorite);
    // TODO: Implement add/remove from favorites functionality
  };
  
  const handleCloseNotification = () => {
    setNotification({ ...notification, open: false });
  };
  
  const nextImage = () => {
    if (!product?.imageUrls?.length) return;
    setCurrentImageIndex((prevIndex) => (prevIndex + 1) % product.imageUrls!.length);
  };
  
  const prevImage = () => {
    if (!product?.imageUrls?.length) return;
    setCurrentImageIndex((prevIndex) => (prevIndex - 1 + product.imageUrls!.length) % product.imageUrls!.length);
  };
  
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
        <CircularProgress />
      </Box>
    );
  }
  
  if (error || !product) {
    return (
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Alert severity="error">
          {error || 'Product not found'}
        </Alert>
        <Button 
          variant="contained" 
          color="primary" 
          onClick={() => navigate('/products')}
          sx={{ mt: 2 }}
        >
          Back to Products
        </Button>
      </Container>
    );
  }
  
  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Grid container spacing={4}>
        {/* Product Images */}
        <Grid item xs={12} md={6}>
          <Paper 
            elevation={2} 
            sx={{ 
              position: 'relative',
              borderRadius: 2,
              overflow: 'hidden'
            }}
          >
            {/* Main Image */}
            <Box 
              sx={{ 
                position: 'relative',
                height: 400,
                display: 'flex',
                justifyContent: 'center',
                backgroundColor: '#f5f5f5'
              }}
            >
              <CardMedia
                component="img"
                image={product.imageUrls && product.imageUrls.length > 0 
                  ? product.imageUrls[currentImageIndex] 
                  : product.imageUrl || 'https://via.placeholder.com/600x400?text=No+Image'}
                alt={product.name}
                sx={{ 
                  height: '100%',
                  objectFit: 'contain'
                }}
              />
              
              {/* Navigation arrows */}
              {product.imageUrls && product.imageUrls.length > 1 && (
                <>
                  <IconButton 
                    sx={{ 
                      position: 'absolute', 
                      left: 8, 
                      top: '50%', 
                      transform: 'translateY(-50%)',
                      backgroundColor: 'rgba(255,255,255,0.8)',
                      '&:hover': { backgroundColor: 'rgba(255,255,255,0.9)' }
                    }}
                    onClick={prevImage}
                  >
                    <ArrowBackIcon />
                  </IconButton>
                  <IconButton 
                    sx={{ 
                      position: 'absolute', 
                      right: 8, 
                      top: '50%', 
                      transform: 'translateY(-50%)',
                      backgroundColor: 'rgba(255,255,255,0.8)',
                      '&:hover': { backgroundColor: 'rgba(255,255,255,0.9)' }
                    }}
                    onClick={nextImage}
                  >
                    <ArrowForwardIcon />
                  </IconButton>
                </>
              )}
            </Box>
            
            {/* Image thumbnails */}
            {product.imageUrls && product.imageUrls.length > 1 && (
              <Box 
                sx={{ 
                  display: 'flex', 
                  overflowX: 'auto',
                  py: 1,
                  px: 2,
                  '&::-webkit-scrollbar': {
                    height: 8,
                  },
                  '&::-webkit-scrollbar-thumb': {
                    backgroundColor: 'rgba(0,0,0,0.2)',
                    borderRadius: 4,
                  }
                }}
              >
                {product.imageUrls.map((url, index) => (
                  <Card 
                    key={index}
                    sx={{ 
                      minWidth: 80, 
                      height: 80, 
                      mr: 1, 
                      cursor: 'pointer',
                      border: currentImageIndex === index ? '2px solid #1976d2' : 'none',
                      opacity: currentImageIndex === index ? 1 : 0.7,
                      transition: 'all 0.2s ease-in-out',
                      '&:hover': { opacity: 1 }
                    }}
                    onClick={() => setCurrentImageIndex(index)}
                  >
                    <CardMedia
                      component="img"
                      image={url}
                      alt={`Product ${index + 1}`}
                      sx={{ height: '100%', objectFit: 'cover' }}
                    />
                  </Card>
                ))}
              </Box>
            )}
          </Paper>
        </Grid>
        
        {/* Product Information */}
        <Grid item xs={12} md={6}>
          <Typography variant="h4" component="h1" gutterBottom>
            {product.name}
          </Typography>
          
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <Rating value={product.averageRating || 0} precision={0.5} readOnly />
            <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }}>
              ({product.reviewCount || 0} reviews)
            </Typography>
          </Box>
          
          <Typography variant="h5" color="primary" gutterBottom sx={{ fontWeight: 'bold', my: 2 }}>
            ${product.price.toFixed(2)}
          </Typography>
          
          <Divider sx={{ my: 2 }} />
          
          <Typography variant="body1" paragraph>
            {product.description}
          </Typography>
          
          <Box sx={{ my: 2 }}>
            <Chip 
              label={typeof product.category === 'string' 
                ? product.category 
                : product.category?.name || 'Uncategorized'} 
              color="primary" 
              variant="outlined" 
              sx={{ mr: 1 }} 
            />
            <Chip 
              label={product.stock > 0 ? 'In Stock' : 'Out of Stock'} 
              color={product.stock > 0 ? 'success' : 'error'} 
              variant="outlined" 
            />
          </Box>
          
          <Box sx={{ my: 3, display: 'flex', alignItems: 'center' }}>
            <Button
              variant="contained"
              color="primary"
              startIcon={<AddCartIcon />}
              size="large"
              disabled={product.stock <= 0}
              onClick={handleAddToCart}
              sx={{ mr: 2, px: 4 }}
            >
              Add to Cart
            </Button>
            
            <IconButton 
              color="secondary" 
              onClick={handleToggleFavorite}
              sx={{ border: '1px solid rgba(0,0,0,0.12)', p: 1 }}
            >
              {isFavorite ? <FavoriteIcon /> : <FavoriteBorderIcon />}
            </IconButton>
          </Box>
          
          <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
            Product ID: {product.id}
          </Typography>
        </Grid>
      </Grid>
      
      <Snackbar 
        open={notification.open} 
        autoHideDuration={6000} 
        onClose={handleCloseNotification}
      >
        <Alert onClose={handleCloseNotification} severity={notification.type} sx={{ width: '100%' }}>
          {notification.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default ProductDetail; 