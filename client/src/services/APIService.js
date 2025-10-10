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

      // Handle expired/invalid JWT tokens
      if (error.response.status === 401 || error.response.status === 403) {
        // Check if error is related to JWT
        const errorMessage = error.response.data?.message || error.response.data || "";
        if (typeof errorMessage === "string" &&
            (errorMessage.includes("JWT") || errorMessage.includes("token") || errorMessage.includes("expired"))) {
          console.warn("JWT token expired or invalid - clearing token from localStorage");
          localStorage.removeItem("token");

          // Optionally reload the page to reset the app state
          // window.location.reload();
        }
      }
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

  /**
   * Fetch a list of charities with optional filters and sorting. 
   * Must at least send an empty JSON object.
   * 
   * @param {Object} query - Pagination, filtering, and sorting options.
   * @returns {Promise<Object>} JSON object containing:
   * ```json
   * {
   *   "message": "success",
   *   "value": [
   *     {
   *       "charity": "1089464",
   *       "humanName": "Cancer research UK",
   *       "homePageUrl": "https://www.cancerresearchuk.org/",
   *       "charityDescritpionFile": "cancer-research-uk.txt",
   *       "charityImageFile": "cancer-research-uk.jpg",
   *       "positiveScore": 0,
   *       "negativeScore": 1,
   *       "totalScore": -1
   *     }
   *     // ...more charities
   *   ]
   * }
   * ```
   * @example
   * CharityAPI.listCharities({
   *   first: 0,
   *   max_count: 10,
   *   filters: [
   *     { field: "humanName", value: "Cancer research UK", filter: "like" }
   *   ],
   *   sorting: { field: "totalScore", ordering: "descending" }
   * });
   */
  listCharities: (query) => api.post("/charities/list", query),

  /**
   * Retrieve detailed information about a specific charity by its organization ID.
   * 
   * @param {string} charityId - The organization ID of the charity.
   * @returns {Promise<Object>} JSON object containing:
   * ```json
   * {
   *   "message": "success",
   *   "value": {
   *     "charity": "1089464",
   *     "humanName": "Cancer research UK",
   *     "homePageUrl": "https://www.cancerresearchuk.org/",
   *     "charityDescritpionFile": "cancer-research-uk.txt",
   *     "charityImageFile": "cancer-research-uk.jpg",
   *     "positiveScore": 0,
   *     "negativeScore": 1,
   *     "totalScore": -1
   *   }
   * }
   * ```
   * @example
   * CharityAPI.getCharity("1089464");
   */
  getCharity: (charityId) => api.get("/charities/get", { params: { charity: charityId } }),

  /**
   * Submit a vote for a charity (requires authentication).
   * 
   * @param {Object} voteData - Contains the charity ID and the vote direction.
   * @param {string} voteData.charityId - The ID of the charity being voted on.
   * @param {boolean} voteData.upvote - `true` for upvote, `false` for downvote.
   * @returns {Promise<string>} Status message, e.g., "Vote posted successfully"
   * @example
   * CharityAPI.vote({
   *   charityId: "1089464",
   *   upvote: true
   * });
   */
  vote: (voteData) => api.post("/charities/vote", {
    charity: voteData.charityId,
    up: voteData.upvote
  }),

  /**
   * Edit a previously made vote for a charity.
   * 
   * @param {Object} editVoteData - Contains the charity ID and new vote direction.
   * @param {string} editVoteData.charityId
   * @param {boolean} editVoteData.upvote
   * @returns {Promise<string>} Status message, e.g., "Vote registered successfully."
   * @example
   * CharityAPI.editVote({
   *   charityId: "1089464",
   *   upvote: false
   * });
   */
  editVote: (editVoteData) => api.put("/charities/edit_vote", {
    charity: editVoteData.charityId,
    up: editVoteData.upvote
  }),

  /**
   * Remove an existing vote for a specific charity.
   * 
   * @param {Object} removeVoteData - Contains the charity ID to remove the vote from.
   * @param {string} removeVoteData.charityId
   * @returns {Promise<string>} Status message, e.g., "Vote edited successfully"
   * @example
   * CharityAPI.removeVote({
   *   charityId: "1089464"
   * });
   */
  removeVote: (removeVoteData) => api.delete("/charities/remove_vote", {
    data: { charity: removeVoteData.charityId }
  }),
};

// ==================== COMMENT ENDPOINTS ====================

export const CommentAPI = {
  listComments: (query) => api.post("/comments/list", query),       // query = { first, max_count, filters, sorting }

  addComment: (data) => api.post("/comments/add", data),            // data = { comment, charity }

  removeComment: (data) => api.delete("/comments/remove", { data }), // data = { comment_id, charity }

  blameComment: (data) => api.post("/comments/blame", data),        // data = { comment_id, charity, reason }
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
