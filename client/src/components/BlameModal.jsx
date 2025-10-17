import { useState } from "react";
import { useEffect } from "react";
import "../styles/LoginModal.css"; // reuse same styles for overlay & content

function BlameModal({ isVisible, onClose, onSubmit, blameInfo }) {
  const [reason, setReason] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isVisible) setReason(""); // clear textarea each time modal opens
  }, [isVisible]);

  if (!isVisible) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!reason.trim()) return alert("Reason cannot be empty");
    setLoading(true);
    try {
      await onSubmit(reason);
      setReason("");
      onClose();
    } catch (err) {
      console.error(err);
      alert("Failed to submit report");
    } finally {
      setLoading(false);
    }
  };

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) onClose();
  };

  return (
    <div className="popup-overlay" onClick={handleOverlayClick}>
      <div className="popup-content" onClick={(e) => e.stopPropagation()}>
        <h2>Report Comment</h2>
        <p><strong>Author:</strong> {blameInfo.user}</p>
        <p><strong>Comment:</strong> {blameInfo.text}</p>
        <form onSubmit={handleSubmit}>
          <textarea
            className="input-field"          
            placeholder="Write your reason to report the comment..."
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            rows={5}                         
          />
          <button
            type="submit"
            className="submit-button"
            disabled={loading}
          >
            {loading ? "Submitting..." : "Submit"}
          </button>
        </form>
        <button className="close-button" onClick={onClose}>X</button>
      </div>
    </div>
  );
}

export default BlameModal;
