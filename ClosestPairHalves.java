/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.loq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sabarandica
 */

public class ClosestPairHalves {
    public static int count; //Counts number of comparisons;
    static LinkedList<Integer> runtimes; //List to store the runtimes for each n integers.
    static LinkedList<Integer> is; //List to store the amount of coords in the iteration.
    static LinkedList<Integer> comparisons; //List to store the amount of comparisons for each n integers.
    static ArrayList<int[]> coords = new ArrayList<int[]>(); //List to store set of coordinates.
    static ArrayList<int[]> posx = new ArrayList<int[]>(); //List to store set of coordinates sorted by x position.
    static ArrayList<int[]> posy = new ArrayList<int[]>(); //List to store set of coordinates soted by y position.
   
   
        public static double[] BruteForce(int N, List<int[]> coords, double mdis){ // Finds closest pair of a given set of coordinates via Brute Force Algorithm.
       double dmin = mdis;
       double [] vec = new double[3]; //Array to store min_dis and the index of points with the closest pair.
       vec[0] = dmin;
       for(int i=0; i<N; i++){
           for(int j=i+1; j<N; j++){
               double d = distance(coords, i, j); //Compares the distance between every pair of coords and return their distance.
               if(d<dmin){
                   count++;
                   dmin = d;
                   vec[0] = d;
                   vec[1] = coords.get(i)[2];
                   vec[2] = coords.get(j)[2];
               } else {
                   count++;
               }
           }
       }
    return vec;
       }
    
    public static void Printc(List<int[]> coords){ // Prints a given set of coordinates (used to test the algorithm).
       for(int i=0; i<coords.size(); i++){
           System.out.println("x: " + coords.get(i)[0] + " y: " + coords.get(i)[1] + " pos: " + coords.get(i)[2]);
       }
    }
    

    
    private static void create (String name) 
	// creates a file with a given name
	{
		try
		{
			// defines the filename
			String fname = (name);
			// creates a new File object
			File f = new File (fname);
                        
			String msg = "creating file `" + fname + "' ... ";
			// creates the new file
			f.createNewFile();

		}
		catch (IOException err)
		{
			// complains if there is an Input/Output Error
			err.printStackTrace();
		}

		return;
	}


        private static void write (String name, int tm)
	// writes data to a file with a given name and size
	{
		try
		{
			// defines the filename
			String filename = (name);
			// creates new PrintWriter object for writing file
			PrintWriter out = new PrintWriter (filename);
                        String fmt = ("%10s %10s %10s\n"); //We store 3 values in different columns per line: amount of integers(n), comparisons and runtime.
                            for (int i = 0; i < tm; ++i){
                                out.printf(fmt, is.get(i), comparisons.get(i), runtimes.get(i));
                            }
			

			out.close();	// closes the output stream
		}
		catch (FileNotFoundException err)
		{
			// complains if file does not exist
			err.printStackTrace();
		}

		return;
	}
        
  public static LinkedList<int[]> FindCandidates(List<int[]> coords, double min){ //We check the pairs near the middles closer to each other than that found in the quadrants.
        LinkedList<int[]> cand = new LinkedList<int[]>();
	  int i = 0;
	  while(i<coords.size()/2){
            if (Math.abs(coords.get(i)[0]-coords.get(coords.size()/2)[0]) < min && Math.abs(coords.get(i)[1]-coords.get(coords.size()/2)[1]) < min){
                count++;
                cand.add(coords.get(i));
		    i++;
                } else {
			count++;
			i = coords.size()/2;
      }
		    }
        while(i<coords.size()){
            if (Math.abs(coords.get(i)[0]-coords.get(coords.size()/2-1)[0]) < min && Math.abs(coords.get(i)[1]-coords.get(coords.size()/2-1)[1]) < min){
                count++;
                cand.add(coords.get(i));
		    i++;
                } else {
			count++;
			i= coords.size();
      }
        }
        return cand;
    }

public static double[] ClosestPair(int N, List<int[]> coords, List<int[]> x, double mdis){
        // Finds closest pair of a given set of coordinates via Brute Force Algorithm, but using recursion.  
        if(N > 3){ //If there are more than 3 points in a region, we divide it in 2.
           double[] g1 = new double[3];
           double[] g2 = new double[3];
           int offset = 0;
           count++;
           if(N % 2 == 1){
             offset = 1;  //Taking into account an odd sized sample. The second half will 1 unit bigger thean the first half.
           }
            //Splits in half the set of coordinates until there are only 3 or less coords and apply the algorithm in both halfs, then compare them.
             g1 = ClosestPair(N/2, x.subList(0, N/2), x.subList(0, N/2), mdis);
             g2 = ClosestPair(N/2 + offset, x.subList(N/2, N), x.subList(N/2, N), mdis);
         double [] g = new double[3];
         if(g1[0] < g2[0]){ //Store the closest pair of the regions.
             g = g1;
             count++;
         } else {
             count++;
             g = g2;
         }
         LinkedList<int[]> candidates = new LinkedList<int[]>(); //List to store possible candidates to apply the algorithm.
           //Comparing the minimum disctance and the distance between one of the coordintes of the pairs.
           candidates = FindCandidates(x, g[0]);
            //Apply the Brute Force algorithm to the set of candidates if there are at least 2.
            if(candidates.size() > 1){
                count++;
                g1 = BruteForce(candidates.size(), candidates, mdis);
              if(g1[0] < g[0]){
               count++;  
               return g1; //Compare the actual min_dis with the one obtained form the candidates and return the lowest.
             } else {
                 count++;
                 return g;
             }
            } else {
                count++;
                return g;
            }       
       } else {
            count++;
            double[] vec = new double[3]; 
            return BruteForce(N, coords, mdis); //Apply BruteForce algorithm when there are 3 or less coords.
    }
}
    
    public static List<int[]> Sortx(List<int[]> ar){ //Sorts a Linked List from lowest to highest based on x position.
        List<int[]> arr= ar;
        int n = arr.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr.get(j)[0] > arr.get(j+1)[0]) {
                    // swap arr[j+1] and arr[j]
                    int[] temp = arr.get(j);
                    arr.set(j, arr.get(j+1));
                    arr.set(j+1, temp);
                }
        return arr;
    }

    
    public static double distance(List<int[]> coords, int i, int j){ // computes the distance between the ith and jth elements
       // unpacks coordinates of the ith and jth elements.
       int x1 = coords.get(i)[0];
       int x2 = coords.get(j)[0];
       int y1 = coords.get(i)[1];
       int y2 = coords.get(j)[1];
       double d = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)); // computes their distance
       return d;
    }
    
    
    private static ArrayList<int[]> Initialize(int tam){ //Creating a set of random coordinates.
     ArrayList list = new ArrayList<int[]>();       
        Random rand = new Random();
        for (int i=0; i<tam; i++){
            int[] temp = new int[3];
            temp[2] = i;
            temp[0] = rand.nextInt(10000);
            temp[1] = rand.nextInt(10000);
            list.add(temp); 
        }
        
     return list;
    }
    

    private static ArrayList<int[]> Initialize(int tam, ArrayList<int[]> lis){ //Create a copy of an arraylist with a set of random coordinates.
     ArrayList list = new ArrayList<int[]>();     
        for (int i=0; i<tam; i++){
            int[] temp = new int[3];
            temp[2] = lis.get(i)[2];
            temp[0] = lis.get(i)[0];
            temp[1] = lis.get(i)[1];
            list.add(temp); 
        }
        
     return list;
    }
    
    private static void Cases(int ii){ //ii is the amount of integers to reach starting at i=32 growing at a rate of 2*i.
                runtimes = new LinkedList<Integer>();
                is = new LinkedList<Integer>();
                comparisons = new LinkedList<Integer>(); //We initialize all 3 lists.
                for (int i=1000; i<ii; i=i*3/2){ 
                    System.out.println(i);
                    count = 0;
                    long total = 0;
                    coords = Initialize(i); //Creating lists of coordinates and to store sorted x and y positions.
                    posx = Initialize(i, coords);
                    posy = Initialize(i, coords);
                    posx.sort(Comparator.comparing(a -> a[0]));
                    posy.sort(Comparator.comparing(a -> a[1]));
                    for(int j=0; j<256; j++){
                        long startTime = System.nanoTime();
                        ClosestPair(i, coords, posx, 999999999);
                        long endTime   = System.nanoTime();
                        long totalTime = endTime - startTime; //We calculate the runtime for each repetition and add it to the previous ones..
                        total = total + totalTime;
                    }
                   count = count/256; //We find the average comparisons per iteration and store it in a list.
                   total = total/256; //Average runtime
                   runtimes.add((int)total);
                   is.add(i); //Amount of coords in the array
                   comparisons.add(count);
                }
                        create("Data.txt");
                        write("Data.txt", is.size());		
        }
    

    public static void main(String[] args) {
        Cases(1000000);
    }
}
