import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { register, login } from './api';

export default function Register({ onRegister }) {
  const [username, setUsername] = useState('');
  const [email,    setEmail]    = useState('');
  const [password, setPassword] = useState('');
  const [err,      setErr]      = useState('');

  useEffect(() => {
    document.title = 'Rejestracja | HotelPriceApp';
  }, []);

  const handleSubmit = async e => {
    e.preventDefault();
    setErr('');
    try {
      await register({ username, email, password });
      const { data: token } = await login({ username, password });
      localStorage.setItem('jwt', token);
      onRegister();
    } catch {
      setErr('Rejestracja nie powiodła się');
    }
  };

  return (
    <div className="mx-auto" style={{ maxWidth: 400 }}>
      <h2>Rejestracja</h2>
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
          <label className="form-label">Email</label>
          <input
            type="email"
            className="form-control"
            value={email}
            onChange={e => setEmail(e.target.value)}
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
        <button className="btn btn-success w-100">Zarejestruj</button>
      </form>
      <div className="text-center mt-3">
        <Link to="/login">Masz już konto? Zaloguj się</Link>
      </div>
    </div>
  );
}
