import { Box, Container, Typography, Button } from '@mui/material';
import { Link } from 'react-router-dom';

const NotFound = () => {
  return (
    <Container maxWidth="md">
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '70vh',
          textAlign: 'center',
          py: 8
        }}
      >
        <Typography variant="h1" component="h1" gutterBottom sx={{ fontSize: { xs: '6rem', md: '8rem' }, fontWeight: 700, color: 'primary.main' }}>
          404
        </Typography>
        
        <Typography variant="h4" component="h2" gutterBottom>
          Page Not Found
        </Typography>
        
        <Typography variant="body1" color="textSecondary" paragraph sx={{ maxWidth: '600px', mb: 4 }}>
          The page you are looking for doesn't exist or has been moved. Please check the URL or go back to the homepage.
        </Typography>
        
        <Button 
          variant="contained" 
          color="primary" 
          size="large"
          component={Link}
          to="/"
        >
          Go to Homepage
        </Button>
      </Box>
    </Container>
  );
};

export default NotFound; 