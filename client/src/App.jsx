import { useEffect, useState } from 'react';
import './styles/App.css';
import Navbar from './components/Navbar.jsx';
import Searchbar from './components/Searchbar.jsx';

function App() {
  // State to hold the search request from the Searchbar component
  const [searchRequest, setSearchRequest] = useState("");
  
  // Effect to log the search request whenever it changes
  const handleSearch = (value) => {setSearchRequest(value); console.log("Search request updated to:", value);};

  return (
    <div style = {{ paddingTop: '80px' }}>
      <Navbar />
      <Searchbar placeholder="Search for charity..." userInput={handleSearch} />
      <h1>Hello Test!</h1>
    </div>
  );
}

export default App;