import { useState } from 'react';
import { UserAPI, EmailAPI } from '../services/APIService.js';
import '../styles/ResetPassword.css';

function ResetPassword({ isVisible, onClose, onSwitchToLogin }) {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [awaitingConfirm, setAwaitingConfirm] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        setAwaitingConfirm(true);
        try {
            const response = await EmailAPI.requestConfirm(email);
            if (response.status !== 200) {
                setError(response.data?.message || 'Failed to send confirmation email.');
                setAwaitingConfirm(false);
                return;
            }
        } catch (error) {
            console.log(error);
            setError('Network error. Try again later.');
            setAwaitingConfirm(false);
            return;
        }

        try {
            const request = await EmailAPI.waitFor(email);
            if (request.status !== 200) {
                setError('Failed to send confirmation email.');
                setAwaitingConfirm(false);
                return;
            }
        } catch (error) {
            setError("Network error. Try again later.");
            setAwaitingConfirm(false);
            return;
        }

        setAwaitingConfirm(false);

        setLoading(true); // Set loading state

        try {
            await UserAPI.resetPassword({
                email: email,
                password: password
            });
            // Show success message
            alert('Password reset successful! Please log in.');

            // Switch to login modal
            onSwitchToLogin();

        } catch (err) {
            if (err.response) {
                // Server responded with error status
                setError(err.response.data?.message || 'Reset failed. Please try again.');
            } else if (err.request) {
                // Request made but no response
                setError('Network error. Please check your connection.');
            } else {
                // Other error
                setError(err.message || 'Reset failed. Please try again.');
            }
            console.error('Password reset error:', err);
        } finally {
            setLoading(false);
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

    if (!isVisible)
        return null;

    return (
        <div className="reset-password-overlay" onClick={handleOverlayClick}>
            <div className="reset-password-content" onClick={(e) => e.stopPropagation()}>
                <h2>Reset password</h2>
                {/* Error message display */}
                {error && <div className="reset-password-error-message">{error}</div>}
                {/* Form that handles user input submission */}
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"               // Text input for username
                        placeholder="Confirmation email..."    // Gray text shown when field is empty
                        className="reset-password-input-field"   // CSS class for styling
                        value={email}          // Current value from state
                        onChange={(e) => setEmail(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    <input
                        type="password"           // Hides characters as user types
                        placeholder="New Password..."    // Gray text shown when field is empty
                        className="reset-password-input-field"   // CSS class for styling
                        value={password}          // Current value from state
                        onChange={(e) => setPassword(e.target.value)} // Update state when user types
                        required                  // Field must be filled before form can submit
                    />
                    {/* Submit button that triggers handleSubmit function */}
                    <button type="submit" className="reset-password-submit-button" disabled={loading}>
                        {awaitingConfirm ? 'Waiting for email confirmation...' : (loading ? 'Confirming email address...' : 'Reset password')}
                    </button>                    
                </form>
                {/* Section with link back to login */}
                <div className="reset-password-link-section">
                    <span>Already have an account? </span>
                    <a href="#" onClick={handleSwitchToLogin} className="reset-password-link">Sign In</a>
                </div>
                {/* X button to close modal */}
                <button className="reset-password-close-button" onClick={onClose}>X</button>
            </div>
        </div>
    );
}

export default ResetPassword;
