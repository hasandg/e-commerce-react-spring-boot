# PostgreSQL and MongoDB Usage in E-Commerce Microservices

## Database Strategy Overview

This e-commerce application employs a polyglot persistence approach, strategically using both PostgreSQL and MongoDB depending on the specific needs of each microservice:

- **PostgreSQL**: Used for structured data with complex relationships, strict schema requirements, and ACID transaction needs
- **MongoDB**: Used for document-oriented data with flexible schemas, nested structures, and high read/write throughput requirements

## Microservice-Specific Database Usage

### 1. Product Service
**Database**: MongoDB  
**Configuration**: `mongodb://localhost:27017/product-service`

**Rationale**:
- Products have complex, nested structures (specifications, images, attributes)
- Product catalog requires frequent schema evolution for new attributes
- High read throughput for catalog browsing
- Document model fits naturally with product representation
- Product search benefits from MongoDB's text search capabilities

**Implementation**:
- Uses `@Document` annotations for MongoDB collections
- Implements specialized queries for product search and filtering
- Utilizes MongoDB's indexing for performance optimization

### 2. Cart Service
**Database**: MongoDB  
**Configuration**: Uses MongoRepository for cart storage

**Rationale**:
- e-commerce carts have dynamic structures with varying items
- Carts require frequent updates as users add/remove items
- Document model allows embedding product details within cart items
- Low consistency requirements (eventual consistency is acceptable)

**Implementation**:
- MongoDB for persistent cart storage
- Redis for caching active cart sessions

### 3. Order Service
**Database**: MongoDB  
**Configuration**: `mongodb://localhost:27017/order-service`

**Rationale**:
- Orders contain nested structures (line items, shipping details)
- Order history benefits from document-based storage
- Historical orders rarely change after creation
- Need to store heterogeneous order data with varying attributes

**Implementation**:
- Uses MongoDB to store complete order documents
- Implements query capabilities for order history and filtering

### 4. Payment Service
**Database**: MongoDB  
**Configuration**: `mongodb://localhost:27017/payment-db`

**Rationale**:
- Payment transactions have varying structures based on payment methods
- Quick lookup of payment history
- Document model suits the event-like nature of payment records
- Need to support different payment providers with distinct data requirements

**Implementation**:
- Stores payment transactions as documents
- Uses MongoDB's transaction capabilities for critical payment operations

### 5. Auth Service
**Database**: PostgreSQL  
**Configuration**: JDBC connection to PostgreSQL database

**Rationale**:
- User authentication requires ACID transactions
- Relational model fits user identity and role relationships
- Security concerns favor the mature transactional capabilities of PostgreSQL
- Integration with Keycloak which uses relational database

**Implementation**:
- Uses Spring Data JPA with PostgreSQL
- Stores authentication-related data
- Keycloak integration for identity management

### 6. Notification Service
**Database**: Mixed (PostgreSQL & MongoDB)  
**Configuration**: 
- PostgreSQL for core notification data
- MongoDB for notification templates and content

**Rationale**:
- Structured notification metadata suits PostgreSQL
- Flexible notification content and templates benefit from MongoDB's schema flexibility
- Different types of notifications require different structures

**Implementation**:
- Core notification records stored in PostgreSQL
- Notification templates and dynamic content stored in MongoDB

## Key Considerations for Database Choices

1. **Data Structure**:
   - **PostgreSQL**: Used for highly structured data with well-defined relationships
   - **MongoDB**: Used for semi-structured data with varying attributes

2. **Query Patterns**:
   - **PostgreSQL**: Complex joins, transactions, and relationship-heavy queries
   - **MongoDB**: Document retrieval, flexible queries, and high-throughput reads

3. **Scalability Requirements**:
   - **PostgreSQL**: Services with moderate write loads but complex transactions
   - **MongoDB**: Services needing horizontal scaling for high read/write throughput

4. **Development Agility**:
   - **MongoDB**: Services requiring frequent schema changes during development
   - **PostgreSQL**: Services with stable, well-defined data models

5. **Consistency Requirements**:
   - **PostgreSQL**: Critical business operations requiring strong consistency
   - **MongoDB**: Features where eventual consistency is acceptable

## Infrastructure Setup

Both databases are containerized using Docker:

- **PostgreSQL**: Running on port 5432 with a shared volume for persistence
- **MongoDB**: Running on port 27017 with a shared volume for persistence

Database instances are configured in the `docker-compose.yml` file, making it easy to start the entire infrastructure stack with a single command.

## Best Practices Implemented

1. **Domain-Specific Data Storage**: Each microservice owns its database, following the microservices principle of decentralized data management
2. **Polyglot Persistence**: Using the right database for the right job based on data characteristics
3. **Configuration Externalization**: Database configurations managed through environment variables and Spring Cloud Config
4. **Connection Pooling**: Optimized connections for efficient resource utilization
5. **Database Indexing**: Strategic indexes for performance optimization

This mixed database approach provides the flexibility and performance needed for different aspects of the e-commerce platform while maintaining the bounded context isolation that's central to the microservices architecture. 