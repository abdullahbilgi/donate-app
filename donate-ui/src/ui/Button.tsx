import clsx from "clsx";

interface ButtonProps {
  size?: "small" | "medium" | "large" | "xxl";
  variation?: "primary" | "secondary" | "danger" | "donation" | "cell";
  onClick: () => void;
  children: React.ReactNode;
}

const Button: React.FC<ButtonProps> = ({
  size = "medium",
  variation = "primary",
  onClick,
  children,
}) => {
  const buttonSize = {
    small: "text-xl py-1 px-2 font-bold",
    medium: "text-md py-2 px-4 font-bold",
    large: "text-lg py-3 px-6 font-bold",
    xxl: "text-xl py-3 px-8 font-bold",
  };

  // Dinamik varyasyon sınıfları
  const buttonVariation = {
    primary: "bg-lime-300 text-teal-800 hover:bg-lime-400",
    secondary: "bg-green-500 text-white hover:bg-green-600",
    danger: "bg-red-500 text-white hover:bg-red-600",
    donation: "bg-lime-200 text-teal-800 hover:bg-lime-300 ",
    cell: "bg-amber-400 text-amber-700 hover:bg-amber-500",
  };
  return (
    <button
      className={clsx(
        "flex justify-center items-center shadow-md rounded-2xl transition duration-300 cursor-pointer",
        buttonSize[size], // Dinamik boyut
        buttonVariation[variation] // Dinamik varyasyon
      )}
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default Button;
