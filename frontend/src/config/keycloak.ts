import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: 'http://localhost:9090',
  realm: 'ecommerce',
  clientId: 'auth-service'
};

const keycloak = new Keycloak(keycloakConfig);

export const initOptions = {
  onLoad: 'check-sso',
  silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
  pkceMethod: 'S256',
  checkLoginIframe: false
};

export default keycloak; 