import { useState } from 'react';

// Custom hook to manage all modal states and their handlers
// onLoginSuccess: callback function to call when login is successful
export function useModals(onLoginSuccess) {
  // State to control whether login modal is visible or hidden
  const [showLogin, setShowLogin] = useState(false);
  // State to control whether registration modal is visible or hidden
  const [showRegistration, setShowRegistration] = useState(false);
  // State to control whether forgot password modal is visible or hidden
  const [showForgotPassword, setShowForgotPassword] = useState(false);

  // Function to show the login modal (called by Navbar)
  const handleLoginClick = () => {
    setShowLogin(true);
    setShowRegistration(false); // Close registration if it's open
    setShowForgotPassword(false); // Close forgot password if it's open
  };

  // Function to hide the login modal (called by LoginModal)
  const closeLogin = () => {
    setShowLogin(false);
  };

  // Function to show the registration modal (called by LoginModal)
  const handleSwitchToRegister = () => {
    setShowLogin(false);
    setShowRegistration(true);
    setShowForgotPassword(false);
  };

  // Function to show the forgot password modal (called by LoginModal)
  const handleSwitchToForgotPassword = () => {
    setShowLogin(false);
    setShowRegistration(false);
    setShowForgotPassword(true);
  };

  // Function to hide the registration modal (called by Registration)
  const closeRegistration = () => {
    setShowRegistration(false);
  };

  // Function to switch back to login modal (called by Registration or ForgotPassword)
  const handleSwitchToLogin = () => {
    setShowRegistration(false);
    setShowForgotPassword(false);
    setShowLogin(true);
  };

  // Function to hide the forgot password modal (called by ForgotPassword)
  const closeForgotPassword = () => {
    setShowForgotPassword(false);
  };

  // Return all state and handlers that components need
  return {
    // State
    showLogin,
    showRegistration,
    showForgotPassword,
    // Handlers
    handleLoginClick,
    closeLogin,
    handleSwitchToRegister,
    handleSwitchToForgotPassword,
    closeRegistration,
    handleSwitchToLogin,
    closeForgotPassword,
    // Callback for successful login
    onLoginSuccess,
  };
}
