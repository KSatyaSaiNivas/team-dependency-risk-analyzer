import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav style={styles.nav}>
            <div style={styles.brand}>
                🛡️ Risk Analyzer
            </div>
            <div style={styles.links}>
                <Link to="/dashboard" style={styles.link}>Dashboard</Link>
                <Link to="/employees" style={styles.link}>Employees</Link>
                <Link to="/projects" style={styles.link}>Projects</Link>
                <Link to="/risk" style={styles.link}>Risk Analysis</Link>
                <Link to="/simulation" style={styles.link}>Exit Simulation</Link>
            </div>
            <div style={styles.user}>
                <span style={styles.email}>{user?.email}</span>
                <button onClick={handleLogout} style={styles.logoutBtn}>
                    Logout
                </button>
            </div>
        </nav>
    );
};

const styles = {
    nav: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        background: '#1a1a2e',
        color: 'white',
        padding: '0 24px',
        height: '60px',
        position: 'sticky',
        top: 0,
        zIndex: 1000,
    },
    brand: { fontSize: '20px', fontWeight: 'bold', color: '#00d4ff' },
    links: { display: 'flex', gap: '24px' },
    link: { color: '#ccc', textDecoration: 'none', fontSize: '14px' },
    user: { display: 'flex', alignItems: 'center', gap: '12px' },
    email: { fontSize: '13px', color: '#aaa' },
    logoutBtn: {
        background: '#e74c3c',
        color: 'white',
        border: 'none',
        padding: '6px 14px',
        borderRadius: '6px',
        cursor: 'pointer',
    },
};

export default Navbar;