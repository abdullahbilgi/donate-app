import React from "react";

interface Props {
  children: React.ReactNode;
}

export const ShowOrderModalRow = ({ children }: Props) => {
  return <td className="px-3.5 py-10">{children}</td>;
};
