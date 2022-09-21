import axios from 'axios'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login({ setAuthenticated }: { setAuthenticated: any }) {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    setAuthenticated(false)
  })

  const handleSubmit = (event: any) => {
    event.preventDefault()

    const data = {
      username: username,
      password: password,
    }

    axios
      .post('http://localhost:8080/api/v1/auth/login', data)
      .then((response) => {
        sessionStorage.setItem('user', JSON.stringify(response.data))
        setAuthenticated(true)
        navigate('/', { replace: true })
      })
      .catch(() => setError('Invalid credentials'))
  }

  return (
    <div className="flex h-screen">
      <div className="w-2/4 h-2/4 m-auto bg-white rounded-xl drop-shadow-lg py-24 px-30 sm:p-12">
        <form className="w-full h-full" onSubmit={handleSubmit}>
          <div className="h-full flex flex-1 flex-col justify-between">
            <div>
              <div className="flex flex-col my-5">
                <label className="font-bold mb-1">Username</label>
                <input
                  className="drop-shadow-lg rounded-md p-2 placeholder:text-slate-400"
                  name="username"
                  type="text"
                  placeholder="email@domain.com"
                  value={username}
                  onChange={(event) => {
                    setError('')
                    setUsername(event.target.value)
                  }}
                />
              </div>
              <div className="flex flex-col my-5">
                <label className="font-bold mb-1">Password</label>
                <input
                  className="drop-shadow-lg rounded-md p-2 placeholder:text-slate-400"
                  name="password"
                  type="password"
                  placeholder="password"
                  value={password}
                  onChange={(event) => {
                    setError('')
                    setPassword(event.target.value)
                  }}
                />
              </div>
              {error != '' ? (
                <p className="m-auto w-max h-max text-red-500">{error}</p>
              ) : (
                <></>
              )}
            </div>
            <button
              className=" bg-cyan-500 shadow-md shadow-cyan-500/50 px-3 py-1.5 w-full rounded-md text-white font-bold opacity-90 hover:opacity-100"
              type="submit"
            >
              Sign in
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
