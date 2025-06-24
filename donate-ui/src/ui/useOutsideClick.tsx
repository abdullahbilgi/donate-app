import { useEffect, useRef, RefObject } from "react";

function useOutsideClick<T extends HTMLElement = HTMLElement>(
  closeModal: () => void
): RefObject<T | null> {
  const ref = useRef<T>(null);

  useEffect(() => {
    function handleClick(e: MouseEvent) {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        closeModal();
      }
    }

    document.addEventListener("click", handleClick, true);
    return () => document.removeEventListener("click", handleClick, true);
  }, [closeModal]);

  return ref;
}

export default useOutsideClick;
