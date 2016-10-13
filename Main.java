
package ai.assign1;

/**
 *Michael Alsbergas, 5104112
 * Assignment 1, Cosc 3P71
 * 
 * This program generates a random 8 puzzle of size nxn and then attempts to 
 * solve it using a blind search and A* star search.
 */

import java.util.Scanner;

//here the global variables are created.
public class Main {
    int[][] solved;
    int blindSearchSteps;
    int starSearchSteps;
    Node tree;
    
    Node small;
    int min;
    
    //This class does the initial method calls for the puzzle generator and 
    //the searches.
    public Main() {
        int[][] puzzle;
        int ptrx;
        int ptry;
        int x;
        int n;
        Scanner console = new Scanner(System.in);
        
        System.out.print("Enter n for puzzle size (nxn):");
        n = console.nextInt();
        
        puzzle = new int[n][n];
        solved = new int[n][n];
        
        x = 0;
        for (int out=0 ; out<n ; out++){
            for (int inner=0 ; inner<n ; inner++){
                puzzle[out][inner] = x;
                solved[out][inner] = x;
                x++;
            }
        }
        ptrx = 0;
        ptry = 0;
        
        System.out.println("---Initial & Goal State---");
        Print(puzzle);
        
        puzzle = Scramble(puzzle,ptrx,ptry);
        
        tree = new Node(puzzle,null,null,null,null);
        
        for (int out=0 ; out<n ; out++){
            for (int inner=0 ; inner<n ; inner++){
                if (puzzle[out][inner] == 0){
                    ptrx = out;
                    ptry = inner;
                    out = n;
                    inner = n;
                }
            }
        }
         
        if (BlindSearch(tree, ptrx, ptry, 1)){
            System.out.println("Blind Search Solution found in " + blindSearchSteps + " steps.");
        }
        else {
            System.out.println("Blind Search Solution NOT found in " + blindSearchSteps + " steps.");
        }
               
        if (AStarSearch(tree, tree)){
            System.out.println("A* Search Solution found in " + starSearchSteps + " steps.");
        }
        
        
     }
        
    //This simply prints the given board state.
    private void Print(int[][] puzzle){
        String line;
        
        for (int out=0 ; out< puzzle.length ; out++){
            line = "";
            for (int inner=0 ; inner< puzzle[0].length ; inner++){
                line = line +"["+ puzzle[out][inner] +"]";
            }
            System.out.println(line);
        }
    }
    
    //This takes a board and randomly scramble it, ensuring a solvable puzzle.
    private int[][] Scramble(int[][] puzzle, int ptrx, int ptry){
        double z;        
        
        for (int i=0 ; i<50 ; i++){
            z = (4.0 * Math.random());
            if (z<=1 && ptrx > 0){
                puzzle = MoveUp(puzzle,ptrx,ptry);
                ptrx = ptrx -1;}
            else if (z<=2 && ptrx < puzzle.length-1) {
                puzzle = MoveDown(puzzle,ptrx,ptry);
                ptrx = ptrx +1;}
            else if (z<=3 && ptry >0) {
                puzzle = MoveLeft(puzzle,ptrx,ptry);
                ptry = ptry -1;}
            else if (z<=4 && ptry < puzzle.length-1) {
                puzzle = MoveRight(puzzle,ptrx,ptry);
                ptry = ptry +1;}
        }
        
        System.out.println("---Scrambled State---");
        Print(puzzle);
        return puzzle;
    }
    
    //This class implements the Blind Search.
    private boolean BlindSearch(Node point, int ptrx, int ptry, int depth){
        blindSearchSteps ++;
        
        if (Check(point.state)){
            return true;
        }
        else if (depth == 10){
            return false;
        }
        
        int[][] tempPoint = new int[point.state.length][point.state.length];
        tempPoint = Duplicate(point.state);
                
        if (ptrx > 0){
            point.up = new Node(MoveUp(tempPoint, ptrx, ptry),null,null,null,null);
            if (point.up !=null && BlindSearch(point.up, ptrx-1, ptry, depth+1)){
                return true;
            }
            tempPoint = Duplicate(point.state);
            }
        if (ptrx < point.state.length-1){
            point.down = new Node(MoveDown(tempPoint, ptrx, ptry),null,null,null,null);
            if (point.down !=null && BlindSearch(point.down, ptrx+1, ptry, depth+1)){
                return true;
            }
            tempPoint = Duplicate(point.state);
            }
        if (ptry > 0){
            point.left = new Node(MoveLeft(tempPoint, ptrx, ptry),null,null,null,null);
            if (point.left !=null && BlindSearch(point.left, ptrx, ptry-1, depth+1)){
                return true;
            }
            tempPoint = Duplicate(point.state);
            }
        if (ptry < point.state.length-1){
            point.right = new Node(MoveRight(tempPoint, ptrx, ptry),null,null,null,null);
            if (point.right !=null && BlindSearch(point.right, ptrx, ptry+1, depth+1)){
                return true;
            }
            }
        
        return false;
    }
    
    //This class implements the A* Search.
    private boolean AStarSearch(Node point, Node root){
        starSearchSteps++;    
                
        if (Check(point.state)){
            return true;
        }
        
        int ptrx = 0;
        int ptry = 0;
        int[][] tempPoint = new int[point.state.length][point.state.length];
        
        for (int out=0 ; out<point.state.length ; out++){
            for (int inner=0 ; inner<point.state.length ; inner++){
                if (point.state[out][inner] == 0){
                    ptrx = out;
                    ptry = inner;
                    out = point.state.length;
                    inner = point.state.length;
                }                
            }
        }
        
        tempPoint = Duplicate(point.state);
                        
        if (ptrx > 0){
            point.up = new Node(MoveUp(tempPoint, ptrx, ptry),null,null,null,null);
            tempPoint = Duplicate(point.state);
        } 
        if (ptrx < point.state.length-1){
            point.down = new Node(MoveDown(tempPoint, ptrx, ptry),null,null,null,null);
            tempPoint = Duplicate(point.state);
        }
        if (ptry > 0){
            point.left = new Node(MoveLeft(tempPoint, ptrx, ptry),null,null,null,null);
            tempPoint = Duplicate(point.state);
        }
        if (ptry < point.state.length-1){
            point.right = new Node(MoveRight(tempPoint, ptrx, ptry),null,null,null,null);
        }
        
        small = null;
        min = 9999999; 
        
        FindMin(root, 0);
                
        return AStarSearch(small, root);
    }
    
    //This duplicates a given puzzle. It solved a bug.
    private int[][] Duplicate(int[][] puzz1){
        int[][] puzz2 = new int[puzz1.length][puzz1.length];
        
        for (int i=0 ; i < puzz1.length ; i++){
            for (int j=0 ; j < puzz1.length ; j++){
                puzz2[i][j] = puzz1[i][j];
            }
        }
        
        return puzz2;
    }
    
    //This class calculates the manhattan distance heuristic.
    private int Heuristic(int[][] puzzle){
        int heur = 0;
        int ptrx;
        int ptry;
        
        for (int i=0 ; i < puzzle.length ; i++){
            for (int j=0 ; j < puzzle.length ; j++){
                if (puzzle[i][j] != solved[i][j]){
                    ptrx = (puzzle[i][j]%puzzle.length)-1;
                    if (ptrx < 0){ 
                        ptrx = puzzle.length - 1;
                    }
                    
                    ptry = (puzzle[i][j]/puzzle.length);
                    
                    heur = heur + Max(ptrx-i, i-ptrx);
                    heur = heur + Max(ptry-j, j-ptry);
                }
            }
        }
        return heur;
    }
    
    //pretty self explanitory.
    private int Max(int a, int b){
        if (a > b){
            return a;
        }
        return b;
    }
    
    //This uses the Heuristic function to find the optimal path.
    private void FindMin(Node point, int depth){
        
        if (point.up != null){
            FindMin(point.up, Heuristic(point.state)+depth);
        }
        if (point.down != null){
            FindMin(point.down, Heuristic(point.state)+depth);
        }
        if (point.left != null){
            FindMin(point.left, Heuristic(point.state)+depth);
        }
        if (point.right != null){
            FindMin(point.right, Heuristic(point.state)+depth);
        }
        
        if (point.up == null && point.down == null && point.left == null && point.right == null
                && (Heuristic(point.state) + depth) <= min){
            min = Heuristic(point.state) + depth;
            small = point;
        }        
        
    }
    
    //These next 4 functions move the space (0) in a direction.
    private int[][] MoveUp(int[][] puzzle, int ptrx, int ptry){        
        if (ptrx > 0) {
            puzzle[ptrx][ptry] = puzzle[ptrx-1][ptry];
            puzzle[ptrx-1][ptry] = 0;
        }
            return puzzle;
    }
        
    private int[][] MoveDown(int[][] puzzle, int ptrx, int ptry){        
        if (ptrx < puzzle.length - 1) {
            puzzle[ptrx][ptry] = puzzle[ptrx+1][ptry];
            puzzle[ptrx+1][ptry] = 0;
        }
        return puzzle;
    }
    
    private int[][] MoveLeft(int[][] puzzle, int ptrx, int ptry){        
        if (ptry > 0) {
            puzzle[ptrx][ptry] = puzzle[ptrx][ptry-1];
            puzzle[ptrx][ptry-1] = 0;
        }
        return puzzle;
    }
    
    private int[][] MoveRight(int[][] puzzle, int ptrx, int ptry){        
        if (ptry < puzzle.length-1) {
            puzzle[ptrx][ptry] = puzzle[ptrx][ptry+1];
            puzzle[ptrx][ptry+1] = 0;
        }
        return puzzle;
    }
    
    //This compares a board state to the goal state.
    private boolean Check(int[][] puzzle){
        for (int i=0 ; i < puzzle.length ; i++){
            for (int j=0 ; j < puzzle.length ; j++){
                if (puzzle[i][j] != solved[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void main(String[] args) {Main m = new Main();}
}
