package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.view.EZShopGUI;
//import it.polito.ezshop.classes.EZShopDB;

public class EZShop {

    public static void main(String[] args){
    	//EZShopDB db = new EZShopDB();
    	//db.createConnection();
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        EZShopGUI gui = new EZShopGUI(ezShop);
    }

}
