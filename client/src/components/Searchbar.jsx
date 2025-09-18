import { useState } from "react";
import "../styles/Searchbar.css"; // Separetes the style part from the logic

function Searchbar({ placeholder, userInput }) {
  const [query, setQuery] = useState("");

  // This runs every time the user types
  const handleChange = (e) => {
    const value = e.target.value;
    setQuery(value);         // updates the input box to the user
    if (userInput) {
      userInput(value);       // gives the parent the written value
    }
  };

  return (
    <div className="searchbar-container">
      <input
        type="text"
        placeholder={placeholder}
        value={query}
        onChange={handleChange}  
        className="searchbar-input"
      />
    </div>
  );
}

export default Searchbar;
