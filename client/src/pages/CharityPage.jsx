import { useParams } from "react-router-dom";
import "../styles/CharityPage.css";

function CharityPage({ charities }) {
  const { orgId } = useParams();
  const charity = charities.find((c) => c.orgId === orgId);

  if (!charity) {
    return <div>Charity not found</div>;
  }

   return (
    <div className="charity-page">
      <div className="charity-header">
        <img
          src={charity.logo ? `/${charity.logo}` : `/${charity.photo}`}
          alt={`${charity.name} logo`}
          className="charity-logo-large"
        />
        <div className="charity-title">
          <h2>{charity.name}</h2>
          <p className="charity-category">{charity.category}</p>
          <p className="charity-score">Score: {charity.score}</p>
        </div>
      </div>

      <div className="charity-body">
        <img
          src={`https://via.placeholder.com/600x300?text=${charity.category}`}
          alt={`${charity.name} work`}
          className="charity-work-img"
        />
        <p className="charity-description">{charity.description}</p>
      </div>
    </div>
  );
}

export default CharityPage;