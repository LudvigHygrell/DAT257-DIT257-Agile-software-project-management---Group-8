import { useState, useEffect } from "react"; 
import "../styles/CommentSection.css";

import { CommentAPI } from "../services/APIService.js";

import CommentCard from "./CommentCard.jsx"

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
        vote: c.vote
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
        {comments.map((c, idx) => <CommentCard
          key={idx}
          orgId={orgId}
          c={c}
          isAuthenticated={isAuthenticated}
          onRequireLogin={onRequireLogin}
          setBlameInfo={setBlameInfo}
          setBlameModalVisible={setBlameModalVisible}/>)}
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
