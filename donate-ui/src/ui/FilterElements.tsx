import FilterElementsContent from "./FilterElementsContent";

interface FilterElementsProps {
  title: string;
  children: React.ReactNode;
}
const FilterElements: React.FC<FilterElementsProps> = ({ title, children }) => {
  return (
    <div className="mt-4 collapsible">
      <button className="flex items-center w-full text-left collapsible-header group">
        <h6 className="underline grow">{title}</h6>
        <div className="shrink-0 text-slate-500 dark:text-zink-200">ICON</div>
      </button>
      <div className="mt-4 collapsible-content">
        <div className="flex flex-col gap-2">{children}</div>
      </div>
    </div>
  );
};

export default FilterElements;
