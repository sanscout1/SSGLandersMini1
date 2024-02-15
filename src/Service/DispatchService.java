package Service;

import DAO.DispatchDao;
import Exception.ReleaseException.ReleaseException;
import Exception.ReleaseException.ReleaseExceptionList;
import VO.DispatchVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DispatchService {
  DispatchDao dispatchDao = new DispatchDao();
  BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
  int did;

  // 배차등록하기
  public int dispatchAdd() {
    int veh_id = 0;
    String input;
    System.out.println("==새로운 배차를 등록합니다==");
    // 차량아이디 입력
    System.out.println("==차량번호 입력하세요==");
    try {
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      veh_id = Integer.parseInt(input);
      Date now = new Date();
      int approval = 1; // default approval: 1

      DispatchVO dispatchVO = new DispatchVO(veh_id, now, approval);
      did = dispatchDao.dispatchInsert(dispatchVO);
      System.out.println("==차량이 등록되었습니다==");
    } catch (ReleaseException e) {
      dispatchAdd();
    }catch (IOException e){

    }

    return did;
  }

  // 배차 리스트 출력 // approval 1인거 출력
  public void dispatchList() {
    List<DispatchVO> dispatchVOList;

    System.out.println("==배차 리스트 출력합니다==");

    dispatchVOList = dispatchDao.dispatchListSelect();  // 리스트 출력리스트
    for (DispatchVO dispatchVO : dispatchVOList) { // 전체출력
      dispatchPrint(dispatchVO);  // 객체 출력메소드
    }
  }

  // 배차 수정
  public void dispatchModify() {
    int searchNum = 0;
    int vehicleNum = 0;
    String input;
    System.out.println("==배차내용 수정하기==");
    // 배차내용 수정하기
    System.out.println("==수정할 배차번호 입력하세요==");
    try {
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      searchNum = Integer.parseInt(input);
    } catch (ReleaseException e) {
      dispatchModify();
    }catch (IOException e){

    }
    System.out.println("==차량번호를 입력하세요==");
    try {
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      vehicleNum = Integer.parseInt(input);
    } catch (ReleaseException e) {
      dispatchModify();
    }catch (IOException e){

    }
    boolean valid = dispatchDao.dispatchUpdate(searchNum, vehicleNum);
    if(valid){
      System.out.println("==변경완료==");
    }
  }

  // 배차 취소// approval 0으로 변경
  public void dispatchCancle() {
    int searchNum = 0;
    String input;
    System.out.println("==배차 취소하기==");
    System.out.println("==취소할 배차번호 입력하세요==");
    try {
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      searchNum = Integer.parseInt(input); // 바꿀 출고번호
    } catch (ReleaseException e) {
      dispatchCancle();
    }catch (IOException e){

    }
    int approvalNum = 0;
    boolean valid = dispatchDao.dispatchDelete(searchNum, approvalNum);
    if(valid){
      System.out.println("==변경완료==");
    }
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
