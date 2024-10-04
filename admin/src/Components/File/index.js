import Excel from "../../assets/images/ico-exel-admin.png";
import Print from "../../assets/images/ico-print-admin.png";
const File = () => {
  return (
    <div className="flex justify-end mb-4">
      <button className="bg-white border p-2 rounded flex items-center mr-2 mr-3">
        <img src={Print} className="w-8 h-8 mr-2"></img> In danh sách
      </button>
      <button className="bg-white border p-2 rounded flex items-center mr-5">
        <img src={Excel} className=" mr-2 "></img> Xuất file
      </button>
    </div>
  );
};
export default File;
