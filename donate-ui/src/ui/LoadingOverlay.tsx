import type { LucideIcon } from "lucide-react";

type Props = {
  label: string;
  size?: number; // px
  icon: LucideIcon;
};

export default function LoadingOverlay({
  label = "Loading...",
  size = 36,
  icon: Icon,
}: Props) {
  return (
    <div className="absolute inset-0 grid place-items-center bg-white/60 backdrop-blur-sm">
      <div className="flex flex-col items-center gap-3">
        <div className="rounded-2xl p-4 shadow-lg bg-white">
          <Icon
            className="animate-[spin_1.1s_linear_infinite]"
            style={{ width: size, height: size }}
          />
        </div>
        <p className="text-sm text-gray-600">{label}</p>
      </div>
    </div>
  );
}
