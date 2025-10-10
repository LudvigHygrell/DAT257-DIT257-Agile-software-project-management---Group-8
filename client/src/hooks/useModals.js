import { useState } from 'react';

// Custom hook to manage all modal states and their handlers
// onLoginSuccess: callback function to call when login is successful
export function useModals(onLoginSuccess) {
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

  // Return all state and handlers that components need
  return {
    // State
    showLogin,
    showRegistration,
    // Handlers
    handleLoginClick,
    closeLogin,
    handleSwitchToRegister,
    closeRegistration,
    handleSwitchToLogin,
    // Callback for successful login
    onLoginSuccess,
  };
}
