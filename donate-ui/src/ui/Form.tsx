import clsx from "clsx";

interface FormProps {
  formVariation?: "sign" | "donate";
  onSubmit: () => void;
  children: React.ReactNode;
}

const Form: React.FC<FormProps> = ({
  formVariation = "sign",
  onSubmit,
  children,
}) => {
  const variations = {
    sign: "flex flex-col gap-5",
    donate: "grid grid-cols-1 md:grid-cols-2 gap-x-6 md:gap-x-20 ",
  };
  return (
    <form onSubmit={onSubmit} className={clsx(variations[formVariation])}>
      {children}
    </form>
  );
};

export default Form;
