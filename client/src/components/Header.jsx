import { useState } from 'react';
import logo from '../assets/sample_logo.svg';

// Create the top bar of the website with logo and login button
function Header() {
    // Keep track of whether the login window is open or closed
    // // Start with it closed (false = closed, true = open)
    const [showLogin, setShowLogin] = useState(false);

    // What happens when user clicks the "Login" button
    const handleLoginClick = () => {
        setShowLogin(true); // Open the login window
    };

    // What happens when user wants to close the login window
    const closeLogin = () => {
        setShowLogin(false); // Close the login window
    };


    // The header component that appears on the webpage
    return (
        <header style={headerStyle}>

            {/* Left side: Project name and logo */}
            <div style={logoSection}>
                <h1 style={titleStyle}>BeneSphere</h1>
                <img src={logo} alt="BeneSphere Logo" style={logoImageStyle} />
            </div>

            {/* Right side: Login button that users can click */}
            <button style={loginButton} onClick={handleLoginClick}>
                Login
            </button>

            {/* Login window, only appears when user clicks Login button */}
            {showLogin && (
                <div style={popupOverlay} onClick={closeLogin}>
                    {/* The actual login box */}
                    <div style={popupContent} onClick={(e) => e.stopPropagation()}>
                        <h2>Login</h2>
                        {/* Close button for the login box */}

                        {/* Where user types their email */}
                        <input
                        type="email" 
                        placeholder="Email"
                        style={inputStyle}
                        />

                        {/* Where user types their password */}
                        <input 
                        type="password" 
                        placeholder="Password" 
                        style={inputStyle} 
                        />

                        {/* Button to submit login information */}
                        <button style={submitButton}>Sign In</button>

                        {/* Links for REGISTRATION and FORGOT PASSWORD */}
                        <div style={linkContainer}>
                            <a href="#" style={linkStyle}>Register</a>
                            <a href="#" style={linkStyle}>Forgot Password?</a>
                        </div>

                        {/* Close button the Login window */}
                        <button style={closeButton} onClick={closeLogin}>X</button>
                    </div>
                </div>
            )}  
        </header>
    );
}


// CSS styles as JavaScript objects
const headerStyle = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '1rem 2rem',
    backgroundColor: '#fffef0ff',
    color: '#000000ff',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
    borderRadius: '15px',              
    margin: '1rem, auto',               
    maxWidth: '100%',                
    width: '90%'                       
};

const logoSection = {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
};

const logoImageStyle = {
    height: '40px',
    width: 'auto'
};

const titleStyle = {
    fontweight: 'bold',
    color: '#28a745',
    fontSize: '1.5rem',
    margin: 0,
};

const loginButton = {
    backgroundColor: '#ffffffff',
    border: '1px solid #28a745',
    color: '#28a745',
    padding: '0.5rem 1rem',
    borderRadius: '25px',
    cursor: 'pointer',
    fontSize: '1rem',
    fontWeight: 'bold',
};

// Styles for the login popup window
const popupOverlay = {
    position: 'fixed',                    // Stay in place when scrolling
    top: 0, left: 0, right: 0, bottom: 0, // Cover entire screen
    backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent black
    display: 'flex',                      // Center the popup box
    alignItems: 'center',                 // Center vertically
    justifyContent: 'center',             // Center horizontally
    zIndex: 1000                          // Appear on top of everything
};

// The actual white box with the login form
const popupContent = {
    backgroundColor: 'white',              // White background
    padding: '2rem',                       // Space inside the box
    borderRadius: '10px',                  // Rounded corners
    boxShadow: '0 10px 30px rgba(0,0,0,0.3)', // Drop shadow
    position: 'relative',                  // So we can position the X button
    minWidth: '300px',                     // Minimum width
    color: '#333'                          // Dark text
};

// Style for email and password input boxes
const inputStyle = {
    width: '100%',           // Full width of the popup
    padding: '0.7rem',       // Space inside the input box
    margin: '0.5rem 0',      // Space above and below
    border: '1px solid #ddd', // Light gray border
    borderRadius: '5px',     // Slightly rounded corners
    fontSize: '1rem',        // Normal text size
    boxSizing: 'border-box'  // Include padding in width calculation
};

// Style for the "Sign In" button
const submitButton = {
    width: '100%',              // Full width of popup
    padding: '0.7rem',          // Space inside button
    backgroundColor: '#28a745', // Blue background
    color: 'white',             // White text
    border: 'none',             // No border
    borderRadius: '5px',        // Rounded corners
    fontSize: '1rem',           // Normal text size
    cursor: 'pointer',          // Hand cursor when hovering
    marginTop: '1rem'           // Space above the button
};

// Container for Registration and Forgot Password links
const linkContainer = {
    display: 'flex',            // Put links side by side
    justifyContent: 'space-between', // Max space between them
    marginTop: '1rem'           // Space above the links
};

// Style for the Registration and Forgot Password links
const linkStyle = {
    color: '#28a745',      // Blue color
    textDecoration: 'none', // Remove underline
    fontSize: '0.9rem'     // Slightly smaller text
};

// Style for the X button to close popup
const closeButton = {
    position: 'absolute',        // Position relative to popup box
    top: '10px',                // 10px from top
    right: '15px',              // 15px from right
    backgroundColor: 'transparent', // No background
    border: 'none',             // No border
    fontSize: '1.5rem',         // Large X
    cursor: 'pointer',          // Hand cursor
    color: '#666'               // Gray color
};


export default Header;