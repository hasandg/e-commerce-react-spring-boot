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
import { User } from '../../types';
import { logoutUser } from '../../services/authService';
import { getCartItemCount } from '../../services/cartService';

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

interface HeaderProps {
  user: User | null;
}

const Header: React.FC<HeaderProps> = ({ user }) => {
  const navigate = useNavigate();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [cartCount, setCartCount] = useState(0);
  
  const isMenuOpen = Boolean(anchorEl);
  
  useEffect(() => {
    // Load cart count
    const fetchCartCount = async () => {
      if (user) {
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
  }, [user]);
  
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
    logoutUser();
    handleMenuClose();
    navigate('/login');
  };
  
  const menuId = 'primary-account-menu';
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: 'bottom',
        horizontal: 'right',
      }}
      id={menuId}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={() => {
        handleMenuClose();
        navigate('/profile');
      }}>
        <ListItemIcon>
          <PersonIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText>Profile</ListItemText>
      </MenuItem>
      <MenuItem onClick={() => {
        handleMenuClose();
        navigate('/orders');
      }}>
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
  
  const drawer = (
    <Drawer
      anchor="left"
      open={mobileMenuOpen}
      onClose={() => setMobileMenuOpen(false)}
    >
      <Box sx={{ width: 250 }} role="presentation">
        <List>
          <ListItem button component={RouterLink} to="/" onClick={() => setMobileMenuOpen(false)}>
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary="Home" />
          </ListItem>
          {!user ? (
            <>
              <ListItem button component={RouterLink} to="/login" onClick={() => setMobileMenuOpen(false)}>
                <ListItemIcon>
                  <PersonIcon />
                </ListItemIcon>
                <ListItemText primary="Login" />
              </ListItem>
              <ListItem button component={RouterLink} to="/register" onClick={() => setMobileMenuOpen(false)}>
                <ListItemIcon>
                  <PersonIcon />
                </ListItemIcon>
                <ListItemText primary="Register" />
              </ListItem>
            </>
          ) : (
            <>
              <ListItem button component={RouterLink} to="/profile" onClick={() => setMobileMenuOpen(false)}>
                <ListItemIcon>
                  <PersonIcon />
                </ListItemIcon>
                <ListItemText primary="Profile" />
              </ListItem>
              <ListItem button component={RouterLink} to="/orders" onClick={() => setMobileMenuOpen(false)}>
                <ListItemIcon>
                  <OrdersIcon />
                </ListItemIcon>
                <ListItemText primary="Orders" />
              </ListItem>
              <ListItem button component={RouterLink} to="/favorites" onClick={() => setMobileMenuOpen(false)}>
                <ListItemIcon>
                  <FavoriteIcon />
                </ListItemIcon>
                <ListItemText primary="Favorites" />
              </ListItem>
              <ListItem button onClick={handleLogout}>
                <ListItemIcon>
                  <LogoutIcon />
                </ListItemIcon>
                <ListItemText primary="Logout" />
              </ListItem>
            </>
          )}
        </List>
      </Box>
    </Drawer>
  );
  
  return (
    <AppBar position="sticky">
      <Container maxWidth="lg">
        <Toolbar>
          {isMobile && (
            <IconButton
              size="large"
              edge="start"
              color="inherit"
              aria-label="menu"
              onClick={handleMobileMenuToggle}
              sx={{ mr: 2 }}
            >
              <MenuIcon />
            </IconButton>
          )}
          
          <Typography
            variant="h6"
            noWrap
            component={RouterLink}
            to="/"
            sx={{
              flexGrow: 1,
              display: { xs: 'none', sm: 'block' },
              textDecoration: 'none',
              color: 'inherit',
              fontWeight: 'bold',
            }}
          >
            E-Commerce
          </Typography>
          
          <Box sx={{ flexGrow: { xs: 0, md: 1 } }} />
          
          <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
            <Button color="inherit" component={RouterLink} to="/">
              Home
            </Button>
            <Button color="inherit" component={RouterLink} to="/products">
              Products
            </Button>
          </Box>
          
          <Box sx={{ display: 'flex' }}>
            <IconButton size="large" color="inherit" component={RouterLink} to="/cart">
              <Badge badgeContent={cartCount} color="secondary">
                <CartIcon />
              </Badge>
            </IconButton>
            
            {user ? (
              <IconButton
                size="large"
                edge="end"
                aria-label="account of current user"
                aria-controls={menuId}
                aria-haspopup="true"
                onClick={handleProfileMenuOpen}
                color="inherit"
              >
                <Avatar
                  sx={{ width: 32, height: 32, bgcolor: 'secondary.main' }}
                  alt={`${user.firstName} ${user.lastName}`}
                >
                  {user.firstName?.charAt(0) || user.username?.charAt(0)}
                </Avatar>
              </IconButton>
            ) : (
              <Button color="inherit" component={RouterLink} to="/login">
                Login
              </Button>
            )}
          </Box>
        </Toolbar>
      </Container>
      {renderMenu}
      {drawer}
    </AppBar>
  );
};

export default Header; 