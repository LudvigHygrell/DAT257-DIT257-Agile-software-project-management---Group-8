import { useState, useEffect } from 'react';

// Custom hook to manage authentication state
export function useAuth() {
  // State to track if user is logged in
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  // State to store username
  const [username, setUsername] = useState(null);

  // Check if user is logged in on component mount
  useEffect(() => {
    const token = localStorage.getItem('token');
    const storedUsername = localStorage.getItem('username');

    if (token && storedUsername) {
      setIsAuthenticated(true);
      setUsername(storedUsername);
    }
  }, []);

  // Function to handle successful login
  const login = (token, user) => {
    localStorage.setItem('token', token);
    localStorage.setItem('username', user);
    setIsAuthenticated(true);
    setUsername(user);
  };

  // Function to handle logout
  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setIsAuthenticated(false);
    setUsername(null);
  };

  return {
    isAuthenticated,
    username,
    login,
    logout,
  };
}
