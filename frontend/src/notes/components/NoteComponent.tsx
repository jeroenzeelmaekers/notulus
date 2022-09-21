import { User } from '../../entities/User'
import { Note } from '../entities/Note'
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline'
import { useState } from 'react'
import axios from 'axios'

export default function NoteComponent({
  note,
  user,
}: {
  note: Note
  user: User
}) {
  const [edit, setEdit] = useState(false)
  const [content, setContent] = useState(note.content)
  const [accessToken] = useState(localStorage.getItem('access_token'))
  const config = {
    headers: {
      'Content-Type': 'text/plain',
      Authorization: 'Bearer ' + accessToken,
    },
  }

  const handleContentChange = (event: any) => {
    setContent(event.target.value)
  }

  async function updateNote() {
    axios
      .post('http://localhost:8080/api/v1/note/' + note.id, content, config)
      .catch((error) => console.log(error))
  }

  async function deleteNote() {
    await axios
      .delete('http://localhost:8080/api/v1/note/' + note.id, config)
      .catch((error) => console.log(error))
  }

  return (
    <div className="w-3/4 my-3 h-min mx-auto bg-white rounded-xl drop-shadow-lg">
      {edit ? (
        <textarea
          className="p-5 w-full"
          value={content}
          onChange={handleContentChange}
        />
      ) : (
        <div className="p-5 w-full break-words">{content}</div>
      )}

      <div className="p-2 flex justify-end">
        {user.roles.includes('ROLE_ADMIN') ? (
          <div className="flex">
            <button
              className="px-1.5 py-1.5 mr-1 shadow-md bg-cyan-500 opacity-90 hover:opacity-100 shadow-cyan-500/50 rounded-md"
              onClick={() => {
                if (edit == true) updateNote()
                setEdit(!edit)
              }}
            >
              <PencilSquareIcon className="h-6 w-6 text-white" />
            </button>
            <button
              className="px-1.5 py-1.5 ml-1 shadow-md bg-red-500 opacity-90 hover:opacity-100 shadow-red-500/50  rounded-md"
              onClick={() => deleteNote()}
            >
              <TrashIcon className="h-6 w-6 text-white" />
            </button>
          </div>
        ) : (
          <></>
        )}
      </div>
    </div>
  )
}
