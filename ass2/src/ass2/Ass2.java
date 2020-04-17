package ass2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Ass2 
{  
    int busy_1 = 0;
    int busy_2 = 0;
    int pqueue_length = 0;
    int squeue_length = 0;
    
    int count_customer = 0;
    int server1_number;
    int server2_number;
    
    int q_length1 = 0;
    int q_length2 = 0;
    
    int[] q1length = new int[10000];
    int[] q2length = new int[10000];

    customer[] clist = new customer[10000];
  
    int pqueue_size =0;
    server[] primary;
    server[] secondary;
        
   customer[] pqueue = new customer[10000];
    //customer[] pqueue;
    customer[] squeue = new customer[10000];
    
    
    public static void main(String[] args) 
    {
         Ass2 a2 = new Ass2();
         
         
         Scanner input = new Scanner(System.in); 
         System.out.print("input file name: ");
        String fname = input.next();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(fname));
            String line = in.readLine();

            int c =0;
            while (line != null) 
            {
                 c++;
                line = line.trim();
                
                String[] data = line.split("\\s+");
                     if(c == 1)
                     {
                      a2.server1_number = Integer.parseInt(data[0]);
                     }
                     if(c == 2)
                     {
                      a2.server2_number = Integer.parseInt(data[0]);
                     }                     
                     if(c > 2)
                     {
                        if(Double.parseDouble(data[0])!=0)
                        a2.insert_customer(0,0,Double.parseDouble(data[0]),Double.parseDouble(data[1]),Double.parseDouble(data[2]),0.0,0.0);
                     }
 
                line = in.readLine();
            }
            in.close();
        }
        catch (IOException e) 
        {
            System.out.println("error info: " + e);
        }
          
        a2.primary = new server[a2.server1_number];
        a2.insert_server1();
        
        a2.secondary = new server[a2.server2_number];
        a2.insert_server2();
       
           for(int i = 0; i < a2.clist.length;i++)
           {
             if(a2.clist[i] != null)
                 a2.initial_primary(a2.clist[i]);
           }

          a2.print();
         //System.out.println(a2.pqueue_length);   

    }

    public void initial_primary(customer customer)
    {     
            if(busy_1 == server1_number)
            {
                //System.out.println(2);
                call_siftup(primary);
                if(primary[0].end_time < customer.arrival)
                {
                   //print();  
                 end_primary(customer);
                }
                else
                { 
                 parrival(customer);
                }
            }
            else
            {   
              parrival(customer);
            }
        
    }
    
    public void initial_secondary(customer customer)
    {     
            if(busy_2 == server2_number)
            {
                //System.out.println(2);
                call_siftup(secondary);
                if(secondary[0].end_time < customer.end_timep)
                {
                   //print();  
                 end_secondary(customer);
                }
                else
                { 
                 sarrival(customer);
                }
            }
            else
            {   
              sarrival(customer);
            }
        
    }
    
    
       
    public void parrival(customer customer)
    {      
       call_siftdown(primary);

       if(busy_1 < server1_number)
       { 
          if(primary[busy_1].end_time < customer.arrival)
          {
           primary[busy_1].end_time = customer.arrival + customer.stime_1;
           customer.end_timep = primary[busy_1].end_time;
          } 
          else 
          {
            primary[busy_1].end_time = primary[busy_1].end_time + customer.stime_1;
            customer.end_timep = primary[busy_1].end_time;
          }
          customer.serverp_id = primary[busy_1].id;    
         initial_secondary(customer);
          busy_1++;
       }
       else
       {
         //System.out.println(++pqueue_size);
         in_pqueue(customer); 
       }       
    }

    
     public void sarrival(customer customer)
    {      
       call_siftdown(secondary);

       if(busy_2 < server2_number)
       { 
          if(secondary[busy_2].end_time < customer.end_timep)
          {
           secondary[busy_2].end_time = customer.end_timep + customer.stime_2;
           customer.end_times = secondary[busy_2].end_time;
          } 
          else 
          {
            secondary[busy_2].end_time = secondary[busy_2].end_time + customer.stime_2;
            customer.end_times = secondary[busy_2].end_time;
          }
          customer.servers_id = secondary[busy_2].id;    
          busy_2++;
       }
       else
       {
         //System.out.println(++pqueue_size);
         in_squeue(customer); 
       }       
    }
      
    public void end_primary(customer customer)
    {
        if(pqueue[0] == null )
        {
          primary[0].end_time = customer.arrival + customer.stime_1;   
           customer.end_timep = primary[0].end_time;
           customer.serverp_id = primary[0].id; 
           initial_secondary(customer);
        }
        else
        {  
            in_pqueue(customer);
            for(int i = 0; i <= pqueue_length;i++)
            {
                call_siftup(primary);  
                if(primary[0].end_time<pqueue[i].arrival)
                {
                  primary[0].end_time = pqueue[i].arrival + pqueue[i].stime_1;
                }
                else
                {
                   primary[0].end_time = primary[0].end_time + pqueue[i].stime_1;
                }
            pqueue[i].end_timep = primary[0].end_time;
            pqueue[i].serverp_id = primary[0].id; 
            initial_secondary(pqueue[i]);
            pqueue_length--;
           // System.out.println(pqueue_length);
            }       
        }
    } 
    
    public void end_secondary(customer customer)
    {
        if(squeue[0] == null )
        {
          secondary[0].end_time = customer.end_timep + customer.stime_2;   
           customer.end_times = secondary[0].end_time;
            customer.servers_id = secondary[0].id;   
        }
        else
        {  
            in_squeue(customer);
            for(int i = 0; i <= squeue_length;i++)
            {
                call_siftup(secondary);  
                if(secondary[0].end_time<squeue[i].end_timep)
                {
                  secondary[0].end_time = squeue[i].end_timep + squeue[i].stime_2;
                }
                else
                {
                   secondary[0].end_time = secondary[0].end_time + squeue[i].stime_2;
                }
            customer.end_times = secondary[0].end_time;
            customer.servers_id = secondary[0].id;   
            squeue_length--;
            }       
        }
    } 
    
    
    
    public void in_pqueue(customer customer)
    {
      pqueue[pqueue_length] = customer;
      siftup_queues(pqueue,pqueue_length); 
      pqueue_length++;
      count_q1(pqueue_length);
      insert_q1length(pqueue_length);
    }
    
    public void in_squeue(customer customer)
    {
      squeue[squeue_length] = customer;
      siftup_queues(squeue,squeue_length); 
      squeue_length++;
      count_q2(squeue_length);
      insert_q2length(squeue_length);
    }

    public void siftup_queues(customer[] slist,int index)
    {
         int p;
        if (index == 0)
        {
            return;
        }

        if (index % 2 == 0)
        {
            p = index / 2 - 1;
        } 
        else
        {
            p = (index - 1) / 2;
        }

        if (slist[p].arrival > slist[index].arrival) 
        {
            swap_queues(slist,p, index);
        }
        siftup_queues(slist,p);
        
    }
    public void swap_queues(customer[]slist,int p, int i)
    {
       customer a = slist[i];
       slist[i] = slist[p];
       slist[p] = a;
    }
    
    
    
    public void siftdown_qlength(int[] slist)
    {
        int mid = 0;
    
        if (slist.length % 2 == 0) {
            mid = (slist.length - 2) / 2;
        } else {
            mid = (slist.length - 3) / 2;
        }

        for (int i = mid; i >= 0; i--) {
            siftdown(slist,i);
        }
    }
     
    public void siftdown(int[] slist,int index)
    {
        int c = index * 2 + 1;
        
        if (c + 1 < slist.length) 
        {
            if (slist[c] < slist[c + 1]) 
            {
                c = c + 1;
            }
        }
        if (slist[index] < slist[c]) 
        {
            swap_queuel(slist,index, c);
        }
    }
    
    
    public void swap_queuel(int[]slist,int p, int i)
    {
       int a = slist[i];
       slist[i] = slist[p];
       slist[p] = a;
    }

     public void call_siftup(server[] slist)
    { 
        for (int i = 0; i < slist.length; i++) {
            sift_up(slist,i);
        }  
    }
    
    public void call_siftdown(server[] slist)
    {
        int mid = 0;
    
        if (slist.length % 2 == 0) {
            mid = (slist.length - 2) / 2;
        } else {
            mid = (slist.length - 3) / 2;
        }

        for (int i = mid; i >= 0; i--) {
            sift_down(slist,i);
        }
    }
     
    public void sift_down(server[] slist,int index)
    {
        int c = index * 2 + 1;
        
        if (c + 1 < slist.length) 
        {
            if (slist[c].end_time < slist[c + 1].end_time) 
            {
                c = c + 1;
            }
        }
        if (slist[index].end_time < slist[c].end_time) 
        {
            swap(slist,index, c);
        }
    }
    
    public void sift_up(server[] slist,int index)
    {
         int p;
        if (index == 0)
        {
            return;
        }

        if (index % 2 == 0)
        {
            p = index / 2 - 1;
        } 
        else
        {
            p = (index - 1) / 2;
        }

        if (slist[p].end_time > slist[index].end_time) 
        {
            swap(slist,p, index);
        }
        sift_up(slist,p);
        
    }
    
    public void swap(server[]slist,int p, int i)
    {
       server a = slist[i];
       slist[i] = slist[p];
       slist[p] = a;
    
    }
    int count_1 = 0;
    public void insert_q1length(int l)
    {
        
    q1length[count_1] = l;
    count_1++;
    
    }
        int count_2 = 0;
    public void insert_q2length(int l)
    {
        q2length[count_2] = l;
    count_2++;
    
    }
    
     
    public void insert_customer(int serverp_id,int servers_id,double arrival,double stime_1, double stime_2,double end_timep,double end_times)
    {
         customer customer = new customer(serverp_id,servers_id,arrival,stime_1,stime_2,end_timep,end_times);
         clist[count_customer] = customer;
         count_customer++;
    }  

    
    public void insert_server1()
    {
        for(int i = 0; i < server1_number;i++)
        {
              server server = new server(i+1,0.0);
              primary[i] = server;
        }
    }     
    
    public void insert_server2()
    {
        for(int i = 0; i < server2_number;i++)
        {
              server server = new server(i+1,0.0);
              secondary[i] = server;
        }
    }   
    
int ltimes1 =0;
    public void count_q1(int l)
    {
        
        q_length1 = q_length1 +l;
         ltimes1++;
    }
    
  int ltimes2 =0;  
    public void count_q2(int l)
    {
        q_length2 = q_length2 +l;
         ltimes2++;
    }
    
    public void print()
    {
        
       call_siftdown(primary);
       siftdown_qlength(q1length);
       siftdown_qlength(q2length);
       int[] qslength = new int[2];
       qslength[0] = q1length[0];
       qslength[1] = q2length[0];
       siftdown_qlength(qslength);  
           
       siftdown_qlength(qslength);
        double sum = 0;
      for (int i = 0; i < clist.length; i++) 
      {
          if(clist[i] != null)
            sum = sum+ clist[i].stime_1+clist[i].stime_2;
       }
         
        System.out.println("number of people served: "+count_customer);
        System.out.println("Time last service request completed: "+secondary[0].end_time);
        System.out.println("Ave total service time: "+ sum/count_customer);
        System.out.println();
        System.out.println("Ave length of the queue1: "+q_length1/ltimes1);
        System.out.println("Ave length of the queue2: "+q_length2/ltimes2);
        System.out.println("Ave length of the queues: "+ (q_length1+q_length2)/(ltimes1+ltimes2));
          System.out.println();
       System.out.println("Max length of queue1: "+q1length[0]);
       System.out.println("Max length of queue2: "+q2length[0]);
       System.out.println("Max length of queues: "+qslength[0]);
        
   System.out.println();
      System.out.println("Idle times of servers:");
      System.out.println("Total idle time for each server in primary servers: ");
     double busytime = 0;
       for (int i = 0; i < primary.length; i++) 
      {
           for(int c = 0; c < clist.length; c++)
           {
               if(clist[c]!=null)
              if(primary[i].id ==clist[c].serverp_id)
              {
                 busytime += clist[c].stime_1;
              }
             
           }
           System.out.println(primary[i].id +": "+(primary[i].end_time - busytime));
       }
      
        System.out.println();
        System.out.println("Total idle time for each server in secondary  servers: ");
      for (int i = 0; i < secondary.length; i++) 
      {
           for(int c = 0; c < clist.length; c++)
           {
               if(clist[c]!=null)
              if(secondary[i].id ==clist[c].servers_id)
              {
                 busytime += clist[c].stime_2;
              }
           }
           System.out.println(secondary[i].id +": "+(secondary[i].end_time - busytime));
       }
    
        
    }
    
    
    
    
    
}

class server
{
    int id;
    double end_time;

    public server(int id, double end_time)
    {
        this.id = id;
        this.end_time = end_time;
    }
}

class customer
{
    int serverp_id;
    int servers_id;
    double arrival;
    double stime_1;
    double stime_2;
    double end_timep;
    double end_times;

    public customer(int serverp_id,int servers_id,double arrival,double stime_1, double stime_2,double end_timep,double end_times) 
    {
        this.serverp_id = serverp_id;
        this.servers_id = servers_id;
        this.arrival = arrival;
        this.stime_1 = stime_1;
        this.stime_2 = stime_2;
        this.end_timep = end_timep;
        this.end_times = end_times;
    }
}

