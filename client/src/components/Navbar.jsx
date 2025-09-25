import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';
import logo from '../assets/sample_logo.png';

// onLoginClick: function to call when user clicks the login button
function Navbar({ onLoginClick }) {
    const navigate = useNavigate();

    const handleTitleClick = () => {
        navigate('/');
    };

    return (
        // Main navigation container with CSS class for styling
        <nav className="navbar">
            {/* Left side: Logo section containing title and image */}
            <div className="logo-section">
                {/* Main website title - clickable to navigate to homepage */}
                <h1 className="title" onClick={handleTitleClick} style={{ cursor: 'pointer' }}>BeneSphere</h1>
                {/* Logo image  */}
                <img src={logo} className="logo-image" />
            </div>
            {/* Right side: Login button that calls parent's function when clicked */}
            <button className="login-button" onClick={onLoginClick}>Login</button>
        </nav>
    );
}

export default Navbar;