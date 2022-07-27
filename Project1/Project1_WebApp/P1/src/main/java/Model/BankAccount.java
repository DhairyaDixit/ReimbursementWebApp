package Model;

public class BankAccount {

    private String accountNo;
    private String routingNo;
    private String accountType;
    private String bankName;

    public BankAccount() {
    }

    public BankAccount(String accountNo, String routingNo, String accountType, String bankName) {
        this.accountNo = accountNo;
        this.routingNo = routingNo;
        this.accountType = accountType;
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRoutingNo() {
        return routingNo;
    }

    public void setRoutingNo(String routingNo) {
        this.routingNo = routingNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
