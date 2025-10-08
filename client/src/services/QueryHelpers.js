// ==================== QUERY BUILDER HELPERS ====================

/**
 * Build a query object for filtering and sorting API requests
 * @param {Object} options - Query options
 * @param {number} options.first - Index of first result (default: 0)
 * @param {number} options.max_count - Maximum number of results (default: 50)
 * @param {Array} options.filters - Array of filter objects (default: [])
 * @param {Object} options.sorting - Sorting object with field and ordering (default: null)
 * @returns {Object} Query object ready to send to API
 */
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

/**
 * Create a filter object for use in queries
 * @param {Object} options - Filter options
 * @param {string} options.filter - Filter type: "less"|"greater"|"equals"|"like"|"or"|"and"|"not"
 * @param {string} options.field - Field name to filter on (for comparison filters)
 * @param {*} options.value - Value to compare against (for comparison filters)
 * @param {Array} options.arguments_ - Sub-filters for logical operators (or/and/not)
 * @returns {Object} Filter object
 */
export function createFilter({ filter, field = null, value = null, arguments_ = [] }) {
  const obj = { filter };
  if (field) obj.field = field;
  if (value !== null) obj.value = value;
  if (arguments_?.length > 0) obj.arguments = arguments_;
  return obj;
}

// ==================== SPECIALIZED QUERY HELPERS ====================

export const QueryHelpers = {
  /**
   * Get newest comments for a specific charity
   * @param {string} charityId - The charity organization ID
   * @param {number} limit - Maximum number of comments to return (default: 20)
   * @param {string} sortby - Sort order: "ascending" or "descending"
   * @returns {Object} Query object ready for CommentAPI.listComments()
   */
  getCommentsByCharity: (charityId, limit = 20, sortby) => {
    const filters = [
      createFilter({
        filter: "equal",
        field: "charity",
        value: charityId,
      }),
    ];

    const sorting = { field: "insertTime", ordering: sortby };  // "ascending" = oldest first, "descending" = newest first

    return buildQuery({
      first: 0,
      max_count: limit,
      filters,
      sorting,
    });
  },
};
