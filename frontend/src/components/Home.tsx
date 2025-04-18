import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
  Box,
  Button,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Container,
  Grid,
  Paper,
  Typography,
  useTheme
} from '@mui/material';

const Home: React.FC = () => {
  const theme = useTheme();
  
  // Mock featured products
  const featuredProducts = [
    {
      id: 1,
      name: 'Smartphone X',
      description: 'Latest flagship smartphone with advanced features',
      price: 999.99,
      image: 'https://images.unsplash.com/photo-1605236453806-6ff36851218e?w=500&auto=format&fit=crop&q=60',
      category: 'Electronics'
    },
    {
      id: 2,
      name: 'Designer Watch',
      description: 'Elegant timepiece with precise movement',
      price: 299.99,
      image: 'https://images.unsplash.com/photo-1539874754764-5a96559165b0?w=500&auto=format&fit=crop&q=60',
      category: 'Fashion'
    },
    {
      id: 3,
      name: 'Wireless Headphones',
      description: 'Premium sound quality with noise cancellation',
      price: 199.99,
      image: 'https://images.unsplash.com/photo-1613040809024-b4ef7ba99bc3?w=500&auto=format&fit=crop&q=60',
      category: 'Electronics'
    },
    {
      id: 4,
      name: 'Stylish Backpack',
      description: 'Durable and spacious with modern design',
      price: 79.99,
      image: 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500&auto=format&fit=crop&q=60',
      category: 'Fashion'
    }
  ];
  
  // Mock categories
  const categories = [
    { id: 1, name: 'Electronics', image: 'https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=500&auto=format&fit=crop&q=60' },
    { id: 2, name: 'Fashion', image: 'https://images.unsplash.com/photo-1537832816519-689ad163238b?w=500&auto=format&fit=crop&q=60' },
    { id: 3, name: 'Home & Garden', image: 'https://images.unsplash.com/photo-1519710164239-da123dc03ef4?w=500&auto=format&fit=crop&q=60' },
    { id: 4, name: 'Sports', image: 'https://images.unsplash.com/photo-1535131749006-b7f58c99034b?w=500&auto=format&fit=crop&q=60' }
  ];
  
  return (
    <Box sx={{ pb: 6 }}>
      {/* Hero Section */}
      <Paper
        sx={{
          position: 'relative',
          backgroundColor: 'grey.800',
          color: '#fff',
          mb: 4,
          backgroundSize: 'cover',
          backgroundRepeat: 'no-repeat',
          backgroundPosition: 'center',
          backgroundImage: 'url(https://images.unsplash.com/photo-1557821552-17105176677c?q=80&w=1200&auto=format&fit=crop)',
          height: '400px',
          display: 'flex',
          alignItems: 'center'
        }}
      >
        <Container maxWidth="lg">
          <Box sx={{ maxWidth: 600, px: { xs: 2, md: 0 } }}>
            <Typography component="h1" variant="h3" color="inherit" gutterBottom>
              Welcome to E-Commerce
            </Typography>
            <Typography variant="h5" color="inherit" paragraph>
              Shop the latest products with the best prices and fast delivery
            </Typography>
            <Button
              variant="contained"
              color="primary"
              size="large"
              component={RouterLink}
              to="/products"
              sx={{ mt: 2 }}
            >
              Shop Now
            </Button>
          </Box>
        </Container>
      </Paper>
      
      {/* Featured Products Section */}
      <Container maxWidth="lg">
        <Box sx={{ my: 4 }}>
          <Typography variant="h4" component="h2" gutterBottom>
            Featured Products
          </Typography>
          <Grid container spacing={3}>
            {featuredProducts.map((product) => (
              <Grid item key={product.id} xs={12} sm={6} md={3}>
                <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                  <CardActionArea component={RouterLink} to={`/products/${product.id}`}>
                    <CardMedia
                      component="img"
                      height="200"
                      image={product.image}
                      alt={product.name}
                    />
                    <CardContent sx={{ flexGrow: 1 }}>
                      <Typography gutterBottom variant="h6" component="div">
                        {product.name}
                      </Typography>
                      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                        {product.description}
                      </Typography>
                      <Typography variant="h6" color="primary">
                        ${product.price.toFixed(2)}
                      </Typography>
                    </CardContent>
                  </CardActionArea>
                </Card>
              </Grid>
            ))}
          </Grid>
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <Button
              variant="outlined"
              color="primary"
              component={RouterLink}
              to="/products"
            >
              View All Products
            </Button>
          </Box>
        </Box>
        
        {/* Categories Section */}
        <Box sx={{ my: 5 }}>
          <Typography variant="h4" component="h2" gutterBottom>
            Shop by Category
          </Typography>
          <Grid container spacing={3}>
            {categories.map((category) => (
              <Grid item key={category.id} xs={6} sm={6} md={3}>
                <Card sx={{ height: '100%' }}>
                  <CardActionArea component={RouterLink} to={`/products?category=${category.name}`}>
                    <CardMedia
                      component="img"
                      height="150"
                      image={category.image}
                      alt={category.name}
                    />
                    <CardContent>
                      <Typography variant="h6" component="div" align="center">
                        {category.name}
                      </Typography>
                    </CardContent>
                  </CardActionArea>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>
        
        {/* Special Offer Banner */}
        <Box sx={{ my: 5 }}>
          <Paper
            sx={{
              p: 4,
              backgroundColor: theme.palette.primary.main,
              color: 'white',
              borderRadius: 2,
              textAlign: 'center'
            }}
          >
            <Typography variant="h4" component="h2" gutterBottom>
              Special Offer
            </Typography>
            <Typography variant="h6" paragraph>
              Get 20% off on your first purchase! Use code: WELCOME20
            </Typography>
            <Button
              variant="contained"
              color="secondary"
              size="large"
              component={RouterLink}
              to="/products?sale=true"
            >
              Shop Now
            </Button>
          </Paper>
        </Box>
      </Container>
    </Box>
  );
};

export default Home; 