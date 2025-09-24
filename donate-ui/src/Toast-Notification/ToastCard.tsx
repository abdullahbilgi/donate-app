import { toast, type Toast } from "react-hot-toast";
import { CheckCircle2, XCircle, Info, AlertTriangle, X } from "lucide-react";
import React from "react";

type Variant = "success" | "error" | "info" | "warning";

const styles: Record<
  Variant,
  {
    accent: string; // sol şerit + bar rengi
    iconWrap: string; // ikon arkaplanı
  }
> = {
  success: {
    accent: "bg-emerald-500",
    iconWrap: "bg-emerald-500/10 text-emerald-600",
  },
  error: { accent: "bg-rose-500", iconWrap: "bg-rose-500/10 text-rose-600" },
  info: { accent: "bg-sky-500", iconWrap: "bg-sky-500/10 text-sky-600" },
  warning: {
    accent: "bg-amber-500",
    iconWrap: "bg-amber-500/10 text-amber-600",
  },
};

const VariantIcon: Record<
  Variant,
  React.ComponentType<React.SVGProps<SVGSVGElement>>
> = {
  success: CheckCircle2,
  error: XCircle,
  info: Info,
  warning: AlertTriangle,
};

type Props = {
  t: Toast;
  title: string;
  description?: string;
  variant?: Variant;
  icon?: React.ReactNode;
};

export function ToastCard({
  t,
  title,
  description,
  variant = "success",
  icon,
}: Props) {
  const Icon = VariantIcon[variant];
  const s = styles[variant];

  return (
    <div
      role={variant === "error" ? "alert" : "status"}
      aria-live="polite"
      className={`pointer-events-auto w-[360px] rounded-2xl border border-slate-200/60
                  bg-white/90 backdrop-blur-md
                  shadow-[0_8px_30px_rgba(0,0,0,.12)] ring-1 ring-black/5
                  ${t.visible ? "animate-toast-in" : "animate-toast-out"}`}
    >
      <div className="relative p-4">
        <div
          className={`absolute left-0 top-0 h-full w-1 rounded-l-2xl ${s.accent}`}
        />

        <div className="flex gap-3">
          <div
            className={`mt-0.5 flex h-9 w-9 items-center justify-center rounded-full ${s.iconWrap}`}
          >
            {icon ?? <Icon className="h-5 w-5" aria-hidden />}
          </div>

          <div className="min-w-0 flex-1">
            <h4 className="text-sm font-semibold leading-5 text-slate-900">
              {title}
            </h4>
            {description && (
              <p className="mt-0.5 text-sm text-slate-600">{description}</p>
            )}
            <div className="mt-3 h-1 w-full overflow-hidden rounded-full bg-slate-200/70">
              <div
                className={`h-full ${s.accent} animate-toast-bar`}
                style={{
                  ["--toast-duration" as any]: `${t.duration ?? 4000}ms`,
                }}
              />
            </div>
          </div>

          <button
            onClick={() => toast.dismiss(t.id)}
            className="ml-2 rounded-md p-1.5 hover:bg-slate-100
                       focus:outline-none focus-visible:ring focus-visible:ring-slate-300"
            aria-label="Close toast"
          >
            <X className="h-4 w-4" aria-hidden />
          </button>
        </div>
      </div>
    </div>
  );
}
