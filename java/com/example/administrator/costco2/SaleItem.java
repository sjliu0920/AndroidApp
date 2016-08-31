package com.example.administrator.costco2;

/**
 * Created by double on 8/18/16.
 */
public class SaleItem {
    private String name;
    private int imageId;
    private double charge;
    private int judge;

    public SaleItem(String name, int imageId, double charge, int judge) {
        this.name = name;
        this.imageId = imageId;
        this.charge = charge;
        this.judge = judge;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
    public String getCharge(){
        return "$" + charge;
    }
    public String getJudge(){
       String strJudge = "";
        if(judge == 0)
            strJudge = "*";
        else if(judge == 1)
            strJudge = "*";
        else if(judge == 2)
            strJudge = "**";
        else if(judge == 3)
            strJudge = "***";
        else if(judge == 4)
            strJudge = "****";
        else if(judge == 5)
            strJudge = "*****";

        return strJudge;
    }
}
