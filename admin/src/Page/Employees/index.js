import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";
import Sidebar from "../../Components/Header/Sidebar";

const Employees = () => {
  <div className="flex h-screen w-screen">
    <Sidebar />
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
    </main>
  </div>;
};

export default Employees;
