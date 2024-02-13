package Service;

import DAO.ReleaseDao;
import VO.ReleaseVO;
import VO.UserVO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReleaseService {

  List<ReleaseVO> ReleaseRequest = new ArrayList<>();
  ReleaseDao releaseDao = new ReleaseDao();
  Scanner sc = new Scanner(System.in);

  // 출고 요청하기 / 둘다가능
  public void releaseRequest() throws ParseException {
    System.out.println("==출고 요청합니다==");

    Date releaseDate = new Date(); // 현재시간 구하기
    System.out.println("==수량을 입력하세요==");
    int releaseQuentity = Integer.parseInt(sc.nextLine());
    System.out.println("==아이디를 입력하세요==");
    int userId = Integer.parseInt(sc.nextLine());
    System.out.println("==창고번호 입력하세요==");
    int releaseWarehouseId = Integer.parseInt(sc.nextLine());
    System.out.println("==출고할 상품ID 입력하세요==");
    int releaseProductId = Integer.parseInt(sc.nextLine());

    ReleaseVO releaseVO = new ReleaseVO(releaseDate,releaseQuentity,0,0,1,1,userId,releaseWarehouseId,releaseProductId);
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

    System.out.println("==검색할 출고 상품이름을 입력하세요==");
    String searchProduct = sc.nextLine();

    System.out.println("== 해당 상품의 출고리스트를 출력합니다==");
    releaseList = releaseDao.releaseSearchSelect(userVO, searchProduct);
    releaseListPrint(releaseList); // 리스트 출력
  }

  // 리스트로 받아서 프린트
  public void releaseListPrint(List<ReleaseVO> releaseList){
    for (ReleaseVO release: releaseList ) {
      releasePrint(release);
    }
  }

  // 미승인 리스트 출력 / 관리자가능
  public void releaseApproveList(){
    List<ReleaseVO> releaseList;
    System.out.println("==미승인 리스트 출력==");
    releaseList = releaseDao.releaseApproveListSelect();
    releaseListPrint(releaseList); // 리스트 출력
  }

  // 승인하기 / 관리자 가능
  public void releaseApprove(){
    System.out.println("==수정하기==");
    System.out.println("==수정할 출고번호 입력하세요==");
    int searchNum = Integer.parseInt(sc.nextLine()); // 바꿀 출고번호
    System.out.println("==승인하시려면 1, 미승인바꾸시려면 0 입력하세요==");
    int approvalNum = Integer.parseInt(sc.nextLine());// 승인여부
    releaseDao.releaseApproveUpdate(searchNum, approvalNum);
    System.out.println("==변경완료==");
  }

  // release 객체  한개 출력
  public void releasePrint(ReleaseVO release){
    System.out.println("출고아이디: "+release.getId()
            +" 출고날짜: "+release.getDate()
            +" 출고수량: "+release.getQuentity()
            +" 출고상태: "+release.getState()
            +" 승인여부: "+release.getApproval()
            +" 배차아이디: "+release.getDispatchId()
            +" 운송장아이디: "+release.getWaybillId()
            +" 아이디: "+release.getId()
            +" 창고아이디: "+release.getWarehouseId());
  }



}
