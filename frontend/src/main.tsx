import React from 'react';
import ReactDOM from 'react-dom/client';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import App from './App';
import './index.css';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import keycloak, { initOptions } from './config/keycloak';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <ReactKeycloakProvider
        authClient={keycloak}
        initOptions={initOptions}
        onTokens={tokens => {
          console.log('Keycloak tokens:', tokens);
        }}
        onEvent={event => {
          console.log('Keycloak event:', event);
        }}
      >
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </ReactKeycloakProvider>
    </QueryClientProvider>
  </React.StrictMode>
); 