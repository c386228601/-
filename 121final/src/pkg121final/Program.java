import java.util.*;

public class Program 
{
    public static void main(String[] args) 
    {
        Account acc1 = new Account ("Jim", 500);
        Account acc2 = new Account ("Kim", 100);
        Account acc3 = new Account ("Sue", 150);
        Account acc4 = new Account ("Jan", 120);
       
        ArrayList<Account> accs = new ArrayList<>();
        accs.add (acc1);
        accs.add (acc2);
        accs.add (acc3);
        accs.add (acc4);
        
//      Collections.sort (accs);
//      accs.forEach(x->x.print());

        HashMap<String,Account> map = new HashMap<String,Account>();
        
  /*          map.put(acc1.getID(),acc1);
            map.put(acc2.getID(),acc2);
            map.put(acc3.getID(),acc3);
            map.put(acc4.getID(),acc4);
  */
  
        for(Account acc : accs)
            map.put(acc.getID(), acc);
        System.out.println (map);
        
        String key = "Sue";
        Account found = map.get (key);
        System.out.println ("Found account: " + found);
        
        
    }
    
    public static void main1(String[] args) 
    {
/*        
        Account acc = new Account ("Fred", 0);
        acc.print ();
        acc.increaseBalance(150);
        acc.print ();
*/
        Account acc1 = new Account ("Jim", 500);
        Account acc2 = new Account ("Kim", 100);
        
//      acc1.transferTo (acc2, 50);
        Account.transferBetween (acc1, acc2, 50);

        acc1.print ();
        acc2.print ();
        
        ViewAccount accV = acc1;
        accV.print ();
    }
}

interface ViewAccount 
{
    public String getID ();
    public void print ();
    
}

class Account implements Comparable<Account>, ViewAccount
{
    private String id;
    private double balance;
    
    public Account (String id, double balance)
    {
        this.id = id;
        this.balance = balance;
    }
    
    public String getID ()
    {
        return this.id;
    }
    
    public double getBalance()
    {
        return this.balance;
    }
    
    public int compareTo (Account other)
    {
        return this.id.compareTo(other.id);
    }
/*    
   public int compareTo (Account other)
    {
        if (this.balance == other.balance)
            return 0;
        
        if (this.balance > other.balance)
            return -1;
        else
            return +1;
    
    //  return this.balance - other.balance;
    }
*/    
    public static boolean transferBetween (Account source, Account dest, double amount)
    {
       if (source.balance < amount)
            return false;
        
        source.balance -= amount;
        dest.balance   += amount;
        return true;
    }
    
    public void increaseBalance (double amount)
    {
        this.balance += amount;
    }
    
    public void print ()
    {
        System.out.println (this.id + " " + this.balance);
    }
    
    public String toString ()
    {
        return this.id + " " + this.balance;
    }
    
    public boolean transferTo (Account dest, double amount)
    {
        if (this.balance < amount)
            return false;
        
        this.balance -= amount;
        dest.balance += amount;
        return true;
    }
}
