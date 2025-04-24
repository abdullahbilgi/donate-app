import { Link } from "react-router";
import Button from "../ui/Button";
import SignupLoginLayout from "../ui/SignupLoginLayout";

const Login = () => {
  return (
    <SignupLoginLayout>
      <h1 className="text-2xl font-bold">Log In</h1>

      <form className="flex flex-col gap-5">
        <div className="flex flex-col gap-2">
          <label className="text-lg font-semibold">Name</label>
          <input
            type="text"
            placeholder="Name"
            className="bg-gray-100 p-3 w-64 rounded-3xl"
          />
        </div>

        <div className="flex flex-col gap-2 mb-8">
          <label className="text-lg font-semibold">Password</label>
          <input
            type="password"
            placeholder="Password"
            className="bg-gray-100 p-3 w-64 rounded-3xl"
          />
        </div>

        <Button onClick={() => console.log("login")}>Log in</Button>

        <p className="font-normal">
          Dont have any account?{" "}
          <Link to={"/signup"} className="font-bold">
            Sign Up
          </Link>
        </p>
      </form>
    </SignupLoginLayout>
  );
};

export default Login;
