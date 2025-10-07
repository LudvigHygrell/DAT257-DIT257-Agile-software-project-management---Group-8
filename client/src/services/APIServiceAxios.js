import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

// ==================== AXIOS INSTANCE ====================

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Automatically add token from localStorage
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

// Optional global error handler
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error);
    return Promise.reject(error);
  }
);

// ==================== HELPER FUNCTIONS ====================

export function buildQuery({
  first = 0,
  max_count = 50,
  filters = [],
  sorting = null,
} = {}) {
  const query = { first, max_count };
  if (filters?.length > 0) query.filters = filters;
  if (sorting?.field) query.sorting = sorting;
  return query;
}

export function createFilter({ filter, field = null, value = null, arguments_ = [] }) {
  const obj = { filter };
  if (field) obj.field = field;
  if (value !== null) obj.value = value;
  if (arguments_?.length > 0) obj.arguments = arguments_;
  return obj;
}

// ==================== USER ENDPOINTS ====================

export const UserAPI = {
  login: (credentials) => api.post("/users/login", credentials),
  create: (userData) => api.post("/users/create", userData),
  changePassword: (data) => api.put("/users/change_password", data),
  resetPassword: (data) => api.put("/users/reset_password", data),
  changeEmail: (data) => api.put("/users/change_email", data),
  getActivity: (data) => api.post("/users/get_activity", data),
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
