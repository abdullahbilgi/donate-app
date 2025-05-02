import { Link } from "react-router";
import Button from "../ui/Button";
import SignupLoginLayout from "../ui/SignupLoginLayout";
import Form from "../ui/Form";
import FormRow from "../ui/FormRow";
import Input from "../ui/Input";

const Login = () => {
  return (
    <SignupLoginLayout>
      <h1 className="text-2xl font-bold">Log In</h1>

      <Form formVariation="sign" onSubmit={() => console.log("submit")}>
        <FormRow labelText="Username">
          <Input type="text" id="username" disabled={false} />
        </FormRow>

        <FormRow labelText="Password">
          <Input type="password" id="password" disabled={false} />
        </FormRow>

        <Button onClick={() => console.log("login")}>Sign Up</Button>

        <p className="font-normal">
          Dont have any account?{" "}
          <Link to={"/signup"} className="font-bold">
            Sign Up
          </Link>
        </p>
      </Form>
    </SignupLoginLayout>
  );
};

export default Login;
