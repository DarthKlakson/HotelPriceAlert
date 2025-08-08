// src/Hotels.js
import React, { useEffect, useState } from 'react';
import { getHotels, addHotel } from './api';

export default function Hotels({ onLogout }) {
  const [hotels, setHotels] = useState([]);
  const [url,    setUrl]    = useState('');
  const [err,    setErr]    = useState('');

  const fetch = async () => {
    try {
      const res = await getHotels();
      setHotels(res.data);
    } catch (e) {
      console.error("Błąd pobierania hoteli:", e);
      setErr('Nie udało się pobrać hoteli');
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  const handleAdd = async e => {
    e.preventDefault();
    setErr('');
    try {
      await addHotel(url);
      setUrl('');
      fetch();
    } catch {
      setErr('Nie udało się dodać hotelu');
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Twoje hotele</h2>
        <button className="btn btn-outline-secondary" onClick={onLogout}>
          Wyloguj
        </button>
      </div>

      {err && <div className="alert alert-danger">{err}</div>}

      {hotels.length === 0 && !err && (
        <div className="alert alert-info">Nie masz jeszcze żadnych hoteli.</div>
      )}

      <div className="row">
        {hotels.map(h => (
          <div key={h.id} className="col-sm-6 col-lg-4 mb-4">
            <div className="card h-100 shadow-sm">
              <div className="card-body d-flex flex-column">
                <h5 className="card-title">{h.name}</h5>
                <p className="card-text fs-5 fw-bold">
                  {h.lastKnownPrice ? `${h.lastKnownPrice} zł` : 'Brak ceny'}
                </p>
                <a
                  href={h.url}
                  className="mt-auto btn btn-outline-primary"
                  target="_blank"
                  rel="noreferrer"
                >
                  Zobacz na Itace
                </a>
              </div>
            </div>
          </div>
        ))}
      </div>

      <hr />

      <h3 className="mb-3">Dodaj hotel</h3>
      {err && <div className="alert alert-danger">{err}</div>}
      <form onSubmit={handleAdd} className="input-group mb-5">
        <input
          type="url"
          className="form-control"
          placeholder="https://www.itaka.pl/..."
          value={url}
          onChange={e => setUrl(e.target.value)}
          required
        />
        <button className="btn btn-primary">Dodaj</button>
      </form>
    </div>
  );
}
