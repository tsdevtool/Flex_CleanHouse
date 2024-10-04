import { CiSearch } from "react-icons/ci";
import { FaRegClock } from "react-icons/fa6";

const HeaderWrapper = () => {
  const now = new Date();
  const time = now.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  });
  const date = now.toLocaleDateString();

  return (
    <div className="headerWrapper flex justify-between items-center mb-3 flex-wrap">
      <div className="search p-2 border rounded w-full md:w-3/4 mb-2 md:mb-0">
        <CiSearch />
        <input type="text" placeholder="Tìm file" />
      </div>
      <div className="flex items-center">
        <div className="timeNow ">
          <div className="text-right mr-4">
            <p>{time}</p>
            <p>{date}</p>
          </div>
          <FaRegClock />
        </div>

        <div className="user w-12 h-12 bg-gray-300 rounded-full flex items-center justify-center">
          <img
            className="fas fa-user w-12 h-12 rounded-full"
            src="https://scontent.fsgn2-10.fna.fbcdn.net/v/t39.30808-1/449789952_1149098856148205_3885276472304786082_n.jpg?stp=cp6_dst-jpg_s200x200&_nc_cat=109&ccb=1-7&_nc_sid=0ecb9b&_nc_ohc=f7vI1TBTz3AQ7kNvgGWA1-x&_nc_ht=scontent.fsgn2-10.fna&_nc_gid=Abm-bg-61i4Q-vnw4JftP9v&oh=00_AYDgWXROLHyivihiJF0Gm2uCyiU2EXkse_5Yy1K4efecHA&oe=6705537B"
          />
        </div>
      </div>
    </div>
  );
};
export default HeaderWrapper;
