package Service;

import DAO.DispatchDao;
import VO.DispatchVO;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DispatchService {
  DispatchDao dispatchDao = new DispatchDao();
  Scanner sc = new Scanner(System.in);

  // 배차등록하기
  public int dispatchAdd() {
    System.out.println("==새로운 배차를 등록합니다==");
    System.out.println("==차량아이디 입력하세요==");
    int veh_id = Integer.parseInt(sc.nextLine());
    Date now = new Date();
    int approval = 1; // default approval: 1

    DispatchVO dispatchVO = new DispatchVO(veh_id, now, approval);
    int did = dispatchDao.dispatchInsert(dispatchVO);
    return did;
  }

  // 배차 리스트 출력 // approval 1인거 출력
  public void dispatchList() {
    List<DispatchVO> dispatchVOList;

    System.out.println("==출고 리스트 출력합니다==");

    dispatchVOList = dispatchDao.dispatchListSelect();  // 리스트 출력리스트
    for (DispatchVO dispatchVO : dispatchVOList) { // 전체출력
      dispatchPrint(dispatchVO);  // 객체 출력메소드
    }
  }

  // 배차 수정
  public void dispatchModify() {
    System.out.println("==배차내용 수정하기==");
    System.out.println("==수정할 배차번호 입력하세요==");
    int searchNum = Integer.parseInt(sc.nextLine());
    System.out.println("==차량번호를 입력하세요==");
    int vehicleNum = Integer.parseInt(sc.nextLine());// 승인여부
    dispatchDao.dispatchUpdate(searchNum, vehicleNum);
    System.out.println("==변경완료==");
  }

  // 배차 취소// approval 0으로 변경
  public void dispatchCancle() {
    System.out.println("==배차 취소하기==");
    System.out.println("==취소할 배차번호 입력하세요==");
    int searchNum = Integer.parseInt(sc.nextLine()); // 바꿀 출고번호
    int approvalNum = 0;
    dispatchDao.dispatchDelete(searchNum, approvalNum);
    System.out.println("==변경완료==");
  }

  // 배차 객체 하나 출력
  public void dispatchPrint(DispatchVO dispatchVo) {
    System.out.print("배차아이디: " + dispatchVo.getId()
                     + " 차량번호: " + dispatchVo.getVehicle()
                     + " 배차날짜: " + dispatchVo.getDate()
                     + " 배차확인: "
    );
    System.out.println((dispatchVo.getApproval() == 1) ? "가능" : "불가능");

  }


}
