import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

/**
 * Generic API request wrapper using axios.
 */
async function apiRequest(endpoint, method = "GET", body = null, token = null) {
  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  try {
    const response = await axios({
      url: `${API_BASE_URL}${endpoint}`,
      method,
      headers,
      data: body,
    });
    return response.data;
  } catch (error) {
    console.error(`API request to ${endpoint} failed:`, error);
    if (error.response) {
      throw new Error(error.response.data?.message || error.response.data || "Unknown server error");
    } else {
      throw new Error("Network error");
    }
  }
}

/**
 * Builds a properly structured query object for filtering, sorting, and pagination.
 * This matches the backend’s expected schema for advanced queries.
 */
export function buildQuery({
  first = 0,
  max_count = 50,
  filters = [],
  sorting = null,
} = {}) {
  const query = { first, max_count };

  if (filters && filters.length > 0) query.filters = filters;
  if (sorting && sorting.field) query.sorting = sorting;

  return query;
}

/**
 * Builds a single filter object.
 * 
 * @param {string} filter - One of: less|greater|equal|like|not|or|and
 * @param {string} [field] - The field name to filter on
 * @param {*} [value] - The value for comparison filters
 * @param {Array} [arguments_] - Optional sub-filters for logical filters (and/or/not)
 */
export function createFilter({ filter, field = null, value = null, arguments_ = [] }) {
  const obj = { filter };
  if (field) obj.field = field;
  if (value !== null) obj.value = value;
  if (arguments_ && arguments_.length > 0) obj.arguments = arguments_;
  return obj;
}

//
// ==================== USER ENDPOINTS ====================
//
export const UserAPI = {
  login: (credentials) => apiRequest("/users/login", "GET", credentials),

  create: (userData) => apiRequest("/users/create", "POST", userData),

  changePassword: (data) => apiRequest("/users/change_password", "PUT", data),

  resetPassword: (data) => apiRequest("/users/reset_password", "PUT", data),

  changeEmail: (data) => apiRequest("/users/change_email", "PUT", data),

  getActivity: (data) => apiRequest("/users/get_activity", "GET", data),

  remove: (data) => apiRequest("/users/remove", "DELETE", data),
};

//
// ==================== CHARITY ENDPOINTS ====================
//
export const CharityAPI = {
  listCharities: (query) => apiRequest("/charities/list", "GET", query),

  getCharity: (orgId) => apiRequest("/charities/get", "GET", { orgId }),

  vote: (data) => apiRequest("/charities/vote", "POST", data),

  editVote: (data) => apiRequest("/charities/edit_vote", "PUT", data),

  removeVote: (data) => apiRequest("/charities/remove_vote", "DELETE", data),

  pauseCharity: (data) => apiRequest("/charities/pause", "POST", data),

  resumeCharity: (data) => apiRequest("/charities/resume", "DELETE", data),

  getPaused: () => apiRequest("/charities/get_paused", "GET"),         
};

//
// ==================== COMMENT ENDPOINTS ====================
//
export const CommentAPI = {
  listComments: (query) => apiRequest("/comments/list", "GET", query),   // query = { first, max_count, filters, sorting }

  addComment: (data) => apiRequest("/comments/add", "POST", data),         // data = { text, charity, name }

  removeComment: (data) => apiRequest("/comments/remove", "DELETE", data),      // data = { commentId, charityId } 

  blameComment: (data) => apiRequest("/comments/blame", "POST", data),        // data = { commentId, charityId reason }
};

//
// ==================== EXPORT ALL ====================
//
const API = {
  UserAPI,
  CharityAPI,
  CommentAPI,
  buildQuery,
  createFilter,
};

export default API;
