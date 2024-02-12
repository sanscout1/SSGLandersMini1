package Service;

import API.IWarehouseService;
import DAO.WarehouseDao;
import VO.WarehouseVO;

import java.util.List;
import java.util.Scanner;

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
    Scanner sc = new Scanner(System.in);
    // UID userservice에서 가져오는거 구현 예정
    int UID = 1;

    /*
    메소드 시작

    Create
     */
    @Override
    public void addWarehouse() {
        System.out.println("창고 타입을 입력해주세요");
        // 예외 필요
        System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
        int wtype = Integer.parseInt(sc.next());
        int charge = setCharge(wtype);
        int cost = setCost(wtype);
        System.out.println("창고의 이름을 작성해주세요");
        // 예외 필요
        String wname = sc.next();
        System.out.println("창고의 주소를 작성해주세요");
        // 예외 필요
        String city = sc.next();
        System.out.println("창고의 최대 수용 용량을 작성해주세요");
        // 예외 필요
        int totalCapacity = Integer.parseInt(sc.next());
        warehouse = new WarehouseVO(UID, wtype, wname, city, totalCapacity, charge, cost);
        warehouseDao.warehouseCreate(warehouse);
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

    @Override
    public void getWarehouse() {
        List<WarehouseVO> warehouseVOList = warehouseDao.warehouseRead();
        System.out.println("1. 전체 조회 | 2. 소재지 별 조회 | 3. 창고명 별 조회 | 4. 종류 별 조회");
        int select = Integer.parseInt(sc.next());
        switch (select) {
            case 1 -> getWarehouseAll(warehouseVOList, null, 0);
            case 2 -> getWarehouseCity(warehouseVOList);
            case 3 -> getWarehouseName(warehouseVOList);
            case 4 -> getWarehouseType(warehouseVOList);
            default -> System.out.println("예외처리 추가할것");
        }
    }

    // type : 0 -- 전체 출력, 1 -- 도시 출력, 2-- 이름 출력 3-- 타입 출력
    private <T> void getWarehouseAll(List<WarehouseVO> warehouseVOList, T identifier, int type) {
        System.out.printf("%-4s%-6s%-17s%-10s%-13s%-13s%-9s%-9s\n", "UID", "창고타입", "창고 이름", "창고 위치", "창고 총 용량",
                "사용 용량", "사용 비용", "유지 비용");

        for (WarehouseVO warehouseVO : warehouseVOList) {
            String wtype;
            if (warehouseVO.getWarehouseType() == 2) {
                wtype = "냉장";
            } else if (warehouseVO.getWarehouseType() == 3) {
                wtype = "냉동";
            } else {
                wtype = "기본";
            }
            if (identifier != null) {

                if (type == 1 && identifier.equals(warehouseVO.getAddressCity())) {
                    printWarehouse(warehouseVO, wtype);
                } else if (type == 2 && warehouseVO.getWarehouseName().contains((CharSequence) identifier)) {
                    printWarehouse(warehouseVO, wtype);
                } else if (type == 3 && identifier.equals(warehouseVO.getWarehouseType())) {
                    printWarehouse(warehouseVO, wtype);
                }
            } else {
                printWarehouse(warehouseVO, wtype);
            }
        }
    }

    private void printWarehouse(WarehouseVO warehouseVO, String wtype) {
        System.out.printf("%-4d%-7s%-20s%-12s%-16d%-16d%-11d%-9d\n",
                warehouseVO.getUserID(), wtype, warehouseVO.getWarehouseName(),
                warehouseVO.getAddressCity(), warehouseVO.getTotalCapacity(), warehouseVO.getUsingCapacity(),
                warehouseVO.getCharge(), warehouseVO.getCost());
    }


    private void getWarehouseCity(List<WarehouseVO> warehouseVOList) {
        System.out.println("도시 입력");
        //예외 처리
        String city = sc.next();
        getWarehouseAll(warehouseVOList, city, 1);

    }

    private void getWarehouseName(List<WarehouseVO> warehouseVOList) {
        System.out.println("창고 이름 입력");
        String name = sc.next();
        getWarehouseAll(warehouseVOList, name, 2);

    }

    private void getWarehouseType(List<WarehouseVO> warehouseVOList) {
        System.out.println("창고 종류 입력");
        System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
        int wtype = Integer.parseInt(sc.next());
        getWarehouseAll(warehouseVOList, wtype, 3);
    }


}
