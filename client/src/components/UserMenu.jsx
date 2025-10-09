import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/UserMenu.css';

// Component for user account dropdown menu (similar to Reddit)
function UserMenu({ username, onLogout }) {
    const navigate = useNavigate();

    // State to control dropdown visibility
    const [isOpen, setIsOpen] = useState(false);
    // Ref to detect clicks outside the menu
    const menuRef = useRef(null);

    // Toggle dropdown open/closed
    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };

        // Add event listener when menu is open
        if (isOpen) {
            document.addEventListener('mousedown', handleClickOutside);
        }

        // Cleanup event listener
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [isOpen]);

    // Handle navigation to settings
    const handleSettingsClick = () => {
        setIsOpen(false);
        navigate('/settings');
    };

    // Handle logout
    const handleLogoutClick = () => {
        setIsOpen(false);
        onLogout();
    };

    return (
        <div className="user-menu-container" ref={menuRef}>
            {/* User button that shows username and avatar */}
            <button className="user-menu-button" onClick={toggleMenu}>
                {/* Simple avatar with first letter of username */}
                <div className="user-avatar">
                    {username ? username.charAt(0).toUpperCase() : 'U'}
                </div>
                <span className="user-menu-username">{username}</span>
                {/* Dropdown arrow */}
                <svg
                    className={`dropdown-arrow ${isOpen ? 'open' : ''}`}
                    width="12"
                    height="12"
                    viewBox="0 0 12 12"
                    fill="currentColor"
                >
                    <path d="M6 9L1.5 4.5L2.55 3.45L6 6.9L9.45 3.45L10.5 4.5L6 9Z" />
                </svg>
            </button>

            {/* Dropdown menu */}
            {isOpen && (
                <div className="user-dropdown-menu">
                    {/* Menu items */}
                    <div className="dropdown-section">
                        <button className="dropdown-item" onClick={handleSettingsClick}>
                            <svg className="dropdown-icon" width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                                <path d="M10 10C12.21 10 14 8.21 14 6C14 3.79 12.21 2 10 2C7.79 2 6 3.79 6 6C6 8.21 7.79 10 10 10ZM10 12C7.33 12 2 13.34 2 16V18H18V16C18 13.34 12.67 12 10 12Z" />
                            </svg>
                            <span>Account Settings</span>
                        </button>

                        <button className="dropdown-item" onClick={() => console.log('Dark Mode')}>
                            <svg className="dropdown-icon" width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                                <path d="M10 2C5.03 2 1 6.03 1 11C1 15.97 5.03 20 10 20C11.83 20 13.5 19.4 14.87 18.4C12.1 18.4 9.85 16.15 9.85 13.38C9.85 10.61 12.1 8.36 14.87 8.36C15.41 8.36 15.93 8.43 16.43 8.56C15.4 4.85 13 2 10 2Z" />
                            </svg>
                            <span>Dark Mode</span>
                            <div className="toggle-switch">
                                <input type="checkbox" id="dark-mode-toggle" />
                                <label htmlFor="dark-mode-toggle"></label>
                            </div>
                        </button>
                    </div>

                    <div className="dropdown-divider"></div>

                    <div className="dropdown-section">
                        <button className="dropdown-item" onClick={handleLogoutClick}>
                            <svg className="dropdown-icon" width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                                <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z" />
                            </svg>
                            <span>Log Out</span>
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default UserMenu;
