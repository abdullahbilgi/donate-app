import clsx from "clsx";
import React, { forwardRef } from "react";

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  inputVariation?: "sign" | "donate";
  type: string;
}

const Input = forwardRef<HTMLInputElement | HTMLTextAreaElement, InputProps>(
  ({ inputVariation = "sign", type, className, ...props }, ref) => {
    const variations = {
      sign: "bg-gray-100 p-3 rounded-3xl w-72",
      donate: "bg-gray-100 p-3 rounded-lg border border-gray-300",
    };

    if (type === "textarea") {
      return (
        <textarea
          ref={ref as React.Ref<HTMLTextAreaElement>}
          className={clsx(
            variations[inputVariation],
            "min-h-20 max-h-20 w-full focus:outline-none focus:ring-2 focus:ring-lime-400 transition-all",
            className
          )}
          {...(props as React.TextareaHTMLAttributes<HTMLTextAreaElement>)}
        />
      );
    }

    return (
      <input
        ref={ref as React.Ref<HTMLInputElement>}
        type={type}
        className={clsx(
          variations[inputVariation],
          "focus:outline-none focus:ring-2 focus:ring-lime-400 transition-all",
          className
        )}
        {...props}
      />
    );
  }
);

Input.displayName = "Input"; // forwardRef kullandığın için eklemen gerekir
export default Input;
