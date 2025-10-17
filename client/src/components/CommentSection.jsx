import { useState, useEffect } from "react"; 
import "../styles/CommentSection.css";

import { CommentAPI } from "../services/APIService.js";

function CommentSection({ orgId, isAuthenticated, onRequireLogin, setBlameInfo, setBlameModalVisible }) {

  const [sortOrder, setSortOrder] = useState("newest");
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [loading, setLoading] = useState(false);


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

  const fetchComments = async () => {
    setLoading(true);
    const query = {
      filters: [
        {
          filter: "equals",
          field: "charity",
          value: orgId
        }
      ],
      max_count: 20,
      sorting: {
        ordering: sortOrder === "newest" ? "descending" : "ascending",
        field: "insertTime"
      }
    };

    try {
      const res = await CommentAPI.listComments(query);
      console.log("Fetched comments:", res.data.value);

      const mappedComments = (res.data.value || []).map(c => ({
        user: c.user,
        date: c.insertTime,
        text: getCommentText(c.comment),
        comment_id: c.commentId,
        vote: "like"
      }));
      setComments(mappedComments);

    } catch (err) {
      console.error("Error fetching comments:", err);
      setComments([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchComments();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [orgId, sortOrder]);

  const openBlameModal = (user, text, comment_id) => {
  if (!isAuthenticated) {
    onRequireLogin();
    return;
  }
  setBlameInfo({comment_id: comment_id, user: user, text: text}); // send info to CharityPage
  setBlameModalVisible(true);     // open the BlameModal
  };

  const handleSubmit = async (event) => { 
    event.preventDefault();
    if (!newComment.trim()) {
      alert("Comment cannot be empty.");
      return;
    }

    if (!isAuthenticated) {
      onRequireLogin();
      return;
    }

    const newCommentObj = {
      comment: { contents: newComment },
      charity: orgId
    };

    try {
      await CommentAPI.addComment(newCommentObj);
      setNewComment("");
      await fetchComments()
    } catch (err) {
      console.error("Error adding comment:", err);
    }finally {
      alert("Comment submitted successfully.");
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
                <strong>{c.user}</strong>
                <span className="comment-date">{c.date ? new Date(c.date).toLocaleString() : ''}</span>
              </div>
              {c.vote === "like" && <img src={'http://localhost:8080/api/files/public/thumbs-up.png'} alt="like" className="vote-icon" />}
              {c.vote === "dislike" && <img src={'http://localhost:8080/api/files/public/thumbs-down.png'} alt="dislike" className="vote-icon" />}
              <button
                className="vote-btn"
                onClick={() => openBlameModal(c.user, c.text, c.comment_id)}
                aria-label="Report"
                title="Report comment"
              >
                <img src={'http://localhost:8080/api/files/public/blame-icon.png'} alt="Report comment" />
              </button>
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
