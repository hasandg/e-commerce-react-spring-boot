# Online e-commerce App - End-to-End Test Scenarios

## Prerequisites

1. Start all services using Docker Compose:
```bash
docker-compose up -d
```

2. Wait for all services to be healthy:
```bash
docker-compose ps
```

3. Set up environment variables:
```bash
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export KEYCLOAK_CLIENT_SECRET=your_keycloak_client_secret
```

## Test Scenarios

### 1. User Authentication Flow

#### 1.1 Google Sign-In

**Request:**
```bash
curl -X GET "http://localhost:8081/api/auth/google"
```

**Expected Response:**
```json
{
  "authUrl": "https://accounts.google.com/o/oauth2/v2/auth?..."
}
```

**Follow-up Request (After Google Authentication):**
```bash
curl -X GET "http://localhost:8081/api/auth/google/callback?code=..."
```

**Expected Response:**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 3600,
  "token_type": "Bearer"
}
```

### 2. Product Management

#### 2.1 Create Product (Admin)

**Request:**
```bash
curl -X POST "http://localhost:8082/api/products" \
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

**Expected Response:**
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
  "createdAt": "2024-03-29T10:00:00",
  "createdBy": "admin456"
}
```

#### 2.2 Search Products

**Request:**
```bash
curl -X GET "http://localhost:8082/api/products/search?query=test&page=0&size=10"
```

**Expected Response:**
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

### 3. e-commerce Cart Operations

#### 3.1 Add Item to Cart

**Request:**
```bash
curl -X POST "http://localhost:8084/api/carts/items" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "prod456",
    "quantity": 2
  }'
```

**Expected Response:**
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
  "updatedAt": "2024-03-29T11:00:00"
}
```

#### 3.2 Get Cart

**Request:**
```bash
curl -X GET "http://localhost:8084/api/carts" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

**Expected Response:**
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
  "updatedAt": "2024-03-29T11:00:00"
}
```

### 4. Order Processing

#### 4.1 Create Order

**Request:**
```bash
curl -X POST "http://localhost:8085/api/orders" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "shippingAddressId": "addr789",
    "paymentMethod": "CREDIT_CARD"
  }'
```

**Expected Response:**
```json
{
  "id": "ord678",
  "userId": "user123",
  "items": [
    {
      "productId": "prod456",
      "productName": "Test Product",
      "quantity": 2,
      "price": 29.99,
      "subTotal": 59.98
    }
  ],
  "totalAmount": 59.98,
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
  "createdAt": "2024-03-29T12:00:00"
}
```

#### 4.2 Process Payment

**Request:**
```bash
curl -X POST "http://localhost:8086/api/payments" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ord678",
    "amount": 59.98,
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

**Expected Response:**
```json
{
  "id": "pay987",
  "orderId": "ord678",
  "userId": "user123",
  "amount": 59.98,
  "status": "COMPLETED",
  "paymentMethod": "CREDIT_CARD",
  "transactionId": "txn1234567",
  "createdAt": "2024-03-29T12:05:00"
}
```

### 5. Notification System

#### 5.1 Check Order Notifications

**Request:**
```bash
curl -X GET "http://localhost:8083/api/notifications?page=0&size=10" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "X-User-ID: user123"
```

**Expected Response:**
```json
{
  "content": [
    {
      "id": "notif123",
      "userId": "user123",
      "subject": "Order Confirmed",
      "message": "Your order #ORD-678 has been confirmed and is being processed.",
      "type": "ORDER",
      "status": "SENT",
      "relatedEntityId": "ord678",
      "relatedEntityType": "ORDER",
      "read": false,
      "createdAt": "2024-03-29T12:05:00"
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

## End-to-End Test Scenarios

### 1. Complete Order Flow

1. User signs in with Google
2. User browses products
3. User adds items to cart
4. User proceeds to checkout
5. User creates order
6. Payment is processed
7. Order is confirmed
8. User receives notification
9. User views order status

### 2. Order Cancellation Flow

1. User signs in with Google
2. User adds items to cart
3. User creates order
4. User cancels order before payment
5. User receives cancellation notification
6. User views updated order status

### 3. Payment Failure and Retry

1. User signs in with Google
2. User adds items to cart
3. User creates order
4. Payment fails (simulated)
5. User retries payment
6. Payment succeeds
7. User receives confirmation notification

### 4. Product Review Flow

1. User signs in with Google
2. User views past orders
3. User leaves a review for a product
4. Product rating is updated
5. Admin receives notification of new review

## Performance Test Scenarios

### 1. High Volume Cart Operations

- Simulate 100+ concurrent users adding items to cart
- Monitor response times and error rates
- Verify cart consistency

### 2. Order Processing Under Load

- Process 50+ orders per minute
- Monitor payment processing throughput
- Verify order status updates

### 3. Notification Delivery

- Monitor notification delivery latency
- Verify notification consistency
- Check notification queue performance

## Security Test Scenarios

### 1. Authentication and Authorization

- Verify JWT token validation
- Test role-based access control
- Check token expiration handling

### 2. Data Protection

- Verify sensitive data encryption
- Check payment information handling
- Test input validation

### 3. API Security

- Test rate limiting
- Verify CORS configuration
- Check API endpoint protection

## Monitoring and Observability

### 1. Metrics Collection

- Monitor service health metrics
- Track business metrics
- Verify log collection

### 2. Distributed Tracing

- Track request flow across services
- Monitor service dependencies
- Analyze performance bottlenecks

### 3. Alerting

- Test alert configurations
- Verify notification channels
- Check alert thresholds

## Conclusion

These test scenarios provide comprehensive coverage of the Online e-commerce App's functionality, performance, and security aspects. The tests ensure:

1. Proper user authentication and authorization
2. Correct order processing and payment handling
3. Reliable notification delivery
4. System performance under load
5. Security of sensitive data
6. Proper monitoring and observability

For automated testing, these scenarios can be implemented using:
- JUnit for unit tests
- TestContainers for integration tests
- JMeter or Gatling for performance tests
- Postman or Newman for API tests
- Custom test harness for end-to-end tests 