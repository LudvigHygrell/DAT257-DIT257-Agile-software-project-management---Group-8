import { useSearchParams } from 'react-router-dom';

const EmailConfirmation = () => {
    const [searchParams] = useSearchParams();
    const status = searchParams.get("status")

    alert(status === 'success' ? "Email confirmed. You may now close this page." : "Error confirming email.");
};

export default EmailConfirmation;
