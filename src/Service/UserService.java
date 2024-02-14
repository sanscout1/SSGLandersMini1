package Service;

import API.IUserService;
import DAO.UserDao;
import VO.UserVO;
import Exception.UserException.UserExceptionList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


public class UserService implements IUserService {
    UserVO myUser;
    UserDao userDAO;
    private BufferedReader reader;
    private static volatile UserService instance;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public UserService() {
        this.myUser = new UserVO();
        this.userDAO = UserDao.getInstance();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    //로그인 메인
    @Override
    public void memberLogin() {
        System.out.println("***로그인 목록입니다***");
        System.out.println("1. 로그인 | 2. 회원가입 | 3. 아이디 찾기 | 4. 비밀번호 찾기");
        System.out.print("메뉴를 선택해주세요 : ");

        try {
            int choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    login();
                    break;

                case 2:
                    approveRequest();
                    break;

                case 3:
                    findID();
                    break;

                case 4:
                    findPw();
                    break;

                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
                    memberLogin();
            }
        } catch (Exception e) {
            memberLogin();
        }
    }

    //1. 로그인
    public void login() {

        try {
            System.out.print("아이디를 입력해주세요 : ");
            String ID = reader.readLine();
            System.out.print("비밀번호를 입력해주세요 : ");
            String password = reader.readLine();
            UserExceptionList.validateUserID(ID);
            UserExceptionList.validatePassword(password);

            UserVO loggedUser = UserDao.getInstance().loginUser(ID, password);

            if (loggedUser != null && loggedUser.isApproval()) {
                System.out.println("로그인이 완료되었습니다.");

                this.myUser = loggedUser;

                if (myUser.getUserType() == 1) {
                    System.out.println("관리자로 로그인되었습니다.");
                    manageMember();
                } else if (myUser.getUserType() == 2) {
                    System.out.println("회원으로 로그인되었습니다.");
                    checkUser();
                }
            } else if (loggedUser != null && !loggedUser.isApproval()) {
                System.out.println("승인 대기 중입니다. 로그인이 제한되었습니다.");
                memberLogin();
            } else {
                UserExceptionList.invalidLogin();
                System.out.println("1. 아이디 찾기 | 2. 비밀번호 찾기");
                int subChoice = Integer.parseInt(reader.readLine());
                if (subChoice == 1) {
                    findID();
                } else if (subChoice == 2) {
                    findPw();
                }
            }
        } catch (Exception e) {
            memberLogin();
        }
    }

    //2. 회원가입
    public void approveRequest() {
        try {
            System.out.println("회원 가입을 시작합니다.");
            System.out.println("회원 정보를 입력해주세요.");

            int utype;
            while (true) {
                System.out.print("회원 유형(1. 관리자 | 2. 회원) : ");
                try {
                    utype = Integer.parseInt(reader.readLine());
                    if (utype == 1 || utype == 2) {
                        break;
                    } else {
                        System.out.println("1 또는 2 중에 선택해주세요.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("숫자를 입력해주세요.");
                }
            }
            System.out.print("아이디는 영어소문자,숫자로만 기입하되, 최소 6자 이상으로 입력해주세요 : ");
            String ID = reader.readLine();
            UserExceptionList.validateUserID(ID);

            System.out.print("비밀번호는 영어대소문자,숫자,특수문자로만 기입하되, 최소 8자 이상으로 입력해주세요 : ");
            String password = reader.readLine();
            UserExceptionList.validatePassword(password);

            System.out.print("이름 : ");
            String userName = reader.readLine();

            System.out.print("사업자 등록 번호 : ");
            String taxID = reader.readLine();

            System.out.print("거주 도시 : ");
            String addressCity = reader.readLine();

            System.out.print("거주 번지(숫자) : ");
            int addressNum = Integer.parseInt(reader.readLine());

            // 가입 승인은 관리자에게 받아햐 함
            boolean approval = false;

            // DAO를 통해 데이터베이스에 회원 정보 추가
            boolean success = userDAO.registerUser(utype, ID, password, userName, taxID, addressCity, addressNum);

            if (success) {
                System.out.println("회원 가입 요청이 완료되었습니다. 가입 승인을 기다려주세요.");
            } else {
                System.out.println("회원 가입에 실패했습니다.");
            }
            memberLogin();
        } catch (Exception e) {
            approveRequest();
        }
    }

    //3. 아이디 찾기
    public void findID() {
        try {
            System.out.println("아이디 찾기를 위한 회원 정보를 입력해주세요.");

            System.out.print("사업자 번호 : ");
            String taxID = reader.readLine();

            String foundID = UserDao.getInstance().findUserID(taxID);

            if (foundID != null) {
                System.out.println("일치하는 아이디는 " + foundID + "입니다.");
                memberLogin();
            } else {
                System.out.println("일치하는 아이디가 없습니다.");
                memberLogin();
            }
        } catch (Exception e) {
            memberLogin();
        }
    }

    //4. 비밀번호 찾기
    public void findPw() {
        try {
            System.out.println("비밀번호 찾기를 위한 회원 정보를 입력해주세요.");
            System.out.print("아이디 : ");
            String ID = reader.readLine();

            String foundPW = UserDao.getInstance().findUserPassword(ID);

            if (foundPW != null) {
                System.out.println("일치하는 비밀번호는 " + foundPW + "입니다.");
                memberLogin();
            } else {
                System.out.println("일치하는 비밀번호가 없습니다.");
                memberLogin();
            }
        } catch (Exception e) {
            memberLogin();
        }
    }

    //관리자 목록 메인
    @Override
    public void manageMember() {
        System.out.println("***관리자 목록입니다***");
        System.out.println("1. 조회 | 2. 수정 | 3. 삭제 | 4. 로그아웃");
        System.out.print("메뉴를 선택해주세요 : ");

        try {
            int choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    memberSearch();
                    break;

                case 2:
                    manageUpdate();
                    break;

                case 3:
                    memberDelete();
                    break;

                case 4:
                    logoutId();
                    break;

                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
                    manageMember();
            }
        } catch (Exception e) {
            manageMember();
        }
    }

    //1. 조회
    public void memberSearch() {
        System.out.println("1. 회원 조회 | 2. 쇼핑몰 사업자 회원 조회");
        System.out.print("조회할 메뉴를 선택해주세요: ");

        try {
            int searchChoice = Integer.parseInt(reader.readLine());

            switch (searchChoice) {
                case 1:
                    System.out.println("1. 회원 상세보기 조회 | 2. 회원 전체 조회 | 3. 승인 대기자 조회");
                    System.out.print("서브 메뉴를 선택해주세요 : ");
                    int subChoice = Integer.parseInt(reader.readLine());

                    switch (subChoice) {
                        case 1:
                            memberDetailSearch();
                            break;

                        case 2:
                            memberList();
                            break;

                        case 3:
                            pandingList();
                            break;

                        default:
                            System.out.println("잘못된 서브 메뉴 선택입니다.");
                            memberSearch();
                    }
                    break;

                case 2:
                    taxIdSearch();
                    break;

                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
                    memberSearch();
            }
        } catch (Exception e) {
            memberSearch();
        }
    }

    // 사용자 정보 출력 메소드
    private void displayUserInfo(UserVO userVO) {
        System.out.println("UserID: " + userVO.getUserID());
        System.out.println("UserType: " + userVO.getUserType());
        System.out.println("ID: " + userVO.getID());
        System.out.println("UserName: " + userVO.getUserName());
        System.out.println("TaxID: " + userVO.getTaxID());
        System.out.println("AddressCity: " + userVO.getAddressCity());
        System.out.println("AddressNum: " + userVO.getAddressNum());
        System.out.println("Approval: " + userVO.isApproval());
    }

    //1-1-1 회원 상세보기 조회
    public void memberDetailSearch() {
        try {
            System.out.print("조회할 회원의 ID를 입력해주세요: ");
            String searchID = reader.readLine();
            System.out.println("================================================================");

            UserVO userVO = userDAO.getUserId(searchID);
            if (userVO != null) {
                displayUserInfo(userVO);
                System.out.println("================================================================");
            } else {
                System.out.println("일치하는 회원이 없습니다.");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            manageMember();
        }
    }

    //1-1-2 회원 전체 조회
    public void memberList() {
        List<UserVO> userVOList = userDAO.userRead();
        System.out.println("================================================================");
        for (UserVO users : userVOList) {
            displayUserInfo(users);
            System.out.println("================================================================");
        }
        manageMember();
    }

    //1-1-3 승인 대기자 조회
    public void pandingList() {
        try {
            List<UserVO> pendingUserList = userDAO.userRead();

            boolean hasPendingUser = false;

            for (UserVO user : pendingUserList) {
                if (!user.isApproval()) {
                    hasPendingUser = true;
                    break; // 승인 대기자가 하나라도 있으면 반복 중단
                }
            }

            if (hasPendingUser) {
                System.out.println("================================================================");
                for (UserVO users : pendingUserList) {
                    if (!users.isApproval()) {
                        displayUserInfo(users);
                        System.out.println("================================================================");
                    }
                }
            } else {
                System.out.println("승인 대기자가 없습니다.");
            }
        } catch (Exception e) {
            System.out.println("승인 대기자 목록을 가져오는 도중 오류가 발생했습니다.");
            e.printStackTrace();
        } finally {
            manageMember();
        }
    }

    //1-2 쇼핑몰 사업자 회원 조회
    public void taxIdSearch() {
        System.out.print("조회할 쇼핑몰 사업자의 TaxID를 입력해주세요: ");
        try {
            String taxID = reader.readLine();
            System.out.println("================================================================");

            List<UserVO> taxIdList = userDAO.userRead().stream()
                    .filter(userVO -> userVO.getTaxID().equals(taxID))
                    .toList();

            for (UserVO users : taxIdList) {
                displayUserInfo(users);
                System.out.println("================================================================");
            }
            manageMember();
        } catch (Exception e) {
            manageMember();
        }
    }

    //2. 수정
    private void manageUpdate() {
        System.out.println("1. 회원 정보 수정 | 2. 쇼핑몰 사업자 정보 수정");
        System.out.print("수정할 메뉴를 선택해주세요: ");

        try {
            int updateChoice = Integer.parseInt(reader.readLine());
            ;

            switch (updateChoice) {
                case 1:
                    memberUpdate();
                    break;

                case 2:
                    taxIdUpdate();
                    break;

                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
                    manageUpdate();
            }
        } catch (Exception e) {
            manageUpdate();
        }
    }

    //2-1 회원 정보 수정
    public void memberUpdate() {
        try {
            System.out.print("수정이 필요한 아이디를 입력해주세요: ");
            String ID = reader.readLine();

            System.out.println("수정할 회원의 정보를 입력해주세요.(1. 관리자 | 2. 회원)");
            System.out.print("회원 유형 : ");
            int utype = Integer.parseInt(reader.readLine());

            System.out.print("비밀번호 : ");
            String password = reader.readLine();

            System.out.print("이름 : ");
            String userName = reader.readLine();

            System.out.print("거주 도시 : ");
            String addressCity = reader.readLine();

            System.out.print("거주 번지 : ");
            int addressNum = Integer.parseInt(reader.readLine());

            System.out.print("승인여부 : ");
            boolean approval = Boolean.parseBoolean(reader.readLine());

            boolean success = userDAO.updateUser(ID, utype, password, userName, addressCity, addressNum, approval);

            if (success) {
                System.out.println("회원정보가 수정되었습니다.");
            } else {
                System.out.println("회원정보 수정에 실패하였습니다.");
            }
        } catch (Exception e) {
            manageMember();
        } finally {
            manageMember();
        }
    }

    //2-2 쇼핑몰 사업자 정보 수정
    public void taxIdUpdate() {
        try {
            System.out.print("쇼핑몰 사업자 정보 수정이 필요한 아이디를 입력해주세요 :");
            String ID = reader.readLine();

            System.out.print("수정할 사업자 등록 번호를 입력해주세요 : ");
            String taxID = reader.readLine();

            boolean success = userDAO.taxIdUpdate(ID, taxID);

            if (success) {
                System.out.println("쇼핑몰 사업자 정보가 수정되었습니다.");
            } else {
                System.out.println("쇼핑몰 사업자 정보 수정에 실패하였습니다.");
            }
            manageMember();
        } catch (Exception e) {
            manageMember();
        }
    }

    //3. 삭제
    public void memberDelete() {
        try {
            System.out.print("삭제할 회원의 아이디를 입력해주세요 : ");
            String ID = reader.readLine();

            boolean success = userDAO.memberDelete(ID);

            if (success) {
                System.out.println("회원 삭제가 완료되었습니다.");
            } else {
                System.out.println("회원 삭제에 실패하였습니다.");
            }
        } catch (Exception e) {
            manageMember();
        } finally {
            manageMember();
        }
    }

    //4. 로그아웃
    public void logoutId() {
        this.myUser = null;
        System.out.println("로그아웃 되었습니다.");
    }

    // 회원 목록 메인
    @Override
    public void checkUser() {
        System.out.println("***회원 목록입니다***");
        System.out.println("1. 내 정보 조회 | 2. 내 정보 수정 | 3. 탈퇴 | 4. 로그아웃");
        System.out.print("메뉴를 선택해주세요 : ");

        try {
            int choice = Integer.parseInt(reader.readLine());


            switch (choice) {
                case 1:
                    shopUserList();
                    break;

                case 2:
                    userUpdate();
                    break;

                case 3:
                    userDelete();
                    break;

                case 4:
                    logoutId();
                    break;

                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
                    checkUser();
            }
        } catch (Exception e) {
            checkUser();
        }
    }

    //1. 내 정보 조회
    public void shopUserList() {
        System.out.println("================================================================");
        displayUserInfo(userDAO.getUserId(myUser.getID()));
        System.out.println("================================================================");
        checkUser();
    }

    //2. 내 정보 수정
    public void userUpdate() {
        System.out.println("1. 회원 정보 수정 | 2. 쇼핑몰 사업자 정보 수정");
        System.out.print("수정할 메뉴를 선택해주세요 : ");

        try {
            int updateChoice = Integer.parseInt(reader.readLine());

            switch (updateChoice) {
                case 1:
                    myInfoUpdate();
                    break;

                case 2:
                    mytaxIdUpdate();
                    break;

                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
                    manageUpdate();
            }
        } catch (Exception e) {
            manageUpdate();
        }
    }

    //2-1 회원 정보 수정
    public void myInfoUpdate() {
        try {
            System.out.println("회원 정보 수정을 시작합니다.");
            System.out.print("아이디를 입력해주세요 : ");
            String ID = reader.readLine();

            System.out.println("수정 할 정보를 입력해주세요.");
            System.out.print("비밀번호 : ");
            String password = reader.readLine();

            System.out.print("이름 : ");
            String userName = reader.readLine();

            System.out.print("거주 도시 : ");
            String addressCity = reader.readLine();

            System.out.print("거주 번지 : ");
            int addressNum = Integer.parseInt(reader.readLine());

            boolean success = userDAO.myInfoUpdate(ID, password, userName, addressCity, addressNum);

            if (success) {
                System.out.println("회원정보가 수정되었습니다.");
            } else {
                System.out.println("회원정보 수정에 실패하였습니다.");
            }
            checkUser();
        } catch (Exception e) {
            checkUser();
        }
    }

    //2-2 내 사업자 등록 번호 수정
    public void mytaxIdUpdate() {
        try {
            System.out.println("쇼핑몰 사업자 정보 수정을 시작합니다.");
            System.out.print("비밀번호를 입력해주세요 : ");
            String password = reader.readLine();

            System.out.print("수정할 사업자 등록 번호를 입력해주세요 : ");
            String taxID = reader.readLine();

            boolean success = userDAO.mytaxIdUpdate(password, taxID);

            if (success) {
                System.out.println("쇼핑몰 사업자 정보가 수정되었습니다.");
            } else {
                System.out.println("쇼핑몰 사업자 정보 수정에 실패하였습니다.");
            }
            checkUser();
        } catch (Exception e) {
            checkUser();
        }
    }

    //3. 회원 탈퇴
    public void userDelete() {
        try {
            System.out.println("회원 탈퇴를 시작합니다.");
            System.out.print("비밀번호를 입력해주세요 : ");
            String password = reader.readLine();
            boolean success = userDAO.userDelete(password);

            if (success) {
                System.out.println("회원 탈퇴가 완료되었습니다.");
            } else {
                System.out.println("회원 탈퇴에 실패하였습니다.");
            }
            logoutId();
        } catch (Exception e) {
            logoutId();
        }
    }
}