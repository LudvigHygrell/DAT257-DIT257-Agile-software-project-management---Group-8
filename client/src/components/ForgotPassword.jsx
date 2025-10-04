import { useState } from 'react';
import '../styles/ForgotPassword.css';

// isVisible: boolean that controls if modal shows or hides
// onClose: function to call when user wants to close the modal
// onSwitchToLogin: function to call when user clicks back to login
function ForgotPassword({ isVisible, onClose, onSwitchToLogin }) {
    // Create state variable to store user input for email
    const [email, setEmail] = useState('');

    // Function that runs when user submits the forgot password form
    const handleSubmit = (e) => {
        // Prevent the form from refreshing the page (default browser behavior)
        e.preventDefault();
        // TODO: Replace this with actual forgot password logic (API call to send reset email)
        console.log('Password reset requested for:', email);
        // TODO: Show success message to user
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

    return (
        // Dark overlay that covers the entire screen
        <div className="forgot-password-overlay" onClick={handleOverlayClick}>
            {/* Content box in the center, stop click events from bubbling up */}
            <div className="forgot-password-content" onClick={(e) => e.stopPropagation()}>
                {/* Modal title */}
                <h2>Forgot Password</h2>
                {/* Instruction text */}
                <p className="forgot-password-description">
                    Enter your email address to send a link to reset your password.
                </p>
                {/* Form that handles user input submission */}
                <form onSubmit={handleSubmit}>
                    {/* Email input field */}
                    <input
                        type="email"                        // Browser validates email format
                        placeholder="Email"                 // Gray text shown when field is empty
                        className="forgot-password-input"   // CSS class for styling
                        value={email}                       // Current value from state
                        onChange={(e) => setEmail(e.target.value)} // Update state when user types
                        required                            // Field must be filled before form can submit
                    />
                    {/* Submit button that triggers handleSubmit function */}
                    <button type="submit" className="forgot-password-button">Send Link</button>
                </form>
                {/* Section with back to login link */}
                <div className="forgot-password-link-section">
                    <a
                        href="#"
                        onClick={(e) => { e.preventDefault(); onSwitchToLogin(); }}
                        className="forgot-password-link"
                    >
                        Back to Login
                    </a>
                </div>
                {/* X button to close modal */}
                <button className="forgot-password-close-button" onClick={onClose}>X</button>
            </div>
        </div>
    );
}

export default ForgotPassword;
