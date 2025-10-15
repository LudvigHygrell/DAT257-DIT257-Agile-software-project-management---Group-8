import '../styles/CharityList.css';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { CharityAPI } from '../services/APIService.js'; 

function CharityList() {
  const [charities, setCharities] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortOption, setSortOption] = useState('score-desc');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCharities = async () => {
      setLoading(true);
      try {
        // Parse sort option to build backend sorting query
        const [field, ordering] = sortOption.split('-');
        const sortingConfig = {
          'score': { field: 'totalScore', ordering: ordering === 'desc' ? 'descending' : 'ascending' },
          'name': { field: 'humanName', ordering: ordering === 'desc' ? 'descending' : 'ascending' }
        };

        const query = {
          first: 0,
          max_count: 100,
          sorting: sortingConfig[field]
        };
        console.log('Fetching charities with query:', query);
        const response = await CharityAPI.listCharities(query);
        console.log('Charity API response:', response);

        if (!response.data || !response.data.value) {
          throw new Error('Invalid response format');
        }

        const mapped = response.data.value.map(c => ({
          orgId: c.charity,
          name: c.humanName,
          logo: c.charityImageFile,
          descriptionFile: c.charityDescritpionFile,
          homepage: c.homePageUrl,
          score: c.totalScore,
          category: ''
        }));
        console.log('Mapped charities:', mapped);
        setCharities(mapped);
      } catch (err) {
        console.error('Error loading charities:' + err);
        console.error('Error details:' + (err.response || err.message));
        setError('Failed to load charities');
      } finally {
        setLoading(false);
      }
    };

    fetchCharities();
  }, [sortOption]);

  const filteredCharities = charities.filter(c =>
    c.name.toLowerCase().startsWith(searchTerm.toLowerCase())
  );

  if (loading) return <p>Loading charities...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="panel">
      <h2>All Charities</h2>
      <div className="charity-controls">
        <select
          value={sortOption}
          onChange={e => setSortOption(e.target.value)}
          className="sort-dropdown"
        >
          <option value="score-desc">Highest Score</option>
          <option value="score-asc">Lowest Score</option>
          <option value="name-asc">Name (A-Z)</option>
          <option value="name-desc">Name (Z-A)</option>
        </select>
        <input
          type="text"
          placeholder="Search charities..."
          value={searchTerm}
          onChange={e => setSearchTerm(e.target.value)}
          className="search-bar"
        />
      </div>
      <div className="charity-list">
        {filteredCharities.length > 0 ? (
          filteredCharities.map((c) => (
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
          ))
        ) : (
          <p className="no-results">No charities found.</p>
        )}
      </div>
    </div>
  );
}

export default CharityList;
