/*
 * Haoran Cheng
 * 5044194
 * Assignment 3
 */
package ass3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.lang.Math;
import java.util.*;

public class Ass3{

	int nv;
	int ne;
	double[][] matrix;
	double[] x;
	double[] y;
	int start;
	int end;
        
        double[]  m_distance;
        boolean[] m_visited;
	int[]  m_parent; 

        public static void main(String[] args) 
                {
             
                   Scanner input = new Scanner(System.in);
                   System.out.print("input file name: ");
                   String fname = input.next();
                     Ass3 a3 = new Ass3();
          
                     try
                     {
                        BufferedReader in  = new BufferedReader(new FileReader(fname));
                        String line = in.readLine();
                        int count = 0;
                        while(line != null&&count!=2)
                        {
                          line = line.trim();
                          String[] list = line.split("\\s+");
                          
                          if(list.length==1)
                          {    
                              count++;
                              if(count ==1)
                                
                                  a3.nv = Integer.parseInt(list[0]);
                                  a3.ne  =  Integer.parseInt(list[0]);
                                  
                               a3.x = new double[Integer.parseInt(list[0])];
                               a3.y = new double[Integer.parseInt(list[0])];
                               a3.matrix = new double[Integer.parseInt(list[0])][Integer.parseInt(list[0])];
                               //System.out.println(list[0]);
                          }                      
                          
                          if(list.length==3)
                          {   
                             a3.insert_x(Double.parseDouble(list[1]));
                             a3.insert_y(Double.parseDouble(list[2]));
                          }
                          //    System.out.println(list[0] + " "+ list[1] +" "+ list[2]);
                             line = in.readLine();      
                         
                        }
                        
                        while(line !=null)
                        {
                            
                            line = line.trim();
                             String[] list = line.split("\\s+");
                             if(list.length==3)
                             {    
                               a3.insert_matrix(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Double.parseDouble(list[2]));
                             }
                             
                             if(list.length==2)
                             {    
                               a3.start = Integer.parseInt(list[0]);
                               a3.end = Integer.parseInt(list[1]);
                             }
                                line = in.readLine();                         
                        }                      
                              in.close();
                     }
                     
                     catch(IOException e)
                     {
                       System.out.print("Error: "+e);
                     
                     }   
                     
                    int length = a3.nv+1;     
                     
                    a3.m_distance = new double[length];
                    a3.m_visited = new boolean[length];
                    a3.m_parent = new int[length];

                     a3.initial_Mda(); 
                     a3.print();      
	        }

        int count_1=0;
        public void insert_x(double v)
        {
            x[count_1] = v;
            count_1++;
        }
        
        int count_2=0;
         public void insert_y(double v)
        {
            x[count_2] = v;
            count_2++;
        }
 
        public void insert_matrix(int x,int y, double v)
        {
            matrix[x][y] = v;
            matrix[y][x] = v;
        } 
        
        
        public void initial_Mda()
        {
            //initial all list
         	for(int i=0; i<=nv; i++)
                {
                    //store the distance between parent node to the connecting node i
			m_distance[i] = 1000;
                        //visiting status
			m_visited[i] = false;
                       //node i's parent node
			m_parent[i] = -1;
		}
                //start distance is 0 now
                m_distance[start] = 0;
                //start finding path
                M_D_Algorithm();
              
        }
        
        
	public void M_D_Algorithm()
        {
		for(int i=0; i<nv; i++)
                {
			double minm_distance = 10000;
			int minVertex = 0;
                        
                        //update the min node
			for(int j=1; j<=nv; j++)
                        {
				if(m_visited[j] == false)
                                {
                                    if(m_distance[j] < minm_distance)
                                    {
					minm_distance = m_distance[j];
					minVertex = j;
                                    }
				}
			}
                        //mark the visited node as true
			m_visited[minVertex] = true;
                        //if already visited the end node stop loop
			if(minVertex == end) break;
                        //count the distance to it's parent node
                        count_minDistance(minVertex);
                        
		}
	}

	public void count_minDistance(int minVertex)
        {
            for (int i = 1; i<= nv; i++) 
            {
                if (m_visited[i] == false)
                {
                    if(m_distance[minVertex] + matrix[minVertex][i] < m_distance[i])
                    {
                        if(matrix[minVertex][i] != 0)
                        {
                            //mindistance = the min distance which already counted + the distance of current node to the next conecting node 
                             m_distance[i] = m_distance[minVertex] + matrix[minVertex][i];
                             m_parent[i] = minVertex;
                       }
                    }
                }
            }
        }
           
        public void print()
        {         
                System.out.println("---Djikstra's Algorithm---"+"\n"+"Length of shortest path: " + m_distance[end]);
                
		int[] m_path = new int[nv];
		
                int count = 0;
		
                //input node's parent node into path
                for(int i = 0; i < nv;i++)
                {
                            if(end != -1)
                            {
                              	m_path[count++] = end;
			        end = m_parent[end];
                            }
                }
                
                //print path
		System.out.print("path: ");
		for(int j=count-1; j>=0; j--)
                {
			System.out.print(m_path[j] + " ");
		}   
		System.out.println();
                
		int total_visited = 0;
		for(int i=0; i<=nv; i++)
                {
			if(m_visited[i] == true)
				total_visited++;
		}
                //total visited - efficency visited  = addtional visited
                int addtional = total_visited - count;
                
		System.out.println("Number of additional nodes: " + addtional);
        }
}