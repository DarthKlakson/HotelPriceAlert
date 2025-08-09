// src/Hotels.js
import React, { useEffect, useState } from 'react';
import { getHotels, addHotel, deleteHotel } from './api';

export default function Hotels({ onLogout }) {
  const [hotels, setHotels] = useState([]);
  const [url, setUrl] = useState('');
  const [err, setErr] = useState('');
  const [deletingId, setDeletingId] = useState(null);

  const fetch = async () => {
    try {
      const res = await getHotels();
      setHotels(res.data);
    } catch (e) {
      console.error('Błąd pobierania hoteli:', e);
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
    } catch (e2) {
      console.error('Błąd dodawania hotelu:', e2);
      setErr('Nie udało się dodać hotelu');
    }
  };

  const handleDelete = async id => {
    if (!window.confirm('Czy na pewno chcesz usunąć ten hotel?')) return;
    setErr('');
    setDeletingId(id);
    try {
      await deleteHotel(id);
      await fetch();
    } catch (e3) {
      console.error('Błąd usuwania hotelu:', e3);
      setErr('Nie udało się usunąć hotelu');
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="container-fluid px-0">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">Twoje hotele</h2>
        <button type="button" className="btn btn-outline-secondary" onClick={onLogout}>
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
                <p className="card-text fs-5 fw-bold mb-4">
                  {h.lastKnownPrice ? `${h.lastKnownPrice} zł` : 'Brak ceny'}
                </p>

                <div className="mt-auto d-grid gap-2">
                  <a
                    href={h.url}
                    className="btn btn-outline-primary"
                    target="_blank"
                    rel="noreferrer"
                  >
                    Zobacz na Itace
                  </a>

                  <button
                    type="button"
                    className="btn btn-outline-danger"
                    onClick={() => handleDelete(h.id)}
                    disabled={deletingId === h.id}
                  >
                    {deletingId === h.id ? 'Usuwanie…' : 'Usuń'}
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <hr />

      <h3 className="mb-3">Dodaj hotel</h3>
      <form onSubmit={handleAdd} className="input-group mb-5">
        <input
          type="url"
          className="form-control"
          placeholder="https://www.itaka.pl/..."
          value={url}
          onChange={e => setUrl(e.target.value)}
          required
        />
        <button type="submit" className="btn btn-primary">Dodaj</button>
      </form>
    </div>
  );
}
