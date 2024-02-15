package Service;

import DAO.WaybillDao;
import Exception.ReleaseException.ReleaseException;
import Exception.ReleaseException.ReleaseExceptionList;
import VO.ReleaseVO;
import VO.UserVO;
import VO.WaybillVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class WaybillService {

  WaybillDao waybillDao = new WaybillDao();
  BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

  // 운송장 내역출력 / 관리자, 유저 구분
  public void waybillList(UserVO userVO) {
    List<WaybillVO> waybillVOList;

    System.out.println("==출고 리스트 출력합니다==");

    waybillVOList = waybillDao.waybillListSelect(userVO);  // 리스트 출력리스트
    for (WaybillVO waybillVO : waybillVOList) {
      waybillPrint(waybillVO); // 리스트 출력
    }

  }

  //운송장 등록
  public void waybillAdd() {
    String dep_name = null;
    String dep_city = null;
    int dep_city_num = 0;
    String arr_name = null;
    String arr_city = null;
    int arr_city_num = 0;
    String input;
    int wayNum = 0;

    try {
      System.out.println("==운송장 등록합니다==");
      System.out.println("==보내는사람 입력하세요==");
      dep_name = bf.readLine();
      System.out.println("==출발지 입력하세요==");
      dep_city = bf.readLine();

      // 출발지 번호 입력
      System.out.println("==출발하는곳 번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      dep_city_num = Integer.parseInt(input);

      System.out.println("==받는사람 입력하세요==");
      arr_name = bf.readLine();
      System.out.println("==받는도시입력하세요==");
      arr_city = bf.readLine();

      // 받는 도시 번호 입력
      System.out.println("==받는도시번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      arr_city_num = Integer.parseInt(input);

      int way_num = (int)(Math.random()*100)+1; // 운송장번호 자동생성
      WaybillVO waybillVO = new WaybillVO( dep_name, dep_city, dep_city_num,  arr_name, arr_city,arr_city_num,way_num);
      waybillDao.waybillInsert(waybillVO); // insert할 객체 보냄

    }catch (ReleaseException e) {
      waybillAdd();
    }catch (IOException e){

    }



//    return wayNum;
  }

  //운송장 수정
  public void waybillModify() {
    int searchNum = 0;
    String dep_name = null;
    String dep_city = null;
    int dep_city_num =0;
    String arr_name = null;
    String arr_city = null;
    int arr_city_num = 0;
    int way_num = 0;
    String input;

    try {
      System.out.println("==운송장수정합니다==");
      // 수정할 출고번호
      System.out.println("==수정할 운송장id 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      searchNum = Integer.parseInt(input);

      System.out.println("==보내는사람 입력하세요==");
      dep_name = bf.readLine();
      System.out.println("==출발지 입력하세요==");
      dep_city = bf.readLine();

      //출발지 번호 입력
      System.out.println("==출발하는곳 번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      dep_city_num = Integer.parseInt(input);

      System.out.println("==받는사람 입력하세요==");
      arr_name = bf.readLine();
      System.out.println("==받는도시입력하세요==");
      arr_city = bf.readLine();

      // 받는곳 번호 입력
      System.out.println("==받는도시번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      arr_city_num = Integer.parseInt(input);

      // 운송장번호 입력
      System.out.println("==운송장번호 입력하세요==");
      input = bf.readLine();
      ReleaseExceptionList.validOverZeroNumber(input);
      way_num = Integer.parseInt(input);

    }catch (ReleaseException e) {
      waybillModify();
    }catch (IOException e){

    }

    WaybillVO waybillVO = new WaybillVO( dep_name, dep_city, dep_city_num,  arr_name, arr_city,arr_city_num,way_num);

    boolean valid = waybillDao.waybillUpdate(searchNum, waybillVO);
    if(valid){
      System.out.println("==변경완료==");

    }
  }

  //운송장 한 객체 출력
  public void waybillPrint(WaybillVO waybillVO) {
    System.out.println("운송장번호: " + waybillVO.getId()
                       + " 보내는사람: " + waybillVO.getDep_name()
                       + " 보내는곳: " + waybillVO.getDep_city()
                       + " 보내는곳 번호: " + waybillVO.getDep_city_num()
                       + " 받는사람: " + waybillVO.getArr_name()
                       + " 받는곳: " + waybillVO.getArr_city()
                       + " 받는곳 번호: " + waybillVO.getArr_city_num()
                       + " 운송장 번호: " + waybillVO.getArr_city_num()
    );
  }
}
