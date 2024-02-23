package Service;

import API.IWarehouseService;
import DAO.WarehouseDao;
import Exception.FinanceException.FinanceExceptionList;
import Exception.WarehouseException.WarehouseException;
import Exception.WarehouseException.WarehouseExceptionList;
import VO.UserVO;
import VO.WarehouseVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Predicate;


// 창고 관리 - Actor : 총관리자

public class WarehouseService implements IWarehouseService {
    private static volatile WarehouseService instance;

    public static WarehouseService getInstance() {  //싱글톤
        if (instance == null) {
            instance = new WarehouseService();
        }
        return instance;
    }

    private WarehouseDao warehouseDao = WarehouseDao.getInstance();
    private WarehouseVO warehouse;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    // UID userservice에서 가져오는거 구현 예정
    private UserVO myUser;


    public void warehouseMenu(UserVO user) {
        try {
            myUser = user;
            if (user.getUserType() == 1) {
                System.out.println("창고 관리 메뉴");
                System.out.println("1. 창고 등록 | 2. 창고 조회 | 3. 나가기");
                int choice = checkInputNum();
                WarehouseExceptionList.validateNumbersSelection(choice);
                switch (choice) {
                    case 1 -> addWarehouse();
                    case 2 -> getWarehouse();
                    default -> {
                        break;
                    }
                }

            } else if (user.getUserType() == 2) {
                System.out.println("창고 요금 메뉴");
                System.out.println("1. 창고 요금 조회 | 2. 나가기 ");
                int choice = checkInputNum();
                WarehouseExceptionList.validateNumberSelection(choice);
                switch (choice) {
                    case 1 -> getWarehouseCharge();
                    default -> {
                        break;
                    }
                }
            }
        } catch (WarehouseException we) {
            warehouseMenu(user);
        } catch (Exception e) {
        }
    }


    private void addWarehouse() {
        try {
            System.out.println("창고 타입을 입력해주세요");
            System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
            int wtype = checkInputNum();
            WarehouseExceptionList.validateNumbersSelection(wtype);
            int charge = setCharge(wtype);
            int cost = setCost(wtype);
            System.out.println("창고의 이름을 작성해주세요");
            String wname = reader.readLine();
            WarehouseExceptionList.validateWarehouseName(wname);
            System.out.println("창고의 주소를 작성해주세요");
            String city = reader.readLine();
            WarehouseExceptionList.validateAddress(city);
            System.out.println("창고의 최대 수용 용량을 작성해주세요");
            int totalCapacity = checkInputNum();
            warehouse = new WarehouseVO(myUser.getUserID(), wtype, wname, city, totalCapacity, charge, cost);
            warehouseDao.warehouseCreate(warehouse);
            warehouseMenu(myUser);
        } catch (WarehouseException we) {
            System.out.println();
            addWarehouse();
        } catch (Exception e) {
        }
    }

    private int setCharge(int wtype) {
        switch (wtype) {
            case 1 -> {
                return 100;
            }
            case 2 -> {
                return 150;
            }
            case 3 -> {
                return 200;
            }
            // 예외 필요
            default -> {
                return 100;
            }
        }
    }

    private int setCost(int wtype) {
        switch (wtype) {
            case 1 -> {
                return 50;
            }
            case 2 -> {
                return 75;
            }
            case 3 -> {
                return 100;
            }
            // 예외 필요
            default -> {
                return 50;
            }
        }
    }


    private void getWarehouse() {
        List<WarehouseVO> warehouseVOList = warehouseDao.warehouseRead();
        System.out.println("1. 전체 조회 | 2. 소재지 별 조회 | 3. 창고명 별 조회 | 4. 종류 별 조회");
        try {
            int select = checkInputNum();
            WarehouseExceptionList.validateMenuSelection(select);
            switch (select) {
                case 1 -> getWarehouseAll(warehouseVOList, null, 0);
                case 2 -> getWarehouseCity(warehouseVOList);
                case 3 -> getWarehouseName(warehouseVOList);
                case 4 -> getWarehouseType(warehouseVOList);
            }
        } catch (WarehouseException we) {
            System.out.println();
            getWarehouse();
        } catch (Exception e) {
        }
    }


    // type : 0 -- 전체 출력, 1 -- 도시 출력, 2-- 이름 출력 3-- 타입 출력


    private <T> void getWarehouseAll(List<WarehouseVO> warehouseVOList, T identifier, int type) {
        System.out.printf("%-4s%-6s%-17s%-10s%-13s%-13s%-9s%-9s\n", "UID", "창고타입", "창고 이름", "창고 위치", "창고 총 용량",
                "사용 용량", "사용 비용", "유지 비용");

        Predicate<WarehouseVO> predicate = getPredicate(identifier, type);
        warehouseVOList.stream()
                .filter(predicate) // predicate를 사용하여 조건에 맞는 요소만 필터링
                .forEach(warehouseVO -> {
                    String wtype = switch (warehouseVO.getWarehouseType()) {
                        case 2 -> "냉장";
                        case 3 -> "냉동";
                        default -> "기본";
                    };
                    printWarehouse(warehouseVO, wtype); // 조건에 맞는 요소에 대해 printWarehouse 호출
                });
    }
    private <T> Predicate<WarehouseVO> getPredicate(T identifier, int type) {
        if (identifier == null) {
            return warehouseVO -> true; // Always true if no identifier is specified.
        }
        return switch (type) {
            case 1 -> warehouseVO -> identifier.equals(warehouseVO.getAddressCity());
            case 2 -> warehouseVO -> warehouseVO.getWarehouseName().contains((CharSequence) identifier);
            case 3 -> warehouseVO -> identifier.equals(warehouseVO.getWarehouseType());
            default -> warehouseVO -> false; // Return false for unrecognized type.
        };
    }
//        for (WarehouseVO warehouseVO : warehouseVOList) {
//            String wtype = switch (warehouseVO.getWarehouseType()) {
//                case 2 -> "냉장";
//                case 3 -> "냉동";
//                default -> "기본";
//            };
//
//            if (predicate.test(warehouseVO)) {
//                printWarehouse(warehouseVO, wtype);
//            }
//        }
//        warehouseMenu(myUser);




//    private <T> void getWarehouseAll(List<WarehouseVO> warehouseVOList, T identifier, int type) {
//        System.out.printf("%-4s%-6s%-17s%-10s%-13s%-13s%-9s%-9s\n", "UID", "창고타입", "창고 이름", "창고 위치", "창고 총 용량",
//                "사용 용량", "사용 비용", "유지 비용");
//
//        for (WarehouseVO warehouseVO : warehouseVOList) {
//            String wtype;
//            if (warehouseVO.getWarehouseType() == 2) {
//                wtype = "냉장";
//            } else if (warehouseVO.getWarehouseType() == 3) {
//                wtype = "냉동";
//            } else {
//                wtype = "기본";
//            }
//            if (identifier != null) {
//
//                if (type == 1 && identifier.equals(warehouseVO.getAddressCity())) {
//                    printWarehouse(warehouseVO, wtype);
//                } else if (type == 2 && warehouseVO.getWarehouseName().contains((CharSequence) identifier)) {
//                    printWarehouse(warehouseVO, wtype);
//                } else if (type == 3 && identifier.equals(warehouseVO.getWarehouseType())) {
//                    printWarehouse(warehouseVO, wtype);
//                }
//            } else {
//                printWarehouse(warehouseVO, wtype);
//            }
//        }
//        warehouseMenu(myUser);
//    }

    private void printWarehouse(WarehouseVO warehouseVO, String wtype) {
        System.out.printf("%-4d%-7s%-20s%-12s%-16d%-16d%-11d%-9d\n",
                warehouseVO.getUserID(), wtype, warehouseVO.getWarehouseName(),
                warehouseVO.getAddressCity(), warehouseVO.getTotalCapacity(), warehouseVO.getUsingCapacity(),
                warehouseVO.getCharge(), warehouseVO.getCost());
    }


    private void getWarehouseCity(List<WarehouseVO> warehouseVOList) {
        System.out.println("도시 입력");
        try {
            String city = reader.readLine();
            WarehouseExceptionList.validateAddress(city);
            getWarehouseAll(warehouseVOList, city, 1);
        } catch (WarehouseException we) {
            getWarehouseCity(warehouseVOList);
        } catch (Exception e) {
        }
    }

    private void getWarehouseName(List<WarehouseVO> warehouseVOList) {
        System.out.println("창고 이름 입력");
        try {
            String name = reader.readLine();
            WarehouseExceptionList.validateWarehouseName(name);
            getWarehouseAll(warehouseVOList, name, 2);
        } catch (WarehouseException we) {
            getWarehouseName(warehouseVOList);
        } catch (Exception e) {
        }
    }

    private void getWarehouseType(List<WarehouseVO> warehouseVOList) {
        System.out.println("창고 종류 입력");
        System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
        try {
            int wtype = checkInputNum();
            WarehouseExceptionList.validateNumbersSelection(wtype);
            getWarehouseAll(warehouseVOList, wtype, 3);
        } catch (WarehouseException we) {
            getWarehouseType(warehouseVOList);
        } catch (Exception e) {
        }
    }

    private void getWarehouseCharge() {
        System.out.println("창고 요금 안내");
        System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
        try {
            int wtype = checkInputNum();
            WarehouseExceptionList.validateNumbersSelection(wtype);
            List<WarehouseVO> warehouseVOList = warehouseDao.warehouseChargeRead(wtype);
            System.out.printf("%-6s%-17s%-10s%-13s%-13s%-9s\n", "창고타입", "창고 이름", "창고 위치", "창고 총 용량",
                    "사용 용량", "사용 비용");
            warehouseVOList.stream().forEach(warehouseVO -> System.out.printf("%-8s%-20s%-12s%-16d%-16d%-11d\n",
                            wtype, // 이 변수는 반복문 외부에서 정의되어야 합니다.
                            warehouseVO.getWarehouseName(),
                            warehouseVO.getAddressCity(),
                            warehouseVO.getTotalCapacity(),
                            warehouseVO.getUsingCapacity(),
                            warehouseVO.getCharge()));

//            for (WarehouseVO warehouseVO : warehouseVOList) {
//                System.out.printf("%-8s%-20s%-12s%-16d%-16d%-11d\n",
//                        wtype, warehouseVO.getWarehouseName(),
//                        warehouseVO.getAddressCity(), warehouseVO.getTotalCapacity(), warehouseVO.getUsingCapacity(),
//                        warehouseVO.getCharge());
//            }
        } catch (WarehouseException we) {
            getWarehouseCharge();
        } catch (Exception e) {
        }
        warehouseMenu(myUser);
    }

    private int checkInputNum() throws IOException {
        String tmp = reader.readLine();
        WarehouseExceptionList.validateInteger(tmp);
        return Integer.parseInt(tmp);
    }
}
