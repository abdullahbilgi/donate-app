import {
  cloneElement,
  createContext,
  useContext,
  useState,
  ReactNode,
  ReactElement,
  useRef,
  MouseEvent,
} from "react";
import { HiMiniXMark } from "react-icons/hi2";
import useOutsideClick from "./useOutsideClick";

// 1. Context tipi
interface ModalContextType {
  openName: string;
  open: (name: string) => void;
  close: () => void;
}

// 2. createContext (varsayılan değeri null ama sonra type cast edilir)
const ModalContext = createContext<ModalContextType | undefined>(undefined);

// 3. Modal Provider
interface ModalProps {
  children: ReactNode;
}

function Modal({ children }: ModalProps) {
  const [openName, setOpenName] = useState<string>("");

  const open = (name: string) => setOpenName(name);
  const close = () => setOpenName("");

  return (
    <ModalContext.Provider value={{ openName, open, close }}>
      {children}
    </ModalContext.Provider>
  );
}

// 4. Modal.Open bileşeni
interface OpenProps {
  children: ReactElement;
  modalName: string;
}

function Open({ children, modalName }: OpenProps) {
  const context = useContext(ModalContext);
  if (!context) throw new Error("Open must be used within a Modal");

  const { open } = context;

  return cloneElement(children, { onClick: () => open(modalName) } as Partial<
    typeof children.props
  >);
}

// 5. Modal.Window bileşeni
interface WindowProps {
  children: ReactElement;
  name: string;
}

function Window({ children, name }: WindowProps) {
  const context = useContext(ModalContext);
  if (!context) throw new Error("Window must be used within a Modal");

  const { openName, close } = context;

  const ref = useOutsideClick<HTMLDivElement>(close);

  if (name !== openName) return null;

  return (
    <div className="fixed top-0 left-0 w-full h-full z-[50] backdrop-filter bg-gray-200/50">
      <div
        ref={ref}
        className="fixed p-5 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-white shadow-xl rounded-lg bg-nature1"
      >
        <button
          className="absolute top-4 right-6 p-1 border-none hover:bg-buttonHover transition-all cursor-pointer"
          onClick={close}
        >
          <HiMiniXMark size={28} />
        </button>
        {cloneElement(children, { onCloseModal: close } as Partial<
          typeof children.props
        >)}
      </div>
    </div>
  );
}

// 6. Alt bileşenleri Modal'a ekle
Modal.Open = Open;
Modal.Window = Window;

export default Modal;
