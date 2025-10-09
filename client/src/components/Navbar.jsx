import { useNavigate } from 'react-router-dom';
import UserMenu from './UserMenu';
import '../styles/Navbar.css';
import logo from '../assets/sample_logo.png';

// onLoginClick: function to call when user clicks the login button
// isAuthenticated: boolean to check if user is logged in
// username: current logged-in username
// onLogout: function to call when user logs out
function Navbar({ onLoginClick, isAuthenticated, username, onLogout }) {
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
            {/* Right side: Show user menu if authenticated, otherwise show login button */}
            {isAuthenticated ? (
                <UserMenu username={username} onLogout={onLogout} />
            ) : (
                <button className="login-button" onClick={onLoginClick}>Login</button>
            )}
        </nav>
    );
}

export default Navbar;