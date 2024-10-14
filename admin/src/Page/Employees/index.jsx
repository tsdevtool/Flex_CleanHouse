import { onValue, ref, remove } from "firebase/database"; // Import remove từ firebase/database
import { useEffect, useState } from "react";
import { IoMdPersonAdd } from "react-icons/io";
import { Link, useNavigate } from "react-router-dom";
import File from "../../Components/File";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";
import { db } from "../../firebase/config";

const Employees = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  // useEffect(() => {
  //   const employeesRef = ref(db, "users"); // Tham chiếu đến "users" trong Realtime Database
  //   onValue(employeesRef, (snapshot) => {
  //     const data = snapshot.val();
  //     if (data) {
  //       const employeesArray = Object.keys(data).map((key) => ({
  //         id: key,
  //         ...data[key], // Gán tất cả các thuộc tính của người dùng
  //       }));
  //       setEmployees(employeesArray); // Cập nhật state với mảng người dùng
  //     }
  //   });
  // }, []);

  useEffect(() => {
    const employeeRef = ref(db, "users"); // Reference to 'users' in Realtime Database
    const unsubscribe = onValue(employeeRef, (snapshot) => {
      const data = snapshot.val();
      setLoading(false); // Stop loading once data is fetched
      if (data) {
        const customerList = Object.keys(data).map((key) => ({
          id: key,
          ...data[key],
        }));
        setEmployees(customerList); // Save customer list in state
      }
    }, (error) => {
      setLoading(false); // Stop loading if there's an error
      setError("Failed to fetch customers. Please try again."); // Set error message
    });

    return () => unsubscribe(); // Clean up subscription on unmount
  }, []);

  const handleDelete = async (employeeId) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa nhân viên này?")) {
      try {
        await remove(ref(db, `users/${employeeId}`)); // Xóa nhân viên từ Realtime Database
        console.log("User deleted successfully");
      } catch (error) {
        console.error("Error deleting user:", error);
      }
    }
  };

  const handleMessageClick = (customerId) => {
    navigate(`/chats/${customerId}`); // Điều hướng đến trang chat với ID khách hàng
};

  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <div className="flex justify-end h-auto mr-0">
        <div className="flex justify-end mb-4">
          <Link
            to={"/employees/add"}
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
            <th className="p-2">Số điện thoại</th>
            <th className="p-2">Lương</th>
            <th className="p-2">Hành động</th>
          </tr>
        </thead>
        <tbody>
          {employees.filter((employee)=>employee.role!=="customer").map((employee, index) => (
            <tr key={employee.id} className="bg-indigo-100">
            <td className="p-2">{index + 1}</td>
            <td className="p-2">{employee.name}</td>
            <td className="p-2">{employee.role}</td>
            <td className="p-2">{employee.email}</td>
            <td className="p-2">{employee.phoneNumber}</td>
            <td className="p-2">
              {employee.salary
                ? employee.salary.toLocaleString("vi-VN")
                : "Chưa có lương"}
            </td>
            <td className="p-2">
              <div >
                <button onClick={() => handleMessageClick(employee.id)} className="bg-success m-2 p-2 btn-action">
                  Nhắn tin
                </button>
              </div>
              <div>
                <Link to={`/employees/update/${employee.id}`}>
                  <button className="bg-yellow-500 m-2 p-2 btn-action">
                    Chỉnh sửa
                  </button>
                </Link>
              </div>
              <div>
                <button type="button" onClick={() => handleDelete(employee.id)} className="bg-red-500 m-2 p-2 btn-action">
                  Xóa
                </button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </main>
  );
};

export default Employees;
