import { Box, Container, Grid, Typography, Link, Divider } from '@mui/material';

const Footer = () => {
  const currentYear = new Date().getFullYear();
  
  return (
    <Box 
      component="footer" 
      sx={{ 
        backgroundColor: 'primary.main', 
        color: 'white',
        py: 6,
        mt: 'auto'
      }}
    >
      <Container maxWidth="lg">
        <Grid container spacing={4}>
          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              E-Commerce
            </Typography>
            <Typography variant="body2">
              Your one-stop shop for all your needs. Quality products, fast shipping, and excellent customer service.
            </Typography>
          </Grid>
          
          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              Shop
            </Typography>
            <Link href="/products" color="inherit" display="block" sx={{ mb: 1 }}>
              All Products
            </Link>
            <Link href="/products?featured=true" color="inherit" display="block" sx={{ mb: 1 }}>
              Featured Products
            </Link>
            <Link href="/products?discount=true" color="inherit" display="block" sx={{ mb: 1 }}>
              Discounted Items
            </Link>
          </Grid>
          
          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              Customer Service
            </Typography>
            <Link href="/contact" color="inherit" display="block" sx={{ mb: 1 }}>
              Contact Us
            </Link>
            <Link href="/shipping-policy" color="inherit" display="block" sx={{ mb: 1 }}>
              Shipping Policy
            </Link>
            <Link href="/returns" color="inherit" display="block" sx={{ mb: 1 }}>
              Returns & Refunds
            </Link>
            <Link href="/faq" color="inherit" display="block" sx={{ mb: 1 }}>
              FAQ
            </Link>
          </Grid>
          
          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              About Us
            </Typography>
            <Link href="/about" color="inherit" display="block" sx={{ mb: 1 }}>
              Our Story
            </Link>
            <Link href="/privacy-policy" color="inherit" display="block" sx={{ mb: 1 }}>
              Privacy Policy
            </Link>
            <Link href="/terms" color="inherit" display="block" sx={{ mb: 1 }}>
              Terms of Service
            </Link>
          </Grid>
        </Grid>
        
        <Divider sx={{ my: 3, backgroundColor: 'rgba(255, 255, 255, 0.2)' }} />
        
        <Box sx={{ textAlign: 'center' }}>
          <Typography variant="body2">
            &copy; {currentYear} E-Commerce Platform. All rights reserved.
          </Typography>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer; 