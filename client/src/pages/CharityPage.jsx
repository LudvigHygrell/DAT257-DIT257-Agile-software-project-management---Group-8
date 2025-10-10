import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import { CharityAPI } from "../services/APIService.js";
import "../styles/CharityPage.css";
import thumbsUp from "../assets/thumbs-up.png";
import thumbsDown from "../assets/thumbs-down.png";

function CharityPage() {
  const { orgId } = useParams();
  const charity = fake_charities.find((c) => c.orgId === orgId);

  const [comments, setComments] = useState([
    { name: "Mari", text: "Wonderful initiative!", date: "2025-09-24", vote: "like" },
    { name: "Lucia", text: "I donated and had a great experience.", date: "2025-09-23", vote: null },
    { name: "Giovanni", text: "I don’t really trust how the money is used.", date: "2025-09-22", vote: "dislike" },
    { name: "Sara", text: "You are an example for everyone.", date: "2025-09-21", vote: "like" },
    { name: "Alessandro", text: "I shared your project with my friends.", date: "2025-09-20", vote: null },
    { name: "Francesca", text: "Thank you for what you do!", date: "2025-09-19", vote: "like" },
    { name: "Paolo", text: "Feels like the organization is not transparent enough.", date: "2025-09-18", vote: "dislike" },
    { name: "Elena", text: "I would love to participate in your activities.", date: "2025-09-17", vote: null },
    { name: "Roberto", text: "Keep it up!", date: "2025-09-16", vote: "like" },
    { name: "Martina", text: "I had a bad experience when asking for help.", date: "2025-09-15", vote: "dislike" },
    { name: "Stefano", text: "I support you every year.", date: "2025-09-14", vote: null },
    { name: "Valentina", text: "Great transparency and communication.", date: "2025-09-13", vote: "like" },
    { name: "Davide", text: "Innovative and useful project.", date: "2025-09-12", vote: null },
    { name: "Chiara", text: "I feel like too much money goes to administration.", date: "2025-09-11", vote: "dislike" },
    { name: "Federico", text: "I have seen the concrete results of your commitment.", date: "2025-09-10", vote: "like" },
    { name: "Laura", text: "I recommend everyone to support you.", date: "2025-09-09", vote: null },
    { name: "Simone", text: "Thank you for helping my family.", date: "2025-09-08", vote: "like" }
  ]);

  const [newComment, setNewComment] = useState("");

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

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    const newCommentObj = {
      name: "Anonymous",
      text: newComment,
      date: new Date().toISOString().slice(0,10),
      vote: null
    };

    setComments([newCommentObj, ...comments]); 
    setNewComment("");
  };
  const [sortOrder, setSortOrder] = useState("newest"); // "newest" or "oldest"
   // Sort comments based on the selected order
  const getSortedComments = () => {
  return [...comments].sort((a, b) => {
    switch (sortOrder) {
      case "newest": 
        return new Date(b.date) - new Date(a.date);
      case "oldest": 
        return new Date(a.date) - new Date(b.date);
      default:
        return 0; // no sorting
    }
  });
};
  const sortedComments = getSortedComments();

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
        {/* Sort buttons */}
          <div className="sort-buttons">
            <button className={`sort-newest ${sortOrder === "newest" ? "active" : ""}`}  
              onClick={() => setSortOrder("newest")}>Newest</button>
            <button className={`sort-oldest ${sortOrder === "oldest" ? "active" : ""}`}  
              onClick={() => setSortOrder("oldest")}>Oldest</button>
          </div>
        <div className="charity-comments">
          {sortedComments.map((c, idx) => (
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
    </div>
  );
}

export default CharityPage;
