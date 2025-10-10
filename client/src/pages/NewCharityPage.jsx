import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import "../styles/NewCharityPage.css";
import thumbsUp from "../assets/thumbs-up.png";
import thumbsDown from "../assets/thumbs-down.png";
import CommentSection from "../components/CommentSection.jsx";
import { CharityAPI } from "../services/APIService.js";

function CharityPage() {
  const { orgId } = useParams();
  const [charity, setCharity] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCharity = async () => {
      try {
        setLoading(true);
        const res = await CharityAPI.getCharity(orgId);
        setCharity(res.data.value);
      } catch (err) {
        console.error("Error fetching charity:", err);
        setError("Failed to load charity data.");
      } finally {
        setLoading(false);
      }
    };
    fetchCharity();
  }, [orgId]);

  // Handle upvote/downvote/delete
  const handleVote = async (up) => {
    try {
      await CharityAPI.vote({ charityId: orgId, upvote: up });
      // Optionally refetch to update score immediately
      const updated = await CharityAPI.getCharity(orgId);
      setCharity(updated.data.value);
    } catch (err) {
      console.error("Vote error:", err);
      alert("Could not submit your vote. Please try again.");
    }
  };

  const handleDeleteVote = async () => {
    try {
      await CharityAPI.removeVote({ charityId: orgId });
      const updated = await CharityAPI.getCharity(orgId);
      setCharity(updated.data.value);
    } catch (err) {
      console.error("Remove vote error:", err);
      alert("Could not delete your vote. Please try again.");
    }
  };

  if (loading) return <p>Loading charity...</p>;
  if (error) return <p>{error}</p>;
  if (!charity) return <p>Charity not found.</p>;

  return (
    <div className="charity-page">
      <div className="charity-page-info">
        <div className="charity-header">
          <img
            src={charity.charityImageFile ? `/${charity.charityImageFile}` : `https://via.placeholder.com/100?text=${charity.humanName?.[0]}`}
            alt={`${charity.humanName} logo`}
            className="charity-logo-large"
          />
          <div className="charity-title">
            <h2>{charity.humanName}</h2>
            <p className="charity-page-score">Score: {charity.totalScore}</p>

            <div className="vote-buttons" role="group" aria-label="Vote">
              <button
                className="vote-btn"
                onClick={() => handleVote(true)}
                aria-label="Like"
                title="Like"
              >
                <img src={thumbsUp} alt="Thumbs up" />
              </button>

              <button
                className="vote-btn"
                onClick={() => handleVote(false)}
                aria-label="Dislike"
                title="Dislike"
              >
                <img src={thumbsDown} alt="Thumbs down" />
              </button>

              <button
                className="delete-vote-btn"
                onClick={handleDeleteVote}
                aria-label="Delete vote"
                title="Delete Vote"
              >
                Delete Vote
              </button>
            </div>
          </div>
        </div>

        <div className="charity-description-section">
          <h3>Who are we?</h3>
          <p className="charity-description">
            {charity.charityDescriptionFile || "No description available."}
          </p>
        </div>

        <div className="charity-photos-section">
          <h3>Our activities</h3>
          <div className="charity-photos">
            <img
              src={`https://via.placeholder.com/400x250?text=${charity.humanName}+1`}
              alt={`${charity.humanName} activity 1`}
            />
            <img
              src={`https://via.placeholder.com/400x250?text=${charity.humanName}+2`}
              alt={`${charity.humanName} activity 2`}
            />
          </div>
        </div>
      </div>

      <CommentSection orgId={orgId} />
    </div>
  );
}

export default CharityPage;
