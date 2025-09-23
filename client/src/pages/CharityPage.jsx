import { useParams } from "react-router-dom";
import { useState } from "react";
import "../styles/CharityPage.css";

function CharityPage({ charities }) {
  const { orgId } = useParams();
  const charity = charities.find((c) => c.orgId === orgId);

  const [comments, setComments] = useState([
    { name: "Mario", text: "Wonderful initiative!" },
    { name: "Lucia", text: "I donated and had a great experience." },
    { name: "Giovanni", text: "Congratulations on your work." },
    { name: "Sara", text: "You are an example for everyone." },
    { name: "Alessandro", text: "I shared your project with my friends." },
    { name: "Francesca", text: "Thank you for what you do!" },
    { name: "Paolo", text: "Serious and reliable organization." },
    { name: "Elena", text: "I would love to participate in your activities." },
    { name: "Roberto", text: "Keep it up!" },
    { name: "Martina", text: "I received assistance and am very satisfied." },
    { name: "Stefano", text: "I support you every year." },
    { name: "Valentina", text: "Great transparency and communication." },
    { name: "Davide", text: "Innovative and useful project." },
    { name: "Chiara", text: "You are always available and kind." },
    { name: "Federico", text: "I have seen the concrete results of your commitment." },
    { name: "Laura", text: "I recommend everyone to support you." },
    { name: "Simone", text: "Thank you for helping my family." }
  ]);
  const [newComment, setNewComment] = useState("");

  if (!charity) {
    return <div className="charity-not-found">Charity not found</div>;
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;
    setComments([...comments, { name: "Anonimo", text: newComment }]);
    setNewComment("");
  };

  return (
    <div className="charity-page">
      {/* LEFT PANEL */}
      <div className="charity-page-info">
        <div className="charity-header">
          <img
            src={charity.logo ? `/${charity.logo}` : `/${charity.photo}`}
            alt={`${charity.name} logo`}
            className="charity-logo-large"
          />
          <div className="charity-title">
            <h2>{charity.name}</h2>
            <p className="charity-page-category">{charity.category}</p>
            <p className="charity-page-score">Score: {charity.score}</p>
          </div>
        </div>

        <div className="charity-description-section">
          <h3>Who are we?</h3>
          <p className="charity-description">{charity.description}</p>
        </div>

        <div className="charity-photos-section">
          <h3>Our activities</h3>
          <div className="charity-photos">
            <img
              src={`https://via.placeholder.com/400x250?text=${charity.category}+1`}
              alt={`${charity.name} activity 1`}
            />
            <img
              src={`https://via.placeholder.com/400x250?text=${charity.category}+2`}
              alt={`${charity.name} activity 2`}
            />
          </div>
        </div>
      </div>

      {/* RIGHT PANEL */}
      <div className="charity-comments-section">
        <h3>Comments</h3>
        <div className="charity-comments">
          {comments.map((c, idx) => (
            <div key={idx} className="comment-card">
              <strong>{c.name}</strong>
              <p>{c.text}</p>
            </div>
          ))}
        </div>

        <form className="charity-comment-form" onSubmit={handleSubmit}>
          <textarea
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="Scrivi un commento..."
            rows="3"
          ></textarea>
          <button type="submit">Send</button>
        </form>
      </div>
    </div>
  );
}

export default CharityPage;
