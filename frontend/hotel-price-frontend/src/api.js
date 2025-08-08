import axios from 'axios';

const API = axios.create({
  baseURL: '/api',
});

API.interceptors.request.use(config => {
  const token = localStorage.getItem('jwt');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const register = data => API.post('/auth/register', data);
export const login    = data => API.post('/auth/login', data);
export const getHotels = ()   => API.get('/hotels');
export const addHotel  = url  => API.post('/hotels', {
  url: url,
  name: null,
  lastKnownPrice: null
});
