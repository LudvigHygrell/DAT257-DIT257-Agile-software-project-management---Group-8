import { useState } from 'react';
import { UserAPI } from '../services/APIService.js';
import '../styles/Registration.css';

// isVisible: boolean that controls if modal shows or hides
// onClose: function to call when user wants to close the modal
// onSwitchToLogin: function to switch back to login modal
function Registration({ isVisible, onClose, onSwitchToLogin }) {
    // Create state variables to store user input
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // Function that runs when user submits the registration form
    const handleSubmit = async (e) => {
        // Prevent the form from refreshing the page (default browser behavior)
        e.preventDefault();
        setError(''); // Clear any previous errors

        // Basic validation for password confirmation
        if (password !== confirmPassword) {
            setError('Passwords do not match!');
            return;
        }

        setLoading(true); // Set loading state

        try {
            // Call the registration API
            const response = await UserAPI.create({
                username,
                email,
                password,
                signed: true
            });

            console.log('Registration successful:', response);

            // Show success message
            alert('Registration successful! Please log in.');

            // Switch to login modal
            onSwitchToLogin();
        } catch (err) {
            // Display error message to user
            setError(err.message || 'Registration failed. Please try again.');
            console.error('Registration error:', err);
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

    // Function to switch back to login modal
    const handleSwitchToLogin = (e) => {
        e.preventDefault();
        onSwitchToLogin();
    };

    // If modal should not be visible, don't render anything
    if (!isVisible) return null;

    // Render the modal JSX structure
    return (
        // Dark overlay that covers the entire screen
        <div className="registration-overlay" onClick={handleOverlayClick}>
            {/* Content box in the center - stop click events from bubbling up */}
            <div className="registration-content" onClick={(e) => e.stopPropagation()}>
                {/* Modal title */}
                <h2>Create Account</h2>
                {/* Error message display */}
                {error && <div className="registration-error-message">{error}</div>}
                {/* Form that handles user input submission */}
                <form onSubmit={handleSubmit}>
                    {/* Username input field */}
                    <input
                        type="text"               // Text input for username
                        placeholder="Username"    // Gray text shown when field is empty
                        className="registration-input-field"   // CSS class for styling
                        value={username}          // Current value from state
                        onChange={(e) => setUsername(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Email input field */}
                    <input
                        type="email"              // Browser validates email format
                        placeholder="Email"       // Gray text shown when field is empty
                        className="registration-input-field"   // CSS class for styling
                        value={email}             // Current value from state
                        onChange={(e) => setEmail(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Password input field */}
                    <input
                        type="password"           // Hides characters as user types
                        placeholder="Password"    // Gray text shown when field is empty
                        className="registration-input-field"   // CSS class for styling
                        value={password}          // Current value from state
                        onChange={(e) => setPassword(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Confirm Password input field */}
                    <input
                        type="password"           // Hides characters as user types
                        placeholder="Confirm Password"    // Gray text shown when field is empty
                        className="registration-input-field"   // CSS class for styling
                        value={confirmPassword}          // Current value from state
                        onChange={(e) => setConfirmPassword(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Submit button that triggers handleSubmit function */}
                    <button type="submit" className="registration-submit-button" disabled={loading}>
                        {loading ? 'Creating Account...' : 'Create Account'}
                    </button>
                </form>
                {/* Section with link back to login */}
                <div className="registration-link-section">
                    <span>Already have an account? </span>
                    <a href="#" onClick={handleSwitchToLogin} className="registration-link">Sign In</a>
                </div>
                {/* X button to close modal */}
                <button className="registration-close-button" onClick={onClose}>X</button>
            </div>
        </div>
    );
}

export default Registration;