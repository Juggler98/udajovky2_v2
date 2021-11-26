
import java.util.ArrayList;
import java.util.Random;

public class TestGenerator {

//    private int operationCount;
//
//    public OperationGenerator(int operationCount) {
//        this.operationCount = operationCount;
//    }
//
//    public Operation getOperation() {
//        Random random = new Random();
//        return Operation.values()[random.nextInt(Operation.values().length)];
//    }
//
//    public int getOperationCount() {
//        return this.operationCount;
//    }

    public void runTests() {
        int testCount = 1;
        Random random = new Random();
        Random random1 = new Random();
        for (int j = 0; j < testCount; j++) {
            System.out.println("--------------------------NEW-TEST-------------------------------------");
            int operationCount = 100;
            int addCount = 0;
            int removeCount = 0;
            int addNotPossible = 0;
            int removeNotPossible = 0;
            random.setSeed(3);
            random1.setSeed(2);
            ArrayList<Integer> testArrayList = new ArrayList<>();
            ArrayList<Long> filePositions = new ArrayList<>();
            ArrayList<TestClass> testClasses = new ArrayList<>();

            boolean goRandom = true;

            TestClass[] testNumbers = new TestClass[20];
            if (!goRandom) {
                for (int i = 0; i < testNumbers.length; i++) {
                    testNumbers[i] = new TestClass(i);
                }
            }

            int addPercentage = 100;
            int removePercentage = 100 - addPercentage;
            int randomNumberBound = 10;
            if (goRandom) {
                for (int i = 0; i < operationCount; i++) {
                    if (i % 10000 == 0) {
                        System.out.println(i + " operation");
                    }
                    TestClass testClass = new TestClass((Integer) random.nextInt(randomNumberBound));
                    int randomOperation = random1.nextInt(100);
                    if (randomOperation < addPercentage) {
                        System.out.println("ADD: " + testClass.getValue());
                        addCount++;
                        boolean arrayListNotAdd = !testArrayList.contains(testClass.getValue());
                        if (arrayListNotAdd) {
                            testArrayList.add(testClass.getValue());
                            testClasses.add(testClass);
                        }
                    } else {
                            removeCount++;
                            int keyToDelete = testArrayList.get(random.nextInt(testArrayList.size()));
                            System.out.println("Remove: " + keyToDelete);
                            if (testArrayList.contains(keyToDelete)) {
                                testArrayList.remove((Integer) keyToDelete);
                            }
                    }
                }

                DataFile<TestClass> d = new DataFile<>();

                //System.out.println("---------NUMBERS-IN-ARRAYLIST----------");
                //Collections.sort(testArrayList);
//                for (Integer i : testArrayList) {
//                    System.out.println(i);
//                }

                System.out.println("---------NUMBERS-IN-CLASS-ARRAYLIST----------");
                for (TestClass t : testClasses) {
                    filePositions.add(d.write(t));
                    System.out.println(t.getValue());
                }

                TestClass temp = new TestClass();
                temp.fromByteArray(d.read(0, temp.getSize()));
                System.out.println(temp);

                d.delete(0, temp);

                temp.fromByteArray(d.read(0, temp.getSize()));
                System.out.println(temp);

                temp.setValue(100);
                temp.setValid(true);
                d.write(temp);

                temp.fromByteArray(d.read(0, temp.getSize()));
                System.out.println(temp);


            }

//            System.out.println("-----------PREORDER------------");
//            testTree.preorder((TTTreeNode<Integer, tests.TestClass>) testTree.getRoot());
//
//            testTree.add(testNumbers[0]);
//            System.out.println("----------TREE-------------");
//            testTree.preorder((TTTreeNode<Integer, tests.TestClass>) testTree.getRoot());
//
//            testTree.remove(3);
//            System.out.println("----------TREE-------------");
//            testTree.preorder((TTTreeNode<Integer, tests.TestClass>) testTree.getRoot());
//
//            testTree.remove(8);
//            tests.TestClass t = new tests.TestClass(3);
//            testTree.add(t);
//
//            System.out.println(((TTTreeNode<?, ?>) testTree.getRoot()).hasParent());
//
//            System.out.println("-------After-change-------");
//
//            testTree.preorder((TTTreeNode<Integer, tests.TestClass>) testTree.getRoot());


            System.out.println();
            System.out.println("addCount: " + addCount);
            System.out.println("notAdded: " + addNotPossible);
            System.out.println("removeCount: " + removeCount);
            System.out.println("notRemove: " + removeNotPossible);
            System.out.println();
            System.out.println("ArrayList size " + testArrayList.size());


        }
    }


}
