package VO;

public class WaybillVO {
  private int id;
  private String dep_name;
  private String dep_city;
  private int dep_city_num;
  private String arr_name;
  private String arr_city;
  private int arr_city_num;
  private int way_num;

  public WaybillVO(){

  }

  public WaybillVO(String dep_name, String dep_city, int dep_city_num, String arr_name, String arr_city, int arr_city_num, int way_num) {
    this.dep_name = dep_name;
    this.dep_city = dep_city;
    this.dep_city_num = dep_city_num;
    this.arr_name = arr_name;
    this.arr_city = arr_city;
    this.arr_city_num = arr_city_num;
    this.way_num = way_num;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDep_name() {
    return dep_name;
  }

  public void setDep_name(String dep_name) {
    this.dep_name = dep_name;
  }

  public String getDep_city() {
    return dep_city;
  }

  public void setDep_city(String dep_city) {
    this.dep_city = dep_city;
  }

  public int getDep_city_num() {
    return dep_city_num;
  }

  public void setDep_city_num(int dep_city_num) {
    this.dep_city_num = dep_city_num;
  }

  public String getArr_name() {
    return arr_name;
  }

  public void setArr_name(String arr_name) {
    this.arr_name = arr_name;
  }

  public String getArr_city() {
    return arr_city;
  }

  public void setArr_city(String arr_city) {
    this.arr_city = arr_city;
  }

  public int getArr_city_num() {
    return arr_city_num;
  }

  public void setArr_city_num(int arr_city_num) {
    this.arr_city_num = arr_city_num;
  }

  public int getWay_num() {
    return way_num;
  }

  public void setWay_num(int way_num) {
    this.way_num = way_num;
  }
}
