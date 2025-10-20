import { useState } from 'react';

import { EmailAPI } from '../services/APIService'

// Custom hook to manage all modal states and their handlers
// onLoginSuccess: callback function to call when login is successful
export function useModals(onLoginSuccess) {
  // State to control whether login modal is visible or hidden
  const [showLogin, setShowLogin] = useState(false);
  // State to control whether registration modal is visible or hidden
  const [showRegistration, setShowRegistration] = useState(false);
  // state to control whether reset password modal is visible or hidden
  const [showResetPassword, setShowResetPassword] = useState(false);

  // Function to show the login modal (called by Navbar)
  const handleLoginClick = () => {
    setShowLogin(true);
    setShowRegistration(false); // Close registration if it's open
    setShowResetPassword(false);
  };

  // Function to hide the login modal (called by LoginModal)
  const closeLogin = () => {
    setShowLogin(false);
  };

  // Function to show the registration modal (called by LoginModal)
  const handleSwitchToRegister = () => {
    setShowLogin(false);
    setShowRegistration(true);
    setShowResetPassword(false);
  };

  // Function to hide the registration modal (called by Registration)
  const closeRegistration = () => {
    setShowRegistration(false);
  };

  // Function to switch back to login modal (called by Registration)
  const handleSwitchToLogin = () => {
    setShowRegistration(false);
    setShowResetPassword(false);
    setShowLogin(true);
  };

  const closeResetPassword = () => {
    setShowResetPassword(false);
  };

  const handleSwitchToResetPassword = () => {
      setShowResetPassword(true);
      setShowLogin(false);
      setShowRegistration(false);
  };

  // Return all state and handlers that components need
  return {
    // State
    showLogin,
    showRegistration,
    showResetPassword,
    // Handlers
    handleLoginClick,
    closeLogin,
    handleSwitchToRegister,
    closeRegistration,
    handleSwitchToLogin,
    closeResetPassword,
    handleSwitchToResetPassword,
    // Callback for successful login
    onLoginSuccess
  };
}
