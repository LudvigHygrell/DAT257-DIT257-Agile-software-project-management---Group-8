import './styles/App.css';
import Navbar from './components/Navbar.jsx';
import CharityList from './components/CharityList.jsx';
import TopCharities from './components/TopCharities.jsx';
import CharityPage from './pages/CharityPage.jsx';
import { fake_charities } from './Dummydata.jsx';

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

function App() {

  return (
    <>
      <Navbar />

      <Routes>
        {/* Main page with two columns */}
        <Route
          path="/"
          element={
            <div className="main-content" style={{ display: "flex", gap: "1rem", padding: "1rem" }}>
              <div className="panel" style={{ flex: 1 }}>
                <TopCharities charities={fake_charities} />
              </div>
              <div className="panel" style={{ flex: 2 }}>
                <CharityList charities={fake_charities} />
              </div>
            </div>
          }
        />
        {/* Charity detail page */}
        <Route
          path="/:orgId"
          element={<CharityPage charities={fake_charities} />}
        />
      </Routes>
    </>
  );
}

export default App;