package API;

import VO.UserVO;

public interface IUserService {

    public UserVO memberLogin();
    public void manageMember();
    public void checkUser();
    public void logoutId();
}
