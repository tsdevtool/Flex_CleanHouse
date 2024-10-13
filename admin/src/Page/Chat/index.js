const Chat = () => {
  return (
    <div className="flex h-screen">
      <div className="w-1/3 border-r border-gray-300">
        <div className="flex items-center justify-between p-4 border-b border-gray-300">
          <h2 className="text-lg font-semibold">Username</h2>
          <button className="p-2 rounded-full hover:bg-gray-200">
            <i className="fas fa-pen"></i>
          </button>
        </div>
        <div className="overflow-y-auto h-full">
          {Array(8)
            .fill()
            .map((_, i) => (
              <div
                key={i}
                className="flex items-center p-4 hover:bg-gray-100 cursor-pointer"
              >
                <img
                  src="https://placehold.co/50x50"
                  alt="User avatar"
                  className="w-12 h-12 rounded-full mr-4"
                />
                <div className="flex-1">
                  <h3 className="text-sm font-semibold">Username</h3>
                  <p className="text-xs text-gray-500 truncate">
                    Xin chào mình có vấn đề liên hệ....
                  </p>
                </div>
                <span className="text-xs text-gray-500">T5</span>
              </div>
            ))}
        </div>
      </div>
      <div className="w-2/3 flex flex-col">
        <div className="flex items-center justify-between p-4 border-b border-gray-300">
          <div className="flex items-center">
            <img
              src="https://placehold.co/50x50"
              alt="User avatar"
              className="w-12 h-12 rounded-full mr-4"
            />
            <h2 className="text-lg font-semibold">Username</h2>
          </div>
          <div className="relative">
            <button className="p-2 rounded-full hover:bg-gray-200">
              <i className="fas fa-ellipsis-h"></i>
            </button>
            <div className="absolute right-0 mt-2 w-48 bg-white border border-gray-300 rounded shadow-lg">
              <button className="flex items-center p-2 w-full hover:bg-gray-100">
                <i className="fas fa-trash-alt mr-2"></i> Xoá trò chuyện
              </button>
            </div>
          </div>
        </div>
        <div className="flex-1 overflow-y-auto p-4">
          <div className="flex items-start mb-4">
            <img
              src="https://placehold.co/50x50"
              alt="User avatar"
              className="w-10 h-10 rounded-full mr-4"
            />
            <div className="bg-gray-200 p-3 rounded-lg">
              <p>Sếp chưa trả lương cho em</p>
              <span className="text-xs text-gray-500">21:00 T5</span>
            </div>
          </div>
          <div className="flex items-start justify-end mb-4">
            <div className="bg-blue-100 p-3 rounded-lg">
              <p>Nhép nhura nhà nhương nho nhem</p>
              <span className="text-xs text-gray-500">22:00 T5</span>
            </div>
            <img
              src="https://placehold.co/50x50"
              alt="User avatar"
              className="w-10 h-10 rounded-full ml-4"
            />
          </div>
        </div>
        <div className="p-4 border-t border-gray-300 flex items-center">
          <input
            type="text"
            placeholder="Tin nhắn mới"
            className="flex-1 p-2 border border-gray-300 rounded-lg mr-4"
          />
          <button className="p-2 bg-blue-500 text-white rounded-lg">gửi</button>
        </div>
      </div>
    </div>
  );
};

export default Chat;
