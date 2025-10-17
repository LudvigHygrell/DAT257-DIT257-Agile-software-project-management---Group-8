import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import "../styles/CharityPage.css";
import CommentSection from "../components/CommentSection.jsx";
import { CharityAPI } from "../services/APIService.js";
import { FileAPI } from "../services/APIService.js";

function CharityPage({ isAuthenticated, onRequireLogin }) {
  const { orgId } = useParams();
  const [charity, setCharity] = useState(null);
  const [charityDescription, setCharityDescription] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [images, setImages] = useState([]);
  
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
    };

    handleResize();

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);


  useEffect(() => {
    const fetchCharity = async () => {
      try {
        setLoading(true);
        const res = await CharityAPI.getCharity(orgId);
        setCharity(res.data.value);
      } catch (err) {
        console.error("Error fetching charity:" + err);
        setError("Failed to load charity data.");
      } finally {
        setLoading(false);
      }
    };
    fetchCharity();
  }, [orgId]);
  
  useEffect(() => {
    if (!charity) return;
    
    const fetchCharityDescription = async () => {
      try {
        if (charity.charityDescriptionFile) {
          const res = await FileAPI.getPublicFile(charity.charityDescriptionFile);
          console.log("Fetched charity description:", res);
          setCharityDescription(res.data);
        }
      } catch (err) {
        console.error("There is no description available", err);
      }
    };

    const fetchActivities = async () => {
      const index = await FileAPI.getPublicFile(`${charity.activitiesDirectory}/index.json`)
        .then((r) => r.data);
      setImages(index.map((v) => `${charity.activitiesDirectory}/${v}`))
    };

    fetchCharityDescription();
    fetchActivities();
  }, [charity]);
  

  // Handle upvote/downvote/delete
  const handleVote = async (up) => {
    if (!isAuthenticated) {
      onRequireLogin();
      return;
    }
    try {
      await CharityAPI.vote({ charity: orgId, up: up });
      // Optionally refetch to update score immediately
      const updated = await CharityAPI.getCharity(orgId);
      setCharity(updated.data.value);
    } catch (err) {
      console.error("Vote error:", err);
      alert("Could not submit your vote. Please try again.");
    }finally {
      alert("Vote submitted successfully.");
    }
  };

  const handleDeleteVote = async () => {
    try {
      if (!isAuthenticated) {
        onRequireLogin();
        return;
      }
      await CharityAPI.removeVote({ charity: orgId });
      const updated = await CharityAPI.getCharity(orgId);
      setCharity(updated.data.value);
    } catch (err) {
      console.error("Remove vote error:", err);
      alert("Could not delete your vote. Please try again.");
    }finally {
      alert("Vote deleted successfully.");
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
            src={`http://localhost:8080/api/files/public/${charity.charityImageFile || 'default_charity_logo.png'}`}
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
                <img src={'http://localhost:8080/api/files/public/thumbs-up.png'} alt="Thumbs up" />
              </button>

              <button
                className="vote-btn"
                onClick={() => handleVote(false)}
                aria-label="Dislike"
                title="Dislike"
              >
                <img src={'http://localhost:8080/api/files/public/thumbs-down.png'} alt="Thumbs down" />
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
          <h3>Charity Description</h3>
          <p className="charity-description">
            {charityDescription || "No description available."}
          </p>
        </div>

        <div className="charity-photos-section">
          <h3>Our activities</h3>
          <div className="charity-photos">
            {images.map((image) => (
              <img
                src={`http://localhost:8080/api/files/public/${image}`}
                key={image}/>
            ))}
          </div>
        </div>
      </div>

      {windowWidth > 850 && <CommentSection orgId={orgId} isAuthenticated={isAuthenticated} onRequireLogin={onRequireLogin} />}
    </div>
  );
}

export default CharityPage;
