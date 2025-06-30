interface ContentProps {
  labelText: string;
  inputId: any;
  onClick?: () => void;
}

const FilterElementsContent: React.FC<ContentProps> = ({
  labelText,
  inputId,
  onClick,
}) => {
  return (
    <div className="flex items-center gap-2">
      <input id={inputId} type="checkbox" onClick={onClick} />
      <label htmlFor={inputId} className="align-middle cursor-pointer">
        {labelText}
      </label>
    </div>
  );
};

export default FilterElementsContent;
