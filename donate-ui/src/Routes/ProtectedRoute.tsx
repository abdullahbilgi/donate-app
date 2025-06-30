import { Navigate, useLocation, useNavigate } from "react-router";
import { useAppSelector } from "../store";

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles: string[];
}

const ProtectedRoute = ({ children, allowedRoles }: ProtectedRouteProps) => {
  const { role, token, isLogged, loading } = useAppSelector(
    (state) => state.Auth
  );

  const pathname = useLocation().pathname;

  if (loading) return <p>Loading...</p>; //Loader ekle

  if (!role || !token || !isLogged) return <Navigate to={"/login"} replace />;

  if (role && token && isLogged && ["/login", "/signup"].includes(pathname)) {
    return <Navigate to={"/home"} replace />;
  }

  if (!allowedRoles.includes(role)) {
    return <Navigate to={"/unauthorized"} replace />;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
