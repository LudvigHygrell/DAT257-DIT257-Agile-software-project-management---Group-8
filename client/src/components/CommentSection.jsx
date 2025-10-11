import { useState, useEffect } from "react"; 
import "../styles/CommentSection.css";

import thumbsUp from "../assets/thumbs-up.png";
import thumbsDown from "../assets/thumbs-down.png";

import APIService from "../services/APIService.js";
import { QueryHelpers } from "../services/QueryHelpers.js";

function CommentSection({ orgId }) {

  // State to manage sorting order of comments, starting with "newest"
  const [sortOrder, setSortOrder] = useState("newest"); // can be "newest" or "oldest"

  // States to manage comments and new comment input
  const [comments, setComments] = useState([]);  
  const [newComment, setNewComment] = useState("");  

  // Fetch comments from backend based on charity orgID and sort order
  useEffect(() => {
    const fetchComments = async () => {
      try {
        // "descending" = newest first, "ascending" = oldest first
        const ordering = sortOrder === "newest" ? "descending" : "ascending";
        const query = QueryHelpers.getCommentsByCharity(orgId, 20, ordering); // get query object from helper with sorting
        const res = await APIService.CommentAPI.listComments(query);  // fetch comments from backend

        // Map backend format to frontend format
        const mappedComments = (res.data.value || []).map(c => ({
          name: c.author,  // Backend: author, Frontend: name
          date: c.insertTime,  // Backend: insertTime, Frontend: date
          text: c.comment?.contents || c.comment || "",  // Backend: comment.contents, Frontend: text
          vote: null  // TODO: Implement comment voting
        }));

        setComments(mappedComments); // update state with mapped comments
      } catch (err) {
        console.error("Error fetching comments:", err);
        setComments([]);
      }
    };

    fetchComments();
  }, [orgId, sortOrder]);  // Re-run effect when orgId or sortOrder changes

  // Handle new comment submission 
  const handleSubmit = async (event) => { 
    event.preventDefault();             // Prevent default form submission behavior
    if (!newComment.trim()) return;     // Do nothing if comment is empty or whitespace

    // Build new comment object
    const newCommentObj = {
      comment: {  contents: newComment }, 
      charity: orgId 
    };

    try {
      // Send new comment to backend
      await APIService.CommentAPI.addComment(newCommentObj);

      setNewComment(""); // Clear the input field after submission

      // Refresh comments after adding a new one
      const ordering = sortOrder === "newest" ? "descending" : "ascending";
      const query = QueryHelpers.getCommentsByCharity(orgId, 20, ordering);
      const res = await APIService.CommentAPI.listComments(query);

      // Map backend format to frontend format
      const mappedComments = (res.data.value || []).map(c => ({
        name: c.author,
        date: c.insertTime,
        text: c.comment?.contents || c.comment || "",
        vote: null
      }));

      setComments(mappedComments);
    } catch (err) {
      console.error("Error adding comment:", err);
    }
  };

  return (
      <div className="charity-comments-section">
        <h3>Comments</h3>

        {/* Sort buttons */}
        <div className="sort-buttons">
          {["newest", "oldest"].map((order) => (
            <button
              key={order}
              className={`sort-${order} ${sortOrder === order ? "active" : ""}`}
              onClick={() => setSortOrder(order)}
            >
              {order.charAt(0).toUpperCase() + order.slice(1)}
            </button>
          ))}
        </div>

        {/* Comments List */}
        <div className="charity-comments">
          {comments.length === 0 && <p>No comments yet.</p>}
          {comments.map((c, idx) => (
            <div key={idx} className="comment-card">
              <div className="comment-header">
                <div className="comment-meta">
                  <strong>{c.name}</strong>   
                  <span className="comment-date">{c.date}</span>  
                </div>
                {c.vote === "like" && <img src={thumbsUp} alt="like" className="vote-icon" />}
                {c.vote === "dislike" && <img src={thumbsDown} alt="dislike" className="vote-icon" />}
              </div>
              <p>{c.text}</p>
            </div>
          ))}
        </div>

        {/* Add comment */}
        <form className="charity-comment-form" onSubmit={handleSubmit}>
          <textarea
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="Write a comment..."
            rows="3"
          ></textarea>
          <button type="submit">Send</button>
        </form>
      </div>
  );
}

export default CommentSection;
