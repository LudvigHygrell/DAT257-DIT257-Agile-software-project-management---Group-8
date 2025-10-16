import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import { CharityAPI } from "../services/APIService.js";
import CommentSection from "../components/CommentSection.jsx";
import "../styles/CharityPage.css";

function CharityPage() {
  const { orgId } = useParams();

  // State variables for charity data
  const [charity, setCharity] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
    };

    handleResize();

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // Fetch charity data from API
  useEffect(() => {
    const fetchCharity = async () => {
      setLoading(true);
      setError(null);
      try {
        console.log('Fetching charity with orgId:', orgId);
        const response = await CharityAPI.getCharity(orgId);
        console.log('Charity API response:', response);
        console.log('Response data:', response.data);

        if (!response.data || !response.data.value) {
          console.error('Invalid response format - response.data:', response.data);
          throw new Error('Invalid response format');
        }

        const charityData = response.data.value;
        console.log('Charity data:', charityData);

        const mappedCharity = {
          orgId: charityData.charity,
          name: charityData.humanName,
          logo: charityData.charityImageFile,
          description: charityData.charityDescritpionFile || "No description available.",
          homepage: charityData.homePageUrl,
          score: charityData.totalScore,
          category: charityData.category || "General"
        };
        console.log('Mapped charity:', mappedCharity);
        setCharity(mappedCharity);
      } catch (err) {
        console.error('Error loading charity:', err);
        console.error('Error details:', err.response);
        setError('Failed to load charity details: ' + (err.message || 'Unknown error'));
      } finally {
        setLoading(false);
      }
    };

    if (orgId) {
      fetchCharity();
    }
  }, [orgId]);

  if (loading) {
    return <div className="charity-not-found">Loading charity details...</div>;
  }

  if (error || !charity) {
    return <div className="charity-not-found">{error || "Charity not found"}</div>;
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

      {/* RIGHT PANEL - Comment Section */}
      {windowWidth > 1200 && <CommentSection orgId={orgId} />}
    </div>
  );
}

export default CharityPage;
