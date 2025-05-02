interface ContentProps {
  labelText: string;
  inputId: any;
}

const FilterElementsContent: React.FC<ContentProps> = ({
  labelText,
  inputId,
}) => {
  return (
    <div className="flex items-center gap-2">
      <input
        id={inputId}
        type="checkbox"
        className="size-4 cursor-pointer bg-white border border-slate-200 checked:bg-none dark:bg-zink-700 dark:border-zink-500 rounded-sm appearance-none arrow-none relative after:absolute after:content-['\eb7b'] after:top-0 after:left-0 after:font-remix after:leading-none after:opacity-0 checked:after:opacity-100 after:text-custom-500 checked:border-custom-500 dark:after:text-custom-500 dark:checked:border-custom-800"
      />
      <label htmlFor={inputId} className="align-middle cursor-pointer">
        {labelText}
      </label>
    </div>
  );
};

export default FilterElementsContent;
