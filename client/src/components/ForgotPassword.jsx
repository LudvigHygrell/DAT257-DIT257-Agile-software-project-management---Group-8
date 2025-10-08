import { useState } from 'react';
import { UserAPI } from '../services/APIService.js';
import '../styles/ForgotPassword.css';

// isVisible: boolean that controls if modal shows or hides
// onClose: function to call when user wants to close the modal
// onSwitchToLogin: function to call when user clicks back to login
function ForgotPassword({ isVisible, onClose, onSwitchToLogin }) {
    // Create state variables to store user input and component state
    const [email, setEmail] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);
    const [step, setStep] = useState(1); // Step 1: Enter email, Step 2: Enter code and new password

    // Function that runs when user submits the forgot password form
    const handleSubmit = async (e) => {
        // Prevent the form from refreshing the page (default browser behavior)
        e.preventDefault();
        setError('');
        setSuccess('');
        setLoading(true);

        try {
            if (step === 1) {
                // Step 1: Request password reset (send verification code to email)
                // IMPLEMENT EMAIL SENDING IN BACKEND
                console.log('Password reset requested for:', email);
                setSuccess('Verification code sent to your email (check console for development)');
                setStep(2); // Move to step 2
            } else {
                // Step 2: Reset password with verification code
                if (newPassword !== confirmPassword) {
                    setError('Passwords do not match!');
                    setLoading(false);
                    return;
                }

                const response = await UserAPI.resetPassword({
                    email,
                    new_password: newPassword,
                    verification_code: parseInt(verificationCode)
                });

                setSuccess(response || 'Password reset successful! Please log in.');

                // Switch back to login after a delay
                setTimeout(() => {
                    onSwitchToLogin();
                }, 2000);
            }
        } catch (err) {
            // Handle axios error response
            if (err.response) {
                // Server responded with error status
                setError(err.response.data || 'Failed to reset password. Please try again.');
            } else if (err.request) {
                // Request made but no response
                setError('Network error. Please check your connection.');
            } else {
                // Other error
                setError(err.message || 'Failed to reset password. Please try again.');
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

    // If modal should not be visible, don't render anything
    if (!isVisible) return null;

    return (
        // Dark overlay that covers the entire screen
        <div className="forgot-password-overlay" onClick={handleOverlayClick}>
            {/* Content box in the center, stop click events from bubbling up */}
            <div className="forgot-password-content" onClick={(e) => e.stopPropagation()}>
                {/* Modal title */}
                <h2>Forgot Password</h2>
                {/* Error message display */}
                {error && <div className="forgot-password-error-message">{error}</div>}
                {/* Success message display */}
                {success && <div className="forgot-password-success-message">{success}</div>}

                {/* Instruction text */}
                <p className="forgot-password-description">
                    {step === 1
                        ? 'Enter your email address to receive a verification code.'
                        : 'Enter the verification code and your new password.'}
                </p>

                {/* Form that handles user input submission */}
                <form onSubmit={handleSubmit}>
                    {step === 1 ? (
                        /* Step 1: Email input field */
                        <input
                            type="email"
                            placeholder="Email"
                            className="forgot-password-input"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    ) : (
                        /* Step 2: Verification code and new password fields */
                        <>
                            <input
                                type="text"
                                placeholder="Verification Code"
                                className="forgot-password-input"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value)}
                                required
                            />
                            <input
                                type="password"
                                placeholder="New Password"
                                className="forgot-password-input"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                required
                            />
                            <input
                                type="password"
                                placeholder="Confirm New Password"
                                className="forgot-password-input"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </>
                    )}

                    {/* Submit button that triggers handleSubmit function */}
                    <button type="submit" className="forgot-password-button" disabled={loading}>
                        {loading ? 'Processing...' : (step === 1 ? 'Send Code' : 'Reset Password')}
                    </button>
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
