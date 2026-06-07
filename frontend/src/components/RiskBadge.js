import React from 'react';

const RiskBadge = ({ level }) => {
    const colors = {
        CRITICAL: { background: '#ff4444', color: 'white' },
        HIGH:     { background: '#ff8800', color: 'white' },
        MEDIUM:   { background: '#ffcc00', color: 'black' },
        LOW:      { background: '#00cc44', color: 'white' },
    };

    const style = {
        ...colors[level],
        padding: '4px 10px',
        borderRadius: '12px',
        fontWeight: 'bold',
        fontSize: '12px',
        display: 'inline-block',
    };

    return <span style={style}>{level}</span>;
};

export default RiskBadge;