package VO;

import java.util.Date;

public class DispatchVO {
  private int id;
  private int vehicle;
  private Date date;
  private int approval;

  public DispatchVO(){}

  public DispatchVO(int vehicle, Date date, int approval) {
    this.vehicle = vehicle;
    this.date = date;
    this.approval = approval;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getVehicle() {
    return vehicle;
  }

  public void setVehicle(int vehicle) {
    this.vehicle = vehicle;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getApproval() {
    return approval;
  }

  public void setApproval(int approval) {
    this.approval = approval;
  }
}
