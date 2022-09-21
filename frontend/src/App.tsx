import { useEffect, useState } from 'react'
import { Route, Routes } from 'react-router-dom'
import Navigation from './components/NavigationComponent'
import ProtectedRoutes from './security/ProtectedRoutes'
import Login from './login/Index'
import { Home } from './notes/Index'
import NotFound from './notfound/Index'

export default function App() {
  function isSignedIn(): boolean {
    const accessToken = localStorage.getItem('access_token')
    const refreshToken = localStorage.getItem('refresh_token')

    if (accessToken != null && refreshToken != null) {
      return true
    } else {
      return false
    }
  }

  const [authenticated, setAuthenticated] = useState(isSignedIn)

  return (
    <div>
      <Navigation
        authenticated={authenticated}
        setAuthenticated={setAuthenticated}
      />
      <Routes>
        <Route
          path="/"
          element={
            <ProtectedRoutes authenticated={authenticated}>
              <Home />
            </ProtectedRoutes>
          }
        />
        <Route
          path="/login"
          element={<Login setAuthenticated={setAuthenticated} />}
        />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </div>
  )
}
