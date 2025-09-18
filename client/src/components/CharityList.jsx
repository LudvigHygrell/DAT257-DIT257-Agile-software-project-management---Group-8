import '../styles/CharityList.css';
import { useState } from 'react';


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
          filteredCharities.map((c, index) => (
          <div key={index} className="charity-card">
            {/* left part: logo + info */}
            <div className="charity-info">
              <img
                src={`https://via.placeholder.com/50?text=${c.name.charAt(0)}`}
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
              <button className="show-more-btn">Show More</button>
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
