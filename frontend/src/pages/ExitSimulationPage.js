import React, { useState, useEffect } from 'react';
import API from '../api/axios';
import Navbar from '../components/Navbar';
import RiskBadge from '../components/RiskBadge';

const ExitSimulationPage = () => {
    const [employees, setEmployees] = useState([]);
    const [projects, setProjects] = useState([]);
    const [selectedEmployee, setSelectedEmployee] = useState('');
    const [selectedProject, setSelectedProject] = useState('');
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        API.get('/employees').then(r => setEmployees(r.data.data));
        API.get('/projects').then(r => setProjects(r.data.data));
    }, []);

    const handleSimulate = async () => {
        if (!selectedEmployee || !selectedProject) return;
        setLoading(true);
        try {
            const res = await API.get(
                `/simulation/exit?employeeId=${selectedEmployee}&projectId=${selectedProject}`
            );
            setResult(res.data.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <Navbar />
            <div style={styles.page}>
                <h2 style={styles.heading}>🚪 Employee Exit Simulation</h2>
                <p style={styles.subtitle}>
                    Simulate what happens if an employee leaves the project
                </p>

                <div style={styles.card}>
                    <div style={styles.row}>
                        <div style={styles.field}>
                            <label style={styles.label}>Select Employee</label>
                            <select
                                style={styles.select}
                                value={selectedEmployee}
                                onChange={e =>
                                    setSelectedEmployee(e.target.value)}>
                                <option value="">-- Select Employee --</option>
                                {employees.map(e => (
                                    <option key={e.id} value={e.id}>
                                        {e.fullName} ({e.department})
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div style={styles.field}>
                            <label style={styles.label}>Select Project</label>
                            <select
                                style={styles.select}
                                value={selectedProject}
                                onChange={e =>
                                    setSelectedProject(e.target.value)}>
                                <option value="">-- Select Project --</option>
                                {projects.map(p => (
                                    <option key={p.id} value={p.id}>
                                        {p.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <button
                            style={styles.button}
                            onClick={handleSimulate}
                            disabled={loading}>
                            {loading ? 'Simulating...' : '▶ Run Simulation'}
                        </button>
                    </div>
                </div>

                {result && (
                    <div style={styles.results}>
                        {/* Impact Banner */}
                        <div style={styles.impactBanner}>
                            <strong>{result.overallImpact}</strong>
                        </div>

                        {/* Stats Row */}
                        <div style={styles.statsRow}>
                            {[
                                {
                                    label: 'Modules Owned',
                                    value: result.totalModulesOwned,
                                    color: '#3498db'
                                },
                                {
                                    label: 'Affected Modules',
                                    value: result.affectedModules.length,
                                    color: '#e67e22'
                                },
                                {
                                    label: 'Orphaned Modules',
                                    value: result.orphanedModules.length,
                                    color: '#e74c3c'
                                },
                                {
                                    label: 'Missing Skills',
                                    value: result.skillsThatBecomeMissing.length,
                                    color: '#9b59b6'
                                },
                            ].map(stat => (
                                <div key={stat.label} style={{
                                    ...styles.statCard,
                                    borderTop: `4px solid ${stat.color}`
                                }}>
                                    <div style={{
                                        ...styles.statValue,
                                        color: stat.color
                                    }}>
                                        {stat.value}
                                    </div>
                                    <div style={styles.statLabel}>
                                        {stat.label}
                                    </div>
                                </div>
                            ))}
                        </div>

                        {/* Risk Change */}
                        <div style={styles.riskChange}>
                            <span>Risk Before: </span>
                            <RiskBadge level={result.riskBefore} />
                            <span style={styles.arrow}> → </span>
                            <span>Risk After: </span>
                            <RiskBadge level={result.riskAfter} />
                        </div>

                        {/* Details */}
                        <div style={styles.detailsGrid}>
                            <div style={styles.detailCard}>
                                <h4 style={styles.detailTitle}>
                                    📦 Affected Modules
                                </h4>
                                {result.affectedModules.length === 0
                                    ? <p style={styles.none}>None</p>
                                    : result.affectedModules.map((m, i) => (
                                        <div key={i} style={styles.item}>
                                            • {m}
                                        </div>
                                    ))
                                }
                            </div>
                            <div style={styles.detailCard}>
                                <h4 style={styles.detailTitle}>
                                    🔴 Orphaned Modules
                                </h4>
                                {result.orphanedModules.length === 0
                                    ? <p style={styles.none}>None</p>
                                    : result.orphanedModules.map((m, i) => (
                                        <div key={i} style={{
                                            ...styles.item,
                                            color: '#e74c3c'
                                        }}>
                                            ⚠ {m}
                                        </div>
                                    ))
                                }
                            </div>
                            <div style={styles.detailCard}>
                                <h4 style={styles.detailTitle}>
                                    💡 Recommendations
                                </h4>
                                {result.recommendations.map((r, i) => (
                                    <div key={i} style={styles.recommendation}>
                                        {r}
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

const styles = {
    page: { padding: '24px', background: '#f5f6fa', minHeight: '100vh' },
    heading: { color: '#1a1a2e', marginBottom: '8px' },
    subtitle: { color: '#666', marginBottom: '24px' },
    card: {
        background: 'white',
        padding: '24px',
        borderRadius: '10px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
        marginBottom: '24px',
    },
    row: { display: 'flex', gap: '16px', alignItems: 'flex-end' },
    field: { flex: 1 },
    label: {
        display: 'block',
        marginBottom: '6px',
        fontWeight: '600',
        fontSize: '14px',
    },
    select: {
        width: '100%',
        padding: '10px',
        borderRadius: '6px',
        border: '1px solid #ddd',
        fontSize: '14px',
    },
    button: {
        padding: '10px 24px',
        background: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontWeight: 'bold',
        fontSize: '14px',
        whiteSpace: 'nowrap',
    },
    results: {},
    impactBanner: {
        background: '#fff3cd',
        border: '1px solid #ffc107',
        padding: '16px',
        borderRadius: '8px',
        marginBottom: '16px',
        color: '#856404',
    },
    statsRow: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '16px',
        marginBottom: '16px',
    },
    statCard: {
        background: 'white',
        padding: '16px',
        borderRadius: '8px',
        textAlign: 'center',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    },
    statValue: { fontSize: '32px', fontWeight: 'bold' },
    statLabel: { color: '#666', fontSize: '13px', marginTop: '4px' },
    riskChange: {
        background: 'white',
        padding: '16px 20px',
        borderRadius: '8px',
        marginBottom: '16px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
        display: 'flex',
        alignItems: 'center',
        gap: '8px',
        fontSize: '15px',
        fontWeight: '600',
    },
    arrow: { fontSize: '20px', color: '#e74c3c' },
    detailsGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(3, 1fr)',
        gap: '16px',
    },
    detailCard: {
        background: 'white',
        padding: '16px',
        borderRadius: '8px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    },
    detailTitle: { margin: '0 0 12px 0', color: '#1a1a2e' },
    item: { padding: '4px 0', fontSize: '14px', color: '#333' },
    none: { color: '#999', fontSize: '14px' },
    recommendation: {
        padding: '6px 0',
        fontSize: '13px',
        color: '#555',
        borderBottom: '1px solid #f0f0f0',
    },
};

export default ExitSimulationPage;