import { icon } from "leaflet";
import { ReactNode } from "react";
import toast from "react-hot-toast";
import { IoBagCheck, IoCloseSharp } from "react-icons/io5";

interface Props {
  title: string;
  text: string;
  icon: ReactNode;
  t: any;
}

export const LoginSignupNotification = ({ title, text, t, icon }: Props) => {
  return (
    <div className="shadow-lg overflow-visible scale-100 flex flex-col gap-2 p-4 bg-gray-50 border border-gray-200  rounded-sm">
      <div className="flex items-center justify-between gap-5 ">
        {icon}
        <div>
          <h4 className="font-semibold text-xl">{title}</h4>
          <p className="text-md font-semibold">{text}</p>
        </div>

        <button
          onClick={() => toast.dismiss(t.id)}
          className="cursor-pointer relative -top-5 -right-2"
        >
          <IoCloseSharp className="w-5 h-5" />
        </button>
      </div>
    </div>
  );
};
