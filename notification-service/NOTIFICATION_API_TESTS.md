# Notification Service API Tests

This document contains test scenarios for all endpoints in the Notification Service.

## Prerequisites

- Notification service is running on port 8083
- A valid JWT token is available for authentication
- Postgresql database is set up and running

## Environment Setup

```bash
# Set base URL for API calls
BASE_URL=http://localhost:8083

# Set authorization token (replace with valid JWT)
TOKEN="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Test Cases

### 1. Create Notification

#### Request

```bash
curl -X POST "${BASE_URL}/api/notifications" \
  -H "Authorization: ${TOKEN}" \
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

#### Expected Response

```json
{
  "id": 1,
  "userId": "user123",
  "subject": "New Order Placed",
  "message": "Your order #ORD-12345 has been placed successfully.",
  "type": "IN_APP",
  "status": "PENDING",
  "relatedEntityId": "ORD-12345",
  "relatedEntityType": "ORDER",
  "read": false,
  "createdAt": "2023-08-01T10:15:30",
  "updatedAt": null,
  "sentAt": null,
  "readAt": null
}
```

#### Validation

- The notification should be created with status PENDING
- The `read` flag should be false
- The `createdAt` timestamp should be set
- The ID should be assigned

### 2. Create Notification - Invalid Request (Missing Required Fields)

#### Request

```bash
curl -X POST "${BASE_URL}/api/notifications" \
  -H "Authorization: ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "message": "Incomplete notification",
    "type": "IN_APP"
  }'
```

#### Expected Response

```json
{
  "status": 400,
  "message": "Validation Failed",
  "timestamp": "2023-08-01T10:16:30",
  "errors": {
    "subject": "Subject is required"
  }
}
```

#### Validation

- Response status code should be 400
- Error message should indicate which required field is missing

### 3. Get User Notifications (Paginated)

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications?page=0&size=10" \
  -H "Authorization: ${TOKEN}" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
{
  "content": [
    {
      "id": 1,
      "userId": "user123",
      "subject": "New Order Placed",
      "message": "Your order #ORD-12345 has been placed successfully.",
      "type": "IN_APP",
      "status": "PENDING",
      "relatedEntityId": "ORD-12345",
      "relatedEntityType": "ORDER",
      "read": false,
      "createdAt": "2023-08-01T10:15:30",
      "updatedAt": null,
      "sentAt": null,
      "readAt": null
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageSize": 10,
    "pageNumber": 0,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "size": 10,
  "number": 0,
  "empty": false
}
```

#### Validation

- Response should include a paginated list of notifications for the specified user
- Notifications should be sorted by creation date in descending order

### 4. Get Unread Notifications

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications/unread" \
  -H "Authorization: ${TOKEN}" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
[
  {
    "id": 1,
    "userId": "user123",
    "subject": "New Order Placed",
    "message": "Your order #ORD-12345 has been placed successfully.",
    "type": "IN_APP",
    "status": "PENDING",
    "relatedEntityId": "ORD-12345",
    "relatedEntityType": "ORDER",
    "read": false,
    "createdAt": "2023-08-01T10:15:30",
    "updatedAt": null,
    "sentAt": null,
    "readAt": null
  }
]
```

#### Validation

- Response should include only unread notifications for the specified user
- Only notifications with `read=false` should be included

### 5. Get Unread Notification Count

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications/count" \
  -H "Authorization: ${TOKEN}" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
{
  "count": 1
}
```

#### Validation

- Response should include the count of unread notifications for the specified user

### 6. Mark Notification as Read

#### Request

```bash
curl -X PUT "${BASE_URL}/api/notifications/1/read" \
  -H "Authorization: ${TOKEN}"
```

#### Expected Response

```json
{
  "id": 1,
  "userId": "user123",
  "subject": "New Order Placed",
  "message": "Your order #ORD-12345 has been placed successfully.",
  "type": "IN_APP",
  "status": "PENDING",
  "relatedEntityId": "ORD-12345",
  "relatedEntityType": "ORDER",
  "read": true,
  "createdAt": "2023-08-01T10:15:30",
  "updatedAt": "2023-08-01T10:20:30",
  "sentAt": null,
  "readAt": "2023-08-01T10:20:30"
}
```

#### Validation

- The `read` flag should be set to true
- The `readAt` timestamp should be set
- The `updatedAt` timestamp should be updated

### 7. Mark Notification as Read - Notification Not Found

#### Request

```bash
curl -X PUT "${BASE_URL}/api/notifications/999/read" \
  -H "Authorization: ${TOKEN}"
```

#### Expected Response

```json
{
  "status": 404,
  "message": "Notification not found with id: '999'",
  "timestamp": "2023-08-01T10:21:30"
}
```

#### Validation

- Response status code should be 404
- Error message should indicate that the notification was not found

### 8. Mark All Notifications as Read

#### Request

```bash
curl -X PUT "${BASE_URL}/api/notifications/read-all" \
  -H "Authorization: ${TOKEN}" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
[
  {
    "id": 1,
    "userId": "user123",
    "subject": "New Order Placed",
    "message": "Your order #ORD-12345 has been placed successfully.",
    "type": "IN_APP",
    "status": "PENDING",
    "relatedEntityId": "ORD-12345",
    "relatedEntityType": "ORDER",
    "read": true,
    "createdAt": "2023-08-01T10:15:30",
    "updatedAt": "2023-08-01T10:22:30",
    "sentAt": null,
    "readAt": "2023-08-01T10:22:30"
  }
]
```

#### Validation

- All notifications for the specified user should have `read=true`
- The `readAt` timestamp should be set for all notifications
- The `updatedAt` timestamp should be updated for all notifications

### 9. Delete Notification

#### Request

```bash
curl -X DELETE "${BASE_URL}/api/notifications/1" \
  -H "Authorization: ${TOKEN}"
```

#### Expected Response

```
HTTP/1.1 204 No Content
```

#### Validation

- Response status code should be 204 (No Content)
- The notification should be deleted from the database

### 10. Delete Notification - Notification Not Found

#### Request

```bash
curl -X DELETE "${BASE_URL}/api/notifications/999" \
  -H "Authorization: ${TOKEN}"
```

#### Expected Response

```json
{
  "status": 404,
  "message": "Notification not found with id: '999'",
  "timestamp": "2023-08-01T10:24:30"
}
```

#### Validation

- Response status code should be 404
- Error message should indicate that the notification was not found

### 11. Get Notifications by Status

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications/status/SENT" \
  -H "Authorization: ${TOKEN}" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
[
  {
    "id": 2,
    "userId": "user123",
    "subject": "Order Shipped",
    "message": "Your order #ORD-12345 has been shipped.",
    "type": "EMAIL",
    "status": "SENT",
    "relatedEntityId": "ORD-12345",
    "relatedEntityType": "ORDER",
    "read": false,
    "createdAt": "2023-08-01T10:25:30",
    "updatedAt": "2023-08-01T10:25:40",
    "sentAt": "2023-08-01T10:25:40",
    "readAt": null
  }
]
```

#### Validation

- Response should include only notifications with the specified status for the specified user

### 12. Get Notifications by Status - Empty Result

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications/status/FAILED" \
  -H "Authorization: ${TOKEN}" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
[]
```

#### Validation

- Response should be an empty array when no notifications match the criteria

## Test Cases for Edge Cases and Error Handling

### 13. Create Notification with Invalid Type

#### Request

```bash
curl -X POST "${BASE_URL}/api/notifications" \
  -H "Authorization: ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "subject": "Invalid Type Test",
    "message": "This has an invalid notification type.",
    "type": "INVALID_TYPE",
    "relatedEntityId": "TEST-123",
    "relatedEntityType": "TEST"
  }'
```

#### Expected Response

```json
{
  "status": 400,
  "message": "Validation Failed",
  "timestamp": "2023-08-01T10:30:30",
  "errors": {
    "type": "must be one of [EMAIL, SMS, PUSH, IN_APP]"
  }
}
```

#### Validation

- Response status code should be 400
- Error message should indicate that the notification type is invalid

### 14. Access API without Authentication

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications/unread" \
  -H "X-User-ID: user123"
```

#### Expected Response

```json
{
  "status": 401,
  "message": "Unauthorized",
  "timestamp": "2023-08-01T10:31:30"
}
```

#### Validation

- Response status code should be 401
- Error message should indicate that authentication is required

### 15. Access API without Required Headers

#### Request

```bash
curl -X GET "${BASE_URL}/api/notifications/unread" \
  -H "Authorization: ${TOKEN}"
```

#### Expected Response

```json
{
  "status": 400,
  "message": "Missing required header 'X-User-ID'",
  "timestamp": "2023-08-01T10:32:30"
}
```

#### Validation

- Response status code should be 400
- Error message should indicate that the required header is missing

## Kafka Event Processing Tests

### 16. Send Order Notification Event to Kafka

Use a Kafka producer client to send this message to the `order-notifications` topic:

```json
{
  "orderId": "ORD-67890",
  "userId": "user123",
  "subject": "Order Confirmed",
  "message": "Your order #ORD-67890 has been confirmed and is being processed."
}
```

#### Verification

- Check the database to confirm that a new notification was created
- The notification should have `type` set to `IN_APP`
- The notification should have `relatedEntityType` set to `ORDER`
- The notification should have `relatedEntityId` set to `ORD-67890`

### 17. Send User Notification Event to Kafka

Use a Kafka producer client to send this message to the `user-notifications` topic:

```json
{
  "userId": "user123",
  "subject": "Welcome to Our Platform",
  "message": "Thank you for signing up. Enjoy your e-commerce!",
  "type": "EMAIL",
  "relatedEntityId": "user123",
  "relatedEntityType": "USER"
}
```

#### Verification

- Check the database to confirm that a new notification was created
- The notification should have all fields matching the Kafka message content

## Performance and Load Testing Scenarios

### 18. Create Multiple Notifications (Bulk Test)

Create a script to send 100 notification creation requests in parallel and measure response times.

### 19. Get Notifications with Large Dataset

Seed the database with 1000+ notifications for a user and test the paginated retrieval endpoint performance.

### 20. Mark All as Read with Large Dataset

Seed the database with 1000+ unread notifications for a user and test the mark-all-as-read endpoint performance.

## Conclusion

These test scenarios cover the core functionality of the Notification Service API, including:

- Basic CRUD operations
- Error handling
- Edge cases
- Kafka event processing
- Performance under load

For automated testing, these scenarios can be implemented using tools like JUnit, Postman, or a custom test harness. 