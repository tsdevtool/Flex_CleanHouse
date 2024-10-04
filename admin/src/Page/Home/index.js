import HeaderWrapper from "../../Components/Header/HeaderWrapper";
import Sidebar from "../../Components/Header/Sidebar";

const Home = () => {
  return (
    <div className="flex h-screen w-screen">
      <Sidebar />
      <main className="flex-1 p-6 bg-gray-100">
        <HeaderWrapper />
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-6">
          <div className="h-24 bg-blue-100 rounded"></div>
          <div className="h-24 bg-pink-100 rounded"></div>
          <div className="h-24 bg-purple-100 rounded"></div>
          <div className="h-24 bg-pink-200 rounded"></div>
        </div>
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
    </div>
  );
};

export default Home;
