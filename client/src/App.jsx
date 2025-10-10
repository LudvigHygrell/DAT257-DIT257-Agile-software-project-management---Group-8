import { Routes, Route } from "react-router-dom";

import Navbar from './components/Navbar.jsx';
import CharityList from './components/CharityList.jsx';
import TopCharities from './components/TopCharities.jsx';
import CharityPage from './pages/NewCharityPage.jsx';
import AccountSettings from './pages/AccountSettings.jsx';
import Modals from './components/Modals.jsx';
import { fake_charities } from './Dummydata.jsx';
import { useModals } from './hooks/useModals.js';
import { useAuth } from './hooks/useAuth.js';

import './styles/App.css';


// Main App component that contains all other components
function App() {
  // Use custom hook to manage authentication state
  const { isAuthenticated, username, login, logout } = useAuth();

  // Handle logout
  const handleLogout = () => {
    logout();
    // Redirect to homepage and reload to reset state
    window.location.href = '/';
  };

  // Use custom hook to manage all modal state and handlers
  // Pass login function to modals so LoginModal can update auth state
  const modalProps = useModals(login);

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
              <div className="panel" style={{ flex: 1 }}>
                <TopCharities charities={fake_charities} />
              </div>

              {/* Right column: shows searchable charity lists (2/3 of page width) */}
              <div className="panel" style={{ flex: 2 }}>
                <CharityList charities={fake_charities} />
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