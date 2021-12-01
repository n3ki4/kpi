package com.rsk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {




    public static void main(String[] args) {
        MatrixContainer matrices = new MatrixContainer(fillListWithUserInput());
        matrices.printMatrix(matrices.getAdjacencyMatrix(), "Adjacency Matrix:");
        System.out.println(matrices.getUniqueElements());
        Groups groups = new Groups(matrices);
        Graph[] graphs = new Graph[groups.getGroups().size()];
        for (int i = 0; i < graphs.length; i++) {
            graphs[i] = new Graph(matrices.getUniqueElements(), matrices.getElements(), groups.getGroups().get(i));
        }
    }


    private static ArrayList<ArrayList<String>> fillListWithUserInput() {
        Scanner console = new Scanner(System.in);
        ArrayList<ArrayList<String>> elementsFromUser = new ArrayList<>();
        int rowNum = 0;
        String line = " ";
        while (!line.equals("")) {
            line = console.nextLine();
            if (!line.equals("")) {
                elementsFromUser.add(new ArrayList<>());
                elementsFromUser.get(rowNum).addAll(Arrays.asList(line.split(",")));
                rowNum++;
            }
        }
        return elementsFromUser;
    }
}
