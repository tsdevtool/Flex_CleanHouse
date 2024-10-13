import { onValue, ref, remove, update } from "firebase/database";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import File from "../../Components/File";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";
import { db } from "../../firebase/config";
const Customers = () => {
  const [customers, setCustomers] = useState([]);
  const navigate = useNavigate(); // Thay thế useHistory bằng useNavigate


  useEffect(() => {
    const customersRef = ref(db, "users"); // Tham chiếu đến 'customers' trong Realtime Database
    onValue(customersRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        const customerList = Object.keys(data).map((key) => ({
          id: key,
          ...data[key],
        }));
        setCustomers(customerList); // Lưu danh sách khách hàng vào state
      }
    });
  }, []);

  const handleDelete = async (customerId) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa thông tin khách hàng này?")) {
      try {
        await remove(ref(db, `users/${customerId}`)); // Xóa nhân viên từ Realtime Database
        console.log("User deleted successfully");
      } catch (error) {
        console.error("Error deleting user:", error);
      }
    }
  };

  const handleLockToggle = async (customerId, currentLockStatus) => {
    const newLockStatus = !currentLockStatus; // Chuyển trạng thái 'lock' giữa true và false
    try {
      await update(ref(db, `users/${customerId}`), { lock: newLockStatus });
      console.log(`User ${newLockStatus ? "locked" : "unlocked"} successfully`);
    } catch (error) {
      console.error("Error updating lock status:", error);
    }
  };
  const handleMessageClick = (customerId) => {
    // Điều hướng đến trang chat với ID của khách hàng
    navigate(`/chats/${customerId}`);
  };
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
            <th className="p-2">SĐT</th>
            {/* <th className="p-2">Địa chỉ</th> */}
            <th className="p-2">Hành động</th>
          </tr>
        </thead>
        <tbody>
          {customers
            .filter((customer) => customer.role === "customer")
            .map((customer) => (
              <tr key={customer.id} className="bg-indigo-100">
                <td className="p-2 truncate w-24">{customer.id}</td>
                <td className="p-2">{customer.name}</td>
                <td className="p-2">{customer.email}</td>
                <td className="p-2">{customer.phoneNumber}</td>
                {/* <td className="p-2">{customer.address}</td> */}
                <td className="p-2">
                  <div>
                  <button
                    onClick={() => handleMessageClick(customer.id)} // Gọi hàm khi nhấn nút nhắn tin
                    className="bg-success m-2 p-2 btn-action"
                  >
                    Nhắn tin
                  </button>
                  </div>
                  <div>
                  <button
                      onClick={() => handleLockToggle(customer.id, customer.lock)}
                      className={`m-2 p-2 btn-action ${
                        customer.lock ? "bg-red-500" : "bg-yellow-500"
                      }`}
                    >
                      {customer.lock ? "Mở khóa" : "Khóa"}
                    </button>
                </div>
                  <div>
                    <button
                      onClick={()=>handleDelete(customer.id)}
                      className="bg-red-500 m-2 p-2 btn-action"
                    >
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

export default Customers;
