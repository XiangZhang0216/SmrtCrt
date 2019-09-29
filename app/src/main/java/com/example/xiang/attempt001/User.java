package com.example.xiang.attempt001;


/**
 * Created by xiang on 2017-06-18.
 */

public class User {

    private String name;
    private String email;
    private String password;
    private String created_at;
    private String newPassword;
    private String token;
    private String _id;
    private String developerToolCode;
    private String[] cart;
    private String suggestions;
    private String SNType;
    private String cartstring;
    private String lookupid;
    private String Message;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDeveloperToolCode(String toolCode){this.developerToolCode = toolCode;}

    public void addToCart(String newItem, int quantity){
        if (this.cart == null)
        {
            this.cart = new String[quantity];
            for (int c1 = 0; c1< quantity; c1++)
            {
               this.cart[c1] = newItem;
            }
        }
        else
        {
            String[] newArr = new String[this.cart.length+quantity];//3
            for (int c = 0; c< this.cart.length; c++)
            {
                newArr[c]=this.cart[c];

            }
            for (int q = 0; q< quantity; q++)
            {
                newArr[this.cart.length+q]=newItem;
            }
            int oldL = this.cart.length;
            this.cart = new String[oldL+quantity];

            for (int d = 0; d<oldL+quantity;d++)
            {
                this.cart[d]=newArr[d];
            }
        }
    }
    public int deleteFromCart(String toDelete, int quantity){
        if(this.cart==null)
        {
            return 0;
        }

        //Count if quantity is valid first.
        int qc = 0;

        for (int qct = 0; qct<this.cart.length;qct++)
        {
            if(this.cart[qct].equals(toDelete))
            {
                qc++;
            }
        }

        if (qc>=quantity)
        {
            for (int tc = 0; tc<quantity; tc++)
            {
                int oldL = this.cart.length;
                int frontHalfCount = 0;
                for (int c = 0; c<oldL;c++)
                {
                    if (!(this.cart[c].equals(toDelete)))
                    {
                        frontHalfCount++;
                    }
                    else
                    {
                        break;
                    }
                }

                int backHalfCount = oldL-frontHalfCount-1;
                String[] newArr = new String[oldL-1];

                for (int putA = 0; putA<frontHalfCount;putA++)
                {
                    newArr[putA]=this.cart[putA];
                }
                for (int putB = 0; putB<backHalfCount;putB++)
                {
                    newArr[frontHalfCount+putB]=this.cart[frontHalfCount+putB+1];
                }
                this.cart = new String[oldL-1];
                for (int f = 0; f<oldL-1;f++)
                {
                    this.cart[f]=newArr[f];
                }
            }

        }

        else
        {return 2;}
        return 1;

    }
    public String[] getCart() {return cart;}
    public int getCartSize(){return cart.length;}
    public void setCartstring(String cartString){this.cartstring = cartString;}
    public String getCartstring(){return cartstring;}
    public void setLookupid(String lookup) {this.lookupid = lookup;}
    public String getLookupid(){return lookupid;}
    public String getId(){return _id;}
    public void clearCartArray(){this.cart = null;}
    public void setSuggestions(String suggestions){this.suggestions = suggestions;}
    public String getSuggestions(){return suggestions;}
    public void setMessage(String message){this.Message = message;}
    public String getMessage(){return Message;}

}