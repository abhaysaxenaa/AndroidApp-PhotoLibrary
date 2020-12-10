package Model;

import java.io.Serializable;
/*
 * @author Venkata Sai Karthik Gandrath
 * @author Abhay Saxena
 *
 *
 */


public class Tag implements Serializable{


    public String name, value;

    /*
     * constructor
     * @param name
     * @param value
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }


    /*
     * sets tag name
     */
    public void setName() {
        this.name = name;
    }

    /*
     * sets tag value
     */

    public void setValue() {
        this.value = value;
    }

    /*
     * @return name
     */
    public  String getType() {
        return name;
    }

    /*
     * @return value
     */
    public String getValue() {
        return value;
    }

    /*
     * @return string of tag
     */
    public String toString() {
        return "Name:" +name + " - value: " + value;
    }

    /*
     * boolean if equals to another tag
     */
    public boolean equals(Tag another) {
        if (another == null){
            return false;
        }
        else {
            return name.equals(another.name ) && value.equals(another.value);
        }
    }
}