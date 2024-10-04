import File from "../../Components/File";
import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";

const Employees = () => {
  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
      <File />
      <table className="w-full bg-white rounded shadow">
        <thead>
          <tr className="bg-indigo-900 text-white">
            <th className="p-2">ID</th>
            <th className="p-2">Nhân viên</th>
            <th className="p-2">Email</th>
            <th className="p-2">Password</th>
            <th className="p-2">Thu vào</th>
          </tr>
        </thead>
        <tbody>
          <tr className="bg-indigo-100">
            <td className="p-2">1</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">Email</td>
            <td className="p-2">Password</td>
            <td className="p-2">Thu vào</td>
          </tr>
          <tr>
            <td className="p-2">2</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">Email</td>
            <td className="p-2">Password</td>
            <td className="p-2">Thu vào</td>
          </tr>
          <tr className="bg-indigo-100">
            <td className="p-2">3</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">Email</td>
            <td className="p-2">Password</td>
            <td className="p-2">Thu vào</td>
          </tr>
          <tr>
            <td className="p-2">4</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">Email</td>
            <td className="p-2">Password</td>
            <td className="p-2">Thu vào</td>
          </tr>
          <tr className="bg-indigo-100">
            <td className="p-2">5</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">Email</td>
            <td className="p-2">Password</td>
            <td className="p-2">Thu vào</td>
          </tr>
          <tr>
            <td className="p-2">6</td>
            <td className="p-2">Nhân viên</td>
            <td className="p-2">Email</td>
            <td className="p-2">Password</td>
            <td className="p-2">Thu vào</td>
          </tr>
        </tbody>
      </table>
    </main>
  );
};

export default Employees;
