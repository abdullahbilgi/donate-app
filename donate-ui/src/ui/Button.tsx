import clsx from "clsx";

interface ButtonProps {
  size?: "small" | "medium" | "large" | "xxl";
  variation?:
    | "primary"
    | "submit"
    | "danger"
    | "donation"
    | "cell"
    | "addToCart"
    | "basketButton"
    | "pagination";
  disabled?: boolean;
  className?: string;
  onClick?: () => void;
  children: React.ReactNode;
}

const Button: React.FC<ButtonProps> = ({
  size = "medium",
  variation = "primary",
  disabled = false,
  className,
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
    primary:
      "bg-lime-300 text-teal-800 hover:bg-lime-400 border border-lime-400",
    submit:
      "bg-gray-500 text-white hover:bg-gray-600 px-5 py-3 transition-transform duration-300 hover:shadow-lg",
    danger:
      "bg-red-700 text-white hover:bg-red-800 px-5 py-3 transition-transform duration-300 hover:shadow-lg",
    donation: "bg-lime-300 text-teal-800 hover:bg-lime-400 ",
    cell: "bg-gray-600 text-gray-100 hover:bg-gray-700",
    addToCart:
      "bg-gray-300 text-teal-800 hover:text-gray-100 hover:bg-gray-400 border border-gray-300",
    basketButton:
      "bg-gray-300 text-teal-800 hover:text-gray-100 hover:bg-gray-400",
    pagination:
      "bg-gray-300 text-teal-800 hover:text-gray-50 hover:bg-gray-400/50 rounded-sm",
  };
  return (
    <button
      className={clsx(
        `flex justify-center items-center shadow-md rounded-2xl transition duration-200 cursor-pointer ${className}`,
        buttonSize[size], // Dinamik boyut
        buttonVariation[variation], // Dinamik varyasyon,
        "disabled:opacity-50 disabled:cursor-not-allowed"
      )}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </button>
  );
};

export default Button;
