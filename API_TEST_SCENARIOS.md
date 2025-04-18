# Online e-commerce App API Test Scenarios

This document contains test scenarios for all microservices in the Online e-commerce App.

## Table of Contents

1. [Keycloak Authentication](#keycloak-authentication)
2. [User Profile Service](#user-profile-service)
3. [Product Service](#product-service)
4. [Cart Service](#cart-service)
5. [Order Service](#order-service)
6. [Payment Service](#payment-service)
7. [Notification Service](#notification-service)

## Test Environment Setup

```bash
# Set base URLs for each service
KEYCLOAK_URL=http://localhost:9090
USER_PROFILE_SERVICE=http://localhost:8081
PRODUCT_SERVICE=http://localhost:8082
NOTIFICATION_SERVICE=http://localhost:8083
CART_SERVICE=http://localhost:8084
ORDER_SERVICE=http://localhost:8085
PAYMENT_SERVICE=http://localhost:8086

# Keycloak configuration
KEYCLOAK_REALM=e-commerce-app
KEYCLOAK_CLIENT_ID=e-commerce-app-client
KEYCLOAK_CLIENT_SECRET=your-client-secret

# User IDs for testing
USER_ID="user123"  # Keycloak user ID
ADMIN_ID="admin456"  # Keycloak user ID
```

## Keycloak Authentication

### 1. User Registration

#### Request

```bash
curl -X POST "${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/registrations" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Password123!",
    "firstName": "Test",
    "lastName": "User",
    "attributes": {
      "phoneNumber": "1234567890"
    }
  }'
```

#### Expected Response

```json
{
  "id": "user123",
  "username": "testuser",
  "email": "test@example.com",
  "firstName": "Test",
  "lastName": "User",
  "attributes": {
    "phoneNumber": "1234567890"
  },
  "enabled": true,
  "emailVerified": false,
  "createdTimestamp": 1690896000000
}
```

### 2. User Login

#### Request

```bash
curl -X POST "${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=${KEYCLOAK_CLIENT_ID}" \
  -d "client_secret=${KEYCLOAK_CLIENT_SECRET}" \
  -d "username=testuser" \
  -d "password=Password123!"
```

#### Expected Response

```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 3600,
  "refresh_expires_in": 1800,
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Get User Profile

#### Request

```bash
curl -X GET "${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/userinfo" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "sub": "user123",
  "username": "testuser",
  "email": "test@example.com",
  "given_name": "Test",
  "family_name": "User",
  "phone_number": "1234567890",
  "email_verified": false
}
```

## User Profile Service

### 1. Create User Profile

#### Request

```bash
curl -X POST "${USER_PROFILE_SERVICE}/api/profiles" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "preferences": {
      "language": "en",
      "currency": "USD",
      "notifications": {
        "email": true,
        "push": true
      }
    }
  }'
```

#### Expected Response

```json
{
  "id": "profile123",
  "userId": "user123",
  "preferences": {
    "language": "en",
    "currency": "USD",
    "notifications": {
      "email": true,
      "push": true
    }
  },
  "createdAt": "2023-08-01T10:00:00"
}
```

### 2. Add User Address

#### Request

```bash
curl -X POST "${USER_PROFILE_SERVICE}/api/profiles/addresses" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "addressLine1": "123 Main St",
    "addressLine2": "Apt 45",
    "city": "New York",
    "state": "NY",
    "postalCode": "10001",
    "country": "USA",
    "isDefault": true,
    "addressType": "SHIPPING"
  }'
```

#### Expected Response

```json
{
  "id": "addr789",
  "userId": "user123",
  "addressLine1": "123 Main St",
  "addressLine2": "Apt 45",
  "city": "New York",
  "state": "NY",
  "postalCode": "10001",
  "country": "USA",
  "isDefault": true,
  "addressType": "SHIPPING",
  "createdAt": "2023-08-01T12:00:00"
}
```

## Product Service

### 1. Create Product (Admin)

#### Request

```bash
curl -X POST "${PRODUCT_SERVICE}/api/products" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "This is a test product",
    "price": 29.99,
    "categoryId": "cat123",
    "inventory": 100,
    "images": ["http://example.com/image1.jpg"],
    "specifications": {
      "weight": "1.5kg",
      "dimensions": "10x20x30cm",
      "color": "Blue"
    }
  }'
```

#### Expected Response

```json
{
  "id": "prod456",
  "name": "Test Product",
  "description": "This is a test product",
  "price": 29.99,
  "categoryId": "cat123",
  "inventory": 100,
  "images": ["http://example.com/image1.jpg"],
  "specifications": {
    "weight": "1.5kg",
    "dimensions": "10x20x30cm",
    "color": "Blue"
  },
  "rating": 0,
  "reviewCount": 0,
  "createdAt": "2023-08-01T13:00:00",
  "createdBy": "admin456"
}
```

### 2. Get Product by ID

#### Request

```bash
curl -X GET "${PRODUCT_SERVICE}/api/products/prod456"
```

#### Expected Response

```json
{
  "id": "prod456",
  "name": "Test Product",
  "description": "This is a test product",
  "price": 29.99,
  "categoryId": "cat123",
  "inventory": 100,
  "images": ["http://example.com/image1.jpg"],
  "specifications": {
    "weight": "1.5kg",
    "dimensions": "10x20x30cm",
    "color": "Blue"
  },
  "rating": 0,
  "reviewCount": 0,
  "createdAt": "2023-08-01T13:00:00"
}
```

### 3. Search Products

#### Request

```bash
curl -X GET "${PRODUCT_SERVICE}/api/products/search?query=test&page=0&size=10"
```

#### Expected Response

```json
{
  "content": [
    {
      "id": "prod456",
      "name": "Test Product",
      "description": "This is a test product",
      "price": 29.99,
      "categoryId": "cat123",
      "inventory": 100,
      "images": ["http://example.com/image1.jpg"],
      "rating": 0,
      "reviewCount": 0
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

### 4. Add Product Review

#### Request

```bash
curl -X POST "${PRODUCT_SERVICE}/api/products/prod456/reviews" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 4,
    "comment": "Great product, works as expected!"
  }'
```

#### Expected Response

```json
{
  "id": "rev789",
  "productId": "prod456",
  "userId": "user123",
  "rating": 4,
  "comment": "Great product, works as expected!",
  "createdAt": "2023-08-01T14:00:00"
}
```

## Cart Service

### 1. Add Item to Cart

#### Request

```bash
curl -X POST "${CART_SERVICE}/api/carts/items" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "prod456",
    "quantity": 2
  }'
```

#### Expected Response

```json
{
  "cartId": "cart123",
  "userId": "user123",
  "items": [
    {
      "id": "item345",
      "productId": "prod456",
      "productName": "Test Product",
      "price": 29.99,
      "quantity": 2,
      "subTotal": 59.98
    }
  ],
  "totalItems": 2,
  "totalPrice": 59.98,
  "updatedAt": "2023-08-01T15:00:00"
}
```

### 2. Get Cart

#### Request

```bash
curl -X GET "${CART_SERVICE}/api/carts" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "cartId": "cart123",
  "userId": "user123",
  "items": [
    {
      "id": "item345",
      "productId": "prod456",
      "productName": "Test Product",
      "price": 29.99,
      "quantity": 2,
      "subTotal": 59.98
    }
  ],
  "totalItems": 2,
  "totalPrice": 59.98,
  "updatedAt": "2023-08-01T15:00:00"
}
```

### 3. Update Cart Item Quantity

#### Request

```bash
curl -X PUT "${CART_SERVICE}/api/carts/items/item345" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 3
  }'
```

#### Expected Response

```json
{
  "cartId": "cart123",
  "userId": "user123",
  "items": [
    {
      "id": "item345",
      "productId": "prod456",
      "productName": "Test Product",
      "price": 29.99,
      "quantity": 3,
      "subTotal": 89.97
    }
  ],
  "totalItems": 3,
  "totalPrice": 89.97,
  "updatedAt": "2023-08-01T15:30:00"
}
```

### 4. Remove Item from Cart

#### Request

```bash
curl -X DELETE "${CART_SERVICE}/api/carts/items/item345" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "cartId": "cart123",
  "userId": "user123",
  "items": [],
  "totalItems": 0,
  "totalPrice": 0,
  "updatedAt": "2023-08-01T16:00:00"
}
```

## Order Service

### 1. Create Order

#### Request

```bash
curl -X POST "${ORDER_SERVICE}/api/orders" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "shippingAddressId": "addr789",
    "paymentMethod": "CREDIT_CARD"
  }'
```

#### Expected Response

```json
{
  "id": "ord678",
  "userId": "user123",
  "items": [
    {
      "productId": "prod456",
      "productName": "Test Product",
      "quantity": 3,
      "price": 29.99,
      "subTotal": 89.97
    }
  ],
  "totalAmount": 89.97,
  "shippingAddress": {
    "addressLine1": "123 Main St",
    "addressLine2": "Apt 45",
    "city": "New York",
    "state": "NY",
    "postalCode": "10001",
    "country": "USA"
  },
  "status": "PENDING",
  "paymentStatus": "PENDING",
  "paymentMethod": "CREDIT_CARD",
  "createdAt": "2023-08-01T17:00:00"
}
```

### 2. Get Order by ID

#### Request

```bash
curl -X GET "${ORDER_SERVICE}/api/orders/ord678" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "id": "ord678",
  "userId": "user123",
  "items": [
    {
      "productId": "prod456",
      "productName": "Test Product",
      "quantity": 3,
      "price": 29.99,
      "subTotal": 89.97
    }
  ],
  "totalAmount": 89.97,
  "shippingAddress": {
    "addressLine1": "123 Main St",
    "addressLine2": "Apt 45",
    "city": "New York",
    "state": "NY",
    "postalCode": "10001",
    "country": "USA"
  },
  "status": "PENDING",
  "paymentStatus": "PENDING",
  "paymentMethod": "CREDIT_CARD",
  "createdAt": "2023-08-01T17:00:00"
}
```

### 3. Get User Orders

#### Request

```bash
curl -X GET "${ORDER_SERVICE}/api/orders?page=0&size=10" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "content": [
    {
      "id": "ord678",
      "userId": "user123",
      "totalAmount": 89.97,
      "status": "PENDING",
      "paymentStatus": "PENDING",
      "createdAt": "2023-08-01T17:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

### 4. Cancel Order

#### Request

```bash
curl -X PUT "${ORDER_SERVICE}/api/orders/ord678/cancel" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "id": "ord678",
  "userId": "user123",
  "status": "CANCELLED",
  "paymentStatus": "REFUNDED",
  "updatedAt": "2023-08-01T18:00:00"
}
```

## Payment Service

### 1. Process Payment

#### Request

```bash
curl -X POST "${PAYMENT_SERVICE}/api/payments" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ord678",
    "amount": 89.97,
    "paymentMethod": "CREDIT_CARD",
    "cardDetails": {
      "cardNumber": "4111111111111111",
      "expiryMonth": "12",
      "expiryYear": "2025",
      "cvv": "123",
      "cardHolderName": "Test User"
    }
  }'
```

#### Expected Response

```json
{
  "id": "pay987",
  "orderId": "ord678",
  "userId": "user123",
  "amount": 89.97,
  "status": "COMPLETED",
  "paymentMethod": "CREDIT_CARD",
  "transactionId": "txn1234567",
  "createdAt": "2023-08-01T19:00:00"
}
```

### 2. Get Payment by ID

#### Request

```bash
curl -X GET "${PAYMENT_SERVICE}/api/payments/pay987" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "id": "pay987",
  "orderId": "ord678",
  "userId": "user123",
  "amount": 89.97,
  "status": "COMPLETED",
  "paymentMethod": "CREDIT_CARD",
  "transactionId": "txn1234567",
  "createdAt": "2023-08-01T19:00:00"
}
```

### 3. Get User Payments

#### Request

```bash
curl -X GET "${PAYMENT_SERVICE}/api/payments?page=0&size=10" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

#### Expected Response

```json
{
  "content": [
    {
      "id": "pay987",
      "orderId": "ord678",
      "amount": 89.97,
      "status": "COMPLETED",
      "paymentMethod": "CREDIT_CARD",
      "createdAt": "2023-08-01T19:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

### 4. Refund Payment

#### Request

```bash
curl -X POST "${PAYMENT_SERVICE}/api/payments/pay987/refund" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 89.97,
    "reason": "Customer cancelled order"
  }'
```

#### Expected Response

```json
{
  "id": "ref654",
  "paymentId": "pay987",
  "orderId": "ord678",
  "userId": "user123",
  "amount": 89.97,
  "status": "COMPLETED",
  "reason": "Customer cancelled order",
  "transactionId": "txn7654321",
  "createdAt": "2023-08-01T20:00:00"
}
```

## Notification Service

The complete test scenarios for the Notification Service can be found in the [NOTIFICATION_API_TESTS.md](notification-service/NOTIFICATION_API_TESTS.md) document. Here's a summary of the key endpoints:

### 1. Create Notification

#### Request

```bash
curl -X POST "${NOTIFICATION_SERVICE}/api/notifications" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "subject": "New Order Placed",
    "message": "Your order #ORD-12345 has been placed successfully.",
    "type": "IN_APP",
    "relatedEntityId": "ORD-12345",
    "relatedEntityType": "ORDER"
  }'
```

### 2. Get User Notifications

#### Request

```bash
curl -X GET "${NOTIFICATION_SERVICE}/api/notifications?page=0&size=10" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "X-User-ID: user123"
```

### 3. Get Unread Notifications

#### Request

```bash
curl -X GET "${NOTIFICATION_SERVICE}/api/notifications/unread" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "X-User-ID: user123"
```

### 4. Mark Notification as Read

#### Request

```bash
curl -X PUT "${NOTIFICATION_SERVICE}/api/notifications/1/read" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

## Integration Test Scenarios

### 1. E2E Order Placement and Fulfillment

1. User logs in through Keycloak and gets authentication token
2. User adds products to cart
3. User proceeds to checkout and creates an order
4. Payment is processed successfully
5. Order status is updated to CONFIRMED
6. Notification is sent to the user
7. User views order details
8. Admin updates order status to SHIPPED
9. Notification is sent to the user
10. Admin updates order status to DELIVERED
11. Notification is sent to the user
12. User leaves a review for the product

### 2. Order Cancellation Flow

1. User logs in through Keycloak and gets authentication token
2. User adds products to cart
3. User proceeds to checkout and creates an order
4. User cancels the order before payment
5. Order status is updated to CANCELLED
6. Notification is sent to the user

### 3. Payment Failure and Retry

1. User logs in through Keycloak and gets authentication token
2. User adds products to cart
3. User proceeds to checkout and creates an order
4. Payment fails due to insufficient funds
5. User retries payment with a different payment method
6. Payment succeeds
7. Order status is updated to CONFIRMED
8. Notification is sent to the user

## Performance Test Scenarios

1. High volume cart operations (100+ concurrent users)
2. Bulk product catalog browsing and searching
3. Order processing under load (50+ orders per minute)
4. Payment processing throughput
5. Notification delivery latency

## Security Test Scenarios

1. Keycloak authentication and authorization checks for all endpoints
2. Data validation and input sanitization
3. JWT token validation and expiration
4. Cross-service communication security
5. Payment information handling compliance

## Conclusion

These test scenarios provide comprehensive coverage of the Online e-commerce App microservices and their endpoints. Implementing these tests will help ensure the application's reliability, performance, and security.

For automated testing, these scenarios can be implemented using tools like JUnit, Postman, or a custom test harness. 