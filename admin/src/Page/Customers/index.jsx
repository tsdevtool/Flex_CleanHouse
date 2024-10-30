import { onValue, ref, remove, update } from "firebase/database";
import { useEffect, useState } from "react";

import { useNavigate } from "react-router-dom";

import File from "../../Components/File";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";
import { db } from "../../firebase/config";

const Customers = () => {
  const [customers, setCustomers] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Replace useHistory with useNavigate

  useEffect(() => {
    const customersRef = ref(db, "users"); // Reference to 'users' in Realtime Database
    const unsubscribe = onValue(customersRef, (snapshot) => {
      const data = snapshot.val();
      setLoading(false); // Stop loading once data is fetched

      if (data) {
        const customerList = Object.keys(data).map((key) => ({
          id: key,
          ...data[key],
        }));

        setCustomers(customerList); // Save customer list in state
      }
    }, (error) => {
      setLoading(false); // Stop loading if there's an error
      setError("Failed to fetch customers. Please try again."); // Set error message
    });

    return () => unsubscribe(); // Clean up subscription on unmount

  }, []);

  const handleDelete = async (customerId) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa thông tin khách hàng này?")) {
      try {

        await remove(ref(db, `users/${customerId}`)); // Remove user from Realtime Database
        alert("User deleted successfully"); // Provide user feedback
      } catch (error) {
        console.error("Error deleting user:", error);
        alert("Error deleting user: " + error.message); // Show error to user

      }
    }
  };

  const handleLockToggle = async (customerId, currentLockStatus) => {

    const newLockStatus = !currentLockStatus; // Toggle 'lock' status between true and false
    try {
      await update(ref(db, `users/${customerId}`), { lock: newLockStatus });
      alert(`User ${newLockStatus ? "locked" : "unlocked"} successfully`); // User feedback
    } catch (error) {
      console.error("Error updating lock status:", error);
      alert("Error updating lock status: " + error.message); // Show error to user
    }
  };

  const handleMessageClick = (customerId) => {
    navigate(`/chats/${customerId}`); // Điều hướng đến trang chat với ID khách hàng
};


  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <File />

      {loading && <p className="text-center">Loading customers...</p>} {/* Loading state */}
      {error && <p className="text-center text-red-500">{error}</p>} {/* Error message */}

      <table className="w-full bg-white rounded shadow">
        <thead>
          <tr className="bg-indigo-900 text-white">
            <th className="p-2">ID</th>
            <th className="p-2">Tên khách hàng</th>
            <th className="p-2">Email</th>
            <th className="p-2">SĐT</th>


            <th className="p-2">Hành động</th>
          </tr>
        </thead>
        <tbody>
          {customers

            .filter((customer) => customer.role === "customer" || customer.role==="admin")
            .map(({ id, name, email, phoneNumber, lock }) => ( // Destructure for cleaner code
              <tr key={id} className="bg-indigo-100">
                <td className="p-2 truncate w-24">{id}</td>
                <td className="p-2">{name}</td>
                <td className="p-2">{email}</td>
                <td className="p-2">{phoneNumber}</td>
                <td className="p-2">
                  <div>
                    <button
                      onClick={() => handleMessageClick(id)} // Call function on button click
                      className="bg-success m-2 p-2 btn-action"
                      aria-label={`Message ${name}`} // Accessibility
                    >
                      Nhắn tin
                    </button>
                  </div>
                  <div>
                    <button
                      onClick={() => handleLockToggle(id, lock)}
                      className={`m-2 p-2 btn-action ${lock ? "bg-red-500" : "bg-yellow-500"}`}
                      aria-label={`${lock ? "Unlock" : "Lock"} ${name}`} // Accessibility
                    >
                      {lock ? "Mở khóa" : "Khóa"}
                    </button>
                  </div>
                  <div>
                    <button
                      onClick={() => handleDelete(id)}
                      className="bg-red-500 m-2 p-2 btn-action"
                      aria-label={`Delete ${name}`} // Accessibility

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
