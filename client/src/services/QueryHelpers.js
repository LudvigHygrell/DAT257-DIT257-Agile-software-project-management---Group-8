import { buildQuery, createFilter } from "./APIServiceAxios.js";

export const QueryHelpers = {
    /**
   * Get newest comments for a specific charity
   */
  getCommentsByCharity: (charityId, limit = 20, sortby) => {
    const filters = [
      createFilter({
        filter: "equal",
        field: "charity",
        value: charityId,
      }),
    ];


    const sorting = { field: "insertTime", ordering: sortby };  // "ascending" = newest or "descending" = oldest

    return buildQuery({
      first: 0,
      max_count: limit,
      filters,
      sorting,
    });
  },
};
