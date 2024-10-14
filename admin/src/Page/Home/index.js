import { getDatabase, onValue, ref } from "firebase/database"; // Import các hàm cần thiết
import React, { useEffect, useState } from "react";
import { BsFillCalendar2DateFill } from "react-icons/bs";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";
import { FaToolbox } from "react-icons/fa";
import { MdPriceChange } from "react-icons/md";
import { GiReceiveMoney } from "react-icons/gi";
import { GiTakeMyMoney } from "react-icons/gi";
const Home = () => {
  const [paymentData, setPaymentData] = useState([]); // State để lưu dữ liệu thanh toán
  const db = getDatabase(); // Khởi tạo database

  // Function để load dữ liệu từ Firebase
  useEffect(() => {
    const fetchData = () => {
      const paymentsRef = ref(db, "jobs/payments"); // Tham chiếu đến bảng payments
      const servicesRef = ref(db, "services"); // Tham chiếu đến bảng services

      // Lấy dữ liệu từ payments và services
      onValue(paymentsRef, (paymentsSnapshot) => {
        const payments = paymentsSnapshot.val() || {}; // Lấy dữ liệu thanh toán
        onValue(servicesRef, (servicesSnapshot) => {
          const services = servicesSnapshot.val() || {}; // Lấy dữ liệu dịch vụ

          // Danh sách kết quả để lưu trữ thông tin
          const results = [];

          // Duyệt qua các payment và so sánh với service
          Object.keys(payments).forEach((paymentKey) => {
            const payment = payments[paymentKey];
            const serviceId = payment.serviceId; // Lấy serviceId từ payment

            // Tìm kiếm service dựa vào serviceId
            const service = Object.values(services).find(
              (s) => s.serviceId === serviceId
            );

            if (service) {
              // Nếu tìm thấy service, tính toán và lưu vào results
              const serviceCost = service.serviceCost; // Lấy chi phí dịch vụ
              const toolCost = serviceCost * 0.04; // Dụng cụ là 4% thu vào
              const totalCost = toolCost + 0; // Chi phí bằng dụng cụ + 0
              const profit = serviceCost - totalCost; // Lợi nhuận

              // Lưu thông tin vào kết quả với ngày
              results.push({
                payDay: payment.payDay.split(" ")[0], // Lấy chỉ phần ngày
                employee: 0, // Nhân viên để trống
                totalRevenue: serviceCost,
                toolCost: toolCost,
                totalCost: totalCost,
                profit: profit,
              });
            }
          });

          // Gom nhóm dữ liệu theo ngày
          const groupedResults = results.reduce((acc, curr) => {
            const { payDay, ...rest } = curr;
            if (!acc[payDay]) {
              acc[payDay] = {
                payDay,
                totalRevenue: 0,
                toolCost: 0,
                totalCost: 0,
                profit: 0,
              };
            }
            acc[payDay].totalRevenue += rest.totalRevenue;
            acc[payDay].toolCost += rest.toolCost;
            acc[payDay].totalCost += rest.totalCost;
            acc[payDay].profit += rest.profit;
            return acc;
          }, {});

          // Chuyển đổi lại thành mảng
          setPaymentData(Object.values(groupedResults));
        });
      });
    };

    fetchData(); // Gọi hàm để fetch dữ liệu
  }, [db]);

  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <div className="overflow-x-auto">
        <table className="w-full bg-white rounded shadow">
          <thead>
            <tr className="bg-indigo-900 text-white">
              <th className="p-2 ">
                <div className="flex">
                  <i className="mt-0.5 mr-1.5">
                    <BsFillCalendar2DateFill />
                  </i>
                  Ngày
                </div>
              </th>
              {/* <th className="p-2">Nhân viên</th> */}
              <th className="p-2">
                <div className="flex">
                  <i className="mt-0.5 mr-1.5">
                    <FaToolbox />
                  </i>
                  Dụng cụ
                </div>
              </th>
              <th className="p-2">
                <div className="flex">
                  <i className="mt-0.5 mr-1.5">
                    <MdPriceChange />
                  </i>
                  Chi phí
                </div>
              </th>
              <th className="p-2 ">
                <div className="flex">
                  <i className="mt-0.5 mr-1.5">
                    <GiReceiveMoney />
                  </i>
                  Thu vào
                </div>
              </th>
              <th className="p-2 flex">
                <i className="mt-0.5 mr-1.5">
                  <GiTakeMyMoney />
                </i>
                Lợi nhuận
              </th>
            </tr>
          </thead>
          <tbody>
            {paymentData.length > 0 ? (
              paymentData.map((payment, index) => (
                <tr key={index} className="odd:bg-gray-100 even:bg-gray-200">
                  <td className="p-2">{payment.payDay}</td>
                  {/* <td className="p-2">{payment.employee}</td> */}
                  <td className="p-2">{payment.toolCost.toFixed(2)} VND</td>
                  <td className="p-2">{payment.totalCost.toFixed(2)} VND</td>
                  <td className="p-2">{payment.totalRevenue.toFixed(2)} VND</td>
                  <td className="p-2">{payment.profit.toFixed(2)} VND</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="text-center p-2">
                  Không có dữ liệu
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </main>
  );
};

export default Home;
