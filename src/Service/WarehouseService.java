package Service;

import API.IWarehouseService;
import DAO.WarehouseDao;
import VO.UserVO;
import VO.WarehouseVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


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
    int UID = 1;



    public void warehouseMenu (UserVO user){
        try {
            if (user.getUserType() == 1) {
                System.out.println("창고 관리 메뉴");
                System.out.println("1. 창고 등록 | 2. 창고 조회 ");
                switch (Integer.parseInt(reader.readLine())){
                    case 1 -> addWarehouse();
                    case 2 -> getWarehouse();
                }

            } else {
                System.out.println("창고 관리 메뉴");
                System.out.println("1. 창고 요금 조회 ");
                switch (Integer.parseInt(reader.readLine())){
                    case 1 -> getWarehouseCharge();
                }
            }
        } catch (Exception e) {}
    }


    private void addWarehouse() {
        try {
            System.out.println("창고 타입을 입력해주세요");
            // 예외 필요
            System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
            int wtype = Integer.parseInt(reader.readLine());
            int charge = setCharge(wtype);
            int cost = setCost(wtype);
            System.out.println("창고의 이름을 작성해주세요");
            // 예외 필요
            String wname = reader.readLine();
            System.out.println("창고의 주소를 작성해주세요");
            // 예외 필요
            String city = reader.readLine();
            System.out.println("창고의 최대 수용 용량을 작성해주세요");
            // 예외 필요
            int totalCapacity = Integer.parseInt(reader.readLine());
            warehouse = new WarehouseVO(UID, wtype, wname, city, totalCapacity, charge, cost);
            warehouseDao.warehouseCreate(warehouse);
        }
        catch(Exception e) {}
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
            int select = Integer.parseInt(reader.readLine());
            switch (select) {
                case 1 -> getWarehouseAll(warehouseVOList, null, 0);
                case 2 -> getWarehouseCity(warehouseVOList);
                case 3 -> getWarehouseName(warehouseVOList);
                case 4 -> getWarehouseType(warehouseVOList);
                default -> System.out.println("예외처리 추가할것");
            }
        } catch (Exception e ){}
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
        try {
            String city = reader.readLine();
            getWarehouseAll(warehouseVOList, city, 1);
        } catch (Exception e) {}

    }

    private void getWarehouseName(List<WarehouseVO> warehouseVOList) {
        System.out.println("창고 이름 입력");
        try {
            String name = reader.readLine();
            getWarehouseAll(warehouseVOList, name, 2);
        } catch (Exception e){}

    }

    private void getWarehouseType(List<WarehouseVO> warehouseVOList) {
        System.out.println("창고 종류 입력");
        System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
        try {
            int wtype = Integer.parseInt(reader.readLine());
            getWarehouseAll(warehouseVOList, wtype, 3);
        } catch (Exception e) {}
    }

    private void getWarehouseCharge(){
        System.out.println("창고 요금 안내");
        System.out.println("1. 기본 | 2. 냉장 | 3. 냉동");
        try {
            int wtype = Integer.parseInt(reader.readLine());
            List<WarehouseVO> warehouseVOList = warehouseDao.warehouseChargeRead(wtype);
            System.out.printf("%-6s%-17s%-10s%-13s%-13s%-9s\n", "창고타입", "창고 이름", "창고 위치", "창고 총 용량",
                    "사용 용량", "사용 비용");
            for (WarehouseVO warehouseVO : warehouseVOList) {
                System.out.printf("%-8s%-20s%-12s%-16d%-16d%-11d\n",
                        wtype, warehouseVO.getWarehouseName(),
                        warehouseVO.getAddressCity(), warehouseVO.getTotalCapacity(), warehouseVO.getUsingCapacity(),
                        warehouseVO.getCharge());
            }
        } catch (Exception e){}
    }
}
