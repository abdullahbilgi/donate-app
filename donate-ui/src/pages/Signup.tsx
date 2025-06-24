import { Link } from "react-router";
import Button from "../ui/Button";
import SignupLoginLayout from "../ui/SignupLoginLayout";
import Form from "../ui/Form";
import FormRow from "../ui/FormRow";
import Input from "../ui/Input";

const Signup = () => {
  return (
    <SignupLoginLayout>
      <h1 className="text-2xl font-bold">Sign Up</h1>

      <Form formVariation="donate" onSubmit={() => console.log("submit")}>
        <FormRow labelText="Name">
          <Input type="text" id="name" disabled={false} />
        </FormRow>
        <FormRow labelText="Surname">
          <Input type="text" id="surname" disabled={false} />
        </FormRow>
        <FormRow labelText="Username">
          <Input type="text" id="username" disabled={false} />
        </FormRow>
        <FormRow labelText="Phone">
          <Input type="text" id="username" disabled={false} />
        </FormRow>
        <FormRow labelText="Email">
          <Input type="email" id="email" disabled={false} />
        </FormRow>
        <FormRow labelText="Password">
          <Input type="password" id="password" disabled={false} />
        </FormRow>

        <Button onClick={() => console.log("login")}>Sign Up</Button>
      </Form>
    </SignupLoginLayout>
  );
};

export default Signup;
