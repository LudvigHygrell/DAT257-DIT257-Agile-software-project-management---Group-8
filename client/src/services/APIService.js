// Base URL for the backend API (using Vite proxy)
const API_BASE_URL = '/api';

// Helper function to make API requests with proper headers and error handling
async function apiRequest(endpoint, method = 'GET', body = null, token = null) {
  const headers = {
    'Content-Type': 'application/json',
  };

  // Add JWT token to headers if provided
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const options = {
    method,
    headers,
  };

  // Add body for all requests (including GET if body is provided)
  if (body) {
    options.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

    // Check if response is ok (status 200-299)
    if (!response.ok) {
      // Try to get error message from response body
      const contentType = response.headers.get('content-type');
      let errorMessage;

      if (contentType && contentType.includes('application/json')) {
        const errorData = await response.json().catch(() => ({}));
        errorMessage = errorData.message || `HTTP error! status: ${response.status}`;
      } else {
        // Backend returns plain text error messages
        errorMessage = await response.text().catch(() => `HTTP error! status: ${response.status}`);
      }

      throw new Error(errorMessage);
    }

    // Try to parse as JSON, fallback to text
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      return await response.text();
    }
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
}

// ==================== USER API ====================

export const UserAPI = {
  /**
   * Login user
   * Note: Backend uses GET with body (non-standard), so we use POST here as fetch doesn't allow GET with body
   * @param {Object} credentials - { username, email, password }
   * @returns {Promise} User data with JWT token
   */
  login: async (credentials) => {
    // Use POST instead of GET because fetch API doesn't allow GET requests with body
    // The backend controller accepts both since Spring allows @RequestBody on @GetMapping
    return await apiRequest('/users/login', 'POST', credentials);
  },

  /**
   * Create new user (registration)
   * @param {Object} userData - { username, password, email, signed }
   * @returns {Promise} Created user data
   */
  create: async (userData) => {
    return await apiRequest('/users/create', 'POST', userData);
  },

  /**
   * Change password
   * @param {Object} data - { username, old, new }
   * @param {string} token - JWT token
   * @returns {Promise} Success response
   */
  changePassword: async (data, token) => {
    return await apiRequest('/users/change_password', 'PUT', data, token);
  },

  /**
   * Reset password (forgot password flow)
   * @param {Object} data - { email, new_password, verification_code }
   * @returns {Promise} Success response
   */
  resetPassword: async (data) => {
    return await apiRequest('/users/reset_password', 'PUT', data);
  },

  /**
   * Change email
   * @param {Object} data - { username, email, password }
   * @param {string} token - JWT token
   * @returns {Promise} Success response
   */
  changeEmail: async (data, token) => {
    return await apiRequest('/users/change_email', 'PUT', data, token);
  },

  /**
   * Get user activity
   * @param {Object} params - { username, type }
   * @param {string} token - JWT token
   * @returns {Promise} User activity data
   */
  getActivity: async (params, token) => {
    return await apiRequest('/users/get_activity', 'POST', params, token);
  },

  /**
   * Delete user
   * @param {Object} data - { username }
   * @param {string} token - JWT token
   * @returns {Promise} Success response
   */
  remove: async (data, token) => {
    return await apiRequest('/users/remove', 'DELETE', data, token);
  },
};


export default {
  UserAPI,
};
