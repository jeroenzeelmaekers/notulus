import { Link, useNavigate } from 'react-router-dom'

export default function Navigation({
  authenticated,
  setAuthenticated,
}: {
  authenticated: boolean
  setAuthenticated: any
}) {
  const navigate = useNavigate()
  const clearToken = () => {
    localStorage.removeItem('user')
    setAuthenticated(false)
    navigate('/login', { replace: true })
  }

  return (
    <div className="z-10 fixed top-0 flex w-screen justify-between bg-white drop-shadow-md font-bold">
      <div>
        <Link to="/">
          <p className="p-3 hover:bg-slate-100">Notulus</p>
        </Link>
      </div>
      <div>
        {!authenticated ? (
          <Link to="/login">
            <p className="p-3 hover:bg-slate-100">Sign in</p>
          </Link>
        ) : (
          <button className="p-3 hover:bg-slate-100" onClick={clearToken}>
            Sign out
          </button>
        )}
      </div>
    </div>
  )
}
