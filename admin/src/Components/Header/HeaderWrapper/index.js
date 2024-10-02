const HeaderWrapper = () => {
  return (
    <div className="d-flex justify-content-between p-3 bg-light">
      <input type="text" className="form-control w-50" placeholder="Tìm file" />
      <div className="d-flex align-items-center">
        <span className="mr-3">3:00 AM</span>
        <span>9/26/2024</span>
      </div>
    </div>
  );
};
export default HeaderWrapper;
