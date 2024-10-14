import { getDatabase, onValue, ref } from "firebase/database";
import { useEffect, useState } from "react";

const BasicInformation = () => {
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
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-6">
      <div className="h-24 bg-blue-100 rounded"></div>
      <div className="h-24 bg-pink-100 rounded"></div>
      <div className="h-24 bg-purple-100 rounded"></div>
      <div className="h-24 bg-pink-200 rounded"></div>
    </div>
  );
};

export default BasicInformation;
