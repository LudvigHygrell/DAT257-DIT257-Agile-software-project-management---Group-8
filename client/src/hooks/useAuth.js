import { useState, useEffect } from 'react';
import axios from 'axios';

// Custom hook to manage authentication state
export function useAuth() {
  // State to track if user is logged in
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  // State to store username
  const [username, setUsername] = useState(null);
  // State to track if we're still validating the token
  const [isValidating, setIsValidating] = useState(true);

  // Validate token on component mount
  useEffect(() => {
    const validateToken = async () => {
      const token = localStorage.getItem('token');
      const storedUsername = localStorage.getItem('username');

      // If no token or username, user is not authenticated
      if (!token || !storedUsername) {
        setIsValidating(false);
        return;
      }

      // Try to validate the token by making a simple API call
      try {
        // Make a lightweight request to verify the token is valid
        // Using charities/list because it doesn't require authentication (public endpoint)
        // but will process the JWT if present
        await axios.get('http://localhost:8080/api/charities/get', {
          params: { charity: '1089464' }, // Test with a known charity ID
          headers: {
            'Authorization': `Bearer ${token}`
          },
          timeout: 5000 // 5 second timeout
        });

        // If request succeeds, token is valid
        setIsAuthenticated(true);
        setUsername(storedUsername);
      } catch (error) {
        // If request fails with 401/403, token is invalid
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          console.warn('Invalid or expired token found in localStorage - clearing');
          localStorage.removeItem('token');
          localStorage.removeItem('username');
          setIsAuthenticated(false);
          setUsername(null);
        } else if (error.code === 'ECONNABORTED' || !error.response) {
          // Network error or timeout - assume token is valid but backend is unreachable
          // Still set authenticated so user can navigate, but API calls will handle errors
          console.warn('Could not validate token - network error. Assuming valid.');
          setIsAuthenticated(true);
          setUsername(storedUsername);
        } else {
          // For other errors, clear token to be safe
          console.warn('Token validation failed with unexpected error - clearing');
          localStorage.removeItem('token');
          localStorage.removeItem('username');
          setIsAuthenticated(false);
          setUsername(null);
        }
      } finally {
        setIsValidating(false);
      }
    };

    validateToken();
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
    isValidating, // Export this so App can show loading state
  };
}
