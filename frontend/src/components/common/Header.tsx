import React, { useState, useEffect } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Badge,
  Menu,
  MenuItem,
  Box,
  Container,
  Avatar,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
  useMediaQuery,
  useTheme,
} from '@mui/material';
import {
  Menu as MenuIcon,
  ShoppingCart as CartIcon,
  Person as PersonIcon,
  Logout as LogoutIcon,
  Home as HomeIcon,
  Search as SearchIcon,
  Favorite as FavoriteIcon,
  ListAlt as OrdersIcon,
} from '@mui/icons-material';
import { styled, alpha } from '@mui/material/styles';
import { useKeycloak } from '@react-keycloak/web';
import { getCartItemCount } from '@/services/cartService';

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(1),
    width: 'auto',
  },
}));

const Header: React.FC = () => {
  const navigate = useNavigate();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const { keycloak } = useKeycloak();
  
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [cartCount, setCartCount] = useState(0);
  
  const isMenuOpen = Boolean(anchorEl);
  
  useEffect(() => {
    // Load cart count
    const fetchCartCount = async () => {
      if (keycloak.authenticated) {
        try {
          const count = await getCartItemCount();
          setCartCount(count);
        } catch (error) {
          console.error('Error fetching cart count:', error);
        }
      }
    };
    
    fetchCartCount();
    
    // You could set up a polling mechanism or websocket to keep this updated
    const interval = setInterval(fetchCartCount, 30000); // Every 30 seconds
    
    return () => clearInterval(interval);
  }, [keycloak.authenticated]);
  
  const handleProfileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  
  const handleMenuClose = () => {
    setAnchorEl(null);
  };
  
  const handleMobileMenuToggle = () => {
    setMobileMenuOpen(!mobileMenuOpen);
  };

  const handleLogout = () => {
    keycloak.logout();
  };

  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={() => { handleMenuClose(); navigate('/profile'); }}>
        <ListItemIcon>
          <PersonIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText>Profile</ListItemText>
      </MenuItem>
      <MenuItem onClick={() => { handleMenuClose(); navigate('/orders'); }}>
        <ListItemIcon>
          <OrdersIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText>Orders</ListItemText>
      </MenuItem>
      <Divider />
      <MenuItem onClick={handleLogout}>
        <ListItemIcon>
          <LogoutIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText>Logout</ListItemText>
      </MenuItem>
    </Menu>
  );

  const renderMobileMenu = (
    <Drawer
      anchor="right"
      open={mobileMenuOpen}
      onClose={handleMobileMenuToggle}
    >
      <Box sx={{ width: 250 }} role="presentation">
        <List>
          <ListItem button component={RouterLink} to="/" onClick={handleMobileMenuToggle}>
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary="Home" />
          </ListItem>
          {keycloak.authenticated && (
            <>
              <ListItem button component={RouterLink} to="/cart" onClick={handleMobileMenuToggle}>
                <ListItemIcon>
                  <CartIcon />
                </ListItemIcon>
                <ListItemText primary="Cart" />
                <Badge badgeContent={cartCount} color="error" />
              </ListItem>
              <ListItem button component={RouterLink} to="/orders" onClick={handleMobileMenuToggle}>
                <ListItemIcon>
                  <OrdersIcon />
                </ListItemIcon>
                <ListItemText primary="Orders" />
              </ListItem>
              <ListItem button component={RouterLink} to="/profile" onClick={handleMobileMenuToggle}>
                <ListItemIcon>
                  <PersonIcon />
                </ListItemIcon>
                <ListItemText primary="Profile" />
              </ListItem>
              <ListItem button onClick={handleLogout}>
                <ListItemIcon>
                  <LogoutIcon />
                </ListItemIcon>
                <ListItemText primary="Logout" />
              </ListItem>
            </>
          )}
          {!keycloak.authenticated && (
            <ListItem button onClick={() => keycloak.login()}>
              <ListItemIcon>
                <PersonIcon />
              </ListItemIcon>
              <ListItemText primary="Login" />
            </ListItem>
          )}
        </List>
      </Box>
    </Drawer>
  );

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="menu"
            onClick={handleMobileMenuToggle}
            sx={{ mr: 2, display: { sm: 'none' } }}
          >
            <MenuIcon />
          </IconButton>

          <Typography
            variant="h6"
            component={RouterLink}
            to="/"
            sx={{
              flexGrow: 1,
              textDecoration: 'none',
              color: 'inherit',
              display: 'flex',
              alignItems: 'center',
            }}
          >
            E-Commerce
          </Typography>

          <Box sx={{ display: { xs: 'none', sm: 'flex' }, alignItems: 'center' }}>
            <Button color="inherit" component={RouterLink} to="/">
              Home
            </Button>
            {keycloak.authenticated && (
              <>
                <Button color="inherit" component={RouterLink} to="/cart">
                  Cart
                  <Badge badgeContent={cartCount} color="error" sx={{ ml: 1 }} />
                </Button>
                <Button color="inherit" component={RouterLink} to="/orders">
                  Orders
                </Button>
                <IconButton
                  edge="end"
                  aria-label="account of current user"
                  aria-controls="menu-appbar"
                  aria-haspopup="true"
                  onClick={handleProfileMenuOpen}
                  color="inherit"
                >
                  <Avatar sx={{ width: 32, height: 32 }}>
                    {keycloak.tokenParsed?.preferred_username?.charAt(0).toUpperCase()}
                  </Avatar>
                </IconButton>
              </>
            )}
            {!keycloak.authenticated && (
              <Button color="inherit" onClick={() => keycloak.login()}>
                Login
              </Button>
            )}
          </Box>
        </Toolbar>
      </Container>
      {renderMenu}
      {renderMobileMenu}
    </AppBar>
  );
};

export default Header; 