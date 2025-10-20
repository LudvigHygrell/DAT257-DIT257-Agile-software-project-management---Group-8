import LoginModal from './LoginModal.jsx';
import Registration from './Registration.jsx';
import ResetPassword from './ResetPassword.jsx'

// Component that renders all modals for the application
function Modals({
  showLogin,
  showRegistration,
  showResetPassword,
  closeLogin,
  handleSwitchToRegister,
  closeRegistration,
  handleSwitchToLogin,
  closeResetPassword,
  handleSwitchToResetPassword,
  onLoginSuccess
}) {
  return (
    <>
      {/* Login modal, only visible when showLogin is true */}
      <LoginModal
        isVisible={showLogin}
        onClose={closeLogin}
        onSwitchToRegister={handleSwitchToRegister}
        onSwitchToResetPassword={handleSwitchToResetPassword}
        onLoginSuccess={onLoginSuccess}
      />
      {/* Registration modal, only visible when showRegistration is true */}
      <Registration
        isVisible={showRegistration}
        onClose={closeRegistration}
        onSwitchToLogin={handleSwitchToLogin}
      />
      {/* Password reset modal, only visible when showPasswordReset is true */}
      <ResetPassword
        isVisible={showResetPassword}
        onClose={closeResetPassword}
        onSwitchToLogin={handleSwitchToLogin}
      />
    </>
  );
}

export default Modals;
