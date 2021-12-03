package rsk.src.com;

import java.util.*;

public final class Groups {

    private ArrayList<ArrayList<Integer>> groups;
    private int[][] groupAdjacencyMatrix;
    private final ArrayList<String> uniqueElements;

    public Groups(MatrixContainer matrices) {
        this.uniqueElements = matrices.getUniqueElements();
        this.groups = divideByGroups(matrices.getQuadraticMatrix());
        System.out.println("BEFORE SORT");
        for (ArrayList<Integer> group : groups) {
            System.out.println(group);
        }
        this.groupAdjacencyMatrix = createGroupAdjacencyMatrix(groups, matrices.getAdjacencyMatrix());
        this.groups = sortByOperationAmount(groupAdjacencyMatrix);
        System.out.println("Groups");
        for (ArrayList<Integer> group : groups) {
            System.out.println(group);
        }
        System.out.println();
        System.out.println("Refined group");
        groupRefinement(matrices.getAdjacencyMatrix());
        for (ArrayList<Integer> group : groups) {
            System.out.println(group);
        }
        System.out.println();
        this.groupAdjacencyMatrix = createGroupAdjacencyMatrix(groups, matrices.getAdjacencyMatrix());

    }


    private void groupRefinement(int[][] adjacencyMatrix) {
        ArrayList<ArrayList<Integer>> refinedGroups = new ArrayList<>(groups.size());
        for (int i = 0; i < groups.size(); i++) {
            refinedGroups.add(new ArrayList<>());
        }
        for (int i = 0; i < groups.size(); i++) {
            for (int j = i + 1; j < groups.size(); j++) {
                if (isGroupAbsorbsAnotherGroup(groupAdjacencyMatrix[i], groupAdjacencyMatrix[j])) {
                    groups.get(i).addAll(groups.get(j));
                    groups.remove(groups.get(j));
                    break;
                } else {
                    for (int k = 0; k < groups.get(j).size(); k++) {
                        if (isGroupAbsorbsAnotherGroup(groupAdjacencyMatrix[i], adjacencyMatrix[groups.get(j).get(k)])) {
                            groups.get(i).add(groups.get(j).get(k));
                            refinedGroups.get(i).add(groups.get(j).get(k));
                            groups.get(j).remove(k);
                            k--;
                        }
                    }
                }
            }
        }
    }

    private ArrayList<ArrayList<Integer>> sortByOperationAmount(int[][] groupAdjacencyMatrix) {
        ArrayList<ArrayList<Integer>> sortedGroups = new ArrayList<>(groups.size());
        int[][] groupMatrix = new int[groupAdjacencyMatrix.length][groupAdjacencyMatrix[0].length];
        for (int i = 0; i < groups.size(); i++) {
            sortedGroups.add(new ArrayList<>());
        }
        for (int i = 0; i < groupAdjacencyMatrix.length; i++) {
            System.out.println(Arrays.toString(groupAdjacencyMatrix[i]));
        }
        int[] zeroAmountInEachGroup = countZerosInGroups(groupAdjacencyMatrix);
        System.out.println(Arrays.toString(zeroAmountInEachGroup) + "kekes");
        int indexContainer;
        for (int i = 0; i < zeroAmountInEachGroup.length; i++) {
            indexContainer = findIndexOfLowestElement(zeroAmountInEachGroup);
            if (zeroAmountInEachGroup[indexContainer] != groupAdjacencyMatrix[0].length) {
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
        int lowestElement = zeroAmountInEachGroup[0];
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
        int[] combinedGroup;
        for (int i = 0; i < groups.size(); i++) {
            combinedGroup = combineOperationsIntoGroup(groups.get(i), adjacencyMatrix);
            System.arraycopy(combinedGroup, 0, groupAdjacencyMatrix[i], 0, groupAdjacencyMatrix[i].length);

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
        int[] groupOperations = new int[adjacencyMatrix[0].length];
        for (int i = 0; i < group.size(); i++) {
            for (int j = 0; j < adjacencyMatrix[group.get(i)].length; j++) {
                if (adjacencyMatrix[group.get(i)][j] == 1) {
                    groupOperations[j] = 1;
                }
            }
        }
        return groupOperations;
    }

    private ArrayList<ArrayList<Integer>> divideByGroups(int[][] quadraticMatrix) {
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>(quadraticMatrix.length);
        LinkedHashSet<Integer> subgroups;
        int[] maxElementCoordinates = findMaxElementCoordinates(quadraticMatrix);
        int maxElement = quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]];
        while (maxElement != 0) {
            ArrayList<ArrayList<Integer>> numberCoordinates = new ArrayList<>();
            subgroups = new LinkedHashSet<>(quadraticMatrix.length);
            // find max element
            subgroups.add(maxElementCoordinates[0]);
            subgroups.add(maxElementCoordinates[1]);
            // mark as visited
            quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]] = 0;
            /* clear vertical and horizontal lines on maxElem[0] and maxElem[1]
            * horizontal lines on i j
            * if we found another cell with the max number -> we add it into the list (numberCoordinate)
            * or just mark current cell as visited (just assign zero to this cell)
            */
            for (int maxElementCoordinate : maxElementCoordinates) {
                for (int i = 0; quadraticMatrix[maxElementCoordinate][i] != -1; i++) {
                    // found another cell with the max number -> we add it into the list
                    if (quadraticMatrix[maxElementCoordinate][i] == maxElement) {
                        numberCoordinates.add(new ArrayList<>());
                        numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinate);
                        numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                        subgroups.add(maxElementCoordinate);
                        subgroups.add(i);

                    } else {
                        // or just mark current cell as visited
                        quadraticMatrix[maxElementCoordinate][i] = 0;
                    }
                }
            }
            // vertical lines i j
            // and same staff here as I mentioned above
            for (int maxElementCoordinate : maxElementCoordinates) {
                for (int i = quadraticMatrix.length - 1; quadraticMatrix[i][maxElementCoordinate] != -1; i--) {
                    if (quadraticMatrix[i][maxElementCoordinate] == maxElement) {
                        numberCoordinates.add(new ArrayList<>());
                        numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                        numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinate);
                        subgroups.add(i);
                        subgroups.add(maxElementCoordinate);
                    } else {
                        quadraticMatrix[i][maxElementCoordinate] = 0;
                    }
                }
            }
            // in this loop we check all cells, that we found on the previous iterations, with the same logic
            while (maxElement > 0 && numberCoordinates.size() > 0) {
                maxElementCoordinates[0] = numberCoordinates.get(0).get(0);
                maxElementCoordinates[1] = numberCoordinates.get(0).get(1);
                maxElement = quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]];
                // mark as visited
                quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]] = 0;
                numberCoordinates.remove(0);
                // clear vertical and horizontal lines on maxElem[0] and maxElem[1]
                // horizontal lines on i j
                for (int maxElementCoordinate : maxElementCoordinates) {
                    for (int i = 0; quadraticMatrix[maxElementCoordinate][i] != -1; i++) {
                        // found another cell with the max number -> we add it into the list
                        if (quadraticMatrix[maxElementCoordinate][i] == maxElement) {
                            numberCoordinates.add(new ArrayList<>());
                            numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinate);
                            numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                            subgroups.add(maxElementCoordinate);
                            subgroups.add(i);

                        } else {
                            // mark as visited
                            quadraticMatrix[maxElementCoordinate][i] = 0;
                        }
                    }
                }
                // vertical lines i j
                // and here ...
                for (int maxElementCoordinate : maxElementCoordinates) {
                    for (int i = quadraticMatrix.length - 1; quadraticMatrix[i][maxElementCoordinate] != -1; i--) {
                        // found another cell with the max number -> we add it into the list
                        if (quadraticMatrix[i][maxElementCoordinate] == maxElement) {
                            numberCoordinates.add(new ArrayList<>());
                            numberCoordinates.get(numberCoordinates.size() - 1).add(i);
                            numberCoordinates.get(numberCoordinates.size() - 1).add(maxElementCoordinate);
                            subgroups.add(i);
                            subgroups.add(maxElementCoordinate);
                        } else {
                            // mark as visited
                            quadraticMatrix[i][maxElementCoordinate] = 0;
                        }
                    }
                }
            }
            // In the end of the iteration we add all operations into the group
            if (subgroups.size() != 0) {
                groups.add(new ArrayList<>());
                groups.get(groups.size() - 1).addAll(subgroups);
            }
            // then we check next maxElement
            maxElementCoordinates = findMaxElementCoordinates(quadraticMatrix);
            maxElement = quadraticMatrix[maxElementCoordinates[0]][maxElementCoordinates[1]];
        }
        return addOperations(groups, quadraticMatrix.length);
    }


    private ArrayList<ArrayList<Integer>> addOperations(ArrayList<ArrayList<Integer>> groups, int maxElementsAmount) {
        List<Integer> allGroups = new ArrayList<>(uniqueElements.size());
        for (ArrayList<Integer> group : groups) {
            allGroups.addAll(group);
        }
        for (int i = 0; i < maxElementsAmount; i++) {
            if (!allGroups.contains(i)) {
                groups.add(new ArrayList<>());
                groups.get(groups.size() - 1).add(i);
            }
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
