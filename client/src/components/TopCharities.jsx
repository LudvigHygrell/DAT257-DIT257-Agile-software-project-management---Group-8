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
        const query = {}; 
        const response = await CharityAPI.listCharities(query);

        const mapped = response.data.value.map(c => ({
          orgId: c.charity,
          name: c.humanName,
          logo: c.charityImageFile,
          descriptionFile: c.charityDescritpionFile,
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

  const topCharities = [...charities]
    .sort((a, b) => b.score - a.score)
    .slice(0, 3);

  return (
    <div className="panel">
      <h2>Top 3 Charities</h2>
      <div className="charity-list">
        {topCharities.map(c => (
          <div key={c.orgId} className="charity-card">
            <div className="charity-info">
              <img
                src={c.logo ? `/${c.logo}` : `https://via.placeholder.com/50?text=${c.name.charAt(0)}`}
                alt={`${c.name} logo`}
                className="charity-logo"
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
