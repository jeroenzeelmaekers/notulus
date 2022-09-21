import { Navigate } from 'react-router-dom'

export default function ProtectedRoutes({
  children,
  authenticated,
}: {
  children: any
  authenticated: boolean
}) {
  return authenticated ? children : <Navigate to="/login" replace={true} />
}
