import React from 'react';
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate
} from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import ExitSimulationPage from './pages/ExitSimulationPage';
import EmployeesPage from './pages/EmployeesPage';
import ProjectsPage from './pages/ProjectsPage';

function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/dashboard" element={
                        <PrivateRoute><DashboardPage /></PrivateRoute>
                    } />
                    <Route path="/employees" element={
                        <PrivateRoute><EmployeesPage /></PrivateRoute>
                    } />
                    <Route path="/projects" element={
                        <PrivateRoute><ProjectsPage /></PrivateRoute>
                    } />
                    <Route path="/simulation" element={
                        <PrivateRoute><ExitSimulationPage /></PrivateRoute>
                    } />
                    <Route path="*" element={<Navigate to="/dashboard" />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;