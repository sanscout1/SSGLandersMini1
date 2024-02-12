package VO;

import java.util.Date;

public class ReleaseVO {
  private int id;
  private Date date;
  private int quentity;
  private int state;
  private int approval;
  private int dispatchId;
  private int waybillId;
  private int userId;
  private int warehouseId;

  public ReleaseVO(Date date, int quentity, int state, int approval, int dispatchId, int waybillId, int userId, int warehouseId, int productId) {
    this.date = date;
    this.quentity = quentity;
    this.state = state;
    this.approval = approval;
    this.dispatchId = dispatchId;
    this.waybillId = waybillId;
    this.userId = userId;
    this.warehouseId = warehouseId;
    this.productId = productId;
  }

  private int productId;

  public ReleaseVO(){
  }


  public int getQuentity() {
    return quentity;
  }

  public void setQuentity(int quentity) {
    this.quentity = quentity;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getApproval() {
    return approval;
  }

  public void setApproval(int approval) {
    this.approval = approval;
  }

  public int getDispatchId() {
    return dispatchId;
  }

  public void setDispatchId(int dispatchId) {
    this.dispatchId = dispatchId;
  }

  public int getWaybillId() {
    return waybillId;
  }

  public void setWaybillId(int waybillId) {
    this.waybillId = waybillId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getWarehouseId() {
    return warehouseId;
  }

  public void setWarehouseId(int warehouseId) {
    this.warehouseId = warehouseId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  @Override
  public String toString() {
    return "ReleaseVO{" +
            "id=" + id +
            ", date=" + date +
            ", quentity=" + quentity +
            ", state=" + state +
            ", approval=" + approval +
            ", dispatchId=" + dispatchId +
            ", waybillId=" + waybillId +
            ", userId=" + userId +
            ", warehouseId=" + warehouseId +
            ", productId=" + productId +
            '}';
  }

}
