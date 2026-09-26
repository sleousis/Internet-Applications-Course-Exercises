package BVShop;
//if present 'package' MUST be the first statement (possibly) followed by 'import'
// The following class follows the structure of a 'Java Bean' 

public class ManBean {
     /**The properties of the VehicleBean*/
     String ManName;
     Integer ManYear;
     String ManCountry;
     
     /** The following non-argument constructor MUST be always present*/
     /** for this class to be a Java Bean*/
     public ManBean(){}
     
     /** Another constructor CAN also be always present*/
     /**  the following initializes all properties whenever a new object is instantiated (with 'new') - */
     public ManBean(String name,Integer year,String country){
      this.ManName= name; this.ManYear = year; this.ManCountry= country;}
     
     /** get & set methods for all properties MUST be present*/
     public String getManName(){return ManName;}
     public void setManName(String name){this.ManName = name;}   
     public Integer getManYear(){return ManYear;}
     public void setManYear(Integer year){this.ManYear = year;}            
     public String getManCountry(){return ManCountry;}
     public void setManCountry(String country){this.ManCountry = country;}
     
     //toString is an in-built method for every Java object. Outputs an informative string
     public String toString(){
        return "'" + ManName + "' in " + ManCountry + " (est. " + ManYear.toString() + ")";}  
}