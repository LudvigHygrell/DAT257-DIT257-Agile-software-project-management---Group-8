import { useState } from 'react';
import logo from '../assets/sample_logo.png';
import '../styles/Navbar.css';

function Navbar() {
    const [showLogin, setShowLogin] = useState(false);

    const handleLoginClick = () => {
        setShowLogin(true);
    };

    const closeLogin = () => {
        setShowLogin(false);
    };

    return (
            <nav className="navbar">
                <div className="logo-section">
                    <h1 className="title">BeneSphere</h1>
                    <img src={logo} alt="BeneSphere Logo" className="logo-image" />
                </div>
                <button className="login-button" onClick={handleLoginClick}>Login</button>
            

            {showLogin && (
                <div className="popup-overlay" onClick={closeLogin}>
                    <div className="popup-content" onClick={(e) => e.stopPropagation()}>
                        <h2>Login</h2>
                        <input
                            type="email"
                            placeholder="Email"
                            className="input-field"
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            className="input-field"
                        />
                        <button className="submit-button">Sign In</button>
                        <div className="link-section">
                            <a href="#" className="link">Register</a>
                            <a href="#" className="link">Forgot Password?</a>
                        </div>
                        <button className="close-button" onClick={closeLogin}>X</button>
                    </div>
                </div>
            )}
        </nav>
    );
}

export default Navbar;