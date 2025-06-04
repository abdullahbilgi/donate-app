type Props = {
  title: string;
};

const TablesColumn = ({ title }: Props) => {
  return (
    <th
      colSpan={1}
      className="true p-3 group-[.bordered]:border group-[.bordered]:border-slate-200 group-[.bordered]:dark:border-zink-500 sorting px-3 py-4 text-slate-900 bg-slate-200/50 font-semibold text-left dark:text-zink-50 dark:bg-zink-600 dark:group-[.bordered]:border-zink-500"
    >
      {title}
    </th>
  );
};

export default TablesColumn;
