import { FaFacebook, FaGithub, FaTelegram } from "react-icons/fa";
import { Link } from "react-router-dom";
import Duc from "../../assets/images/duc.jpg";
import Logo from "../../assets/images/logo-rebg.png";
import Mui from "../../assets/images/mui.jpg";
import Sieu from "../../assets/images/sieu.jpg";
import Trang from "../../assets/images/trang.jpg";

const Other = () => {
  return (
    <div>
      <header class="flex items-center justify-between p-4 bg-white shadow-md">
        <div class="flex items-center">
          <img src={Logo} alt="Happy House Logo" class="h-auto w-14 mr-3" />
          <h1 class="text-xl font-bold">Happy Homes</h1>
        </div>
      </header>
      <main class="p-8">
        <section class="text-center mb-12">
          <h2 class="text-3xl font-bold mb-2  text-purple-500">
            Đồ án môn học tự chọn:
          </h2>
          <h2 class="text-3xl font-bold mb-2  text-purple-600 uppercase">
            Công cụ và môi trường phát triển phần mềm
          </h2>

          <p class="text-gray-600">
            Cảm ơn Thầy đã xem đồ án của chúng em. Trong quá trình thực hiện còn
            nhiều hạn chế về kiến thức cũng như kỹ năng thực tế chưa có mong
            Thầy xem xét bỏ qua. Chúc Thầy nhiều sức khỏe!
          </p>
        </section>

        <section class="grid grid-cols-4 gap-8 mb-12">
          <div class="text-center">
            <img
              src={Mui}
              alt="Ngô Xuân Mùi"
              class="w-full h-[250px] rounded-lg mb-4 object-cover w-full h-full"
            />
            <h3 class="text-xl font-bold">Ngô Xuân Mùi</h3>
            <p class="text-gray-600">UI / Developer</p>
            <p class="text-gray-500">ngomuimax@gmail.com</p>
          </div>
          <div class="text-center">
            <img
              src={Sieu}
              alt="Nguyễn Thanh Siêu"
              class="w-full h-[250px] rounded-lg mb-4 object-cover w-full h-full"
            />
            <h3 class="text-xl font-bold">Nguyễn Thanh Siêu</h3>
            <p class="text-gray-600">Project Manager / Website Developer</p>
            <p class="text-gray-500">sieusml03@gmail.com</p>
          </div>
          <div class="text-center">
            <img
              src={Trang}
              alt="Đặng Lê Ngọc Trạng"
              class="w-full h-[250px] rounded-lg mb-4 object-cover w-full h-full"
            />
            <h3 class="text-xl font-bold">Đặng Lê Ngọc Trạng</h3>
            <p class="text-gray-600">Mobile Developer</p>
            <p class="text-gray-500">dlntranga3@gmail.com</p>
          </div>
          <div class="text-center">
            <img
              src={Duc}
              alt="Trần Việt Đức"
              class="w-full h-[250px] rounded-lg mb-4 object-cover w-full h-full"
            />
            <h3 class="text-xl font-bold">Trần Việt Đức</h3>
            <p class="text-gray-600">Mobile Developer</p>
            <p class="text-gray-500">tranvietduc85vip1@gmail.com</p>
          </div>
        </section>
        <section class="text-center mb-12">
          <h2 class="text-3xl font-bold text-purple-600 mb-4">
            Đến Repositories của dự án này
          </h2>
          <p class="text-gray-600 mb-4">
            Cảm ơn vì đã xem demo project của chúng tôi! Click vào để có thể xem
            source của chúng tôi nhé!
          </p>
          <Link
            to={"https://github.com/tsdevtool/Flex_CleanHouse.git"}
            class="bg-purple-600 text-white px-6 py-2 rounded-full underline-offset-0"
          >
            Meet the Team
          </Link>
        </section>
      </main>
      <footer class="bg-gray-100 py-8">
        <div class="text-center text-gray-600 mb-4">
          <p>&copy; 2024 Flex, Inc. </p>
        </div>
        <div class="flex justify-center space-x-4 text-gray-600">
          <Link to={"https://github.com/tsdevtool/Flex_CleanHouse.git"}>
            <FaGithub />
          </Link>
          <Link to={"https://www.facebook.com/thannsiiu/"}>
            <FaFacebook />{" "}
          </Link>
          <Link to={"https://t.me/tsdevtool"}>
            <FaTelegram />
          </Link>
        </div>
        <div class="text-center mt-4">
          <button class="border border-gray-300 px-4 py-2 rounded-full">
            Việt Nam
          </button>
        </div>
      </footer>
      <div class="text-center py-4 bg-gray-200">
        <p class="text-gray-600">
          Made with <i class="fas fa-heart text-red-500"></i> by FlexTeam
        </p>
      </div>
    </div>
  );
};
export default Other;
