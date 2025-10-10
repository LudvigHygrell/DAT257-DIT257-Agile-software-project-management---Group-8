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
        const errorMessage = error.response.data?.message || error.response.data || "";

        // If 403 with empty response, it's likely an invalid JWT token
        // Clear token and retry without authentication
        if (error.response.status === 403 && errorMessage === "") {
          console.warn("403 Forbidden with empty response - likely invalid JWT token");
          localStorage.removeItem("token");
          console.log("Cleared invalid token from localStorage - please refresh the page");
        }

        // Check if error message explicitly mentions JWT/token
        if (typeof errorMessage === "string" &&
            (errorMessage.includes("JWT") || errorMessage.includes("token") || errorMessage.includes("expired"))) {
          console.warn("JWT token expired or invalid - clearing token from localStorage");
          localStorage.removeItem("token");
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
/**
 * CharityAPI — Wrapper for /api/charities endpoints
 * 
 * Axios automatically handles JSON serialization (for requests) 
 * and parsing (for responses).
 */
export const CharityAPI = {

  /**
   * Fetch a list of charities with optional filters and sorting.
   *
   * @param {Object} query - Pagination, filtering, and sorting options.
   * @param {number} [query.first] - Starting index for pagination.
   * @param {number} [query.max_count] - Maximum number of results to fetch.
   * @param {Array<{field: string, value: any, filter: string}>} [query.filters] - List of field filters.
   * @param {{field: string, ordering: "ascending"|"descending"}} [query.sorting] - Sorting criteria.
   * @returns {Promise<{message: string, value: Array<{
   *   charity: string,
   *   humanName: string,
   *   homePageUrl: string,
   *   charityDescriptionFile: string,
   *   charityImageFile: string,
   *   positiveScore: number,
   *   negativeScore: number,
   *   totalScore: number
   * }>>}> Response data containing charities.
   * 
   * @example
   * const res = await CharityAPI.listCharities({
   *   first: 0,
   *   max_count: 10,
   *   filters: [{ field: "humanName", value: "Cancer", filter: "like" }],
   *   sorting: { field: "totalScore", ordering: "descending" }
   * });
   * console.log(res.data.value);
   */
  listCharities: (query) => api.post("/charities/list", query),

  /**
   * Retrieve detailed information about a specific charity by its organization ID.
   *
   * @param {string} charity - The organization ID of the charity.
   * @returns {Promise<{message: string, value: {
   *   charity: string,
   *   humanName: string,
   *   homePageUrl: string,
   *   charityDescriptionFile: string,
   *   charityImageFile: string,
   *   positiveScore: number,
   *   negativeScore: number,
   *   totalScore: number
   * } }>} Detailed charity data.
   * 
   * @example
   * const res = await CharityAPI.getCharity("1089464");
   * console.log(res.data.value.humanName);
   */
  getCharity: (charity) => api.get("/charities/get", { params: { charity } }),

  /**
   * Submit a vote for a charity (requires authentication).
   *
   * @param {{charity: string, up: boolean}} voteData - The charity and vote direction.
   * @returns {Promise<string>} Confirmation message, e.g. `"Vote posted successfully"`.
   * 
   * @example
   * const res = await CharityAPI.vote({ charity: "1089464", up: true });
   * console.log(res.data); // "Vote posted successfully"
   */
  vote: (voteData) => api.post("/charities/vote", {
    charity: voteData.charity,
    up: voteData.up
  }),

  /**
   * Edit a previously made vote for a charity.
   *
   * @param {{charity: string, up: boolean}} editVoteData - The charity ID and new vote direction.
   * @returns {Promise<string>} Confirmation message, e.g. `"Vote registered successfully."`.
   * 
   * @example
   * const res = await CharityAPI.editVote({ charity: "1089464", up: false });
   * console.log(res.data); // "Vote registered successfully."
   */
  editVote: (editVoteData) => api.put("/charities/edit_vote", {
    charity: editVoteData.charity,
    up: editVoteData.up
  }),

  /**
   * Remove an existing vote for a specific charity.
   *
   * @param {{charity: string}} removeVoteData - The charity ID to remove the vote from.
   * @returns {Promise<string>} Confirmation message, e.g. `"Vote edited successfully"`.
   * 
   * @example
   * const res = await CharityAPI.removeVote({ charity: "1089464" });
   * console.log(res.data); // "Vote edited successfully"
   */
  removeVote: (removeVoteData) => api.delete("/charities/remove_vote", {
    data: { charity: removeVoteData.charity }
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
