package tests;

import data.DataFile;
import data.EmptyPosition;
import BOrderThreeTree.BOTTree;
import BOrderThreeTree.BOTTreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TestGenerator {

    private DataFile<TestClass> dataFile = new DataFile<>("0TestClass", new TestClass());
    private BOTTree<TestClassIndex> testTree;
    private ArrayList<Integer> testArrayList;
    private Random randomValue = new Random();
    private Random randomForOperation = new Random();
    private Random randomSeed = new Random();

    public TestGenerator() {
    }

    public void runTests() {
        int testCount = 1;
        for (int j = 0; j < testCount; j++) {
            dataFile.clearData();
            System.out.println("--------------------------NEW-TEST-------------------------------------");
            int operationCount = 100;
            int addCount = 0;
            int removeCount = 0;
            int addNotPossible = 0;
            int removeNotPossible = 0;
            int randomValueSeed = randomSeed.nextInt(100000000);
            int randomForOperationSeed = randomSeed.nextInt(100000000);
            randomValue.setSeed(randomValueSeed);
            randomForOperation.setSeed(randomForOperationSeed);
            ArrayList<Integer> testArrayList = new ArrayList<>();
            ArrayList<Long> filePositions = new ArrayList<>();
            ArrayList<TestClass> testClasses = new ArrayList<>();

            boolean goRandom = true;

            TestClass[] testNumbers = new TestClass[100];
            if (!goRandom) {
                for (int i = 0; i < testNumbers.length; i++) {
                    testNumbers[i] = new TestClass(i, "nothing");
                }
            }

            int addPercentage = 50;
            int randomNumberBound = 100;

            if (goRandom) {
                for (int i = 0; i < operationCount; i++) {
                    if (i % 10000 == 0) {
                        System.out.println(i + " operation");
                    }
                    TestClass testClass = new TestClass((Integer) randomValue.nextInt(randomNumberBound), "nothing");
                    int randomOperation = randomForOperation.nextInt(100);
                    if (randomOperation < addPercentage) {
                        System.out.println("ADD: " + testClass.getValue());
                        addCount++;
                        boolean arrayListNotAdd = !testArrayList.contains(testClass.getValue());
                        if (arrayListNotAdd) {
                            testArrayList.add(testClass.getValue());
                            testClasses.add(testClass);
                            filePositions.add(dataFile.write(testClass));
                        }
                    } else {
                        removeCount++;
                        if (testArrayList.size() > 0) {
                            int keyToDelete = testArrayList.get(randomValue.nextInt(testArrayList.size()));
                            System.out.println("Remove: " + keyToDelete);
                            for (int deleteIndex = 0; deleteIndex < testArrayList.size(); deleteIndex++) {
                                if (testArrayList.get(deleteIndex) == keyToDelete) {
                                    testArrayList.remove(deleteIndex);
                                    testClasses.remove(deleteIndex);
                                    dataFile.delete(filePositions.get(deleteIndex));
                                    filePositions.remove(deleteIndex);
                                    break;
                                }
                            }
                        } else {
                            removeNotPossible++;
                        }
                    }
                }

                System.out.println("---------NUMBERS-IN-CLASS-ARRAYLIST----------");
                for (TestClass t : testClasses) {
                    System.out.println(t.getValue());
                }

                System.out.println("-----DATA-IN-FILE-FROM-POSITION-ARRAYLIST-------");
                for (long i : filePositions) {
                    TestClass t = new TestClass();
                    t.fromByteArray(dataFile.read(i));
                    System.out.println(t);
                }

                this.dataInFileToConsole();
                this.emptyPositionsToConsole();

                System.out.println("Add 100");
                TestClass temp = new TestClass(100, "AdamBeliansky");
                filePositions.add(dataFile.write(temp));
                testClasses.add(temp);
                testArrayList.add(100);

                this.dataInFileToConsole();
                this.emptyPositionsToConsole();

                boolean dataIsGood = true;
                for (int i = 0; i < filePositions.size(); i++) {
                    long position = filePositions.get(i);
                    TestClass t = new TestClass();
                    t.fromByteArray(dataFile.read(position));
                    if (t.compareTo(testClasses.get(i)) != 0) {
                        dataIsGood = false;
                        break;
                    }
                }
                if (dataIsGood) {
                    System.out.println("\nVYSLEDOK TESTU:");
                    System.out.println("Uspesny. Data su rovnake v arrayliste aj v subore.");
                } else {
                    System.out.println("CHYBA!!! Data NIE su rovnake v arrayliste a v subore.");
                    System.out.println("RandomValueSeed: " + randomValueSeed + " RandomForOperationSeed: " + randomForOperationSeed);
                    break;
                }

//                tests.TestClass temp = new tests.TestClass();
//                temp.fromByteArray(dataFile.read(0, temp.getSize()));
//                System.out.println(temp);
//
//                dataFile.delete(0, temp);
//
//                temp.fromByteArray(dataFile.read(0, temp.getSize()));
//                System.out.println(temp);
//
//                temp.setValue(100);
//                temp.setValid(true);
//                dataFile.write(temp);
//
//                temp.fromByteArray(dataFile.read(0, temp.getSize()));
//                System.out.println(temp);

                System.out.println();
                System.out.println("addCount: " + addCount);
                System.out.println("notAdded: " + addNotPossible);
                System.out.println("removeCount: " + removeCount);
                System.out.println("notRemove: " + removeNotPossible);
                System.out.println();
                System.out.println("ArrayList size " + testClasses.size());

            } else {
                System.out.println("Add 0 2 4 5");
                dataFile.write(testNumbers[0]);
                dataFile.write(testNumbers[2]);
                dataFile.write(testNumbers[4]);
                dataFile.write(testNumbers[5]);
                this.dataInFileToConsole();

                System.out.println("\nDelete 0 2 4");
                dataFile.delete(0);
                dataFile.delete(45);
                dataFile.delete(90);
                this.emptyPositionsToConsole();
                this.dataInFileToConsole();

                System.out.println("delete 5");
                dataFile.delete(135);
                this.dataInFileToConsole();
                this.emptyPositionsToConsole();

                System.out.println("\nadd 6");
                dataFile.write(testNumbers[6]);
                this.dataInFileToConsole();
                this.emptyPositionsToConsole();
            }
        }
    }

    public void runTestsTree() {
        int testCount = 1;
        for (int j = 0; j < testCount; j++) {
            dataFile.clearData();
            System.out.println("--------------------------NEW-TEST-------------------------------------");
            int operationCount = 1000;
            int addCount = 0;
            int removeCount = 0;
            int addNotPossible = 0;
            int removeNotPossible = 0;
            int randomValueSeed = randomSeed.nextInt(100000000);
            int randomForOperationSeed = randomSeed.nextInt(100000000);
            System.out.println("RandomValueSeed: " + randomValueSeed + " RandomForOperationSeed: " + randomForOperationSeed);
            randomValue.setSeed(randomValueSeed);
            randomForOperation.setSeed(randomForOperationSeed);
            testArrayList = new ArrayList<>();
            testTree = new BOTTree<>("0testingTree", new BOTTreeNode<>(new TestClassIndex()));
            ArrayList<TestClass> testClasses = new ArrayList<>();

            boolean goRandom = true;

            TestClass[] testNumbers = new TestClass[20];
            if (!goRandom) {
                for (int i = 0; i < testNumbers.length; i++) {
                    testNumbers[i] = new TestClass(i, "nothing");
                }
            }

            int addPercentage = 50;
            int randomNumberBound = 500;
            if (goRandom) {
                for (int i = 0; i < operationCount; i++) {
                    if (i % 10000 == 0) {
                        System.out.println(i + " operation");
                    }
                    System.out.printf("------------------OPERATION-%d------------------\n", i);
                    TestClass testClass = new TestClass(randomValue.nextInt(randomNumberBound), "nothing");
                    int randomOperation = randomForOperation.nextInt(100);
                    if (randomOperation < addPercentage) {
                        System.out.println("ADD: " + testClass.getValue());
                        addCount++;
                        boolean arrayListNotAdd = !testArrayList.contains(testClass.getValue());
                        boolean treeNotAdd = false;
                        if (arrayListNotAdd) {
                            testArrayList.add(testClass.getValue());
                            testClasses.add(testClass);
                            TestClassIndex t = new TestClassIndex(testClass.getValue(), dataFile.write(testClass));
                            treeNotAdd = testTree.add(t);
                            if (!treeNotAdd) {
                                addNotPossible++;
                            }
                            this.treeToConsole();
                            this.dataInFileToConsole();
                            this.emptyPositionsToConsole();
                            this.emptyPositionsTreeToConsole();
                            this.testArrayToConsole();
                            //System.out.println("root: " + testTree.getRoot());
                        }
                        if (!treeNotAdd && arrayListNotAdd) {
                            System.out.println(testClass.getValue());
                            System.out.println("-------Test-Problem------");
                            System.out.println(randomValueSeed);
                            System.out.println(randomForOperationSeed);
                            throw new IllegalStateException("treeNotAdd && !arrayListNotAdd");
                            //break;
                        }
                    } else {
                        if (testTree.getSize() > 0) {
                            removeCount++;
                            int keyToDelete = testArrayList.get(randomValue.nextInt(testArrayList.size()));
                            System.out.println("Remove: " + keyToDelete);

                            //TestClass t = new TestClass(keyToDelete, "");
                            TestClassIndex testClassIndex = new TestClassIndex(keyToDelete);

                            for (int deleteIndex = 0; deleteIndex < testArrayList.size(); deleteIndex++) {
                                if (testArrayList.get(deleteIndex) == keyToDelete) {
                                    testArrayList.remove(deleteIndex);
                                    testClasses.remove(deleteIndex);
                                    testClassIndex = testTree.remove(testClassIndex);
                                    dataFile.delete(testClassIndex.getDataPosition());
                                    this.treeToConsole();
                                    this.dataInFileToConsole();
                                    this.emptyPositionsToConsole();
                                    this.emptyPositionsTreeToConsole();
                                    this.testArrayToConsole();
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Empty tree");
                        }
                    }
                }
            } else {
                TestClassIndex t = new TestClassIndex(testNumbers[0].getValue(), dataFile.write(testNumbers[0]));
                testTree.add(t);

                this.treeToConsole();

                t = new TestClassIndex(testNumbers[1].getValue(), dataFile.write(testNumbers[1]));
                testTree.add(t);

                this.treeToConsole();

                System.out.println();
                this.dataInFileToConsole();
            }

//            TestClass testClassTemp = new TestClass(randomValue.nextInt(randomNumberBound), "AdamBeliansky");
//            testArrayList.add(testClassTemp.getValue());
//            testClasses.add(testClassTemp);
//            TestClassIndex tc = new TestClassIndex(testClassTemp.getValue(), dataFile.write(testClassTemp));
//            boolean treeNotAdd = testTree.add(tc);
//            if (!treeNotAdd) {
//                addNotPossible++;
//            }
//            this.treeToConsole();
//            this.dataInFileToConsole();
//            this.emptyPositionsToConsole();
//            this.emptyPositionsTreeToConsole();
//            this.testArrayToConsole();

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

            System.out.println("-----------PREORDER------------");
            testTree.preorder(testTree.getRoot());

            System.out.println("-----------INORDER------------");
            testTree.inOrderRecursive(testTree.getRoot());

            System.out.println("---------LEAF-DEEP----------");
            testTree.deepOfLeaf(testTree.getRoot());

            System.out.println();
            System.out.println("addCount: " + addCount);
            System.out.println("notAdded: " + addNotPossible);
            System.out.println("removeCount: " + removeCount);
            System.out.println("notRemove: " + removeNotPossible);
            System.out.println();
            System.out.println("Tree size: " + testTree.getSize());
            System.out.println("ArrayList size " + testArrayList.size());
            System.out.println("Tree height: " + testTree.getHeight());

            this.testArrayToConsole();

            System.out.println("Inorder vsetky hodnoty stromu:");
            ArrayList<TestClassIndex> hodnoty = testTree.getInOrderData();
            for (TestClassIndex testClass : hodnoty) {
                System.out.println(testClass.getValue());
            }
            System.out.println("Inorder interval:");
            TestClassIndex t1 = new TestClassIndex(20, 0);
            TestClassIndex t2 = new TestClassIndex(70, 0);
            ArrayList<TestClassIndex> hodnotyInterval = testTree.getIntervalData(t1, t2);
            for (TestClassIndex testClass : hodnotyInterval) {
                System.out.println(testClass.getValue());
            }

            boolean dataIsGood = true;
            Collections.sort(testClasses, (a, b) -> a.compareTo(b));
            for (int i = 0; i < hodnoty.size(); i++) {
                TestClassIndex testClassIndex = hodnoty.get(i);
                TestClass t = new TestClass();
                t.fromByteArray(dataFile.read(testClassIndex.getDataPosition()));
                if (t.compareTo(testClasses.get(i)) != 0 && !testClassIndex.getValue().equals(t.getValue())) {
                    dataIsGood = false;
                    break;
                }
            }
            if (dataIsGood) {
                System.out.println("\nVYSLEDOK TESTU:");
                System.out.println("Uspesny. Data v strome obsahuju a odkazuju na spravne data v neutriedenom subore.");
            } else {
                System.out.println("CHYBA!!! Data v strome neobsahuju alebo neodkazuju na spravne data v subore.");
                System.out.println("RandomValueSeed: " + randomValueSeed + " RandomForOperationSeed: " + randomForOperationSeed);
                throw new IllegalStateException("CHYBA!!! Data v strome neobsahuju alebo neodkazuju na spravne data v subore.");
                //break;
            }

        }
    }

    public void emptyPositionsToConsole() {
        System.out.println("----------------EMPTY-POSITIONS-----------------");
        ArrayList<EmptyPosition> emptyPositionsArrayList = dataFile.getAllEmptyPositions();
        for (EmptyPosition t : emptyPositionsArrayList) {
            System.out.println(t);
        }
    }

    public void dataInFileToConsole() {
        System.out.println("----------------DATA-IN-FILE-----------------");
        ArrayList<TestClass> tempArrayList = dataFile.getAllData();
        for (TestClass t : tempArrayList) {
            System.out.println(t);
        }
    }

    public void treeToConsole() {
        System.out.println("----------------TREE-FILE------------------");
        testTree.printInfo();
        ArrayList<BOTTreeNode<TestClassIndex>> arrayList = testTree.getAllData();
        int i = 0;
        for (BOTTreeNode<TestClassIndex> node : arrayList) {
            if (node.hasDataL()) {
                i++;
            }
            if (node.hasDataR()) {
                i++;
            }
        }
        System.out.println("Item count: " + i);
        for (BOTTreeNode<TestClassIndex> node : arrayList) {
            System.out.println(node);
        }
    }

    public void testArrayToConsole() {
        System.out.println("---------NUMBERS-IN-ARRAYLIST----------");
        Collections.sort(testArrayList);
        for (Integer tempI : testArrayList) {
            System.out.println(tempI);
        }
    }

    public void emptyPositionsTreeToConsole() {
        System.out.println("---------TREE-EMPTY-POSITIONS----------");
        ArrayList<EmptyPosition> arrayList = testTree.getAllEmptyPositions();
        for (EmptyPosition e : arrayList) {
            System.out.println(e.getPosition());
        }
    }





}
