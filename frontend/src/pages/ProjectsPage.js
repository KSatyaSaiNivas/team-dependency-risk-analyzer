import React, { useEffect, useState } from 'react';
import API from '../api/axios';
import Navbar from '../components/Navbar';
import RiskBadge from '../components/RiskBadge';

const ProjectsPage = () => {
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [message, setMessage] = useState('');
    const [form, setForm] = useState({
        name: '', description: '',
        startDate: '', status: 'ACTIVE',
        criticality: 'MEDIUM'
    });

    useEffect(() => { fetchProjects(); }, []);

    const fetchProjects = () => {
        API.get('/projects')
            .then(r => setProjects(r.data.data))
            .finally(() => setLoading(false));
    };

    const handleSubmit = async () => {
        try {
            await API.post('/projects', form);
            setMessage('Project created successfully!');
            setShowForm(false);
            fetchProjects();
        } catch (err) {
            setMessage('Error creating project');
        }
    };

    return (
        <div>
            <Navbar />
            <div style={styles.page}>
                <div style={styles.header}>
                    <h2 style={styles.heading}>📁 Projects</h2>
                    <button
                        style={styles.addBtn}
                        onClick={() => setShowForm(!showForm)}>
                        {showForm ? 'Cancel' : '+ Add Project'}
                    </button>
                </div>

                {message && (
                    <div style={styles.message}>{message}</div>
                )}

                {showForm && (
                    <div style={styles.formCard}>
                        <h3 style={styles.formTitle}>Add New Project</h3>
                        <div style={styles.formGrid}>
                            <div style={styles.field}>
                                <label style={styles.label}>Project Name</label>
                                <input
                                    style={styles.input}
                                    value={form.name}
                                    onChange={e => setForm({
                                        ...form, name: e.target.value
                                    })}
                                />
                            </div>
                            <div style={styles.field}>
                                <label style={styles.label}>Start Date</label>
                                <input
                                    type="date"
                                    style={styles.input}
                                    value={form.startDate}
                                    onChange={e => setForm({
                                        ...form, startDate: e.target.value
                                    })}
                                />
                            </div>
                            <div style={styles.field}>
                                <label style={styles.label}>Status</label>
                                <select
                                    style={styles.input}
                                    value={form.status}
                                    onChange={e => setForm({
                                        ...form, status: e.target.value
                                    })}>
                                    <option>ACTIVE</option>
                                    <option>ON_HOLD</option>
                                    <option>COMPLETED</option>
                                </select>
                            </div>
                            <div style={styles.field}>
                                <label style={styles.label}>Criticality</label>
                                <select
                                    style={styles.input}
                                    value={form.criticality}
                                    onChange={e => setForm({
                                        ...form, criticality: e.target.value
                                    })}>
                                    <option>LOW</option>
                                    <option>MEDIUM</option>
                                    <option>HIGH</option>
                                    <option>CRITICAL</option>
                                </select>
                            </div>
                            <div style={{
                                ...styles.field,
                                gridColumn: 'span 2'
                            }}>
                                <label style={styles.label}>Description</label>
                                <input
                                    style={styles.input}
                                    value={form.description}
                                    onChange={e => setForm({
                                        ...form, description: e.target.value
                                    })}
                                />
                            </div>
                        </div>
                        <button
                            style={styles.submitBtn}
                            onClick={handleSubmit}>
                            Save Project
                        </button>
                    </div>
                )}

                {loading ? (
                    <div style={styles.loading}>Loading...</div>
                ) : (
                    <div style={styles.grid}>
                        {projects.map(p => (
                            <div key={p.id} style={styles.projectCard}>
                                <div style={styles.cardHeader}>
                                    <h3 style={styles.projectName}>
                                        {p.name}
                                    </h3>
                                    <RiskBadge level={p.criticality} />
                                </div>
                                <p style={styles.description}>
                                    {p.description || 'No description'}
                                </p>
                                <div style={styles.cardFooter}>
                                    <span style={styles.status}>
                                        {p.status}
                                    </span>
                                    <span style={styles.date}>
                                        Started: {p.startDate || 'N/A'}
                                    </span>
                                </div>
                            </div>
                        ))}
                        {projects.length === 0 && (
                            <p style={styles.empty}>
                                No projects found. Add one above.
                            </p>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

const styles = {
    page: { padding: '24px', background: '#f5f6fa', minHeight: '100vh' },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '24px',
    },
    heading: { color: '#1a1a2e', margin: 0 },
    addBtn: {
        background: '#1a1a2e',
        color: 'white',
        border: 'none',
        padding: '10px 20px',
        borderRadius: '6px',
        cursor: 'pointer',
        fontWeight: 'bold',
    },
    message: {
        background: '#d4edda',
        color: '#155724',
        padding: '12px',
        borderRadius: '6px',
        marginBottom: '16px',
    },
    formCard: {
        background: 'white',
        padding: '24px',
        borderRadius: '10px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
        marginBottom: '24px',
    },
    formTitle: { margin: '0 0 16px 0', color: '#1a1a2e' },
    formGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(3, 1fr)',
        gap: '16px',
        marginBottom: '16px',
    },
    field: {},
    label: {
        display: 'block',
        marginBottom: '6px',
        fontWeight: '600',
        fontSize: '13px',
    },
    input: {
        width: '100%',
        padding: '8px 10px',
        border: '1px solid #ddd',
        borderRadius: '6px',
        fontSize: '14px',
        boxSizing: 'border-box',
    },
    submitBtn: {
        background: '#27ae60',
        color: 'white',
        border: 'none',
        padding: '10px 24px',
        borderRadius: '6px',
        cursor: 'pointer',
        fontWeight: 'bold',
    },
    loading: { textAlign: 'center', padding: '40px' },
    grid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(3, 1fr)',
        gap: '16px',
    },
    projectCard: {
        background: 'white',
        padding: '20px',
        borderRadius: '10px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    },
    cardHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '8px',
    },
    projectName: { margin: 0, color: '#1a1a2e', fontSize: '16px' },
    description: { color: '#666', fontSize: '13px', margin: '8px 0' },
    cardFooter: {
        display: 'flex',
        justifyContent: 'space-between',
        marginTop: '12px',
        fontSize: '12px',
        color: '#999',
    },
    status: { fontWeight: 'bold', color: '#27ae60' },
    date: {},
    empty: { color: '#999', gridColumn: 'span 3', textAlign: 'center' },
};

export default ProjectsPage;