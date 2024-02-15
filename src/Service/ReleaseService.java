package Service;

import API.IReleaseService;
import DAO.ReleaseDao;
import Exception.ReleaseException.ReleaseException;
import Exception.ReleaseException.ReleaseExceptionList;
import VO.ReleaseVO;
import VO.UserVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReleaseService implements IReleaseService {

  ReleaseDao releaseDao = new ReleaseDao();
  WaybillService waybillService = new WaybillService();
  DispatchService dispatchService = new DispatchService();
  BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
  public static int waynum;


  public void printMenu(UserVO userVO){
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    ReleaseService releaseService = new ReleaseService();
    DispatchService dispatchService = new DispatchService();
    WaybillService waybillService = new WaybillService();

    int selectNum = 0;

    if (userVO.getUserType() == 1) {   //관리자
      try {
        System.out.println("==출고 메뉴==");
        System.out.println("1.출고요청 관리 | 2.출고관리 | 3.배차관리 | 4.운송장관리 | 5. 나가기");
        selectNum = Integer.parseInt(bf.readLine());
        switch (selectNum) {
          case 1 -> {
            System.out.println("1.미승인 리스트보기 | 2.승인 및 취소하기 | 3.출고요청");
            selectNum = Integer.parseInt(bf.readLine());
            switch (selectNum) {
              case 1 -> {
                releaseService.releaseApproveList();
              }
              case 2 -> {
                releaseService.releaseApprove();
              }
              case 3 -> {
                releaseService.releaseRequest(userVO);
              }
            }printMenu(userVO);
          }
          case 2 -> {
            System.out.println("1.출고리스트 보기 | 2.출고상품 검색");
            selectNum = Integer.parseInt(bf.readLine());
            switch (selectNum) {
              case 1 -> {
                releaseService.releaseList(userVO);
              }
              case 2 -> {
                releaseService.releaseSearch(userVO);
              }
            }printMenu(userVO);
          }
          case 3 -> {
            System.out.println("1.배차등록 | 2.배차리스트 조회 | 3.배차정보 수정 | 4.배차 취소");
            selectNum = Integer.parseInt(bf.readLine());
            switch (selectNum) {
              case 1 -> {
                dispatchService.dispatchAdd();
              }
              case 2 -> {
                dispatchService.dispatchList();
              }
              case 3 -> {
                dispatchService.dispatchModify();
              }
              case 4 -> {
                dispatchService.dispatchCancle();
              }
            }printMenu(userVO);
          }
          case 4 -> {
            System.out.println("1.운송장등록 | 2.운송장리스트 조회 | 3.운송장 수정");
            selectNum = Integer.parseInt(bf.readLine());
            switch (selectNum) {
              case 1 -> {
                waybillService.waybillAdd();
              }
              case 2 -> {
                waybillService.waybillList(userVO);
              }
              case 3 -> {
                waybillService.waybillModify();
              }
            }
            printMenu(userVO);
          }
          default -> {break;}
        }

      } catch (IOException e) {
      }
    }
    // 일반유저
    else {
      try {
        System.out.println("==출고 메뉴==");
        System.out.println("1.출고요청 | 2.출고리스트 조회 | 3.출고상품 검색 | 4.운송장 조회 | 5. 나가기");
        selectNum = Integer.parseInt(bf.readLine());
        switch (selectNum) {
          case 1 -> {
            releaseService.releaseRequest(userVO);
            printMenu(userVO);
          }
          case 2 -> {
            releaseService.releaseList(userVO);
            printMenu(userVO);
          }
          case 3 -> {
            releaseService.releaseSearch(userVO);
            printMenu(userVO);
          }
          case 4 ->{
            waybillService.waybillList(userVO);
            printMenu(userVO);
          }
          default -> {break;}
        }

      } catch (IOException e) {
      }


    }
  }

  // 출고 요청하기 / 둘다가능
  public void releaseRequest(UserVO userVO) {

    int releaseQuentity = 0;
    int userId = 0;
    int releaseWarehouseId = 0;
    int releaseProductId = 0;
    Date releaseDate = new Date(); // 현재시간 구하기
    String input;
    try {
      System.out.println("==출고 요청합니다==");

      // 수량 입력
      System.out.println("==수량을 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      releaseQuentity = Integer.parseInt(input);

      // 유저아이디 가져오기
      userId = userVO.getUserID();

      // 창고번호 입력
      System.out.println("==창고번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      releaseWarehouseId = Integer.parseInt(input);

      // 출고 상품 iD 입력
      System.out.println("==출고할 상품ID 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      releaseProductId = Integer.parseInt(input);
    } catch (ReleaseException e) {
      releaseRequest(userVO);
    } catch (Exception e) {
    }
    ReleaseVO releaseVO = new ReleaseVO(releaseDate, releaseQuentity, 0, 0, 1, 1, userId, releaseWarehouseId, releaseProductId);
    releaseDao.releaseRequestInsert(releaseVO); // insert할 객체 보냄
  }

  // 출고리스트 출력하기 / 유저, 관리자 구분
  public void releaseList(UserVO userVO) {
    List<ReleaseVO> releaseList; // 리스트

    System.out.println("==출고 리스트 출력합니다==");
    releaseList = releaseDao.releaseListSelect(userVO);  // 리스트 출력리스트
    releaseListPrint(releaseList); // 리스트 출력
  }


  // 출고 상품 검색 / 유저 관리자 구분
  public void releaseSearch(UserVO userVO) {
    List<ReleaseVO> releaseList;
    String searchProduct = null;
    try {
      System.out.println("==검색할 출고 상품이름을 입력하세요==");
      searchProduct = bf.readLine();
    } catch (ReleaseException e) {
      releaseSearch(userVO);
    } catch (IOException e) {

    }

    System.out.println("== 해당 상품의 출고리스트를 출력합니다==");
    releaseList = releaseDao.releaseSearchSelect(userVO, searchProduct);
    releaseListPrint(releaseList); // 리스트 출력
  }

  // 리스트로 받아서 프린트
  public void releaseListPrint(List<ReleaseVO> releaseList) {
    for (ReleaseVO release : releaseList) {
      releasePrint(release);
    }
  }

  // 미승인 리스트 출력 / 관리자가능
  public void releaseApproveList() {
    List<ReleaseVO> releaseList;
    System.out.println("==미승인 리스트 출력==");
    releaseList = releaseDao.releaseApproveListSelect();
    releaseListPrint(releaseList); // 리스트 출력
  }

  // 승인하기 / 관리자 가능
  public void releaseApprove() {
    int wayNum = 0;
    int dNum = 0;
    int searchNum = 0;
    int approvalNum = 0;
    String input;

    try {
      System.out.println("==수정하기==");
      // 수정할 출고번호 입력
      System.out.println("==수정할 출고번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      searchNum = Integer.parseInt(input);

      // 승인할 0,1 입력
      System.out.println("==승인하시려면 1, 미승인바꾸시려면 0 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validZeoroOneNumber(input);
      approvalNum = Integer.parseInt(input); // 승인여부
    } catch (ReleaseException e) {
      releaseApprove();
    } catch (IOException e) {

    }
    // 출고번호로 승인해주기
    boolean valid = releaseDao.releaseApproveUpdate(searchNum, approvalNum);

    if (approvalNum == 1 && valid) {
      waybillService.waybillAdd();// 운송장 추가
      dNum = dispatchService.dispatchAdd(); // 배차 추가
    }

    // 운송장, 대차 수정
    if (approvalNum == 1 && valid) {
      releaseDao.releaseDispatchWaybillUpdate(waynum, dNum);
    }
//    System.out.println("==변경완료==");
  }

  // release 객체  한개 출력
  public void releasePrint(ReleaseVO release) {
    System.out.println("출고아이디: " + release.getId()
                       + " 출고날짜: " + release.getDate()
                       + " 출고수량: " + release.getQuentity()
                       + " 출고상태: " + release.getState()
                       + " 승인여부: " + release.getApproval()
                       + " 배차아이디: " + release.getDispatchId()
                       + " 운송장아이디: " + release.getWaybillId()
                       + " 상품아이디: " + release.getProductId()
                       + " 창고아이디: " + release.getWarehouseId());
  }


}
