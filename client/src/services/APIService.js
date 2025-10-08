import axios from "axios";
import { buildQuery, createFilter } from "./QueryHelpers.js";

const API_BASE_URL = "http://localhost:8080/api";

// ==================== AXIOS INSTANCE ====================

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add request interceptor for auth tokens
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle specific error cases
    if (error.response) {
      // The request was made and the server responded with a status code
      // that falls out of the range of 2xx
      console.error("API Error:", error.response.status, error.response.data);
    } else if (error.request) {
      // The request was made but no response was received
      console.error("Network Error:", error.request);
    } else {
      // Something happened in setting up the request that triggered an Error
      console.error("Error:", error.message);
    }
    return Promise.reject(error);
  }
);

// ==================== USER ENDPOINTS ====================

/**
 * User API Service
 * All user-related endpoints for authentication and profile management
 */
export const UserAPI = {
  /**
   * Login user
   * @param {Object} credentials - { username, email, password }
   * @returns {Promise<{token: string}>} JWT token
   * @example
   * UserAPI.login({ username: "john", email: "john@example.com", password: "pass123" })
   *   .then(response => {
   *     localStorage.setItem('token', response.data.token);
   *   });
   */
  login: (credentials) => api.post("/users/login", credentials),

  /**
   * Create new user (registration)
   * @param {Object} userData - { username, password, email, signed? }
   * @returns {Promise<string>} Status message
   * @example
   * UserAPI.create({
   *   username: "john",
   *   password: "pass123",
   *   email: "john@example.com",
   *   signed: false
   * });
   */
  create: (userData) => api.post("/users/create", userData),

  /**
   * Change password (requires authentication)
   * @param {Object} data - { username, old, new }
   * @returns {Promise<string>} Status message
   * @example
   * UserAPI.changePassword({
   *   username: "john",
   *   old: "oldpass123",
   *   new: "newpass456"
   * });
   */
  changePassword: (data) => api.put("/users/change_password", data),

  /**
   * Reset password (forgot password flow)
   * @param {Object} data - { email, new_password, verification_code }
   * @returns {Promise<string>} Status message
   * @example
   * UserAPI.resetPassword({
   *   email: "john@example.com",
   *   new_password: "newpass456",
   *   verification_code: 1234
   * });
   */
  resetPassword: (data) => api.put("/users/reset_password", data),

  /**
   * Change email (requires authentication)
   * @param {Object} data - { username, email, password }
   * @returns {Promise<string>} Status message
   * @example
   * UserAPI.changeEmail({
   *   username: "john",
   *   email: "newemail@example.com",
   *   password: "currentpassword"
   * });
   */
  changeEmail: (data) => api.put("/users/change_email", data),

  /**
   * Get user activity (requires authentication)
   * @param {Object} params - { username, type, query? }
   * @param {string} params.type - "comments" | "likes"
   * @param {Object} params.query - Optional query params { type, first, max_count, filters, sorting }
   * @returns {Promise<{value: Array, message: string}>} Activity data
   * @example
   * UserAPI.getActivity({
   *   username: "john",
   *   type: "comments",
   *   query: {
   *     type: "comments",
   *     first: 0,
   *     max_count: 10,
   *     sorting: { field: "insertTime", ordering: "descending" }
   *   }
   * });
   */
  getActivity: (params) => api.post("/users/get_activity", params),

  /**
   * Delete user (requires authentication)
   * @param {Object} data - { username }
   * @returns {Promise<string>} Status message
   * @example
   * UserAPI.remove({ username: "john" });
   */
  remove: (data) => api.delete("/users/remove", { data }),
};

// ==================== CHARITY ENDPOINTS ====================

export const CharityAPI = {
  listCharities: (query) => api.post("/charities/list", query),
  getCharity: (orgId) => api.post("/charities/get", { orgId }),
  vote: (data) => api.post("/charities/vote", data),
  editVote: (data) => api.put("/charities/edit_vote", data),
  removeVote: (data) => api.delete("/charities/remove_vote", { data }),
  pauseCharity: (data) => api.post("/charities/pause", data),
  resumeCharity: (data) => api.post("/charities/resume", data),
  getPaused: () => api.get("/charities/get_paused"),
};

// ==================== COMMENT ENDPOINTS ====================

export const CommentAPI = {
  listComments: (query) => api.post("/comments/list", query),       // query = { first, max_count, filters, sorting }

  addComment: (data) => api.post("/comments/add", data),            // data = { text, charity, name }

  removeComment: (data) => api.delete("/comments/remove", { data }), // data = { commentId, charityId }

  blameComment: (data) => api.post("/comments/blame", data),        // data = { commentId, charityId, reason }
};

// ==================== EXPORT ALL ====================

const API = {
  UserAPI,
  CharityAPI,
  CommentAPI,
  buildQuery,
  createFilter,
};

export default API;
export { api };
