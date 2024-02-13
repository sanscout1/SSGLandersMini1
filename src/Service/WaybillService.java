package Service;

import DAO.WaybillDao;
import VO.ReleaseVO;
import VO.UserVO;
import VO.WaybillVO;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class WaybillService {
  WaybillDao waybillDao = new WaybillDao();
  Scanner sc = new Scanner(System.in);

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
    System.out.println("==운송장 등록합니다==");
    System.out.println("==보내는사람 입력하세요==");
    String dep_name = sc.nextLine();
    System.out.println("==출발지 입력하세요==");
    String dep_city = sc.nextLine();
    System.out.println("==출발하는곳 번호 입력하세요==");
    int dep_city_num = Integer.parseInt(sc.nextLine());
    System.out.println("==받는사람 입력하세요==");
    String arr_name = sc.nextLine();
    System.out.println("==받는도시입력하세요==");
    String arr_city = sc.nextLine();
    System.out.println("==받는도시번호 입력하세요==");
    int arr_city_num = Integer.parseInt(sc.nextLine());
    System.out.println("==운송장번호 입력하세요==");
    int way_num = Integer.parseInt(sc.nextLine());

    WaybillVO waybillVO = new WaybillVO( dep_name, dep_city, dep_city_num,  arr_name, arr_city,arr_city_num,way_num);
    waybillDao.waybillInsert(waybillVO); // insert할 객체 보냄
  }

  //운송장 수정
  public void waybillModify() {
    System.out.println("==운송장수정합니다==");
    System.out.println("==수정할 출고번호 입력하세요==");
    int searchNum = Integer.parseInt(sc.nextLine());

    System.out.println("==보내는사람 입력하세요==");
    String dep_name = sc.nextLine();
    System.out.println("==출발지 입력하세요==");
    String dep_city = sc.nextLine();
    System.out.println("==출발하는곳 번호 입력하세요==");
    int dep_city_num = Integer.parseInt(sc.nextLine());
    System.out.println("==받는사람 입력하세요==");
    String arr_name = sc.nextLine();
    System.out.println("==받는도시입력하세요==");
    String arr_city = sc.nextLine();
    System.out.println("==받는도시번호 입력하세요==");
    int arr_city_num = Integer.parseInt(sc.nextLine());
    System.out.println("==운송장번호 입력하세요==");
    int way_num = Integer.parseInt(sc.nextLine());

    WaybillVO waybillVO = new WaybillVO( dep_name, dep_city, dep_city_num,  arr_name, arr_city,arr_city_num,way_num);

    waybillDao.waybillUpdate(searchNum, waybillVO);
    System.out.println("==변경완료==");
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
