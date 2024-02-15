package Service;

import API.IFinanceService;
import DAO.FinanceDao;
import Exception.FinanceException.FinanceException;
import Exception.FinanceException.FinanceExceptionList;
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

    private FinanceService() {

    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void financeMenu() {
        try {
            System.out.println();
            System.out.println("재무 관리 메뉴");
            System.out.println("1. 모든 재무 조회 | 2. 창고 별 재무 조회 | 3. 나가기");
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int choice = Integer.parseInt(tmp);
            FinanceExceptionList.validateNumbersSelection(choice);
            switch (choice) {
                case 1 -> printAllFinance();
                case 2 -> printWarehouseFinance();
                default -> {break;}
            }
        } catch (FinanceException fe) {
            System.out.println();
            financeMenu();
        }
        catch (Exception e) {
        }
    }

    private void printAllFinance() {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        System.out.println();
        System.out.println("1.전체 내역 조회 | 2.전체 수익 조회 | 3.전체 지출 조회");
        try {
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int choice = Integer.parseInt(tmp);
            FinanceExceptionList.validateNumbersSelection(choice);
            financeVOList = financeDao.financeTypeRead(choice);
            System.out.printf("%-12s%-12s%-23s%-12s\n", "입고 번호", "재무 종류", "날짜", "금액");
            for (FinanceVO financeVO : financeVOList) {
                String ftype;
                if (financeVO.getFtype() == 0) ftype = "수익";
                else ftype = "지출";
                System.out.printf("%-15d%-12s%-26s%-12d\n", financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
            }
            System.out.println();
            printCalculateAllFinance();
        } catch (FinanceException fe) {
            System.out.println();
            printAllFinance();
        }catch (Exception e) {
        }

    }

    private void printCalculateAllFinance() {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        System.out.println();
        System.out.println("1. 총 정산하기 | 2.나가기");
        int total = 0;
        try {
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int choice = Integer.parseInt(tmp);
            FinanceExceptionList.validateNumberSelection(choice);
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
            financeMenu();
        } catch (FinanceException fe) {
            System.out.println();
            printCalculateAllFinance();
        }catch (Exception e) {
        }
    }


    private void printWarehouseFinance() {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        System.out.println();
        System.out.println("내역을 조회 할 창고 ID를 입력하세요");

        try {
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int WID = Integer.parseInt(tmp);
            financeVOList = financeDao.financeWarehouseRead(WID);

                System.out.printf("%-12s%-12s%-15s%-25s%-12s\n", "창고 번호", "입고 번호", "재무 종류", "날짜", "금액");
                for (FinanceVO financeVO : financeVOList) {
                    String ftype;
                    if (financeVO.getFtype() == 0) ftype = "수익";
                    else ftype = "지출";
                    System.out.printf("%-14d%-14d%-18s%-26s%-12d\n", financeVO.getWarehouseID(), financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
                }
            if(!financeVOList.isEmpty()) {
            System.out.println();
            System.out.println("1.창고 수익 조회 | 2.창고 지출 조회 | 3. 창고 정산 하기 ");
            tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int choice = Integer.parseInt(tmp);
            FinanceExceptionList.validateNumbersSelection(choice);
            if (choice == 1) {
                printChargeWarehouse(financeVOList, WID);
            } else if (choice == 2) {
                printcostWarehouse(financeVOList, WID);
            } else if (choice == 3) {
                printCalculateWarehouseFinance(WID);
            }
            } else {
                System.out.println("내역이 없습니다.");
                financeMenu();
            }

        }
        catch (FinanceException fe) {
            System.out.println();
            printWarehouseFinance();
        }catch (Exception e) {
        }
    }

    private void printChargeWarehouse(List<FinanceVO> financeVOList, int WID) {
        System.out.printf("%-12s%-12s%-15s%-25s%-12s\n", "창고 번호", "입고 번호", "재무 종류", "날짜", "금액");
        String ftype;
        ftype = "수익";
        for (FinanceVO financeVO : financeVOList) {
            if (financeVO.getFtype() == 0) {
                System.out.printf("%-14d%-14d%-18s%-26s%-12d\n", financeVO.getWarehouseID(), financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
            }
        }
        try {
        if(!financeVOList.isEmpty()){
        System.out.println();
        System.out.println("1. 내역 수정하기 | 2. 내역 삭제하기 | 3. 나가기");

            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int choice = Integer.parseInt(tmp);
            FinanceExceptionList.validateNumbersSelection(choice);
            if (choice == 1) {
                updateWarehouseFinance(0,WID);
            } else if (choice == 2) {
                deleteWarehouseFinance(0,WID);
            } else {
                financeMenu();
            }
        } else {
            System.out.println("해당 창고는 비어 있습니다.");
        }

        } catch (FinanceException fe) {
            System.out.println();
            printChargeWarehouse(financeVOList,WID);
        }catch (Exception e) {
        }
    }

    private void printcostWarehouse(List<FinanceVO> financeVOList, int WID) {
        System.out.printf("%-12s%-12s%-15s%-25s%-12s\n", "창고 번호", "입고 번호", "재무 종류", "날짜", "금액");
        String ftype;
        ftype = "지출";
        for (FinanceVO financeVO : financeVOList) {
            if (financeVO.getFtype() == 1) {
                System.out.printf("%-14d%-14d%-18s%-26s%-12d\n", financeVO.getWarehouseID(), financeVO.getRecID(), ftype, financeVO.getFdate(), financeVO.getAmount());
            }
        }
        System.out.println();
        System.out.println("1. 내역 수정하기 | 2. 내역 삭제하기 | 3. 나가기");
        try {
        if(!financeVOList.isEmpty()){
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int choice = Integer.parseInt(tmp);
            FinanceExceptionList.validateNumbersSelection(choice);
            if (choice == 1) {
                updateWarehouseFinance(1,WID);
            } else if (choice == 2) {
                deleteWarehouseFinance(1,WID);
            } else {
                printWarehouseFinance();
            }
        } else {
            System.out.println("해당 창고는 비어 있습니다.");
        }
        } catch (FinanceException fe) {
            System.out.println();
            printcostWarehouse(financeVOList,WID);
        }catch (Exception e) {
        }
    }


    private void printCalculateWarehouseFinance(int WID) {
        List<FinanceVO> financeVOList = new ArrayList<FinanceVO>();
        int total = 0;
        financeVOList = financeDao.financeWarehouseRead(WID);
        for (FinanceVO financeVO : financeVOList) {
            if (financeVO.getFtype() == 0) {
                total += financeVO.getAmount();
            } else {
                total -= financeVO.getAmount();
            }
        }

        System.out.println("창고 번호 : " + WID + "   창고 정산 내역: " + total);
        financeMenu();
    }

    private void updateWarehouseFinance(int ftype, int WID) {
        System.out.println();
        System.out.println("수정 할 입고 번호를 입력해주세요");
        try {
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int recid = Integer.parseInt(tmp);
            System.out.println("금액을 입력해주세요");
            tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int amount = Integer.parseInt(tmp);
            financeDao.financeUpdate(amount, recid, ftype,WID);
            financeMenu();
        } catch (FinanceException fe) {
            System.out.println();
            updateWarehouseFinance(ftype, WID);
        }catch (Exception e) {
        }
    }

    private void deleteWarehouseFinance(int ftype, int WID) {
        try {
            System.out.println();
            System.out.println("삭제 할 입고 번호를 입력해주세요");
            String tmp = reader.readLine();
            FinanceExceptionList.validateInteger(tmp);
            int recid = Integer.parseInt(tmp);
            financeDao.financeDelete(recid, ftype,WID);
            financeMenu();
        } catch (FinanceException fe) {
            System.out.println();
            deleteWarehouseFinance(ftype,WID);}
        catch (Exception e) {
        }
    }


}
