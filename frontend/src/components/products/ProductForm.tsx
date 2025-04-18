import React, { useState, useEffect, ChangeEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  Grid,
  MenuItem,
  InputAdornment,
  Container,
  Divider,
  IconButton,
  CircularProgress,
  Snackbar,
  Alert,
  Stack,
  Card,
  CardMedia
} from '@mui/material';
import {
  Delete as DeleteIcon,
  Add as AddIcon,
  PhotoCamera as CameraIcon
} from '@mui/icons-material';
import { getProductById, createProduct, updateProduct } from '../../services/productService';
import { getAllCategories } from '../../services/categoryService';
import { Product, Category } from '../../types';

interface FormData {
  name: string;
  description: string;
  price: string;
  stockQuantity: string;
  categoryId: string;
  active: boolean;
}

const ProductForm: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isEditMode = Boolean(id);
  
  const [formData, setFormData] = useState<FormData>({
    name: '',
    description: '',
    price: '',
    stockQuantity: '',
    categoryId: '',
    active: true
  });
  
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [saveLoading, setSaveLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviewUrls, setImagePreviewUrls] = useState<string[]>([]);
  const [existingImages, setExistingImages] = useState<string[]>([]);
  const [notification, setNotification] = useState<{ open: boolean; message: string; type: 'success' | 'error' }>({
    open: false,
    message: '',
    type: 'success'
  });
  
  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      try {
        // Load categories
        const categoriesData = await getAllCategories();
        setCategories(categoriesData);
        
        // If in edit mode, load product data
        if (isEditMode && id) {
          const product = await getProductById(Number(id));
          setFormData({
            name: product.name,
            description: product.description,
            price: product.price.toString(),
            stockQuantity: product.stockQuantity?.toString() || product.stock.toString(),
            categoryId: typeof product.category === 'string' 
              ? product.category 
              : product.category?.id?.toString() || '',
            active: true
          });
          
          // Set existing images
          if (product.imageUrls && product.imageUrls.length > 0) {
            setExistingImages(product.imageUrls);
          } else if (product.imageUrl) {
            setExistingImages([product.imageUrl]);
          }
        }
      } catch (err) {
        console.error('Error loading data:', err);
        setError('Failed to load data. Please try again.');
      } finally {
        setLoading(false);
      }
    };
    
    loadData();
  }, [id, isEditMode]);
  
  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
  
  const handleImageUpload = (e: ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files || e.target.files.length === 0) return;
    
    const newFiles = Array.from(e.target.files);
    setImages([...images, ...newFiles]);
    
    // Create preview URLs for the images
    const newPreviews = newFiles.map(file => URL.createObjectURL(file));
    setImagePreviewUrls([...imagePreviewUrls, ...newPreviews]);
  };
  
  const handleRemoveImage = (index: number) => {
    const newImages = [...images];
    const newPreviewUrls = [...imagePreviewUrls];
    
    // Release object URL to prevent memory leaks
    URL.revokeObjectURL(newPreviewUrls[index]);
    
    newImages.splice(index, 1);
    newPreviewUrls.splice(index, 1);
    
    setImages(newImages);
    setImagePreviewUrls(newPreviewUrls);
  };
  
  const handleRemoveExistingImage = (index: number) => {
    const newExistingImages = [...existingImages];
    newExistingImages.splice(index, 1);
    setExistingImages(newExistingImages);
  };
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaveLoading(true);
    
    try {
      const productData = {
        name: formData.name,
        description: formData.description,
        price: parseFloat(formData.price),
        stockQuantity: parseInt(formData.stockQuantity),
        categoryId: formData.categoryId,
        active: formData.active,
        imageUrls: existingImages
      };
      
      let productId: string | number;
      
      if (isEditMode && id) {
        // Update existing product
        const updatedProduct = await updateProduct(id, productData);
        productId = updatedProduct.id;
        
        setNotification({
          open: true,
          message: 'Product updated successfully!',
          type: 'success'
        });
      } else {
        // Create new product
        const newProduct = await createProduct(productData);
        productId = newProduct.id;
        
        setNotification({
          open: true,
          message: 'Product created successfully!',
          type: 'success'
        });
      }
      
      // Upload images if any
      if (images.length > 0 && productId) {
        const formData = new FormData();
        images.forEach(image => {
          formData.append('images', image);
        });
        
        // Example upload endpoint - implement this in your service
        await fetch(`/api/products/${productId}/images`, {
          method: 'POST',
          body: formData
        });
      }
      
      // Redirect after a brief delay to show success message
      setTimeout(() => {
        navigate(`/products/${productId}`);
      }, 1500);
      
    } catch (err) {
      console.error('Error saving product:', err);
      setNotification({
        open: true,
        message: 'Error saving product. Please try again.',
        type: 'error'
      });
    } finally {
      setSaveLoading(false);
    }
  };
  
  const handleCloseNotification = () => {
    setNotification({ ...notification, open: false });
  };
  
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
        <CircularProgress />
      </Box>
    );
  }
  
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Paper elevation={2} sx={{ p: 4, borderRadius: 2 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          {isEditMode ? 'Edit Product' : 'Create New Product'}
        </Typography>
        
        <Divider sx={{ mb: 4 }} />
        
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}
        
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                label="Product Name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                fullWidth
                required
                variant="outlined"
                autoFocus
              />
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                label="Description"
                name="description"
                value={formData.description}
                onChange={handleInputChange}
                fullWidth
                required
                multiline
                rows={4}
                variant="outlined"
              />
            </Grid>
            
            <Grid item xs={12} sm={6}>
              <TextField
                label="Price"
                name="price"
                type="number"
                value={formData.price}
                onChange={handleInputChange}
                fullWidth
                required
                variant="outlined"
                InputProps={{
                  startAdornment: <InputAdornment position="start">$</InputAdornment>,
                }}
              />
            </Grid>
            
            <Grid item xs={12} sm={6}>
              <TextField
                label="Stock Quantity"
                name="stockQuantity"
                type="number"
                value={formData.stockQuantity}
                onChange={handleInputChange}
                fullWidth
                required
                variant="outlined"
              />
            </Grid>
            
            <Grid item xs={12}>
              <TextField
                select
                label="Category"
                name="categoryId"
                value={formData.categoryId}
                onChange={handleInputChange}
                fullWidth
                required
                variant="outlined"
              >
                {categories.map((category) => (
                  <MenuItem key={category.id} value={category.id}>
                    {category.name}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            
            <Grid item xs={12}>
              <Typography variant="h6" gutterBottom>
                Product Images
              </Typography>
              
              {/* Existing Images */}
              {existingImages.length > 0 && (
                <>
                  <Typography variant="subtitle1" gutterBottom>
                    Current Images
                  </Typography>
                  <Stack direction="row" spacing={2} sx={{ mb: 3, flexWrap: 'wrap', gap: 2 }}>
                    {existingImages.map((url, index) => (
                      <Box 
                        key={`existing-${index}`}
                        sx={{ 
                          position: 'relative',
                          width: 120,
                          height: 120,
                          border: '1px solid #eee',
                          borderRadius: 1,
                          overflow: 'hidden'
                        }}
                      >
                        <CardMedia
                          component="img"
                          image={url}
                          alt={`Product image ${index + 1}`}
                          sx={{ height: '100%', objectFit: 'cover' }}
                        />
                        <IconButton
                          size="small"
                          color="error"
                          sx={{ 
                            position: 'absolute', 
                            top: 4, 
                            right: 4,
                            backgroundColor: 'rgba(255,255,255,0.8)',
                            '&:hover': { backgroundColor: 'rgba(255,255,255,0.95)' }
                          }}
                          onClick={() => handleRemoveExistingImage(index)}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Box>
                    ))}
                  </Stack>
                </>
              )}
              
              {/* New Images Previews */}
              {imagePreviewUrls.length > 0 && (
                <>
                  <Typography variant="subtitle1" gutterBottom>
                    New Images
                  </Typography>
                  <Stack direction="row" spacing={2} sx={{ mb: 3, flexWrap: 'wrap', gap: 2 }}>
                    {imagePreviewUrls.map((url, index) => (
                      <Box 
                        key={`preview-${index}`}
                        sx={{ 
                          position: 'relative',
                          width: 120,
                          height: 120,
                          border: '1px solid #eee',
                          borderRadius: 1,
                          overflow: 'hidden'
                        }}
                      >
                        <CardMedia
                          component="img"
                          image={url}
                          alt={`New image ${index + 1}`}
                          sx={{ height: '100%', objectFit: 'cover' }}
                        />
                        <IconButton
                          size="small"
                          color="error"
                          sx={{ 
                            position: 'absolute', 
                            top: 4, 
                            right: 4,
                            backgroundColor: 'rgba(255,255,255,0.8)',
                            '&:hover': { backgroundColor: 'rgba(255,255,255,0.95)' }
                          }}
                          onClick={() => handleRemoveImage(index)}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Box>
                    ))}
                  </Stack>
                </>
              )}
              
              {/* Upload Button */}
              <Button
                variant="outlined"
                component="label"
                startIcon={<CameraIcon />}
                sx={{ mt: 2 }}
              >
                Add Images
                <input
                  type="file"
                  accept="image/*"
                  hidden
                  multiple
                  onChange={handleImageUpload}
                />
              </Button>
            </Grid>
            
            <Grid item xs={12} sx={{ mt: 2 }}>
              <Stack direction="row" spacing={2} justifyContent="flex-end">
                <Button 
                  variant="outlined" 
                  onClick={() => navigate(-1)}
                >
                  Cancel
                </Button>
                <Button 
                  type="submit" 
                  variant="contained" 
                  color="primary"
                  disabled={saveLoading}
                  startIcon={saveLoading ? <CircularProgress size={20} color="inherit" /> : null}
                >
                  {isEditMode ? 'Update Product' : 'Create Product'}
                </Button>
              </Stack>
            </Grid>
          </Grid>
        </form>
      </Paper>
      
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

export default ProductForm; 