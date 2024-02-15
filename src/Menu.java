import API.*;
import Service.*;
import VO.UserVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Menu {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    IUserService userService = UserService.getInstance();
    IFinanceService financeService = FinanceService.getInstance();
    IWarehouseService warehouseService = WarehouseService.getInstance();
    IStockService stockService = StockService.getInstance();

    IReceiptService receiptService = new ReceiptService();
    IReleaseService releaseService = new ReleaseService();


    public void mainMenu() {
        while (true) {
            System.out.println("\u001B[34;1m");
            System.out.println("  ███████ ███████  ██████  ██       █████  ███    ██ ██████  ███████ ██████  ███████ ██████  ███████ ████████  █████  ██ ██       ");
            System.out.println("  ██      ██      ██       ██      ██   ██ ████   ██ ██   ██ ██      ██   ██ ██      ██   ██ ██         ██    ██   ██ ██ ██           ");
            System.out.println("  ███████ ███████ ██   ███ ██      ███████ ██ ██  ██ ██   ██ █████   ██████  ███████ ██████  █████      ██    ███████ ██ ██   ");
            System.out.println("       ██      ██ ██    ██ ██      ██   ██ ██  ██ ██ ██   ██ ██      ██   ██      ██ ██   ██ ██         ██    ██   ██ ██ ██ ");
            System.out.println("  ███████ ███████  ██████  ███████ ██   ██ ██   ████ ██████  ███████ ██   ██ ███████ ██   ██ ███████    ██    ██   ██ ██ ███████  ");
            UserVO userVO = userService.memberLogin();
            try {
                switch (userVO.getUserType()) {
                    case 1 -> {
                        menuAdminList(userVO);
                    }
                    case 2 -> {
                        menuUserList(userVO);
                    }
                }
            } catch (Exception e) {}
        }
    }


    public void menuAdminList(UserVO user) {
        try {
            System.out.println("****** 기능 선택 ******");
            System.out.println("1.회원 관리 | 2. 재무 관리 | 3. 창고 관리 | 4. 재고 관리 | 5. 입고 관리 | 6. 출고 관리 | 7. 로그 아웃");
            switch (Integer.parseInt(reader.readLine())) {
                case 1 -> {
                    userService.manageMember();
                    menuAdminList(user);
                }

                case 2 -> {
                    financeService.financeMenu();
                    menuAdminList(user);
                }
                case 3 -> {
                    warehouseService.warehouseMenu(user);
                    menuAdminList(user);
                }
                case 4 -> {
                    stockService.stockMenu(user);
                    menuAdminList(user);
                }
                case 5 -> {
                    receiptService.receiptAdminMenu(user);
                    menuAdminList(user);
                }
                case 6 -> {
                    releaseService.printMenu(user);
                    menuAdminList(user);
                }
                case 7 -> userService.logoutId();
                default -> {
                    System.out.println("다시 입력해주세요");
                    menuAdminList(user);
                }
                //case 6 ->
            }
        } catch (Exception e) {
        }
    }

    public void menuUserList(UserVO user) {
        try {
            System.out.println("1.회원 관리 | 2. 창고 관리 | 3. 재고 관리 | 4. 입고 관리 | 5. 출고 관리 | 6. 로그 아웃");
            switch (Integer.parseInt(reader.readLine())) {
                case 1 -> {
                    userService.checkUser();
                    menuUserList(user);
                }
                case 2 -> {
                    warehouseService.warehouseMenu(user);
                    menuUserList(user);
                }
                case 3 -> {
                    stockService.stockMenu(user);
                    menuUserList(user);
                }
                case 4 -> {
                    receiptService.receiptUserMenu(user);
                    menuUserList(user);
                }
                case 5 -> {
                    releaseService.printMenu(user);
                    menuUserList(user);
                }
                case 6 -> userService.logoutId();
                default -> {
                    System.out.println("다시 입력해주세요");
                    menuUserList(user);
                }
                //case 6 ->
            }
        } catch (Exception e) {
        }
    }
}


