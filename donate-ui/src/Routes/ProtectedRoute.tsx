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

  console.log(allowedRoles);
  console.log(role, token, isLogged);
  const pathname = useLocation().pathname;

  if (loading) return <p>Loading...</p>; //Loader ekle
  console.log("#", role, token, isLogged);

  if (!role || !token || !isLogged) return <Navigate to={"/login"} replace />;

  if (role && token && isLogged && ["/login", "/signup"].includes(pathname)) {
    return <Navigate to={"/home"} replace />;
  }
  console.log(allowedRoles.includes("ADMIN"));

  if (!allowedRoles.includes(role)) {
    return <Navigate to={"/unauthorized"} replace />;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
