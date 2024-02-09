package VO;


import lombok.Data;

@Data
public class UserVO {
    private int userID;
    private int userType;
    private String ID;
    private String userName;
    private String taxID;
    private String addressCity;
    private int addressNum;
    private boolean approval;

}
