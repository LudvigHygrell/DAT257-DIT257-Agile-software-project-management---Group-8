import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserAPI } from '../services/APIService.js';
import '../styles/AccountSettings.css';

function AccountSettings({ username, onLogout }) {
  const navigate = useNavigate();

  // State for email change
  const [emailForm, setEmailForm] = useState({
    newEmail: '',
    password: ''
  });
  const [emailError, setEmailError] = useState('');
  const [emailSuccess, setEmailSuccess] = useState('');
  const [emailLoading, setEmailLoading] = useState(false);

  // State for password change
  const [passwordForm, setPasswordForm] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [passwordError, setPasswordError] = useState('');
  const [passwordSuccess, setPasswordSuccess] = useState('');
  const [passwordLoading, setPasswordLoading] = useState(false);

  // State for delete account
  const [deletePassword, setDeletePassword] = useState('');
  const [deleteError, setDeleteError] = useState('');
  const [deleteLoading, setDeleteLoading] = useState(false);

  // Handle email change
  const handleEmailChange = async (e) => {
    e.preventDefault();
    setEmailError('');
    setEmailSuccess('');
    setEmailLoading(true);

    try {
      await UserAPI.changeEmail({
        username,
        email: emailForm.newEmail,
        password: emailForm.password
      });

      setEmailSuccess('Email changed successfully!');
      setEmailForm({ newEmail: '', password: '' });
    } catch (err) {
      if (err.response) {
        setEmailError(err.response.data || 'Failed to change email');
      } else {
        setEmailError('Network error. Please try again.');
      }
      console.error('Email change error:', err);
    } finally {
      setEmailLoading(false);
    }
  };

  // Handle password change
  const handlePasswordChange = async (e) => {
    e.preventDefault();
    setPasswordError('');
    setPasswordSuccess('');

    // Validation
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      setPasswordError('New passwords do not match');
      return;
    }

    if (passwordForm.newPassword.length < 6) {
      setPasswordError('New password must be at least 6 characters');
      return;
    }

    if (passwordForm.oldPassword === passwordForm.newPassword) {
      setPasswordError('New password must be different from old password');
      return;
    }

    setPasswordLoading(true);

    try {
      await UserAPI.changePassword({
        username,
        old: passwordForm.oldPassword,
        new: passwordForm.newPassword
      });

      setPasswordSuccess('Password changed successfully!');
      setPasswordForm({ oldPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      if (err.response) {
        setPasswordError(err.response.data || 'Failed to change password');
      } else {
        setPasswordError('Network error. Please try again.');
      }
      console.error('Password change error:', err);
    } finally {
      setPasswordLoading(false);
    }
  };

  // Handle account deletion
  const handleDeleteAccount = async (e) => {
    e.preventDefault();
    setDeleteError('');
    setDeleteLoading(true);

    try {
      // Delete account (backend will handle auth check via JWT)

      await UserAPI.remove({ username: username });

      // Logout and redirect
      alert('Your account has been deleted successfully.');
      onLogout();
      navigate('/');
    } catch (err) {
      if (err.response) {
        const errorMsg = err.response.data || 'Failed to delete account';
        // Check if it's a database constraint error
        if (typeof errorMsg === 'string' && errorMsg.includes('database')) {
          setDeleteError('Database error: Please contact support to delete your account.');
        } else {
          setDeleteError(errorMsg);
        }
      } else {
        setDeleteError('Network error. Please try again.');
      }
      console.error('Delete account error:', err);
    } finally {
      setDeleteLoading(false);
    }
  };

  return (
    <div className="account-settings-container">
      <div className="account-settings-content">
        <h1 className="settings-title">Account Settings</h1>

        {/* Profile Info Section */}
        <section className="settings-section">
          <h2 className="section-title">Profile</h2>
          <div className="settings-card">
            <div className="profile-info">
              <div className="profile-avatar">
                {username ? username.charAt(0).toUpperCase() : 'U'}
              </div>
              <span className="username-text">{username}</span>
            </div>
          </div>
        </section>

        {/* Change Email Section */}
        <section className="settings-section">
          <h2 className="section-title">Change Email</h2>
          <div className="settings-card">
            {emailSuccess && <div className="success-message">{emailSuccess}</div>}
            {emailError && <div className="error-message">{emailError}</div>}

            <form onSubmit={handleEmailChange} className="settings-form">
              <input
                type="email"
                value={emailForm.newEmail}
                onChange={(e) => setEmailForm({ ...emailForm, newEmail: e.target.value })}
                placeholder="New email address"
                required
              />
              <input
                type="password"
                value={emailForm.password}
                onChange={(e) => setEmailForm({ ...emailForm, password: e.target.value })}
                placeholder="Current password"
                required
              />
              <button type="submit" className="btn btn-primary" disabled={emailLoading}>
                {emailLoading ? 'Changing...' : 'Change Email'}
              </button>
            </form>
          </div>
        </section>

        {/* Change Password Section */}
        <section className="settings-section">
          <h2 className="section-title">Change Password</h2>
          <div className="settings-card">
            {passwordSuccess && <div className="success-message">{passwordSuccess}</div>}
            {passwordError && <div className="error-message">{passwordError}</div>}

            <form onSubmit={handlePasswordChange} className="settings-form">
              <input
                type="password"
                value={passwordForm.oldPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, oldPassword: e.target.value })}
                placeholder="Current password"
                required
              />
              <input
                type="password"
                value={passwordForm.newPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })}
                placeholder="New password"
                required
                minLength="6"
              />
              <input
                type="password"
                value={passwordForm.confirmPassword}
                onChange={(e) => setPasswordForm({ ...passwordForm, confirmPassword: e.target.value })}
                placeholder="Confirm new password"
                required
                minLength="6"
              />
              <button type="submit" className="btn btn-primary" disabled={passwordLoading}>
                {passwordLoading ? 'Changing...' : 'Change Password'}
              </button>
            </form>
          </div>
        </section>

        {/* Delete Account Section */}
        <section className="settings-section">
          <h2 className="section-title">Delete Account</h2>
          <div className="settings-card">
            {deleteError && <div className="error-message">{deleteError}</div>}

            <form onSubmit={handleDeleteAccount} className="settings-form">
              <input
                type="password"
                value={deletePassword}
                onChange={(e) => setDeletePassword(e.target.value)}
                placeholder="Current password"
                required
              />
              <button type="submit" className="btn btn-danger" disabled={deleteLoading}>
                {deleteLoading ? 'Deleting...' : 'Delete Account'}
              </button>
            </form>
          </div>
        </section>
      </div>
    </div>
  );
}

export default AccountSettings;
