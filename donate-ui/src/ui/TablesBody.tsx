interface TablesBodyProps {
  children: React.ReactNode;
}
const TablesBody = ({ children }: TablesBodyProps) => {
  return (
    <tr className="group-[.stripe]:even:bg-slate-50 font-medium group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
      {children}
    </tr>
  );
};

export default TablesBody;
