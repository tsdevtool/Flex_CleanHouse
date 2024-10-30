import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  Title,
  Tooltip,
} from "chart.js";
import { getDatabase, onValue, ref } from "firebase/database";
import { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import { BsCalendar2DayFill, BsCalendar2MonthFill } from "react-icons/bs";
import { MdCalendarMonth } from "react-icons/md";
import "../../../App.css";
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
);

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

  // Chuẩn bị dữ liệu cho biểu đồ
  const prepareChartData = () => {
    const monthProfit = {};

    paymentData.forEach((item) => {
      const date = new Date(item.payDay);
      const month = date.toLocaleString("default", { month: "long" });

      if (!monthProfit[month]) {
        monthProfit[month] = 0;
      }
      monthProfit[month] += item.profit; // Cộng dồn lợi nhuận theo tháng
    });

    return {
      labels: Object.keys(monthProfit), // Tháng
      datasets: [
        {
          label: "Lợi nhuận",
          data: Object.values(monthProfit), // Lợi nhuận
          backgroundColor: "rgba(75, 192, 192, 0.6)",
        },
      ],
    };
  };

  // Tính toán thu nhập cho hôm nay
  const todayRevenue = paymentData.reduce((acc, curr) => {
    const today = new Date().toISOString().split("T")[0]; // Ngày hôm nay
    return acc + (curr.payDay === today ? curr.totalRevenue : 0);
  }, 0);

  // Tính toán thu nhập cho tháng này
  const currentMonthRevenue = paymentData.reduce((acc, curr) => {
    const currentMonth = new Date().getMonth(); // Tháng hiện tại
    const paymentDate = new Date(curr.payDay);
    return (
      acc + (paymentDate.getMonth() === currentMonth ? curr.totalRevenue : 0)
    );
  }, 0);

  // Tính toán thu nhập cho năm này
  const currentYearRevenue = paymentData.reduce((acc, curr) => {
    const currentYear = new Date().getFullYear(); // Năm hiện tại
    const paymentDate = new Date(curr.payDay);
    return (
      acc + (paymentDate.getFullYear() === currentYear ? curr.totalRevenue : 0)
    );
  }, 0);

  return (
    <div className="flex flex-col items-center justify-center w-full">
      {/* Biểu đồ cột lợi nhuận theo tháng */}
      <div className="mb-6 w-full flex justify-center">
        <div className="h-64 w-full bg-blue-100 rounded flex justify-center items-center">
          <Bar data={prepareChartData()} />
        </div>
      </div>

      {/* Thông tin thu nhập */}
      <div className="flex w-full gap-4 mb-4">
        {/* Thu nhập hôm nay */}
        <div className="flex-grow h-24 bg-pink-100 rounded flex items-center justify-center">
          <i className="mr-4 text-5xl">
            <BsCalendar2DayFill />
          </i>
          <div className="d-flex flex-column justify-content-center align-items-center">
            <p className="text-xl font-bold uppercase">Thu nhập hôm nay</p>
            <p className="text-xl font-bold align-items-center">
              {todayRevenue.toLocaleString()} VNĐ
            </p>
          </div>
        </div>
        {/* Thu nhập tháng này */}
        <div className="flex-grow h-24 bg-purple-100 rounded flex items-center justify-center">
          <i className="mr-4 text-5xl">
            <BsCalendar2MonthFill />
          </i>
          <div className="d-flex flex-column justify-content-center align-items-center">
            <p className="text-xl font-bold uppercase">Thu nhập tháng này</p>
            <p className="text-xl font-bold align-items-center">
              {currentMonthRevenue.toLocaleString()} VNĐ
            </p>
          </div>
        </div>
        {/* Thu nhập năm nay */}
        <div className="flex-grow h-24 bg-pink-200 rounded flex items-center justify-center">
          <i className="mr-4 text-5xl">
            <MdCalendarMonth />
          </i>
          <div className="d-flex flex-column justify-content-center align-items-center">
            <p className="text-xl font-bold uppercase"> Thu nhập năm nay</p>
            <p className="text-xl font-bold align-items-center">
              {currentYearRevenue.toLocaleString()} VNĐ
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BasicInformation;
