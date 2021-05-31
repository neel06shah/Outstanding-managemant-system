package com.example.kanchancollection.models;

public class receipt {
    private String Party,Type,Amount,ChequeNo,ChequeDate,Bank,date_type,Date,id,ChequeBranch;
    receipt(){}

    public receipt(String party, String type, String amount, String chequeNo, String chequeDate, String bank, String date_type, String date, String id, String chequeBranch) {
        Party = party;
        Type = type;
        Amount = amount;
        ChequeNo = chequeNo;
        ChequeDate = chequeDate;
        Bank = bank;
        this.date_type = date_type;
        Date = date;
        this.id = id;
        ChequeBranch = chequeBranch;
    }

    public String getChequeBranch() {
        return ChequeBranch;
    }

    public void setChequeBranch(String chequeBranch) {
        ChequeBranch = chequeBranch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParty() {
        return Party;
    }

    public void setParty(String party) {
        Party = party;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }

    public String getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(String chequeDate) {
        ChequeDate = chequeDate;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getDate_type() {
        return date_type;
    }

    public void setDate_type(String date_type) {
        this.date_type = date_type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
