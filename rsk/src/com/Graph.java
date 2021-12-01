package rsk.src.com;

import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    /*
     *  Realization:
     *  To create graph of the group we need three things: uniqueElements list, allElements list and ofc groups list
     *  Then, we check each element of the current group and mark in the matrix all connections of the current element
     *  How we mark connections:
     *      (each operation has appropriate index in the matrix, f.e. first operation from the uniqueElements has 0 index
     *       and this index means the same operation in the graph matrix second - 1 and so on)
     *      1. We read each operation of the group and mark connections in the graph matrix
     *      What is interesting, that we should read first element of the operation and use it like index for the row
     *      of the graph matrix, then we read second element and mark this element on the index of the first element,
     *      then the second element becomes index for the row of the graph matrix, and we perform similar manipulations.
     *
     *      Additional methods to implement:
     *      I think I should implement method that
     */
    private int[][] graph;
    private final ArrayList<String> uniqueElements;

    public Graph(ArrayList<String> uniqueElements, ArrayList<ArrayList<String>> elements,
                 ArrayList<Integer> group) {
        this.uniqueElements = refineUniqueElementsForCurrentGroup(uniqueElements, group, elements);
        graph = createGraph(this.uniqueElements,elements,group);
        printGraph();

    }

    public ArrayList<String> getUniqueElements() {
        return uniqueElements;
    }

    public int[][] getGraph() {
        return graph;
    }

    public void printGraph() {
        System.out.println(uniqueElements);
        for (int i = 0; i < graph.length; i++) {
            System.out.println(Arrays.toString(graph[i]));
        }
    }
    private ArrayList<String> refineUniqueElementsForCurrentGroup(
            ArrayList<String> uniqueElements, ArrayList<Integer> group, ArrayList<ArrayList<String>> elements) {

        ArrayList<String> updatedUniqueElements = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            for (int j = 0; j < elements.get(group.get(i)).size(); j++) {
                if(!updatedUniqueElements.contains(elements.get(group.get(i)).get(j))) {
                    updatedUniqueElements.add(elements.get(group.get(i)).get(j));
                }
            }
        }
        return updatedUniqueElements;
    }

    private int[][] createGraph(ArrayList<String> uniqueElements, ArrayList<ArrayList<String>> elements,
                                ArrayList<Integer> group) {
        int[][] graph = new int[uniqueElements.size()][uniqueElements.size()];
        int firstNode;
        int secondNode;
        for (int i = 0; i < group.size(); i++) {
            firstNode = uniqueElements.indexOf(elements.get(group.get(i)).get(0));
            for (int j = 1; j < elements.get(group.get(i)).size(); j++) {
                secondNode = uniqueElements.indexOf(elements.get(group.get(i)).get(j)); /// need to recheck
                graph[firstNode][secondNode] = 1;
                firstNode = secondNode;
            }
        }


        return graph;
    }


}
