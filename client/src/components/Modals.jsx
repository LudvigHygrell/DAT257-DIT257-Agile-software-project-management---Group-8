import LoginModal from './LoginModal.jsx';
import Registration from './Registration.jsx';
import ForgotPassword from './ForgotPassword.jsx';

// Component that renders all modals for the application
function Modals({
  showLogin,
  showRegistration,
  showForgotPassword,
  closeLogin,
  handleSwitchToRegister,
  handleSwitchToForgotPassword,
  closeRegistration,
  handleSwitchToLogin,
  closeForgotPassword
}) {
  return (
    <>
      {/* Login modal, only visible when showLogin is true */}
      <LoginModal
        isVisible={showLogin}
        onClose={closeLogin}
        onSwitchToRegister={handleSwitchToRegister}
        onSwitchToForgotPassword={handleSwitchToForgotPassword}
      />
      {/* Registration modal, only visible when showRegistration is true */}
      <Registration
        isVisible={showRegistration}
        onClose={closeRegistration}
        onSwitchToLogin={handleSwitchToLogin}
      />
      {/* Forgot password modal, only visible when showForgotPassword is true */}
      <ForgotPassword
        isVisible={showForgotPassword}
        onClose={closeForgotPassword}
        onSwitchToLogin={handleSwitchToLogin}
      />
    </>
  );
}

export default Modals;
