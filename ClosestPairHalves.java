package com.mycompany.closestpairhalves;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/*
 * Algorithms and Complexity                                  October 29, 2022
 * IST 4310-01
 * Prof. M. Diaz-Maldonado
 *
 * Laboratory # 2: Closest Pair Problem Dividing by Halves.
 *
 * Synopsis: Find the closest pair of points by recursively dividing in subsets with half the size the set of coordinates 
 * and applying the Brute Force Algorithm when there are only 3 or less points in the subset. Must take into account the 
 * possibility of the closest pair being from 2 different subsets. This means creating a list of candidates by comparing
 * the points in the borders of different subsets between them and applying Brute Force in the candidates. 
 * The original set of coordinates must be created with n points of random coordinates.
 * The program finds the minimum distance 200 times for every set of coordinates of size n, where n varies from 1000 to 
 * 10000000 incrementing by a factor of 3/2. This is done in order to analyze the time complexity of the algorithm.
 * 
 * Author: Santiago Andrés Mercado Barandica y David Salgado Cortés
 * ID: 200155614 / 
*/

class Point{ //Define a Point class with x and y positions and a position to classify them.
        int x;
        int y;
        int pos;
        
        public Point(int xx, int yy, int poss){
            this.x = xx;
            this.y = yy;
            this.pos = poss;
        }
}

class TimeComplexityAnalysis{
    
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


        private static void write (String name, int tm, ArrayList<Integer> is, ArrayList<Integer> comparisons, ArrayList<Integer> runtimes)
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
    
        public void Analysis(int ii){ //ii is the amount of integers to reach starting at i=1000 growing at a rate of 2*i.
                ArrayList<Integer> runtimes = new ArrayList<Integer>();
                ArrayList<Integer> is = new ArrayList<Integer>();
                ArrayList<Integer> comparisons = new ArrayList<Integer>(); //We initialize all 3 lists.
                for (int i=1000; i<ii; i=i*3/2){ 
                    ClosestPairHalves cph = new ClosestPairHalves(); //Create a new ClosestPair class to test it.
                    System.out.println(i);
                    long total = 0;
                    int count;
                    ArrayList<Point> coords = cph.Initialize(i); //Creating lists of coordinates and to store sorted x and y positions.
                    coords.sort(Comparator.comparing(Point -> Point.x)); //Sorting set of coordinates by their x position.
                    for(int j=0; j<256; j++){
                        long startTime = System.nanoTime();
                        cph.ClosestPair(i, coords, 999999999);
                        long endTime   = System.nanoTime();
                        long totalTime = endTime - startTime; //We calculate the runtime for each repetition and add it to the previous ones..
                        total = total + totalTime;
                    }
                   count = cph.getComparisons();
                   count = count/256; //We find the average comparisons per iteration and store it in a list.
                   total = total/256; //Average runtime
                   runtimes.add((int)total);
                   is.add(i); //Amount of coords in the array
                   comparisons.add(count);
                }
                        create("Data.txt");
                        write("Data.txt", is.size(), is, comparisons, runtimes);		
        }
}

public class ClosestPairHalves {

    public static int count; //Counts number of comparisons;
  
    public ClosestPairHalves(){
        count = 0;
    }
    public int getComparisons(){
        return count;
    }
    
        public static double[] BruteForce(int N, List<Point> coords, double mdis){ // Finds closest pair of a given set of coordinates via Brute Force Algorithm.
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
                   vec[1] = coords.get(i).pos;
                   vec[2] = coords.get(j).pos;
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
        
  public static ArrayList<Point> FindCandidates(List<Point> coords, double min){ //We check the pairs near the middles closer to each other than that found in the quadrants.
        ArrayList<Point> cand = new ArrayList<Point>();
	  int i = 0;
	  while(i<coords.size()/2){ //Comparing the distance in both x and y position of the points in the border of both subsets.
            if (Math.abs(coords.get(i).x-coords.get(coords.size()/2).x) < min && Math.abs(coords.get(i).y-coords.get(coords.size()/2).y) < min){
                count++;
                cand.add(coords.get(i)); //If the distance is less than the actual minimum distance, they are candidates.
		    i++;
                } else {
			count++;
			i = coords.size()/2;
      }
		    }
        while(i<coords.size()){ //We compare first the points in one subset and then the other to avoid repeating points.
            if (Math.abs(coords.get(i).x-coords.get(coords.size()/2-1).x) < min && Math.abs(coords.get(i).y-coords.get(coords.size()/2-1).y) < min){
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

public static double[] ClosestPair(int N, List<Point> x, double mdis){
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
             g1 = ClosestPair(N/2, x.subList(0, N/2), mdis);
             g2 = ClosestPair(N/2 + offset, x.subList(N/2, N), mdis);
         double [] g = new double[3];
         if(g1[0] < g2[0]){ //Store the closest pair of the regions.
             g = g1;
             count++;
         } else {
             count++;
             g = g2;
         }
         ArrayList<Point> candidates = new ArrayList<Point>(); //List to store possible candidates to apply the algorithm.
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
            return BruteForce(N, x, mdis); //Apply BruteForce algorithm when there are 3 or less coords.
    }
}
    
    public static double distance(List<Point> coords, int i, int j){ // computes the distance between the ith and jth elements
       // unpacks coordinates of the ith and jth elements.
       int x1 = coords.get(i).x;
       int x2 = coords.get(j).x;
       int y1 = coords.get(i).y;
       int y2 = coords.get(j).y;
       double d = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)); // computes their distance
       return d;
    }
    
    
    public static ArrayList<Point> Initialize(int tam){ //Creating a set of random coordinates.
     ArrayList list = new ArrayList<Point>();       
        Random rand = new Random();
        for (int i=0; i<tam; i++){
            Point temp = new Point(rand.nextInt(10000), rand.nextInt(10000), i);
            list.add(temp); 
        }
        
     return list;
    }
    
    
    public static void main(String[] args) {
        TimeComplexityAnalysis tca = new TimeComplexityAnalysis();
        tca.Analysis(5000000);
    }
}
