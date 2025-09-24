import { AlertTriangle, CalendarClock, Hourglass, Timer } from "lucide-react";

export function ExpiryBadge({ date }: { date: string }) {
  const msPerDay = 24 * 60 * 60 * 1000;

  const now = new Date();
  const target = new Date(date);

  if (isNaN(target.getTime())) return "Geçersiz tarih";

  const n = new Date(
    now.getFullYear(),
    now.getMonth(),
    now.getDate()
  ).getTime();
  const t = new Date(
    target.getFullYear(),
    target.getMonth(),
    target.getDate()
  ).getTime();

  const diffDays = Math.round((t - n) / msPerDay);

  let cls =
    "inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ring-1";
  let label = "";
  let Icon = CalendarClock;

  if (diffDays < 0) {
    cls += " bg-red-100 text-red-700 ring-red-200";
    label = `Expired ${Math.abs(diffDays)} days ago`;
    Icon = AlertTriangle;
  } else if (diffDays === 0) {
    cls += " bg-red-100 text-red-700 ring-red-200";
    label = "Last day";
    Icon = Timer;
  } else if (diffDays <= 2) {
    cls += " bg-orange-100 text-orange-700 ring-orange-200";
    label = `${diffDays} days left`;
    Icon = Hourglass;
  } else if (diffDays <= 7) {
    cls += " bg-amber-100 text-amber-700 ring-amber-200";
    label = `${diffDays} days left`;
    Icon = CalendarClock;
  } else {
    cls += " bg-emerald-100 text-emerald-700 ring-emerald-200";
    label = `${diffDays} days left`;
    Icon = CalendarClock;
  }

  return (
    <span className={cls} role="status" aria-live="polite">
      <Icon className="h-3.5 w-3.5" aria-hidden />
      <span className="truncate">{label}</span>
    </span>
  );
}
