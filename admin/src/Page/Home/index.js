import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";

const Home = () => {
  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <div className="overflow-x-auto">
        <table className="w-full bg-white rounded shadow">
          <thead>
            <tr className="bg-indigo-900 text-white">
              <th className="p-2">Ngày</th>
              <th className="p-2">Nhân viên</th>
              <th className="p-2">Dụng cụ</th>
              <th className="p-2">Chi phí</th>
              <th className="p-2">Thu vào</th>
              <th className="p-2">Lợi nhuận</th>
            </tr>
          </thead>
          <tbody>
            {Array.from({ length: 10 }).map((_, index) => (
              <tr key={index} className="odd:bg-gray-100 even:bg-gray-200">
                <td className="p-2">Ngày</td>
                <td className="p-2">Nhân viên</td>
                <td className="p-2">Dụng cụ</td>
                <td className="p-2">Chi phí</td>
                <td className="p-2">Thu vào</td>
                <td className="p-2">Lợi nhuận</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </main>
  );
};

export default Home;
