/*
Ass1
Haoran Cheng
hc633
 */
package ass1;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class Ass1 
{
      int count = 0;
      int count1 = 0;
      int lastCount = 0;
      //original list of words
      String[] Olist = new String[1000];  
      //original list of time count
      Integer[] Otimes = new Integer[1000];
      
    public static void main(String[] args){
    
     //create scanner to mention user input file name
     Scanner input = new Scanner(System.in);
     System.out.print("Input file name: ");
     String filename = input.next();
     
     //create an instance for calling the mehods(insert data into the list)  
     Ass1 a = new Ass1();
      
     //no repeating words
      List<String>  noReaptList = new ArrayList<String>();
      //no repeating times
      List<Integer> noReaptTime = new ArrayList<Integer>();
      //final results
      List<String> finalResult = new ArrayList<String>();

      //create an instance of node for calling the methods of BST
      node node = new node();
      
      //read in file
      try
      {
          //create buffer start read
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = in.readLine();
        //receive the reading results
        while(line !=null)
        { 
            //clear " " from the backward and frontward
          line = line.trim();
          //clear all punctuation
          line = line.replaceAll("[\"';:?.!,]", "");
          //convert all letters to lowercase
          line = line.toLowerCase();
          //cut line into single words and temporally save them into info list
          String[] info = line.split(" ");

          //print info list to get single words
          for(int i = 0; i < info.length; i++)
          {
            //insert the results(single words) into Olist(include repeating data)
            a.InsertToList(info[i]);
            //insert the results into noReateList,no repeating results
            if(!noReaptList.contains(info[i]))//if list not contains this words
            {
                noReaptList.add(info[i]);//add into noReateList
            }
          }
             line = in.readLine();//circle
        }
        in.close();//stop
      }
      //catch errors
      catch(IOException e)
      {
          //print errors
            System.out.println("Error information: "+e);
      }
      
      //this block is used to count reapeting times of each words
        for(String b: noReaptList)//print noReaptList
        {
          int count2 = 0;       
          for(String c:a.Olist)// print orignal list to compare with noReaptList
          {
             if(b.equals(c))//count how many times each words appear
             {
              count2++;
             }
          }
           a.inputTimes(count2);//save reapeting times into Otimes list
           //save reapeting times into noReaptTime list(no reapting data in this list)
           if(!noReaptTime.contains(count2))//if noReaptTime not contains this data go ahead
           {
               noReaptTime.add(count2);//add unique data into noReaptTime
            
           }
        } 
        
   //BST will be called in this block. 
  //print noReaptTime
    for(int t = 0; t<noReaptTime.size(); t++)
        //insert into BST
          node.insert(noReaptTime.get(t));

    //traserval BST
    for(int i: node.Traserval())
     {
        //this list create for sort the list by alphabet
       List<String> Alphabet = new ArrayList<String>();
         //traserval Otimes list to get each word's index
        for(int t = 0; t<a.Otimes.length; t++)
        {
            //filter null data in Otimes list
          if(a.Otimes[t] != null)
          {
            //word's index(not includes indexs of data which are null)
             if(i ==a.Otimes[t])//get the words fllow the BST sequence
             {
                 //noReaptList.get(t) already includes the words which are orderd by repeating times
                 Alphabet.add(noReaptList.get(t));//put these words into Alphabet list, to sort words by alphabet which are has same repeating times
             }
          }
         
        }    
        //alphabet sort begin
        //Alphabet.sort(new Comparator<String>(){public int compare(String s1,String s2){return s1.compareTo(s2); }});
        Collections.sort(Alphabet);
       //traserval the final results that words are fllowing decresing order by times and ascending order by alphabet
                for(String r:Alphabet)           
                   finalResult.add(String.valueOf(i+" "+r));//save the final result into finalResult list
     }
      
   System.out.println("---print first 10---");
    //print first 10 
   for(int l = 0; l < 10;l++)        
   System.out.println(finalResult.get(l));
    
    System.out.println("---print last 10---");
    //print last 10
    for(int l = finalResult.size()-1; l > finalResult.size()-11;l--)        
    System.out.println(finalResult.get(l));
    
    

} 
    //insert into Olist
    public void InsertToList(String value)
    {
        Olist[count] = value;
        count++;
    }
    //insert into Otimes
    public void inputTimes(int value)
    {
        Otimes[count1] = value;
        count1++;
    }
     
}

class node
{
   int value;
   node leftnode;
   node rightnode;

    public void insert(int v)
    {
        //if nothing in root, then set the first intput value as root
      if(value == 0)
         value = v;
     else
     {
       //if input value <= parent go ahead
       if( v <= value)
       {
          //nothing in left, ok , put the value in left
          if(leftnode ==null)
            //create child nodes for left
           leftnode = new node();
           leftnode.insert(v);
       }
       //if input value > parent go ahead
       else{
           //nothing in left, ok , put the value in right
        if(rightnode ==null)
            //create child nodes for right
          rightnode = new node();
          rightnode.insert(v);
        }
      }     
    }
    
    public List<Integer> Traserval()
    {
        //create a list to store the sorting results
        List<Integer> numbers = new ArrayList<Integer>();
        
        //node has value, add into array
        if(rightnode !=null)
           numbers.addAll(rightnode.Traserval());
        
            numbers.add(value);
       
        if(leftnode != null)
           numbers.addAll(leftnode.Traserval());
           return numbers;       
   }

}
