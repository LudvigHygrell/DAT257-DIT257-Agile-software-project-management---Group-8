import { useParams } from "react-router-dom";
import { useState } from "react";
import "../styles/NewCharityPage.css";
import thumbsUp from "../assets/thumbs-up.png";
import thumbsDown from "../assets/thumbs-down.png";
import CommentSection from "../components/CommentSection.jsx";

function CharityPage({ charities }) {
  const { orgId } = useParams();
  const charity = charities.find((c) => c.orgId === orgId);

  if (!charity) {
    return <div className="charity-not-found">Charity not found</div>;
  }


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
      {/* Comment Section */}
      <div>
        <CommentSection orgId={orgId} />
      </div> 
    </div>
  );
}

export default CharityPage;
