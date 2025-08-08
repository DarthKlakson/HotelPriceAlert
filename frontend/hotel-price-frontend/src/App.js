// src/App.js
import React, { useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Login    from './Login';
import Register from './Register';
import Hotels   from './Hotels';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

export default function App() {
  const [logged, setLogged] = useState(!!localStorage.getItem('jwt'));

  const handleLogout = () => {
    localStorage.removeItem('jwt');
    setLogged(false);
  };

  return (
    <div className="container py-5">
      <div className="text-center mb-4">
        <img
          src="/logo.png"
          alt="Hotel Price Alert"
          style={{ maxWidth: 180 }}
        />
      </div>

      <Routes>
        {!logged && (
          <>
            <Route
              path="/login"
              element={<Login onLogin={() => setLogged(true)} />}
            />
            <Route
              path="/register"
              element={<Register onRegister={() => setLogged(true)} />}
            />
            <Route
              path="*"
              element={<Navigate to="/login" replace />}
            />
          </>
        )}

        {logged && (
          <>
            <Route
              path="/hotels"
              element={<Hotels onLogout={handleLogout} />}
            />
            <Route
              path="*"
              element={<Navigate to="/hotels" replace />}
            />
          </>
        )}
      </Routes>
    </div>
  );
}
