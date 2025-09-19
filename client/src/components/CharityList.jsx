import '../styles/CharityList.css';
import { useState } from 'react';
import { Link } from 'react-router-dom';

function CharityList({ charities }) {
  
  const [searchTerm, setSearchTerm] = useState('');
  
  const filteredCharities = charities.filter(c =>
    c.name.toLowerCase().startsWith(searchTerm.toLowerCase())
  );

  return (
    <div className="panel">
      <h2>All Charities</h2>
      <input     
        type="text"
        placeholder="Search charities..."
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
        className="search-bar"
      />
      <div className="charity-list">
        {filteredCharities.length > 0 ? (    // Only show chairty list if there are charities
          filteredCharities.map((c) => (
          <div key={c.orgId} className="charity-card">
            {/* left part: logo + info */}
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

            {/* right part: points + botton */}
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
