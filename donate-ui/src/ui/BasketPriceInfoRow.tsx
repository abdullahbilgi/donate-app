interface InfoProps {
  rowTitle: string;
  rowValue: number;
}

const BasketPriceInfoRow: React.FC<InfoProps> = ({ rowTitle, rowValue }) => {
  return (
    <div className="flex justify-between items-center p-2 mr-3 text-gray-500 text-md font-medium">
      <p className="">{rowTitle}:</p>
      <p
        className={`${
          rowTitle === "Discount" ? "text-red-950" : ""
        } text-lg font-bold`}
      >
        {rowTitle === "Discount" ? "-" : ""}${rowValue}
      </p>
    </div>
  );
};

export default BasketPriceInfoRow;
