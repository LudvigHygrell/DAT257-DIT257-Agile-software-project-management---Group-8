import { useState } from 'react';
import { UserAPI } from '../services/APIService.js';
import '../styles/LoginModal.css';

// isVisible: boolean that controls if modal shows or hides
// onClose: function to call when user wants to close the modal
// onSwitchToRegister: function to call when user clicks register
// onLoginSuccess: function to call when login is successful
function LoginModal({ isVisible, onClose, onSwitchToRegister, onLoginSuccess }) {
    // Create state variables to store user input for username/email and password
    // useState('') creates a variable with empty string as initial value
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // Function that runs when user submits the login form
    const handleSubmit = async (e) => {
        // Prevent the form from refreshing the page (default browser behavior)
        e.preventDefault();
        setError(''); // Clear any previous errors
        setLoading(true); // Set loading state

        try {
            // Backend expects EITHER 'username' OR 'email', not both
            // Check if input contains @ to determine if it's an email
            const isEmail = usernameOrEmail.includes('@');
            const loginData = isEmail
                ? { email: usernameOrEmail, password }
                : { username: usernameOrEmail, password };

            const response = await UserAPI.login(loginData);

            // Axios returns response in response.data
            const token = response.data.token;
            const username = response.data.username;

            // Store JWT token in localStorage and update auth state
            if (token && username) {
                console.log('Login successful! Token stored.');

                // Update authentication state via callback
                if (onLoginSuccess) {
                    onLoginSuccess(token, username);
                }

                // Close modal on successful login
                onClose();
            } else {
                throw new Error('No token or username received from server');
            }
        } catch (err) {
            // Handle axios error response
            if (err.response) {
                // Server responded with error status
                setError(err.response.data || 'Login failed. Please check your credentials.');
            } else if (err.request) {
                // Request made but no response
                setError('Network error. Please check your connection.');
            } else {
                // Other error
                setError(err.message || 'Login failed. Please try again.');
            }
            console.error('Login error:', err);
        } finally {
            setLoading(false); // Reset loading state
        }
    };

    // Function that runs when user clicks outside the modal content
    const handleOverlayClick = (e) => {
        // Only close if user clicked the dark overlay, not the white content box
        if (e.target === e.currentTarget) {
            onClose(); // Call the function passed from parent to close modal
        }
    };

    // If modal should not be visible, don't render anything
    if (!isVisible) return null;

    // Render the modal JSX structure
    return (
        // shadow overlay that covers the entire screen
        <div className="popup-overlay" onClick={handleOverlayClick}>
            {/* content box in the center - stop click events from bubbling up */}
            <div className="popup-content" onClick={(e) => e.stopPropagation()}>
                {/* Modal title */}
                <h2>Login</h2>
                {/* Error message display */}
                {error && <div className="error-message">{error}</div>}
                {/* Form that handles user input submission */}
                <form onSubmit={handleSubmit}>
                    {/* Username or Email input field */}
                    <input
                        type="text"               // Text input for username or email
                        placeholder="Username or Email"    // Gray text shown when field is empty
                        className="input-field"   // CSS class for styling
                        value={usernameOrEmail}   // Current value from state
                        onChange={(e) => setUsernameOrEmail(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Password input field */}
                    <input
                        type="password"           // Hides characters as user types
                        placeholder="Password"    // Gray text shown when field is empty
                        className="input-field"   // CSS class for styling
                        value={password}          // Current value from state
                        onChange={(e) => setPassword(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Submit button that triggers handleSubmit function */}
                    <button type="submit" className="submit-button" disabled={loading}>
                        {loading ? 'Signing in...' : 'Sign In'}
                    </button>
                </form>
                {/* Section with additional links */}
                <div className="link-section">
                    {/* Register link that switches to registration modal */}
                    <a href="#" onClick={(e) => { e.preventDefault(); onSwitchToRegister(); }} className="link">Register</a>
                </div>
                {/* X button to close modal */}
                <button className="close-button" onClick={onClose}>X</button>
            </div>
        </div>
    );
}

export default LoginModal;