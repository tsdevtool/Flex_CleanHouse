import {
  createUserWithEmailAndPassword,
  fetchSignInMethodsForEmail,
  sendEmailVerification,
  sendPasswordResetEmail,
} from "firebase/auth";
import { onValue, ref, set } from "firebase/database";
import {
  getDownloadURL,
  ref as storageRef,
  uploadBytes,
} from "firebase/storage";
import { useEffect, useState } from "react";
import { BiRename } from "react-icons/bi";
import { CiCamera } from "react-icons/ci";
import { FaAngleLeft, FaIdCard } from "react-icons/fa";
import { FaMoneyBillWave } from "react-icons/fa6";
import { IoIosLock } from "react-icons/io";
import { MdContactPhone, MdEmail } from "react-icons/md";
import { Link } from "react-router-dom";
import "../../App.css";
import { auth, db, storage } from "../../firebase/config";

const AddEmployee = () => {
  const [image, setImage] = useState(null); // Change to null for image file
  const [imageUrl, setImageUrl] = useState("");
  const [roles, setRoles] = useState([]);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [position, setPosition] = useState("");
  const [salary, setSalary] = useState("");

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      //   setImage(file); // Set the file instead of the image URL
      //   const imageUrl = URL.createObjectURL(file);
      //   setImageUrl(imageUrl);

      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onloadend = () => {
          setImage(reader.result);
        };
        reader.readAsDataURL(file);
      }
    }
  };

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

  const handleCreateAccount = async (e) => {
    e.preventDefault();
    try {
      const signInMethods = await fetchSignInMethodsForEmail(auth, email);
      if (signInMethods.length > 0) {
        alert("Email đã tồn tại. Vui lòng sử dụng email khác hoặc đăng nhập.");
        return; // Ngừng thực hiện nếu email đã tồn tại
      }

      // Tạo người dùng mới trong Firebase Authentication
      const userCredential = await createUserWithEmailAndPassword(
        auth,
        email,
        password
      );
      const user = userCredential.user;

      // Tải hình ảnh lên Firebase Storage
      if (image) {
        const imageRef = storageRef(storage, `images/${user.uid}/profile.jpg`); // Lưu hình ảnh theo ID người dùng
        await uploadBytes(imageRef, image); // Tải lên hình ảnh

        // Lấy URL của hình ảnh
        const imageUrl = await getDownloadURL(imageRef);

        const selectedRole = roles.find((role) => role.id === position); // position là id của role
        const roleName = selectedRole ? selectedRole.name : null;
        // Lưu thông tin người dùng vào Realtime Database
        const userRef = ref(db, "users/" + user.uid);
        await set(userRef, {
          email: email,
          name: name,
          phoneNumber: phone,
          role: roleName,
          salary: parseFloat(salary),
          image: imageUrl, // Lưu đường dẫn hình ảnh
        });
      } else {
        const selectedRole = roles.find((role) => role.id === position); // position là id của role
        const roleName = selectedRole ? selectedRole.name : null;
        // Nếu không có hình ảnh, lưu người dùng mà không có hình ảnh
        const userRef = ref(db, "users/" + user.uid);
        await set(userRef, {
          email: email,
          name: name,
          phoneNumber: phone,
          role: roleName,
          salary: parseFloat(salary),
          image: null, // Hoặc bạn có thể không lưu trường này
        });
      }

      alert("Tạo tài khoản thành công!");
      await sendPasswordResetEmail(auth, email);
    } catch (error) {
      console.error("Lỗi tạo tài khoản:", error);
      alert(
        "Kiểm tra lại thông tin người dùng hoặc người dùng đã có thông tin."
      );
    }
  };

  return (
    <main className="flex-1 p-6 bg-gray-100">
      <div className="flex items-center mb-8">
        <Link to={"/employees"} className="fas fa-arrow-left mr-2 text-3xl">
          <FaAngleLeft />
        </Link>
        <h1 className="text-2xl font-bold">Thêm nhân viên mới</h1>
      </div>
      <form onSubmit={handleCreateAccount}>
        <div className="flex justify-center mb-8">
          <div className=" text-center">
            <img
              src={image}
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
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) => setEmail(e.target.value)}
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
                onChange={(e) => setPassword(e.target.value)}
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
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) => setName(e.target.value)}
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
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) => setPhone(e.target.value)}
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
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) => setPosition(e.target.value)}
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
                className="w-full outline-none p-2 bg-white mt-2"
                required
                onChange={(e) => setSalary(e.target.value)}
              />
            </div>
          </div>
        </div>
        <div className="flex justify-center mt-8">
          <button type="submit" className=" button-box rounded-full">
            Tạo tài khoản
          </button>
        </div>
      </form>
    </main>
  );
};

export default AddEmployee;
