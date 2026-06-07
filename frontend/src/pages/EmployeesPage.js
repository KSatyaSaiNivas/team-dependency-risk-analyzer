import React, { useEffect, useState } from 'react';
import API from '../api/axios';
import Navbar from '../components/Navbar';

const EmployeesPage = () => {
    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(true);
    const [form, setForm] = useState({
        fullName: '', email: '',
        department: '', designation: '',
        joiningDate: ''
    });
    const [showForm, setShowForm] = useState(false);
    const [message, setMessage] = useState('');

    useEffect(() => {
        fetchEmployees();
    }, []);

    const fetchEmployees = () => {
        API.get('/employees')
            .then(r => setEmployees(r.data.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    const handleSubmit = async () => {
        try {
            await API.post('/employees', form);
            setMessage('Employee created successfully!');
            setShowForm(false);
            setForm({
                fullName: '', email: '',
                department: '', designation: '',
                joiningDate: ''
            });
            fetchEmployees();
        } catch (err) {
            setMessage('Error: ' + err.response?.data?.message);
        }
    };

    const statusColor = (status) => {
        if (status === 'ACTIVE') return '#00cc44';
        if (status === 'RESIGNED') return '#ff4444';
        return '#ffcc00';
    };

    return (
        <div>
            <Navbar />
            <div style={styles.page}>
                <div style={styles.header}>
                    <h2 style={styles.heading}>👥 Employees</h2>
                    <button
                        style={styles.addBtn}
                        onClick={() => setShowForm(!showForm)}>
                        {showForm ? 'Cancel' : '+ Add Employee'}
                    </button>
                </div>

                {message && (
                    <div style={styles.message}>{message}</div>
                )}

                {showForm && (
                    <div style={styles.formCard}>
                        <h3 style={styles.formTitle}>Add New Employee</h3>
                        <div style={styles.formGrid}>
                            {[
                                { key: 'fullName', label: 'Full Name', type: 'text' },
                                { key: 'email', label: 'Email', type: 'email' },
                                { key: 'department', label: 'Department', type: 'text' },
                                { key: 'designation', label: 'Designation', type: 'text' },
                                { key: 'joiningDate', label: 'Joining Date', type: 'date' },
                            ].map(field => (
                                <div key={field.key} style={styles.field}>
                                    <label style={styles.label}>
                                        {field.label}
                                    </label>
                                    <input
                                        type={field.type}
                                        style={styles.input}
                                        value={form[field.key]}
                                        onChange={e => setForm({
                                            ...form,
                                            [field.key]: e.target.value
                                        })}
                                    />
                                </div>
                            ))}
                        </div>
                        <button
                            style={styles.submitBtn}
                            onClick={handleSubmit}>
                            Save Employee
                        </button>
                    </div>
                )}

                {loading ? (
                    <div style={styles.loading}>Loading...</div>
                ) : (
                    <div style={styles.tableCard}>
                        <table style={styles.table}>
                            <thead>
                                <tr style={styles.thead}>
                                    <th style={styles.th}>Name</th>
                                    <th style={styles.th}>Email</th>
                                    <th style={styles.th}>Department</th>
                                    <th style={styles.th}>Designation</th>
                                    <th style={styles.th}>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {employees.map(emp => (
                                    <tr key={emp.id} style={styles.tr}>
                                        <td style={styles.td}>
                                            {emp.fullName}
                                        </td>
                                        <td style={styles.td}>{emp.email}</td>
                                        <td style={styles.td}>
                                            {emp.department}
                                        </td>
                                        <td style={styles.td}>
                                            {emp.designation}
                                        </td>
                                        <td style={styles.td}>
                                            <span style={{
                                                color: statusColor(emp.status),
                                                fontWeight: 'bold'
                                            }}>
                                                {emp.status}
                                            </span>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        {employees.length === 0 && (
                            <p style={styles.empty}>
                                No employees found. Add one above.
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
    tableCard: {
        background: 'white',
        padding: '20px',
        borderRadius: '10px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    },
    table: { width: '100%', borderCollapse: 'collapse' },
    thead: { background: '#f8f9fa' },
    th: {
        padding: '12px 16px',
        textAlign: 'left',
        fontSize: '13px',
        fontWeight: '600',
        color: '#666',
        borderBottom: '2px solid #eee',
    },
    tr: { borderBottom: '1px solid #f0f0f0' },
    td: { padding: '12px 16px', fontSize: '14px' },
    empty: { textAlign: 'center', color: '#999', padding: '20px' },
};

export default EmployeesPage;