// ui/TablesCell.tsx
export const TablesCell = ({ children }: { children: React.ReactNode }) => (
  <td className="p-3 group-[.bordered]:border group-[.bordered]:border-slate-200 group-[.bordered]:dark:border-zink-500">
    {children}
  </td>
);
