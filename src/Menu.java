import API.IFinanceService;
import API.IStockService;
import API.IUserService;
import API.IWarehouseService;
import Service.FinanceService;
import Service.StockService;
import Service.UserService;
import Service.WarehouseService;
import VO.UserVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Menu {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    IUserService userService = UserService.getInstance();
    IFinanceService financeService = FinanceService.getInstance();
    IWarehouseService warehouseService = WarehouseService.getInstance();
    IStockService stockService = StockService.getInstance();
    public void mainMenu() {

        menuList(userService.memberLogin());
    }

    public void menuList(UserVO user){
        try {
            System.out.println("1.회원 관리 | 2. 재무 관리 | 3. 창고 관리 | 4. 재고 관리");
            switch (Integer.parseInt(reader.readLine())) {
                case 1 -> {
                    if (user.getUserType() == 1) {
                        System.out.println("관리자 관리 화면");
                        userService.manageMember();
                    } else if (user.getUserType() == 2) {
                        System.out.println("회원 관리 화면");
                        userService.checkUser();
                    }
                    menuList(user);
                }
                case 2 -> financeService.financeMenu(user);
                case 3 -> warehouseService.warehouseMenu(user);
                case 4 -> stockService.stockMenu(user);
                //case 5 ->
                //case 6 ->
            }
        }catch (Exception e){}
        }
    }


