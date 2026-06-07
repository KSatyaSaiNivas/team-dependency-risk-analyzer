import React, { useEffect, useState } from 'react';
import { Doughnut, Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    ArcElement,
    Tooltip,
    Legend,
    CategoryScale,
    LinearScale,
    BarElement,
} from 'chart.js';
import API from '../api/axios';
import Navbar from '../components/Navbar';
import RiskBadge from '../components/RiskBadge';

ChartJS.register(
    ArcElement, Tooltip, Legend,
    CategoryScale, LinearScale, BarElement
);

const DashboardPage = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        API.get('/dashboard/summary')
            .then(res => setData(res.data.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return (
        <div>
            <Navbar />
            <div style={styles.loading}>Loading dashboard...</div>
        </div>
    );

    if (!data) return <div>Error loading dashboard</div>;

    const doughnutData = {
        labels: ['Critical', 'High', 'Medium', 'Low'],
        datasets: [{
            data: [
                data.criticalRiskModules,
                data.highRiskModules,
                data.mediumRiskModules,
                data.lowRiskModules,
            ],
            backgroundColor: ['#ff4444', '#ff8800', '#ffcc00', '#00cc44'],
        }],
    };

    const barData = {
        labels: data.top5HighRiskModules.map(m => m.moduleName),
        datasets: [{
            label: 'Risk Score',
            data: data.top5HighRiskModules.map(m => m.riskScore),
            backgroundColor: data.top5HighRiskModules.map(m =>
                m.riskLevel === 'CRITICAL' ? '#ff4444' :
                m.riskLevel === 'HIGH'     ? '#ff8800' :
                m.riskLevel === 'MEDIUM'   ? '#ffcc00' : '#00cc44'
            ),
        }],
    };

    return (
        <div>
            <Navbar />
            <div style={styles.page}>
                <h2 style={styles.heading}>📊 Dashboard Overview</h2>

                {/* Stats Cards */}
                <div style={styles.statsGrid}>
                    {[
                        { label: 'Projects', value: data.totalProjects, color: '#3498db' },
                        { label: 'Modules', value: data.totalModules, color: '#9b59b6' },
                        { label: 'Employees', value: data.totalEmployees, color: '#27ae60' },
                        { label: 'Skills', value: data.totalSkills, color: '#e67e22' },
                    ].map(stat => (
                        <div key={stat.label} style={{
                            ...styles.statCard,
                            borderTop: `4px solid ${stat.color}`
                        }}>
                            <div style={styles.statValue}>{stat.value}</div>
                            <div style={styles.statLabel}>{stat.label}</div>
                        </div>
                    ))}
                </div>

                {/* Overall Risk */}
                <div style={styles.riskBanner}>
                    <span style={styles.riskLabel}>Overall System Risk: </span>
                    <RiskBadge level={data.overallSystemRisk} />
                    <span style={styles.riskInfo}>
                        &nbsp;| Orphaned Modules: {data.orphanedModules}
                        &nbsp;| Single Owner Modules: {data.singleOwnerModules}
                    </span>
                </div>

                {/* Charts */}
                <div style={styles.chartsRow}>
                    <div style={styles.chartCard}>
                        <h3 style={styles.chartTitle}>Risk Distribution</h3>
                        <Doughnut data={doughnutData} />
                    </div>
                    <div style={styles.chartCard}>
                        <h3 style={styles.chartTitle}>Top Risk Modules</h3>
                        <Bar data={barData} />
                    </div>
                </div>

                {/* High Risk Modules Table */}
                <div style={styles.tableCard}>
                    <h3 style={styles.chartTitle}>🚨 High Risk Modules</h3>
                    <table style={styles.table}>
                        <thead>
                            <tr style={styles.thead}>
                                <th style={styles.th}>Module</th>
                                <th style={styles.th}>Project</th>
                                <th style={styles.th}>Owners</th>
                                <th style={styles.th}>Score</th>
                                <th style={styles.th}>Risk Level</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data.top5HighRiskModules.map(m => (
                                <tr key={m.moduleId} style={styles.tr}>
                                    <td style={styles.td}>{m.moduleName}</td>
                                    <td style={styles.td}>{m.projectName}</td>
                                    <td style={styles.td}>
                                        {m.ownerNames.join(', ') || 'None'}
                                    </td>
                                    <td style={styles.td}>{m.riskScore}</td>
                                    <td style={styles.td}>
                                        <RiskBadge level={m.riskLevel} />
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

const styles = {
    page: { padding: '24px', background: '#f5f6fa', minHeight: '100vh' },
    heading: { color: '#1a1a2e', marginBottom: '24px' },
    loading: { padding: '40px', textAlign: 'center', fontSize: '18px' },
    statsGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '16px',
        marginBottom: '24px',
    },
    statCard: {
        background: 'white',
        padding: '20px',
        borderRadius: '10px',
        textAlign: 'center',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    },
    statValue: { fontSize: '36px', fontWeight: 'bold', color: '#1a1a2e' },
    statLabel: { color: '#666', marginTop: '4px', fontSize: '14px' },
    riskBanner: {
        background: 'white',
        padding: '16px 20px',
        borderRadius: '10px',
        marginBottom: '24px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
        display: 'flex',
        alignItems: 'center',
    },
    riskLabel: { fontWeight: 'bold', marginRight: '8px' },
    riskInfo: { color: '#666', fontSize: '14px' },
    chartsRow: {
        display: 'grid',
        gridTemplateColumns: '1fr 2fr',
        gap: '16px',
        marginBottom: '24px',
    },
    chartCard: {
        background: 'white',
        padding: '20px',
        borderRadius: '10px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    },
    chartTitle: { margin: '0 0 16px 0', color: '#1a1a2e' },
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
    td: { padding: '12px 16px', fontSize: '14px', color: '#333' },
};

export default DashboardPage;