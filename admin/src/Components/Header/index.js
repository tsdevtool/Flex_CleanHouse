import Sidebar from "./Sidebar";

const Header = () => {
  return (
    <div className="d-flex">
      <div className="col-2">
        <Sidebar />
      </div>
      <div className="col-10">
        <Header />
        {/* <MainContent /> */}
      </div>
    </div>
  );
};

export default Header;
