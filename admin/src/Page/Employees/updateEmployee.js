import { getAuth, sendPasswordResetEmail } from "firebase/auth";
import { onValue, ref, update } from "firebase/database";
import {
  getDownloadURL,
  getStorage,
  ref as storageRef,
  uploadString,
} from "firebase/storage"; // Thêm import cho Firebase Storage
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

const UpdateEmployee = () => {
  const { id } = useParams(); // Lấy ID nhân viên từ URL
  const [employee, setEmployee] = useState(null); // Trạng thái lưu thông tin nhân viên
  const [image, setImage] = useState(null); // Trạng thái lưu ảnh
  const [roles, setRoles] = useState([]); // Lưu các role (chức vụ)
  const navigate = useNavigate();

  // Lấy danh sách các chức vụ (roles) từ Firebase
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

  // Lấy thông tin nhân viên từ Firebase dựa trên ID
  useEffect(() => {
    const employeeRef = ref(db, `users/${id}`); // Tham chiếu đến nhân viên trong Firebase
    onValue(employeeRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        setEmployee(data); // Cập nhật trạng thái với dữ liệu nhân viên
      }
    });
  }, [id]);

  // Hàm xử lý cập nhật thông tin nhân viên
  const handleUpdateAccount = async (e) => {
    e.preventDefault();

    // Cập nhật thông tin nhân viên trong Firebase
    const employeeRef = ref(db, `users/${id}`); // Tham chiếu đến nhân viên trong Firebase

    const selectedRole = roles.find((role) => role.id === employee.position); // position là id của role
    const roleName = selectedRole ? selectedRole.name : employee.role; // Nếu không thay đổi chức vụ thì giữ nguyên

    // Tạo một đối tượng với dữ liệu nhân viên mới
    const updatedData = {
      email: employee.email,
      name: employee.name,
      phoneNumber: employee.phoneNumber,
      role: roleName,
      salary: employee.salary,
      image: employee.image, // Ban đầu giữ nguyên ảnh cũ
    };

    try {
      // Tải lên hình ảnh vào Firebase Storage nếu có ảnh mới
      if (image) {
        const storage = getStorage(); // Khởi tạo Firebase Storage
        const storageImageRef = storageRef(storage, `images/${id}/profile.jpg`); // Đường dẫn lưu ảnh
        await uploadString(storageImageRef, image, "data_url"); // Tải hình ảnh lên
        const downloadURL = await getDownloadURL(storageImageRef); // Lấy URL tải xuống

        updatedData.image = downloadURL; // Cập nhật URL ảnh vào đối tượng cập nhật
      }

      // Cập nhật dữ liệu nhân viên
      await update(employeeRef, updatedData);

      // Gửi email reset mật khẩu nếu có thay đổi mật khẩu
      const auth = getAuth();
      if (employee.password) {
        await sendPasswordResetEmail(auth, employee.email);
        alert(`Đã gửi email reset mật khẩu đến ${employee.email}`);
      }

      navigate("/employees"); // Điều hướng về trang danh sách nhân viên sau khi cập nhật thành công
    } catch (error) {
      console.error("Lỗi khi cập nhật thông tin nhân viên:", error);
      alert("Đã có lỗi xảy ra trong quá trình cập nhật.");
    }
  };

  // Hàm xử lý thay đổi file ảnh
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImage(reader.result); // Lưu dữ liệu hình ảnh vào state
      };
      reader.readAsDataURL(file);
    }
  };

  if (!employee) return <div>Loading...</div>; // Hiển thị loading nếu dữ liệu chưa tải xong

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
          <div className="text-center">
            <img
              src={image || employee.image} // Hiển thị ảnh đã chọn hoặc ảnh hiện tại của nhân viên
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
          {/* Email */}
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

          {/* Mật khẩu */}
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
                onChange={(e) =>
                  setEmployee({ ...employee, password: e.target.value })
                }
              />
            </div>
          </div>

          {/* Họ tên */}
          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Họ tên
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <BiRename />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="text"
                placeholder="Nhập họ tên"
                value={employee.name}
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) =>
                  setEmployee({ ...employee, name: e.target.value })
                }
              />
            </div>
          </div>

          {/* Chức vụ */}
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
                className="w-full outline-none p-2 bg-white mt-2"
                value={employee.position}
                onChange={(e) =>
                  setEmployee({ ...employee, position: e.target.value })
                }
              >
                {roles.map((role) => (
                  <option key={role.id} value={role.id}>
                    {role.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Số điện thoại */}
          <div className="relative ml-4 mr-4 mb-4">
            <label className="text-input-box absolute -top-3 left-3 bg-white px-2 border-black border-2 rounded-xl ml-4">
              Số điện thoại
            </label>
            <div className="flex items-center border-2 border-black rounded-xl p-2 bg-white">
              <i className="fas fa-envelope text-blue-500 mr-1 text-2xl justify-center mt-1 iconbox p-2">
                <MdContactPhone />
              </i>
              <div className="border-l-2 border-black h-9 pb-4 mt-2 w-0"></div>
              <input
                type="text"
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

          {/* Lương */}
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
          <button type="submit" className="button-box rounded-full">
            Cập nhật
          </button>
        </div>
      </form>
    </main>
  );
};

export default UpdateEmployee;
