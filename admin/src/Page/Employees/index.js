import BasicInformation from "../../Components/Header/BasicInformation";
import HeaderWrapper from "../../Components/Header/HeaderWrapper";

const Employees = () => {
  return (
    <main className="flex-1 p-6 bg-gray-100">
      <HeaderWrapper />
      <BasicInformation />
    </main>
  );
};

export default Employees;
