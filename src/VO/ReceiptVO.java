package VO;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ReceiptVO {
    private int receiptId;      // 입고 요청 ID
    private String receiptDate;       // 입고 날짜
    private int productQuantity;        // 상품 수량
    private int state;      // 상태
    private int qrCode;     // qr 코드
    private int approval = 0;       // 입고 요청 승인
    private int uId;         //
    private int pId;      // 상품 ID
    private int wId;        // 창고 ID
    public ReceiptVO(){}
    public ReceiptVO(String receiptDate, int productQuantity, int uId, int pId, int wId,int approval) {
        this.receiptDate = receiptDate;
        this.productQuantity = productQuantity;
        this.uId = uId;
        this.pId = pId;
        this.wId = wId;
        this.approval = approval;
    }

}
