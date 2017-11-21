package ch.uzh.csg.nfcbridge;

public class TransactionDTO {
    String bazoaddress;
    String amount;
    String posid;

    public TransactionDTO(String bazoaddress, String amount, String posid){
        this.bazoaddress = bazoaddress;
        this.amount = amount;
        this.posid = posid;
    }
}
