import axios from 'axios'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { User } from '../entities/User'
import NoteComponent from './components/NoteComponent'
import { Note } from './entities/Note'

export function Home() {
  const navigate = useNavigate()
  const [accessToken] = useState(localStorage.getItem('access_token'))
  const [user] = useState(parseUser(localStorage.getItem('user')))
  const [notes, setNotes] = useState([])

  function parseUser(input: any): User {
    if (input == null) {
      navigate('/login', { replace: true })
    }
    return JSON.parse(input)
  }

  useEffect(() => {
    const config = {
      headers: {
        Authorization: 'Bearer ' + accessToken,
      },
    }

    axios
      .get('http://localhost:8080/api/v1/note/', config)
      .then((response) => setNotes(response.data))
      .catch((error) => console.log(error))
  })

  return (
    <div className="w-screen mt-20">
      <div className="w-3/4 m-auto">
        {notes
          .sort((a: Note, b: Note) => (a.id > b.id ? 1 : -1))
          .map((note: Note) => {
            return <NoteComponent key={note.id} note={note} user={user} />
          })}
      </div>
    </div>
  )
}
