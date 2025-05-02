import clsx from "clsx";
import React, { ReactElement } from "react";

interface FormRowProps {
  labelText?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
  errors?: string;
}

const FormRow: React.FC<FormRowProps> = ({
  labelText,
  children,
  className,
  errors,
}) => {
  const childId = React.isValidElement(children)
    ? (children as ReactElement<{ id?: string }>).props.id
    : undefined;

  return (
    <div className={clsx("flex flex-col gap-2 min-h-31 ", className)}>
      {labelText && (
        <label
          htmlFor={childId}
          className="text-md font-semibold flex items-center gap-2"
        >
          {labelText}
        </label>
      )}
      {children}
      {errors && (
        <span className="bg-red-200 text-red-800 text-xs font-semibold p-2 rounded-md relative">
          {errors}
        </span>
      )}
    </div>
  );
};

export default FormRow;
