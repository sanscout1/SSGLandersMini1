package Service;

import API.IStockService;
import DAO.StockDao;
import VO.StockVO;

import java.util.*;

public class StockService implements IStockService {
    Scanner sc= new Scanner(System.in);

    private static volatile StockService instance;

    public static StockService getInstance() {  //싱글톤
        if (instance == null) {
            instance = new StockService();
        }
        return instance;
    }
    private StockDao stockDao = StockDao.getInstance();

    // 서비스 구현하기
    // 전체조회, 대분류, 중분류, 소분류 조회 만들기


    // 총관리자 입장
    public void printStock() {
        System.out.println("조회 방법 : 1.전체 조회 | 2.대분류 조회 ");
        int choice = Integer.parseInt(sc.nextLine());
        if( choice == 1 ) {
            printList("");
        } else if(choice ==2){
            System.out.println("대분류를 입력해주세요");
            printList(sc.nextLine());
            System.out.println("중분류 조회 : 1.조회 하기 | 2.조회 취소");
            choice = Integer.parseInt(sc.nextLine());
            if( choice == 1 ) {
                System.out.println("중분류를 입력해주세요");
                printList(sc.nextLine());
                System.out.println("소분류 조회 : 1.조회 하기 | 2.조회 취소");
                choice = Integer.parseInt(sc.nextLine());
                if(choice == 1) {
                    System.out.println("소분류를 입력해주세요");
                    printList(sc.nextLine());
                }
            }
        }
    }
    private<T> void printList(T identifier){
        List<StockVO> stockList = new ArrayList<StockVO>();
        stockList = stockDao.stockRead();
        System.out.printf("%-10s%-10s%-15s%-10s%-15s%-10s%-10s%-25s\n", "대분류", "중분류", "소분류", "창고 ID",
                "창고 이름", "창고 주소","제품 재고량", "제품 이름");
        for (StockVO stockVO : stockList) {
            if(stockVO.getTcategory().contains((CharSequence) identifier) || stockVO.getIcategory().contains((CharSequence) identifier)
                    || stockVO.getScategory().contains((CharSequence) identifier)) {
                System.out.printf("%-11s%-10s%-15s%-12s%-17s%-12s%-13d%-25s\n", stockVO.getTcategory(),
                        stockVO.getIcategory(), stockVO.getScategory(), stockVO.getWarehouseID(),
                        stockVO.getWarehouseName(), stockVO.getAddressCity(), stockVO.getQuantity(), stockVO.getPname());
            }
        }
    }
}
