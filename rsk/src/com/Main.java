package rsk.src.com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {




    public static void main(String[] args) {
        MatrixContainer matrices = new MatrixContainer(fillListWithUserInput());
        matrices.printMatrix(matrices.getAdjacencyMatrix(), "Adjacency Matrix:");
        matrices.printMatrix(matrices.getQuadraticMatrix(), "Quadratic matrix:");

        System.out.println(matrices.getUniqueElements());
        Groups groups = new Groups(matrices);
        Graph[] graphs = new Graph[groups.getGroups().size()];
        for (int i = 0; i < graphs.length; i++) {
            graphs[i] = new Graph(matrices.getUniqueElements(), matrices.getElements(), groups.getGroups().get(i));
        }
//        Module module = new Module(graphs[0]);
//        ArrayList<ArrayList<String>> myModule = module.getModule();
//        for (int i = 0; i < myModule.size(); i++) {
//            System.out.println(myModule.get(i));
//        }
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
                elementsFromUser.get(rowNum).addAll(line.length() > 2 ?
                        Arrays.asList(line.trim().split(String.valueOf(line.charAt(2)))) : Collections.singleton(line));
                rowNum++;
            }
        }
        return elementsFromUser;
    }
}
