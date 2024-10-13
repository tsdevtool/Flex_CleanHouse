import { onValue, ref, update } from "firebase/database";
import { useEffect, useState } from "react";
import { BiRename } from "react-icons/bi";
import { CiCamera } from "react-icons/ci";
import { FaAngleLeft, FaIdCard } from "react-icons/fa";
import { FaMoneyBillWave } from "react-icons/fa6";
import { IoIosLock } from "react-icons/io";
import { MdContactPhone, MdEmail } from "react-icons/md";
import { Link, useNavigate, useParams } from "react-router-dom";
import "../../App.css";
import { db } from "../../firebase/config";
import { getAuth, sendPasswordResetEmail } from "firebase/auth";

const UpdateEmployee = () => {
  const { id } = useParams(); // Lấy ID nhân viên từ URL
  const [employee, setEmployee] = useState(null); // Trạng thái lưu thông tin nhân viên
  const [image, setImage] = useState(null); // Trạng thái lưu ảnh
  const [roles, setRoles] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const rolesRef = ref(db, "roles");
    onValue(rolesRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const rolesArray = Object.entries(data).map(([key, value]) => ({
          id: key,
          name: value.name,
        }));
        setRoles(rolesArray);
      } else {
        setRoles([]);
      }
    });
  }, []);

  useEffect(() => {
    const employeeRef = ref(db, `users/${id}`); // Tham chiếu đến nhân viên trong Firebase
    onValue(employeeRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        setEmployee(data); // Cập nhật trạng thái với dữ liệu nhân viên
      }
    });
  }, [id]);

  const handleUpdateAccount = (e) => {
    e.preventDefault();

    // Cập nhật thông tin nhân viên trong Firebase
    const employeeRef = ref(db, `users/${id}`); // Tham chiếu đến nhân viên trong Firebase

    const selectedRole = roles.find((role) => role.id === employee.position); // position là id của role
    const roleName = selectedRole ? selectedRole.name : null;
    // Tạo một đối tượng với dữ liệu nhân viên mới
    const updatedData = {
      email: employee.email,
      password: employee.password,
      name: employee.name,
      phoneNumber: employee.phoneNumber,
      role: roleName,
      salary: employee.salary,
      image: image || employee.image, // Giữ lại hình ảnh cũ nếu không có hình mới
    };

    update(employeeRef, updatedData)
      .then(() => {
        const auth = getAuth();
        sendPasswordResetEmail(auth, employee.email)
          .then(() => {
            console.log(
              "Email reset mật khẩu đã được gửi đến:",
              employee.email
            );
            alert(
              "Thông báo: Đã gửi email reset mật khẩu đến " + employee.email
            );
          })
          .catch((error) => {
            console.error("Lỗi khi gửi email reset mật khẩu:", error);
            alert("Đã có lỗi xảy ra khi gửi email reset mật khẩu.");
          });

        navigate("/employees"); // Điều hướng về trang chủ sau khi cập nhật thành công
      })
      .catch((error) => {
        console.error("Lỗi khi cập nhật thông tin nhân viên:", error);
      });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      // Xử lý tải ảnh lên (nếu cần)
      const reader = new FileReader();
      reader.onloadend = () => {
        setImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  if (!employee) return <div>Loading...</div>;

  return (
    <main className="flex-1 p-6 bg-gray-100">
      <div className="flex items-center mb-8">
        <Link to={"/employees"} className="fas fa-arrow-left mr-2 text-3xl">
          <FaAngleLeft />
        </Link>
        <h1 className="text-2xl font-bold">Cập nhật thông tin nhân viên</h1>
      </div>
      <form onSubmit={handleUpdateAccount}>
        <div className="flex justify-center mb-8">
          <div className=" text-center">
            <img
              src={image || employee.image}
              alt="Ảnh đại diện"
              className="rounded-full mb-4 ml-3 w-24 h-24 object-cover border-1 border-black"
            />
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="hidden"
              id="file-upload"
            />
            <label
              htmlFor="file-upload"
              className="flex bg-white border border-gray-300 rounded-full px-4 py-2 text-center text-sm justify-items-center"
            >
              <i className="fas fa-camera mr-2 text-lg">
                <CiCamera className="flex" />
              </i>
              Upload file
            </label>
          </div>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Email
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <MdEmail />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="email"
                placeholder="Nhập email"
                value={employee.email}
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, email: e.target.value })
                }
              />
            </div>
          </div>

          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Mật khẩu
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <IoIosLock />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="password"
                placeholder="Nhập mật khẩu"
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, password: e.target.value })
                }
              />
            </div>
          </div>

          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Họ & Tên
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <BiRename />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="text"
                placeholder="Nhập họ và tên"
                value={employee.name}
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, name: e.target.value })
                }
              />
            </div>
          </div>

          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              SĐT
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <MdContactPhone />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="phone"
                placeholder="Nhập số điện thoại"
                value={employee.phoneNumber}
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, phoneNumber: e.target.value })
                }
              />
            </div>
          </div>

          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Chức vụ
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <FaIdCard />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <select
                type="text"
                placeholder="Nhập chức vụ"
                value={employee.roles}
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, position: e.target.value })
                }
              >
                <option value="">Chọn chức vụ</option>
                {roles.map((role) => (
                  <option
                    key={role.id}
                    value={role.id}
                    className="w-full outline-none p-2 bg-white mt-2"
                  >
                    {role.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Lương cơ bản
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <FaMoneyBillWave />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="number"
                placeholder="Nhập lương cơ bản"
                value={employee.salary}
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, salary: e.target.value })
                }
              />
            </div>
          </div>
        </div>
        <div className="flex justify-center mt-8">
          <button type="submit" className=" button-box rounded-full">
            Cập nhật
          </button>
        </div>
      </form>
    </main>
  );
};

export default UpdateEmployee;
