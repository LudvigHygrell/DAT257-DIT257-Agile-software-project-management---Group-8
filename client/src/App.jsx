import { Routes, Route } from "react-router-dom";

import Navbar from './components/Navbar.jsx';
import CharityList from './components/CharityList.jsx';
import TopCharities from './components/TopCharities.jsx';
import CharityPage from './pages/CharityPage.jsx';
import Modals from './components/Modals.jsx';
import { fake_charities } from './Dummydata.jsx';
import { useModals } from './hooks/useModals.js';

import './styles/App.css';


// Main App component that contains all other components
function App() {
  // Use custom hook to manage all modal state and handlers
  const modalProps = useModals();

  return (
    // main cointainer with padding
    <div className="app-container">
      {/* Navigation bar at top - receuves function to show login modal */}
      <Navbar onLoginClick={modalProps.handleLoginClick} />

      <Routes>
        {/* Main page with two columns layout */}
        <Route
          path="/"
          element={
            <div className="main-content" style={{ display: "flex", gap: "1rem", padding: "1rem" }}>
              
              { /* Left column: shows top rated charities (1/3 of page width) */}
              <div className="panel" style={{ flex: 1 }}>
                <TopCharities charities={fake_charities} />
              </div>

              {/* Right column: shows searchable charity lists (2/3 of page width) */}
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

      {/* All modals for the application */}
      <Modals {...modalProps} />
    </div>

  );
}

export default App;