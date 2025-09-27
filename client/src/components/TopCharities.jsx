import '../styles/CharityList.css';
import { Link } from 'react-router-dom';

function TopCharities({ charities }) {

  const topCharities = [...charities]
    .sort((a, b) => b.score - a.score)
    .slice(0, 3);

  return (
    <div className="panel">
      <h2>Top 3 Charities</h2>
      <div className="charity-list">
        {topCharities.map(c => (
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

            {/* right part: points + button */}
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
