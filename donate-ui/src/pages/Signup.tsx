import { Link } from "react-router";
import Button from "../ui/Button";
import SignupLoginLayout from "../ui/SignupLoginLayout";

const Signup = () => {
  return (
    <SignupLoginLayout>
      <h1 className="text-2xl font-bold">Sign Up</h1>

      <form className="flex flex-col gap-5">
        <div className="flex flex-col gap-2">
          <label className="text-md font-semibold">Full Name</label>
          <input
            type="text"
            placeholder="Full Name"
            className="bg-gray-100 p-3 rounded-3xl w-72"
          />
        </div>

        <div className="flex flex-col gap-2">
          <label className="text-md font-semibold">Username</label>
          <input
            type="text"
            placeholder="Username"
            className="bg-gray-100 p-3 rounded-3xl w-72"
          />
        </div>

        <div className="flex flex-col gap-2">
          <label className="text-md font-semibold">Email</label>
          <input
            type="text"
            placeholder="Email"
            className="bg-gray-100 p-3 rounded-3xl w-72"
          />
        </div>

        <div className="flex flex-col gap-2 mb-3">
          <label className="text-md font-semibold">Password</label>
          <input
            type="password"
            placeholder="Password"
            className="bg-gray-100 p-3 rounded-3xl w-72"
          />
        </div>

        <Button onClick={() => console.log("login")}>Sign Up</Button>
      </form>
    </SignupLoginLayout>
  );
};

export default Signup;
