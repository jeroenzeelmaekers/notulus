import axios from 'axios'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { User } from '../entities/User'
import NoteComponent from './components/NoteComponent'
import { Note } from './entities/Note'
import { NotePage } from './entities/Page'

export function Home() {
  const navigate = useNavigate()
  const [user] = useState(parseUser(localStorage.getItem('user')))
  const [currentPage, setCurrentPage] = useState(0)
  const [notePage, setNotePage] = useState<NotePage>()

  function parseUser(input: any): User {
    if (input == null) {
      navigate('/login', { replace: true })
    }
    return JSON.parse(input)
  }

  useEffect(() => {
    getPage(0)
  },[])

  const getPage = (pageNo: number) => {
    setCurrentPage(pageNo)

    const config = {
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + user.accessToken
      },
    }

    axios
      .get(`http://localhost:8080/api/v1/note?page=${pageNo}&size=${5}&sort=${"id"}`, config)
      .then((response) => setNotePage(response.data))
      .catch((error) => console.log(error))
  }

  const previousPage = () => {
    const currentPage = notePage?.number

    if (currentPage != undefined && currentPage != 0) {
      getPage(currentPage - 1)
    }
  }

  const nextPage = () => {
    const currentPage = notePage?.number

    if (currentPage != undefined && currentPage != notePage?.totalPages) {
      getPage(currentPage + 1)
    }
  }

  return (
    <div className="w-screen h-screen flex flex-col justify-between">
      <div className="w-3/4 mt-20 m-auto max-h-40">
        { notePage?.notes
          .sort((a: Note, b: Note) => (a.id > b.id ? 1 : -1))
          .map((note: Note) => {
            return <NoteComponent key={note.id} note={note} user={user} />
          })}
      </div>
      <div className="w-2/4 m-auto flex justify-between my-5">
      <button
              className=" bg-cyan-500 shadow-md shadow-cyan-500/50 px-3 py-1.5 w-1/3 mx-5 rounded-md text-white font-bold opacity-90 hover:opacity-100 disabled:bg-cyan-300"
              onClick={previousPage}
              disabled={currentPage == 0}
            >
              Previous
            </button>
            <p className='my-1.5'>{notePage?.number}</p>
            <button
              className=" bg-cyan-500 shadow-md shadow-cyan-500/50 px-3 py-1.5 w-1/3 mx-5 rounded-md text-white font-bold opacity-90 hover:opacity-100 disabled:bg-cyan-300"
              onClick={nextPage}
              disabled={currentPage == notePage?.totalPages}
            >
              Next
            </button>
      </div>
    </div>
  )
}
