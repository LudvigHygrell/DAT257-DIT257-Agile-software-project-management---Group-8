import { Routes, Route } from "react-router-dom";

import Navbar from './components/Navbar.jsx';
import CharityList from './components/CharityList.jsx';
import TopCharities from './components/TopCharities.jsx';
import CharityPage from './pages/NewCharityPage.jsx';
import AccountSettings from './pages/AccountSettings.jsx';
import Modals from './components/Modals.jsx';
import { useModals } from './hooks/useModals.js';
import { useAuth } from './hooks/useAuth.js';

import './styles/App.css';


// Main App component that contains all other components
function App() {
  // Use custom hook to manage authentication state
  const { isAuthenticated, username, login, logout, isValidating } = useAuth();

  // Handle logout
  const handleLogout = () => {
    logout();
    // Redirect to homepage and reload to reset state
    window.location.href = '/';
  };

  // Use custom hook to manage all modal state and handlers
  // Pass login function to modals so LoginModal can update auth state
  const modalProps = useModals(login);

  // Show loading state while validating token
  if (isValidating) {
    return (
      <div className="app-container" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <p>Loading...</p>
      </div>
    );
  }

  return (
    // main cointainer with padding
    <div className="app-container">
      {/* Navigation bar at top - receives function to show login modal and auth state */}
      <Navbar
        onLoginClick={modalProps.handleLoginClick}
        isAuthenticated={isAuthenticated}
        username={username}
        onLogout={handleLogout}
      />

      <Routes>
        {/* Main page with two columns layout */}
        <Route
          path="/"
          element={
            <div className="main-content">
              { /* Left column: shows top rated charities (1/3 of page width) */}
              <div className="panel panel-small">
                <TopCharities />
              </div>

              {/* Right column: shows searchable charity lists (2/3 of page width) */}
              <div className="panel panel-large">
                <CharityList />
              </div>
            </div>
          }
        />
        {/* Charity detail page */}
        <Route path="/:orgId" element={<CharityPage />} />
        {/* Account Settings page */}
        <Route
          path="/settings"
          element={<AccountSettings username={username} onLogout={handleLogout} />}
        />
      </Routes>

      {/* All modals for the application */}
      <Modals {...modalProps} />
    </div>

  );
}

export default App;