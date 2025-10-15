import { useState, useEffect } from "react"; 
import "../styles/CommentSection.css";

import { CommentAPI } from "../services/APIService.js";

function CommentSection({ orgId }) {

  const [sortOrder, setSortOrder] = useState("newest");
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");


  // === Add this helper ===
  const getCommentText = (raw) => {
    if (raw == null) return "";

    // If backend already returned an object (JsonNode parsed by axios)
    if (typeof raw === "object") {
      return raw.contents ?? raw.text ?? JSON.stringify(raw);
    }

    // If backend returned a string: might be JSON string or plain string
    if (typeof raw === "string") {
      try {
        const parsed = JSON.parse(raw);
        if (parsed == null) return "";
        if (typeof parsed === "object") {
          return parsed.contents ?? parsed.text ?? JSON.stringify(parsed);
        }
        return String(parsed);
      } catch {
        // not JSON — treat as plain string
        return raw;
      }
    }

    // fallback for numbers/booleans/etc.
    return String(raw);
  };
  // === end helper ===

  useEffect(() => {
    const fetchComments = async () => {
      const query = {
        charity: orgId,
        limit: 20,
        order: sortOrder === "newest" ? "descending" : "ascending"
      };

      try {
        const res = await CommentAPI.listComments(query);

        const mappedComments = (res.data.value || []).map(c => ({
          name: c.user,
          date: c.insertTime,
          text: getCommentText(c.comment),
          vote: null
        }));
        setComments(mappedComments);

      } catch (err) {
        console.error("Error fetching comments:", err);
        setComments([]);
      }
    };

    fetchComments();
  }, [orgId, sortOrder]);

  const handleSubmit = async (event) => { 
    event.preventDefault();
    if (!newComment.trim()) return;

    const newCommentObj = {
      comment: { contents: newComment },
      charity: orgId
    };

    try {
      await CommentAPI.addComment(newCommentObj);
      setNewComment("");

      // Refresh comments
      const query = {
        charity: orgId,
        limit: 20,
        order: sortOrder === "newest" ? "descending" : "ascending"
      };
      const res = await CommentAPI.listComments(query);

      const mappedComments = (res.data.value || []).map(c => ({
        name: c.author,
        date: c.insertTime,
        text: (() => {
          try {
            const parsed = JSON.parse(c.comment);
            return parsed.contents || "";
          } catch {
            return c.comment || "";
          }
        })(),
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
