package ai.assign1;

/**
 *Michael Alsbergas, 5104112
 * 
 * This generates the nodes used for the tree.
 */
class Node {
    int[][] state;
    Node up;
    Node left;
    Node down;
    Node right; 
    
    public Node(int[][] s, Node u, Node d, Node l, Node r){
        state = s;
        up = u;
        down = d;
        left = l;
        right = r;
    }
    
}
