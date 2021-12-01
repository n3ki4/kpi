package rsk.src.com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {



    // what i need to do to generate module from the graph:
    /*
    * to check vertices on beginning and ending element cases
    * to check vertices if they form a closed loop
    * to check vertices if they form a chain
    * Then we get modules, that we need to refine simple absorption of the modules like it was with groups
    */

    private int[][] graph;
    private ArrayList<String> uniqueElements;
    private ArrayList<ArrayList<String>> module;

    public Module(Graph graph) {
        this.graph = graph.getGraph();
        this.uniqueElements = graph.getUniqueElements();
    }

    // method that checks vertices on beginning and ending cases

    /**
     * If it finds beginning or ending vertex it deletes them from the graph(sets 0 on their coordinates)
     * Beginning vertex classification:
     *      1. This row in the graph has out connections, but it hasn't into connections
     *      (all elements of the column with the index of this vertex equals to zero,
     *      but row with this index has more than 1 connection)
     * Ending vertex classification:
     *      1. This row in the graph is empty (all row elements are equal to zero)
     *      but column elements have more than 1 connection
     */
                    // line 9
    private void checkVertices() {
        int in;
        int out;
        for (int i = 0; i < graph.length; i++) {
            in = 0;
            out = 0;
            // check vertical line
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == 1) {
                    out++;
                }
            }
            // check horizontal line
            for (int j = i; j < graph.length; j++) {
                if (graph[j][i] == 1) {
                    in++;
                }
            }
            if (in == 0 && out >= 2 || in >=2 && out == 0) {
                // horizontal
                Arrays.fill(graph[i], 0);
                //vertical
                for(int k = 0; k < graph.length; k++) {
                    graph[k][i] = 0;
                }
            }

        }
    }

    private void check_chain() {

    }

    private void check_loop() {

    }






}
