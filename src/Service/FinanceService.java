package Service;

import API.IFinanceService;
import DAO.FinanceDao;
import VO.FinanceVO;
import VO.UserVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FinanceService implements IFinanceService {

    private static volatile FinanceService instance;
    private FinanceDao financeDao = FinanceDao.getInstance();

    public static FinanceService getInstance() {  //싱글톤
        if (instance == null) {
            instance = new FinanceService();
        }
        return instance;
    }
    private FinanceService(){

    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void financeMenu(UserVO user){
        try {
            if (user.getUserType() == 1) {
                System.out.println("재무 관리 메뉴");
                System.out.println("1. 모든 재무 조회 | 2. 창고 별 재무 조회 ");
                switch (Integer.parseInt(reader.readLine())){
                    case 1 -> printAllFinance();
                    case 2 -> printWarehouseFinance();
                }

            }
        } catch (Exception e) {}
    }

    private void printAllFinance() {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        System.out.println("1.전체 내역 조회 | 2.전체 수익 조회 | 3.전체 지출 조회");
        try {
            financeVOList = financeDao.financeTypeRead(Integer.parseInt(reader.readLine()));
            System.out.printf("%-12s%-12s%-23s%-12s\n", "입고 번호", "재무 종류", "날짜", "금액");
            for (FinanceVO financeVO : financeVOList) {
                String ftype;
                if (financeVO.getFtype() == 0) ftype = "수익";
                else ftype = "지출";
                System.out.printf("%-15d%-12s%-26s%-12d\n", financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
            }
            System.out.println();
            printCalculateAllFinance();
        } catch (Exception e) {}

    }
    private void printCalculateAllFinance() {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        System.out.println("1. 총 정산하기 | 2.나가기");
        int total =0;
        try {
            int choice = Integer.parseInt(reader.readLine());
            if (choice == 1) {
                financeVOList = financeDao.financeTypeRead(1);
                for (FinanceVO financeVO : financeVOList) {
                    if (financeVO.getFtype() == 0) {
                        total += financeVO.getAmount();
                    } else {
                        total -= financeVO.getAmount();
                    }
                }

                System.out.println("총 정산 내역: " + total);
            }
        } catch (Exception e) {}
    }


    private void printWarehouseFinance(){
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        System.out.println("내역을 조회 할 창고 ID를 입력하세요");
        try {
        int WID = Integer.parseInt(reader.readLine());
        financeVOList = financeDao.financeWarehouseRead(WID);
        System.out.printf("%-12s%-12s%-15s%-25s%-12s\n", "창고 번호","입고 번호", "재무 종류", "날짜", "금액");
        for (FinanceVO financeVO : financeVOList) {
            String ftype;
            if(financeVO.getFtype() == 0) ftype ="수익";
            else ftype ="지출";
            System.out.printf("%-14d%-14d%-18s%-26s%-12d\n", financeVO.getWarehouseID(),financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
        }
        System.out.println("1.창고 수익 조회 | 2.창고 지출 조회 | 3. 창고 정산 하기 ");
        int choice = Integer.parseInt(reader.readLine());
        if(choice == 1){
            printChargeWarehouse(financeVOList,WID);
        } else if (choice == 2) {
            printcostWarehouse(financeVOList,WID);
        } else if (choice == 3) {
            printCalculateWarehouseFinance(WID);
        }} catch (Exception e) {}
    }
    private void printChargeWarehouse (List<FinanceVO> financeVOList,int WID){
        System.out.printf("%-12s%-12s%-15s%-25s%-12s\n", "창고 번호","입고 번호", "재무 종류", "날짜", "금액");
        String ftype;
        ftype ="수익";
        for (FinanceVO financeVO : financeVOList) {
            if(financeVO.getFtype() ==0){
                System.out.printf("%-14d%-14d%-18s%-26s%-12d\n", financeVO.getWarehouseID(),financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
            }}
        System.out.println("1. 내역 수정하기 | 2. 내역 삭제하기 | 3. 나가기");
        try {
            int choice = Integer.parseInt(reader.readLine());
            if (choice == 1) {
                updateWarehouseFinance(0);
            } else if (choice == 2) {
                deleteWarehouseFinance(0);
            }
        } catch (Exception e) {}
    }
    private void printcostWarehouse (List<FinanceVO> financeVOList,int WID){
        System.out.printf("%-12s%-12s%-15s%-25s%-12s\n", "창고 번호","입고 번호", "재무 종류", "날짜", "금액");
        String ftype;
        ftype ="지출";
        for (FinanceVO financeVO : financeVOList) {
            if(financeVO.getFtype() ==1){
                System.out.printf("%-14d%-14d%-18s%-26s%-12d\n", financeVO.getWarehouseID(),financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
            }}
        System.out.println("1. 내역 수정하기 | 2. 내역 삭제하기 | 3. 나가기");
        try {
            int choice = Integer.parseInt(reader.readLine());
            if (choice == 1) {
                updateWarehouseFinance(1);
            } else if (choice == 2) {
                deleteWarehouseFinance(1);
            }
        } catch (Exception e) {}
    }


    private void printCalculateWarehouseFinance(int WID) {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        int total =0;
            financeVOList = financeDao.financeWarehouseRead(WID);
            for (FinanceVO financeVO : financeVOList) {
                if(financeVO.getFtype()==0){
                    total += financeVO.getAmount();
                } else{ total -= financeVO.getAmount();}
            }

            System.out.println("창고 번호 : "+WID+"   창고 정산 내역: "+total);

    }

    private void updateWarehouseFinance(int ftype) {
        System.out.println("수정 할 입고 번호를 입력해주세요");
        try {
            int recid = Integer.parseInt(reader.readLine());
            System.out.println("금액을 입력해주세요");
            int amount = Integer.parseInt(reader.readLine());
            financeDao.financeUpdate(amount, recid, ftype);
        } catch (Exception e) {}
    }

    private void deleteWarehouseFinance(int ftype) {
        try {
            System.out.println("삭제 할 입고 번호를 입력해주세요");
            int recid = Integer.parseInt(reader.readLine());
            financeDao.financeDelete(recid, ftype);
        } catch (Exception e) {}
    }



}
