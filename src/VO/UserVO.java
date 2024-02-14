package VO;

import lombok.Data;

@Data
public class UserVO {
  private int userID;
  private int userType;
  private String ID;
  private String password;
  private String userName;
  private String taxID;
  private String addressCity;
  private int addressNum;
  private boolean approval;

  public UserVO(int userID, int userType, String id, String password, String userName, String taxID,
                String addressCity, int addressNum, boolean approval) {
    this.userID = userID;
    this.userType = userType;
    this.ID = id;
    this.password = password;
    this.userName = userName;
    this.taxID = taxID;
    this.addressCity = addressCity;
    this.addressNum = addressNum;
    this.approval = approval;
  }
  public UserVO(){}
}