import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import Home from './components/Home';
import './App.css';

function App() {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) {
    return <div>Loading...</div>;
  }

  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <div className="nav-brand">
            <Link to="/">E-Commerce</Link>
          </div>
          <div className="nav-links">
            <Link to="/">Home</Link>
            {keycloak.authenticated ? (
              <>
                <button onClick={() => keycloak.logout()}>Logout</button>
                <span>Welcome, {keycloak.tokenParsed?.preferred_username}</span>
              </>
            ) : (
              <button onClick={() => keycloak.login()}>Login</button>
            )}
          </div>
        </nav>

        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App; 