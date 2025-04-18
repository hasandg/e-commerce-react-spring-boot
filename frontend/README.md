# E-Commerce Frontend

This is the frontend application for the E-Commerce platform. It's built with React, TypeScript, Material-UI, and other modern web technologies.

## Features

- User authentication (register, login)
- Product browsing and searching
- Product details with reviews
- e-commerce cart functionality
- Checkout process
- Order history
- User profile management
- Responsive design

## Tech Stack

- React 18
- TypeScript
- Material UI (MUI) for components and styling
- React Router for navigation
- Formik and Yup for form handling and validation
- Axios for API requests

## Getting Started

### Prerequisites

- Node.js (v14 or later)
- npm or yarn

### Installation

1. Clone the repository
2. Navigate to the frontend directory
3. Install dependencies:

```bash
npm install
# or
yarn install
```

### Configuration

Create a `.env` file in the root of the frontend directory with the following variables:

```
REACT_APP_API_URL=http://localhost:9090/api
```

Adjust the URL based on your backend configuration.

### Running the Development Server

```bash
npm start
# or
yarn start
```

The application will be available at [http://localhost:3000](http://localhost:3000).

### Building for Production

```bash
npm run build
# or
yarn build
```

This will create an optimized production build in the `build` folder.

## Project Structure

```
frontend/
├── public/              # Static files
├── src/                 # Source code
│   ├── components/      # React components
│   │   ├── auth/        # Authentication related components
│   │   ├── cart/        # e-commerce cart components
│   │   ├── layout/      # Layout components (header, footer)
│   │   ├── products/    # Product related components
│   │   └── ...          # Other components
│   ├── services/        # API services
│   ├── types/           # TypeScript type definitions
│   ├── utils/           # Utility functions
│   ├── App.tsx          # Main app component
│   └── index.tsx        # Entry point
├── .env                 # Environment variables
├── package.json         # Dependencies and scripts
└── tsconfig.json        # TypeScript configuration
```

## API Integration

The frontend communicates with the backend API using Axios. The API base URL is configured via the `REACT_APP_API_URL` environment variable.

API services are organized in the `src/services` directory, with separate modules for different resource types (products, users, orders, etc.).

## Authentication

The application uses JWT (JSON Web Token) for authentication. The token is stored in localStorage and included in API requests using Axios interceptors.

## Contributing

Please read the contributing guidelines before submitting a pull request.

## License

This project is licensed under the MIT License. 