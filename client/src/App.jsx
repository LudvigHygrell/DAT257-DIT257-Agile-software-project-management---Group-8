import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Navbar from './components/Navbar.jsx';
import CharityList from './components/CharityList.jsx';
import TopCharities from './components/TopCharities.jsx';
import CharityPage from './pages/CharityPage.jsx';
import { fake_charities } from './Dummydata.jsx';
import LoginModal from './components/LoginModal.jsx';
import Registration from './components/Registration.jsx';

import './styles/App.css';


// Main App component that contains all other components
function App() {
  // State to control whether login modal is visible or hidden
  const [showLogin, setShowLogin] = useState(false);
  // State to control whether registration modal is visible or hidden
  const [showRegistration, setShowRegistration] = useState(false);

  // Function to show the login modal (called by Navbar)
  const handleLoginClick = () => {
    setShowLogin(true);
    setShowRegistration(false); // Close registration if it's open
  };

  // Function to hide the login modal (called by LoginModal)
  const closeLogin = () => {
    setShowLogin(false);
  };

  // Function to show the registration modal (called by LoginModal)
  const handleSwitchToRegister = () => {
    setShowLogin(false);
    setShowRegistration(true);
  };

  // Function to hide the registration modal (called by Registration)
  const closeRegistration = () => {
    setShowRegistration(false);
  };

  // Function to switch back to login modal (called by Registration)
  const handleSwitchToLogin = () => {
    setShowRegistration(false);
    setShowLogin(true);
  };

  return (
    // main cointainer with padding
    <div className="app-container">
      {/* Navigation bar at top - receuves function to show login modal */}
      <Navbar onLoginClick={handleLoginClick} />

      <Routes>
        {/* Main page with two columns layout */}
        <Route
          path="/"
          element={
            <div className="main-content" style={{ display: "flex", gap: "1rem", padding: "1rem" }}>
              
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
        <Route
          path="/:orgId"
          element={<CharityPage charities={fake_charities} />}
        />
      </Routes>
      {/* Login modal, only visible when showLogin is true */}
      <LoginModal
        isVisible={showLogin}
        onClose={closeLogin}
        onSwitchToRegister={handleSwitchToRegister}
      />
      {/* Registration modal, only visible when showRegistration is true */}
      <Registration
        isVisible={showRegistration}
        onClose={closeRegistration}
        onSwitchToLogin={handleSwitchToLogin}
      />
    </div>

  );
}

export default App;