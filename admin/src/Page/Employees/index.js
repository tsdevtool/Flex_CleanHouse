import { IoMdPersonAdd } from "react-icons/io";
import { Link } from "react-router-dom";
import File from "../../Components/File";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";

const Employees = () => {
  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <div className="flex justify-end h-auto mr-0">
        <div className="flex justify-end mb-4">
          <Link
            to={"#"}
            className="bg-white border p-2 rounded flex items-center mr-2 mr-3 text-decoration-none"
          >
            <IoMdPersonAdd className="w-8 h-8 mr-3" /> Thêm nhân viên
          </Link>
        </div>
        <File />
      </div>

      <table className="w-full bg-white rounded shadow">
        <thead>
          <tr className="bg-indigo-900 text-white">
            <th className="p-2">ID</th>
            <th className="p-2">Tên nhân viên</th>
            <th className="p-2">Chức vụ</th>
            <th className="p-2">Email</th>
            <th className="p-2">Mật khẩu</th>
            <th className="p-2">Lương</th>
            <th className="p-2">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr className="bg-indigo-100">
            <td className="p-2">1</td>
            <td className="p-2">Nguyễn Thanh Siêu</td>
            <td className="p-2">Quản lý trưởng</td>
            <td className="p-2">sieusml03@gmail.com</td>
            <td className="p-2">sieu123</td>
            <td className="p-2">32,000,000</td>
            <td className="p-2">
              <form onClick={true}>
                <button className="bg-success m-2 p-2 btn-action">
                  Nhắn tin
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-yellow-500 m-2 p-2 btn-action">
                  Chỉnh sửa
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-red-500 m-2 p-2 btn-action">Xóa</button>
              </form>
            </td>
          </tr>
          <hr></hr>
          <tr className="bg-indigo-100">
            <td className="p-2">2</td>
            <td className="p-2">Trần Việt Đức</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">tranvietduc85vip1@gmail.com</td>
            <td className="p-2">tranvietduc123123</td>
            <td className="p-2">14,000,000</td>
            <td className="p-2">
              <form onClick={true}>
                <button className="bg-success m-2 p-2 btn-action">
                  Nhắn tin
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-yellow-500 m-2 p-2 btn-action">
                  Chỉnh sửa
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-red-500 m-2 p-2 btn-action">Xóa</button>
              </form>
            </td>
          </tr>
          <hr></hr>
          <tr className="bg-indigo-100">
            <td className="p-2">3</td>
            <td className="p-2">Đăng Lê Ngọc Trạng</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">dlnt2010@gmail.com</td>
            <td className="p-2">trangchodien</td>
            <td className="p-2">14,000,000</td>
            <td className="p-2">
              <form onClick={true}>
                <button className="bg-success m-2 p-2 btn-action">
                  Nhắn tin
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-yellow-500 m-2 p-2 btn-action">
                  Chỉnh sửa
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-red-500 m-2 p-2 btn-action">Xóa</button>
              </form>
            </td>
          </tr>
          <hr></hr>
          <tr className="bg-indigo-100">
            <td className="p-2">4</td>
            <td className="p-2">Ngô Xuân Mùi</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">teawild@gmail.com</td>
            <td className="p-2">trumdesign</td>
            <td className="p-2">14,000,000</td>
            <td className="p-2">
              <form onClick={true}>
                <button className="bg-success m-2 p-2 btn-action">
                  Nhắn tin
                </button>
              </form>
              <form onClick={true}>
                <button className="bg-yellow-500 m-2 p-2 btn-action">
                  Chỉnh sửa
                </button>
              </form>
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

export default Employees;
