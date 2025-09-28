import { useState } from 'react';
import '../styles/LoginModal.css';

// isVisible: boolean that controls if modal shows or hides
// onClose: function to call when user wants to close the modal
// onSwitchToRegister: function to call when user clicks register
function LoginModal({ isVisible, onClose, onSwitchToRegister }) {
    // Create state variables to store user input for email and password
    // useState('') creates a variable with empty string as initial value
    // setEmail is the function to update the email value
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    // Function that runs when user submits the login form
    const handleSubmit = (e) => {
        // Prevent the form from refreshing the page (default browser behavior)
        e.preventDefault();
        // TODO: Replace this with actual login logic (API call, validation, etc.)
        console.log('Login attempt:', { email, password });
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
                {/* Form that handles user input submission */}
                <form onSubmit={handleSubmit}>
                    {/* Email input field */}
                    <input
                        type="email"              // Browser validates email format
                        placeholder="Email"       // Gray text shown when field is empty
                        className="input-field"   // CSS class for styling
                        value={email}             // Current value from state
                        onChange={(e) => setEmail(e.target.value)} // Update state when user types
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
                    <button type="submit" className="submit-button">Sign In</button>
                </form>
                {/* Section with additional links */}
                <div className="link-section">
                    {/* Register link that switches to registration modal */}
                    <a href="#" onClick={(e) => { e.preventDefault(); onSwitchToRegister(); }} className="link">Register</a>
                    {/* TODO: Replace # with actual forgot password page */}
                    <a href="#" className="link">Forgot Password?</a>
                </div>
                {/* X button to close modal */}
                <button className="close-button" onClick={onClose}>X</button>
            </div>
        </div>
    );
}

export default LoginModal;