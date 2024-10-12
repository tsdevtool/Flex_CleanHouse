import File from "../../Components/File";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";

const Customers = () => {
  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <File />
      <table className="w-full bg-white rounded shadow">
        <thead>
          <tr className="bg-indigo-900 text-white">
            <th className="p-2">ID</th>
            <th className="p-2">Tên khách hàng</th>
            <th className="p-2">Email</th>
            <th className="p-2">Địa chỉ</th>
            <th className="p-2">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr className="bg-indigo-100">
            <td className="p-2">1</td>
            <td className="p-2">Nguyễn Văn A</td>
            <td className="p-2">nguyenvana@gmail.com</td>
            <td className="p-2">88B Trương Văn Thành, Hiệp Phú, Tp.Thủ Đức</td>
            <td className="p-2">
              <form onClick={true}>
                <button className="bg-success m-2 p-2 btn-action">
                  Nhắn tin
                </button>
              </form>
              {/* <form onClick={true}>
                <button className="bg-yellow-500 m-2 p-2 btn-action">
                  Khóa
                </button>
              </form> */}
              <form onClick={true}>
                <button className="bg-red-500 m-2 p-2 btn-action">Xóa</button>
              </form>
            </td>
          </tr>
        </tbody>
      </table>
    </main>
  );
};

export default Customers;
