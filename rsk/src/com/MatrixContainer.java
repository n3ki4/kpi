package com.rsk;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixContainer {
    private final ArrayList<String> uniqueElements;
    private final int[][] adjacencyMatrix;
    private final int[][] quadraticMatrix;
    private final ArrayList<ArrayList<String>> elements;


    public MatrixContainer(ArrayList<ArrayList<String>> elements){
        this.elements = elements;
        this.uniqueElements = findUniqueElements(elements);
        this.adjacencyMatrix = createAdjacencyMatrix(elements, uniqueElements);
        this.quadraticMatrix = createQuadraticMatrix(adjacencyMatrix,uniqueElements.size());
    }

    public ArrayList<ArrayList<String>> getElements() {
        return elements;
    }

    public void printMatrix(int[][] matrix, String message) {
        System.out.println(message);
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        System.out.println();
    }

    public int[][] getAdjacencyMatrix() {
        return this.adjacencyMatrix;
    }

    public ArrayList<String> getUniqueElements() {
        return uniqueElements;
    }

    public int[][] getQuadraticMatrix() {
        return quadraticMatrix;
    }

    private static int[][] createQuadraticMatrix(int[][] adjacencyMatrix, int uniqueElementsAmount) {
        int[][] quadraticMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];
        for (int i = 0; i < quadraticMatrix.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == i) {
                    quadraticMatrix[i][j] = -1;
                } else {
                    quadraticMatrix[i][j] = uniqueElementsAmount -
                            countDifferentOperations(adjacencyMatrix[i],adjacencyMatrix[j]);
                }
            }
        }
        return quadraticMatrix;
    }

    private static int countDifferentOperations(int[] firstRow, int[] secondRow) {
        int counter = 0;
        for (int i = 0; i < firstRow.length; i++) {
            if (firstRow[i] == 0 && secondRow[i] == 1 ||
                    firstRow[i] == 1 && secondRow[i] == 0) {
                counter++;
            }
        }
        return counter;

    }

    private static int[][] createAdjacencyMatrix(ArrayList<ArrayList<String>> elements, ArrayList<String> uniqueElements) {
        int[][] adjacencyMatrix = new int[elements.size()][uniqueElements.size()];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[0].length; j++) {
                if (elements.get(i).contains(uniqueElements.get(j))) {
                    adjacencyMatrix[i][j] = 1;
                } else {
                    adjacencyMatrix[i][j] = 0;
                }
            }
        }
        return adjacencyMatrix;

    }

    private static ArrayList<String> findUniqueElements(ArrayList<ArrayList<String>> inputElems) {
        ArrayList<String> uniqueElements = new ArrayList<>();
        for (int i = 0; i < inputElems.size(); i++) {
            for (int j = 0; j < inputElems.get(i).size(); j++) {
                if (!uniqueElements.contains(inputElems.get(i).get(j))) {
                    uniqueElements.add(inputElems.get(i).get(j));
                }
            }
        }
        return uniqueElements;
    }
}

