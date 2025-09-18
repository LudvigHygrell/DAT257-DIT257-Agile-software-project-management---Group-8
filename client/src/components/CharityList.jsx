import '../styles/CharityList.css';


function CharityList({ charities }) {
  return (
    <div className="panel">
      <h2>All Charities</h2>
      <div className="charity-list">
        {charities.map((c, index) => (
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
        ))}
      </div>
    </div>
  );
}

export default CharityList;
