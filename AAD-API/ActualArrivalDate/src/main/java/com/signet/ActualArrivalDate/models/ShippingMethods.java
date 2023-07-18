package com.signet.ActualArrivalDate.models;

public class ShippingMethods {

    String Second_Day_Air = "2";
    String Next_Day_Air = "1";
    String Ship_to_Store = "2";
    String USPS_Priority = "3";
    String UPS_Surepost = "8";
    String Ground = "5";
    String Second_Business_Day = "1";
    String ShopRunner = "2";
    String Surepost_APO_FPO_DPA_PO = "13";
    String Surepost_Street = "8";

    public ShippingMethods() {

    }

    public int getLeadTime(String method){

        if(method.equals("2nd Day Air")){
            return Integer.parseInt(Second_Day_Air);
        }
        else if(method.equals("Next Day Air")){
            return Integer.parseInt(Next_Day_Air);
        }
        else if(method.equals("Ship to Store")){
            return Integer.parseInt(Ship_to_Store);
        }
        else if(method.equals("USPS Priority")){
            return Integer.parseInt(USPS_Priority);
        }
        else if(method.equals("UPS Surepost")){
            return Integer.parseInt(UPS_Surepost);
        }
        else if(method.equals("Ground")){
            return Integer.parseInt(Ground);
        }
        else if(method.equals("2nd Business Day")){
            return Integer.parseInt(Second_Business_Day);
        }
        else if(method.equals("ShopRunner")){
            return Integer.parseInt(ShopRunner);
        }
        else if(method.equals("Surepost APO/FPO/DPA/PO")){
            return Integer.parseInt(Surepost_APO_FPO_DPA_PO);
        }
        else if(method.equals("Surepost Street")){
            return Integer.parseInt(Surepost_Street);
        }
        else{
            return 0;
        }

    }
    
}
