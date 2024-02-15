package Service;

import API.IReceiptService;
import DAO.ReceiptDao;
import Exception.ReceiptException.ReceiptExceptionList;
import VO.ReceiptVO;
import VO.UserVO;
import VO.WarehouseVO;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class ReceiptService implements IReceiptService {

    private ReceiptDao receiptDao;
    private ReceiptVO receipt;



    List<ReceiptVO> receiptVOList = new ArrayList<>();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    // -----------------------------------------------------관리자
    /* 총관리자가 이용하는 입고 요청 메뉴
    1. 입고 요청 승인
    2. 입고 요청 수정 -> 입고 요청 지정, 입고 날짜 지정
    3. 입고 요청 취소
    4. 입고 고지서 출력
    5. 입고 현황 조회 (기간별, 월별, QR 코드)
    6. 로그 아웃(뒤로 가기?)
     */
    public ReceiptService() {
        receiptDao = new ReceiptDao();
    }

    public void receiptAdminMenu(UserVO userVO) {
        System.out.println("==========================");
        System.out.println("관리자 전용 입고 관리 메뉴입니다.");
        System.out.println("==========================");
        System.out.println("1. 입고 요청 승인 | 2. 입고 요청 수정 | 3. 입고 요청 취소\n" +
                "4. 입고 고지서 출력 | 5. 입고 현황 조회 | 6. 나가기");

        System.out.print("입력 : ");
        try {
            String choice = input.readLine();
            ReceiptExceptionList.validateAdminChoice(choice);

            switch (choice) {
                case "1" -> receiptApprove(userVO);
                case "2" -> receiptRequestUpdate(userVO);
                case "3" -> receiptRequestDelete(userVO);
                case "4" -> receiptPaper(userVO);
                case "5" -> receiptListChoice(userVO);
//            case "6" -> UserService.memberLogin();
            }
        } catch (Exception e) {
            receiptAdminMenu(userVO);
        }
    }

    // 입고 요청 승인 화면
    public void receiptApprove(UserVO userVO) {
        try {
//            ReceiptVO receiptVO = new ReceiptVO();

            System.out.println("입고 요청 승인 화면입니다.");
            receiptVOList = receiptDao.receiptReadApproval(); // -> approval 값이 0인 얘들 리스트
            System.out.println("승인할 입고 요청의 입고 ID를 입력하세요.");
            System.out.println("q!를 입력하면 입력이 중지됩니다.");



            while (true) {
                System.out.print("입력 : ");
                String choice = input.readLine();

                if (choice.equals("q!")) {
                    break;
                }

                for (ReceiptVO receiptVO : receiptVOList){
                    if (receiptVO.getReceiptId() == Integer.parseInt(choice)) {
                        receiptVO.setApproval(1);
                        receiptVO.setState(1);
                        receiptVO.setQrCode(1);

                        receiptDao.receiptUpdate(receiptVO);
                    }
                }
            }
            System.out.println("입고 요청이 승인되었습니다.");
            receiptAdminMenu(userVO);
        } catch (Exception e) {

        }

    }

    // 입고 고지서 출력
    /*
        총관리자 기능
        입고 승인된 / approval = 1인 것들
        회원들의 입고 요청 승인된 것들
        리스트 출력해서 선택하면 그 입고 고지서가 출력
     */
    private void receiptPaper(UserVO userVO) {
        try {
            System.out.println("=====입고 고지서 조회=====");
            while (true) {
                receiptList(userVO);    // 관리자로 들어가서 회원들 입고 요청 승인된 리스트 출력
                System.out.print("출력할 입고 건을 선택하세요\n UID 입력 : ");
                String choice = input.readLine();
                ReceiptExceptionList.validateInput(choice);

                for (ReceiptVO receiptVO : receiptVOList) {
                    if (receiptVO.getUId() == Integer.parseInt(choice)) {
                        System.out.println("=====입고 고지서 출력=====");
                        System.out.printf("입고 ID : %d\n", receipt.getReceiptId());
                        System.out.printf("입고 날짜 : %s\n", receipt.getReceiptDate());
                        System.out.printf("상품 ID : %s, 상품 수량 : %s\n", receipt.getPId(), receipt.getProductQuantity());
                        System.out.printf("입고될 창고 ID : %d\n", receipt.getWId());
                        System.out.printf("상태 : %d\n", receipt.getState());
                    }
                }
                System.out.print("더 출력하시겠습니까? (y/n) : ");
                String inputChoice = input.readLine();
                ReceiptExceptionList.validateInputChoice(inputChoice);


                if (inputChoice.equalsIgnoreCase("n"))
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    // ----------------------------------------------------- 회원

    /* 회원이 이용하는 입고 요청 메뉴
    1. 입고 요청
    2. 입고 요청 수정
    3. 입고 요청 취소
    4. 입고 요청 조회 (기간별, 월별, QR 코드)
    5. 로그 아웃 ( 뒤로 가기?)
    */

    public void receiptUserMenu(UserVO userVO) {
        try {
            System.out.println("==========================");
            System.out.println("입고 관리 메뉴입니다.");
            System.out.println("==========================");
            System.out.println("1. 입고 요청 | 2. 입고 요청 수정 | 3. 입고 요청 취소" +
                    "\n4. 입고 요청 조회 | 5. 나가기");
            System.out.print("입력 : ");
            String choice = input.readLine();
            ReceiptExceptionList.validateMenuChoice(choice);

            switch (choice) {
                case "1" -> receiptRequest(userVO);
                case "2" -> receiptRequestUpdate(userVO);
                case "3" -> receiptRequestDelete(userVO);
                case "4" -> receiptListChoice(userVO);
//            case "5" -> UserService.memberLogin();
            }
        } catch (Exception e) {
            receiptUserMenu(userVO);
        }

    }

    // 입고 요청 화면 --> User 만 이용
    private void receiptRequest(UserVO userVO) {
        try {
            System.out.println("==========================");
            System.out.println("회원 전용 입고 요청 화면입니다.");
            System.out.println("==========================");


            System.out.println("상품의 ID를 입력해주세요.");
            System.out.print("입력 : ");
            String productId = input.readLine();
            ReceiptExceptionList.validateInput(productId);

            System.out.println("입력한 상품의 수량을 입력해주세요.");
            System.out.print("입력 : ");
            String productQuantity = input.readLine();
            ReceiptExceptionList.validateInput(productQuantity);

            System.out.println("창고 ID를 입력하세요.");
            System.out.print("입력 : ");
            String warehouseId = input.readLine();
            ReceiptExceptionList.validateWarehouseId(warehouseId);

            int approval = 0;
            receipt = new ReceiptVO(
                    Integer.parseInt(productQuantity), userVO.getUserID(),
                    Integer.parseInt(productId), Integer.parseInt(warehouseId),
                    approval);
            receiptVOList.add(receipt);
            receiptDao.receiptCreate(receipt);

            System.out.println("\n입고 요청되었습니다.");
            System.out.println();
            receiptUserMenu(userVO);
        } catch (Exception e) {
            System.out.println();
            receiptRequest(userVO);
        }

    }


    // 입고 요청 수정 화면 -> 입고 위치 지정, 입고 날짜 지정 같이
    private void receiptRequestUpdate(UserVO userVO) {
        try {
            System.out.println("==========================");
            System.out.println("입고 요청 수정 화면입니다.");
            System.out.println("==========================");

            receiptVOList = receiptDao.receiptRead(userVO);     // 입고 요청 리스트 보기
            ReceiptVO receiptVO = new ReceiptVO();

            System.out.println("입고 ID를 입력해주세요.");
            String pId = input.readLine();
            ReceiptExceptionList.validateInput(pId);
            for (ReceiptVO vo : receiptVOList) {
                if (vo.getReceiptId() == Integer.parseInt(pId)) {
                    receiptVO = vo;
                }
            }

            System.out.println("입력한 상품의 수량을 입력해주세요.");
            String productQuantity = input.readLine();
            ReceiptExceptionList.validateInput(productQuantity);
            receiptVO.setProductQuantity(Integer.parseInt(productQuantity));


            System.out.println("입고 날짜를 입력해주세요. (입력 형식 : yyyymmdd)");
            System.out.print("입력 : ");
            String updateDate = input.readLine();
            ReceiptExceptionList.validateInput(updateDate);
            receiptVO.setReceiptDate(updateDate);


            receiptDao.receiptUpdate(receiptVO);

            System.out.println("수정 되었습니다.");
            System.out.println();
            if (userVO.getUserType() == 1) {
                receiptAdminMenu(userVO);
            } else if (userVO.getUserType() == 2) {
                receiptUserMenu(userVO);
            }


        } catch (Exception e) {
            System.out.println();
            receiptRequestUpdate(userVO);
        }

    }

    // 입고 요청 취소 화면
    private void receiptRequestDelete(UserVO userVO) {
        try {
            System.out.println("==========================");
            System.out.println("입고 요청 삭제 화면입니다.");
            System.out.println("==========================");

            receiptVOList = receiptDao.receiptRead(userVO);

            System.out.print("취소할 입고 ID 입력 : ");
            String choiceUId = input.readLine();
            ReceiptExceptionList.validateInput(choiceUId);

            System.out.print("입고 요청을 삭제하시겠습니까? (y/n)? : ");
            String inputChoice = input.readLine();
            ReceiptExceptionList.validateInputChoice(inputChoice);

            if (inputChoice.equalsIgnoreCase("y")) {
                for (ReceiptVO receiptVO : receiptVOList) {
                    if (receiptVO.getReceiptId() == Integer.parseInt(choiceUId)) {
                            receiptDao.receiptCancel(receiptVO);
                        this.receipt = receiptVO;
                    }
                }
                receiptVOList.remove(this.receipt);
                System.out.println("입고 요청이 삭제되었습니다.");

                if (userVO.getUserType() == 1) {
                    receiptAdminMenu(userVO);
                } else if (userVO.getUserType() == 2)
                    receiptUserMenu(userVO);

            } else if (inputChoice.equalsIgnoreCase("n")) {
                System.out.println("입고 요청이 삭제되지 않았습니다.");

                if (userVO.getUserType() == 1) {
                    receiptAdminMenu(userVO);
                } else if (userVO.getUserType() == 2)
                    receiptUserMenu(userVO);

            }
        } catch (Exception e) {
            receiptRequestDelete(userVO);
        }
    }

    //----------------------------------------현황 조회 선택 화면
    // 회원일 경우
    // 1. 입고 현황 조회
    // 2. 기간별 입고 현황
    // 3. 월별 입고 현황

    // 관리자일 경우
    // 4. qr 코드 조회  추가

    public void receiptListChoice(UserVO userVO) {
        try {
            System.out.println("==========================");
            System.out.println("입고 현황 조회 선택 화면");
            System.out.println("==========================");
            System.out.println("1. 입고 현황 조회 | 2. 기간별 입고 현황" +
                    "\n3. 월별 입고 현황 | 4. 뒤로 가기");

            System.out.print("입력 : ");
            String choice = input.readLine();
            ReceiptExceptionList.validateMenuChoice(choice);

            switch (choice) {
                case "1" -> receiptList(userVO);
                case "2" -> receiptPeriodicList(userVO);
                case "3" -> receiptMonthList(userVO);
                case "4" -> {
                    if (userVO.getUserType() == 1)   // admin 계정
                        receiptAdminMenu(userVO);
                    else
                        receiptUserMenu(userVO);
                }
            }
        } catch (Exception e) {
            System.out.println();
            receiptListChoice(userVO);
        }

    }

    // 입고 현황 조회 화면 -> 회원일 때랑 관리자일 때 다르게
    private void receiptList(UserVO userVO) {
        receiptDao.receiptRead(userVO);
        receiptListChoice(userVO);
    }

    // 기간별 입고 현황 화면 -> 회원일 때랑 관리자일 때 다르게
    private void receiptPeriodicList(UserVO userVO) {
        try {

            System.out.println("==========================");
            System.out.println("기간 별 입고 현황 입니다.");
            System.out.println("==========================");
            System.out.println("조회할 기간을 입력해주세요.");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd");

            System.out.print("시작 날짜 : ");
            String firstDate = input.readLine();

            Date formatDate = simpleDateFormat.parse(firstDate);
            String strNewDtFormat = newDtFormat.format(formatDate);

            ReceiptExceptionList.validateInput(firstDate);

            System.out.print("끝 날짜 : ");
            String lastDate = input.readLine();

            Date formatDate1 = simpleDateFormat.parse(lastDate);
            String strNewDtFormat1 = newDtFormat.format(formatDate1);

            ReceiptExceptionList.validateInput(lastDate);




            System.out.println(strNewDtFormat + " ~ " + strNewDtFormat1 + " 기간의 입고 현황입니다.\n");
            receiptDao.receiptPeriodRead(userVO, strNewDtFormat, strNewDtFormat1);


            System.out.println();
            receiptListChoice(userVO);
        } catch (Exception e) {
            System.out.println();
            receiptPeriodicList(userVO);
        }

    }

    // 월별 입고 현황 조회 화면 -> 회원일 때랑 관리자일 때 다르게
    private void receiptMonthList(UserVO userVO) {
        System.out.println("==========================");
        System.out.println(userVO.getUserName() + "의 월 별 입고 현황입니다.");
        System.out.println("==========================");
        receiptDao.receiptMonthRead(userVO);
        System.out.println();
        receiptListChoice(userVO);
    }


}