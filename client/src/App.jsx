// Import React's useState hook for managing component state
import { useState } from 'react';
// Import CSS file for styling this main App component
import './styles/App.css';
// Import all the components used in this app
import Navbar from './components/Navbar.jsx';
import CharityList from './components/CharityList.jsx';
import TopCharities from './components/TopCharities.jsx';
import LoginModal from './components/LoginModal.jsx';

// Main App component that contains all other components
function App() {
  // State to control whether login modal is visible or hidden
  const [showLogin, setShowLogin] = useState(false);

  // Function to show the login modal (called by Navbar)
  const handleLoginClick = () => {
    setShowLogin(true);
  };

  // Function to hide the login modal (called by LoginModal)
  const closeLogin = () => {
    setShowLogin(false);
  };

  // fake data
  const fake_charities = [
    { name: "Save the Planet", category: "Environment", score: 85 },
    { name: "Food for All", category: "Hunger", score: 92 },
    { name: "Health Heroes", category: "Health", score: 78 },
    { name: "Education First", category: "Education", score: 88 },
    { name: "Animal Rescue", category: "Animals", score: 90 },
    { name: "Clean Water Initiative", category: "Environment", score: 80 },
    { name: "Global Relief", category: "Humanitarian", score: 95 },
    { name: "Youth Empowerment", category: "Education", score: 82 },
    { name: "Green Earth", category: "Environment", score: 87 },
    { name: "Medical Aid Worldwide", category: "Health", score: 91 },
    { name: "Feed the Hungry", category: "Hunger", score: 89 },
    { name: "Wildlife Guardians", category: "Animals", score: 84 },
    { name: "Community Builders", category: "Humanitarian", score: 77 },
    { name: "Scholarship Fund", category: "Education", score: 86 },
    { name: "Ocean Protectors", category: "Environment", score: 93 },
    { name: "Hope for Health", category: "Health", score: 79 },
    { name: "Hunger No More", category: "Hunger", score: 90 },
    { name: "Animal Allies", category: "Animals", score: 88 },
    { name: "Relief Now", category: "Humanitarian", score: 81 },
    { name: "Learning Ladder", category: "Education", score: 83 }
  ];

    return (
    // Main container with padding to account for fixed navbar
    <div className="app-container">
      {/* Navigation bar at top - receives function to show login modal */}
      <Navbar onLoginClick={handleLoginClick} />

      {/* Main content area with two-column layout */}
      <div className="main-content" style={{ display: "flex", gap: "1rem", padding: "1rem" }}>
        {/* Left column: Shows top-rated charities (1/3 width) */}
        <div className="panel" style={{ flex: 1 }}>
          <TopCharities charities={fake_charities} />
        </div>

        {/* Right column: Shows searchable charity list (2/3 width) */}
        <div className="panel" style={{ flex: 2 }}>
          <CharityList charities={fake_charities} />
        </div>
      </div>

      {/* Login modal - only visible when showLogin is true */}
      <LoginModal isVisible={showLogin} onClose={closeLogin} />
    </div>
  );

  
}

export default App;