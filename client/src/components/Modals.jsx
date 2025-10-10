import LoginModal from './LoginModal.jsx';
import Registration from './Registration.jsx';

// Component that renders all modals for the application
function Modals({
  showLogin,
  showRegistration,
  closeLogin,
  handleSwitchToRegister,
  closeRegistration,
  handleSwitchToLogin,
  onLoginSuccess
}) {
  return (
    <>
      {/* Login modal, only visible when showLogin is true */}
      <LoginModal
        isVisible={showLogin}
        onClose={closeLogin}
        onSwitchToRegister={handleSwitchToRegister}
        onLoginSuccess={onLoginSuccess}
      />
      {/* Registration modal, only visible when showRegistration is true */}
      <Registration
        isVisible={showRegistration}
        onClose={closeRegistration}
        onSwitchToLogin={handleSwitchToLogin}
      />
    </>
  );
}

export default Modals;
