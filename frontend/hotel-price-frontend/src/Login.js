import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { login } from './api';

export default function Login({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [err, setErr] = useState('');

  useEffect(() => {
    document.title = 'Logowanie | HotelPriceApp';
  }, []);

  const handleSubmit = async e => {
    e.preventDefault();
    setErr('');
    try {
      const { data: token } = await login({ username, password });
      localStorage.setItem('jwt', token);
      onLogin();
    } catch {
      setErr('Nieprawidłowa nazwa użytkownika lub hasło');
    }
  };

  return (
    <div className="mx-auto" style={{ maxWidth: 400 }}>
      <h2>Logowanie</h2>
      {err && <div className="alert alert-danger">{err}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Nazwa użytkownika</label>
          <input
            type="text"
            className="form-control"
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Hasło</label>
          <input
            type="password"
            className="form-control"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
        </div>
        <button className="btn btn-primary w-100">Zaloguj</button>
      </form>
      <div className="text-center mt-3">
        <Link to="/register">Nie masz konta? Zarejestruj się</Link>
      </div>
    </div>
  );
}
