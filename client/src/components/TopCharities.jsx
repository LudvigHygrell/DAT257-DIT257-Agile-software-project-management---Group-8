import '../styles/CharityList.css';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { CharityAPI } from '../services/APIService.js';

function TopCharities() {
  const [charities, setCharities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCharities = async () => {
      setLoading(true);
      try {
        // Use backend sorting to get top 3 charities directly
        const query = {
          first: 0,
          max_count: 3,
          sorting: {
            field: "totalScore",
            ordering: "descending"
          }
        };
        console.log('TopCharities - Fetching with query:', query);
        const response = await CharityAPI.listCharities(query);
        console.log('TopCharities - Response:', response);

        const mapped = response.data.value.map(c => ({
          orgId: c.charity,
          name: c.humanName,
          logo: c.charityImageFile,
          descriptionFile: c.charityDescriptionFile,
          homepage: c.homePageUrl,
          score: c.totalScore,
          category: ''
        }));

        setCharities(mapped);
      } catch (err) {
        console.error(err);
        setError('Failed to load charities');
      } finally {
        setLoading(false);
      }
    };

    fetchCharities();
  }, []);

  if (loading) return <p>Loading top charities...</p>;
  if (error) return <p>{error}</p>;

  // No need to sort client-side anymore - backend already sorted and limited to top 3
  const topCharities = charities;

  return (
    <div className="panel">
      <h2>Top 3 Charities</h2>
      <div className="charity-list">
        {topCharities.map(c => (
          <div key={c.orgId} className="charity-card">
            <div className="charity-info">
              <img
                src={`http://localhost:8080/api/files/public/${c.logo || 'default_charity_logo.png'}`}
                alt={`${c.name} logo`}
                className="charity-logo"
                onError={(e) => { e.target.src = 'http://localhost:8080/api/files/public/default_charity_logo.png'; }}
              />
              <div className="charity-text">
                <h3 className="charity-name">{c.name}</h3>
                <p className="charity-category">{c.category}</p>
              </div>
            </div>
            <div className="charity-actions">
              <span className="charity-score">{c.score}</span>
              <Link to={`/${c.orgId}`} className="show-more-btn">
                Show More
              </Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TopCharities;
