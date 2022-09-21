import { useState } from 'react'
import { Route, Routes } from 'react-router-dom'
import Navigation from './components/NavigationComponent'
import ProtectedRoutes from './security/ProtectedRoutes'
import Login from './login/Index'
import { Home } from './notes/Index'
import NotFound from './notfound/Index'
import { User } from './entities/User'

export default function App() {

  const [user] = useState<User | null>(parseUser)

  function parseUser(): User | null {
    const lsUser = localStorage.getItem('user')

    if (lsUser == null) {
      return null;
    }

    return JSON.parse(lsUser)
  }

  const [authenticated, setAuthenticated] = useState(user?.accessToken != null)

  return (
    <div>
      <Navigation
        authenticated={authenticated}
        setAuthenticated={setAuthenticated}
        user={user}
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
