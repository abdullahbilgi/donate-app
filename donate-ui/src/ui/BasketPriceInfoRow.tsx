interface InfoProps {
  rowTitle: string;
  rowValue: string;
}

const BasketPriceInfoRow: React.FC<InfoProps> = ({ rowTitle, rowValue }) => {
  return (
    <div className="flex justify-between items-center p-2 mr-3 text-gray-500 text-sm font-medium">
      <p className="">{rowTitle}:</p>
      <p>
        {rowTitle === "Discount" ? "-" : ""}${rowValue}
      </p>
    </div>
  );
};

export default BasketPriceInfoRow;
