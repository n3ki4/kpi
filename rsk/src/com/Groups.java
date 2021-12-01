package com.rsk;

import java.util.*;
import java.util.stream.Collectors;

public final class Groups {

    private ArrayList<ArrayList<Integer>> groups;
    private int[][] groupAdjacencyMatrix;

    public Groups(MatrixContainer matrices) {
        this.groups = divideByGroups(matrices.getQuadraticMatrix());
        this.groupAdjacencyMatrix = createGroupAdjacencyMatrix(groups, matrices.getAdjacencyMatrix());
        this.groups = sortByOperationAmount(groupAdjacencyMatrix);
        System.out.println("Groups");
        for (int i = 0; i < groups.size(); i++) {
            System.out.println(groups.get(i));
        }
        System.out.println();
        System.out.println("Refined group");
        groupRefinement(matrices.getAdjacencyMatrix());
        for (int i = 0; i < groups.size(); i++) {
            System.out.println(groups.get(i));
        }
        this.groupAdjacencyMatrix = createGroupAdjacencyMatrix(groups, matrices.getAdjacencyMatrix());

    }

    // sort group adjacencyMatrix
    private void groupRefinement(int[][] adjacencyMatrix) {
        ArrayList<ArrayList<Integer>> refinedGroups = new ArrayList<>(groups.size());
        for (int i = 0; i < groups.size(); i++) {
            refinedGroups.add(new ArrayList<>());
        }
        for (int i = 0; i < groups.size(); i++) {
            for (int j = i + 1; j < groups.size(); j++) {
                if (isGroupAbsorbsAnotherGroup(groupAdjacencyMatrix[i], groupAdjacencyMatrix[j])) {
//                    System.out.println("Absorber" + groups.get(i));
                    groups.get(i).addAll(groups.get(j));
//                    System.out.println("After absorb" + groups.get(i));
//                    System.out.println("Before remove: " + groups.get(j));
                    groups.remove(groups.get(j));
                    break;
                } else {
                    for (int k = 0; k < groups.get(j).size(); k++) {
                        if (isGroupAbsorbsAnotherGroup(groupAdjacencyMatrix[i], adjacencyMatrix[groups.get(j).get(k)])) {
//                            System.out.println("Absorber " + groups.get(i));
//                            System.out.println("Group that is being checked " + groups.get(j));
//                            System.out.println("Operation to absorb " + groups.get(j).get(k));
                            groups.get(i).add(groups.get(j).get(k));
                            refinedGroups.get(i).add(groups.get(j).get(k));
//                            System.out.println("After absorb " + refinedGroups.get(i));
                            groups.get(j).remove(k);
                            k--;
                        }
                    }
                }
            }
        }
    }

    private ArrayList<ArrayList<Integer>> sortByOperationAmount(int[][] groupAdjacencyMatrix) {
        ArrayList<ArrayList<Integer>> sortedGroups = new ArrayList<>(groupAdjacencyMatrix.length);
        int[][] groupMatrix = new int[groupAdjacencyMatrix.length][groupAdjacencyMatrix[0].length];
        for (int i = 0; i < groupAdjacencyMatrix.length; i++) {
            sortedGroups.add(new ArrayList<>());
        }
        int[] zeroAmountInEachGroup = countZerosInGroups(groupAdjacencyMatrix);
        int indexContainer;
        for (int i = 0; i < zeroAmountInEachGroup.length; i++) {
            indexContainer = findIndexOfLowestElement(zeroAmountInEachGroup);
            if (zeroAmountInEachGroup[indexContainer] != zeroAmountInEachGroup.length) {
                sortedGroups.get(i).addAll(groups.get(indexContainer));
                zeroAmountInEachGroup[indexContainer] = zeroAmountInEachGroup.length;
                groupMatrix[i] = groupAdjacencyMatrix[indexContainer];
            }
        }
        this.groupAdjacencyMatrix = groupMatrix;
        return sortedGroups;
    }

    private static int[] countZerosInGroups(int[][] groupAdjacencyMatrix) {
        int[] zeroAmountInEachGroup = new int[groupAdjacencyMatrix.length];
        int nullCounter;
        for (int i = 0; i < groupAdjacencyMatrix.length; i++) {
            nullCounter = 0;
            for (int j = 0; j < groupAdjacencyMatrix[i].length; j++) {
                if (groupAdjacencyMatrix[i][j] == 0) {
                    nullCounter++;
                }
            }
            zeroAmountInEachGroup[i] = nullCounter;
        }
        return zeroAmountInEachGroup;
    }

    private static int findIndexOfLowestElement(int[] zeroAmountInEachGroup) {
        int index = 0;
        int lowestElement = zeroAmountInEachGroup.length;
        for (int i = 0; i < zeroAmountInEachGroup.length; i++) {
            if (zeroAmountInEachGroup[i] < lowestElement) {
                index = i;
                lowestElement = zeroAmountInEachGroup[i];
            }
        }
        return index;
    }

    private static int[][] createGroupAdjacencyMatrix(ArrayList<ArrayList<Integer>> groups, int[][] adjacencyMatrix) {
        int[][] groupAdjacencyMatrix = new int[groups.size()][adjacencyMatrix[0].length];
        for (int i = 0; i < groups.size(); i++) {
            int[] combinedGroup = combineOperationsIntoGroup(groups.get(i), adjacencyMatrix);
            for (int j = 0; j < groupAdjacencyMatrix[i].length; j++) {
                groupAdjacencyMatrix[i][j] = combinedGroup[j];
            }
        }
        return groupAdjacencyMatrix;
    }

    private static boolean isGroupAbsorbsAnotherGroup(int[] absorber, int[] potentialGroupToAbsorb) {
        if (absorber.length != potentialGroupToAbsorb.length) {
            System.err.println("Length of both groups should be the same!");
            return false;
        } else {
            for (int i = 0; i < absorber.length; i++) {
                if (potentialGroupToAbsorb[i] == 1 && absorber[i] != potentialGroupToAbsorb[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private static int[] combineOperationsIntoGroup(ArrayList<Integer> group, int[][] adjacencyMatrix) {
        int[] groupOperations = new int[adjacencyMatrix[group.get(0)].length];
        for (int i = 0; i < group.size(); i++) {
            for (int j = 0; j < adjacencyMatrix[group.get(i)].length; j++) {
                if (adjacencyMatrix[group.get(i)][j] == 1) {
                    groupOperations[j] = adjacencyMatrix[group.get(i)][j];
                }
            }
        }
        return groupOperations;
    }

    private static ArrayList<ArrayList<Integer>> divideByGroups(int[][] quadraticMatrix) {

        ArrayList<ArrayList<Integer>> groups = new ArrayList<>(quadraticMatrix.length);

        while (true) {
            ArrayList<ArrayList<Integer>> numberCoordinates = new ArrayList<>();
            LinkedHashSet<Integer> subgroups = new LinkedHashSet<>(quadraticMatrix.length);
            // find max element
            int[] maxElementCoordinates = findMaxElementCoordinates(quadraticMatrix);
            int maxElement = quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]];
            if (maxElement == 0) {

                return addOperations(groups);
            }
            subgroups.add(maxElementCoordinates[0]);
            subgroups.add(maxElementCoordinates[1]);
            quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]] = 0;
            // clear vertical and horizontal lines on maxElem[0] and maxElem[1]
            // horizontal lines on i j
            for (int j = 0; j < maxElementCoordinates.length; j++) {
                for (int i = 0; quadraticMatrix[maxElementCoordinates[j]][i] != -1; i++) {
                    if (quadraticMatrix[maxElementCoordinates[j]][i] == maxElement) {
                        numberCoordinates.add(new ArrayList<>());
                        numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinates[j]);
                        numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                        subgroups.add(maxElementCoordinates[j]);
                        subgroups.add(i);

                    } else {
                        quadraticMatrix[maxElementCoordinates[j]][i] = 0;
                    }
                }
            }
            // vertical lines i j
            for (int j = 0; j < maxElementCoordinates.length; j++) {
                for (int i = quadraticMatrix.length - 1; quadraticMatrix[i][maxElementCoordinates[j]] != -1; i--) {
                    if (quadraticMatrix[i][maxElementCoordinates[j]] == maxElement) {
                        numberCoordinates.add(new ArrayList<>());
                        numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                        numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinates[j]);
                        subgroups.add(i);
                        subgroups.add(maxElementCoordinates[j]);
                    } else {
                        quadraticMatrix[i][maxElementCoordinates[j]] = 0;
                    }
                }
            }
            for (int j = 0; numberCoordinates.size() != 0; ) {
                maxElementCoordinates[0] = numberCoordinates.get(j).get(0);
                maxElementCoordinates[1] = numberCoordinates.get(j).get(1);
                maxElement = quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]];
                quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]] = 0;
                numberCoordinates.remove(j);
                // clear vertical and horizontal lines on maxElem[0] and maxElem[1]
                // horizontal lines on i j
                for (int k = 0; k < maxElementCoordinates.length; k++) {
                    for (int i = 0; quadraticMatrix[maxElementCoordinates[k]][i] != -1; i++) {
                        if (quadraticMatrix[maxElementCoordinates[k]][i] == maxElement) {
                            numberCoordinates.add(new ArrayList<>());
                            numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinates[k]);
                            numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                            subgroups.add(maxElementCoordinates[k]);
                            subgroups.add(i);

                        } else {
                            quadraticMatrix[maxElementCoordinates[k]][i] = 0;
                        }
                    }
                }
                // vertical lines i j
                for (int k = 0; k < maxElementCoordinates.length; k++) {
                    for (int i = quadraticMatrix.length - 1; quadraticMatrix[i][maxElementCoordinates[k]] != -1; i--) {
                        if (quadraticMatrix[i][maxElementCoordinates[k]] == maxElement) {
                            numberCoordinates.add(new ArrayList<>());
                            numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                            numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinates[k]);
                            subgroups.add(i);
                            subgroups.add(maxElementCoordinates[k]);
                        } else {
                            quadraticMatrix[i][maxElementCoordinates[k]] = 0;
                        }
                    }
                }
            }
            groups.add(new ArrayList<>());
            groups.get(groups.size() - 1).addAll(subgroups);
        }
    }


    private static ArrayList<ArrayList<Integer>> addOperations(ArrayList<ArrayList<Integer>> groups) {
        List<Integer> allGroups = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < groups.get(i).size(); j++) {
                allGroups.add(groups.get(i).get(j));
            }
        }
        List<Integer> sortedGroups = allGroups.stream().sorted().collect(Collectors.toList());
        int counter = 0;
        for (int i = 0; i < sortedGroups.size(); i++) {

            if (i != sortedGroups.get(counter)) {
                groups.add(new ArrayList<>());
                groups.get(groups.size() - 1).add(i);
                counter--;
            }
            counter++;
        }
        return groups;
    }

    private static int[] findMaxElementCoordinates(int[][] quadraticMatrix) {
        int[] coordinates = new int[]{0, 2};
        int maxNum = 0;
        for (int i = 1; i < quadraticMatrix.length; i++) {
            for (int j = 0; j < i; j++) {
                if (quadraticMatrix[i][j] > maxNum) {
                    maxNum = quadraticMatrix[i][j];
                    coordinates[0] = i;
                    coordinates[1] = j;
                }
            }
        }
        return coordinates;
    }

    public ArrayList<ArrayList<Integer>> getGroups() {
        return groups;
    }
}