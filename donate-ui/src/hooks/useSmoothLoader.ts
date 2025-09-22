import { useEffect, useRef, useState } from "react";

export function useSmoothLoader(
  isLoading: boolean,
  { showAfter = 200, minVisible = 400 } = {}
) {
  const [show, setShow] = useState(false);
  const shownAtRef = useRef<number | null>(null);
  const delayRef = useRef<number | null>(null);
  const minRef = useRef<number | null>(null);

  useEffect(() => {
    return () => {
      if (delayRef.current) clearTimeout(delayRef.current);
      if (minRef.current) clearTimeout(minRef.current);
    };
  }, []);

  useEffect(() => {
    if (isLoading) {
      // Hızlı yüklemelerde spinner'ı hiç göstermeyelim.
      if (!show) {
        if (delayRef.current) clearTimeout(delayRef.current);
        delayRef.current = window.setTimeout(() => {
          setShow(true);
          shownAtRef.current = Date.now();
        }, showAfter);
      }
    } else {
      // Gösterilmişse en az minVisible kalsın.
      if (delayRef.current) {
        clearTimeout(delayRef.current);
        delayRef.current = null;
      }
      if (show) {
        const elapsed = shownAtRef.current
          ? Date.now() - shownAtRef.current
          : 0;
        const remain = Math.max(minVisible - elapsed, 0);
        if (minRef.current) clearTimeout(minRef.current);
        minRef.current = window.setTimeout(() => {
          setShow(false);
          shownAtRef.current = null;
        }, remain);
      }
    }
  }, [isLoading, show, showAfter, minVisible]);

  return show;
}
