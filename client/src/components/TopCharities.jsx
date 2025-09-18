function TopCharities({ charities }) {
    
  const topCharities = charities.slice(0, 3);

  return (
    <div className="panel">
      <h2> Top 3 Charities</h2>
      {topCharities.map((c, index) => (
        <div key={index} className="charity-card">
          <h3>{c.name}</h3>
          <p>Score: {c.score}</p>
          <p>Category: {c.category}</p>
        </div>
      ))}
    </div>
  );
}

export default TopCharities;