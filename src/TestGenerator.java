
import java.util.ArrayList;
import java.util.Random;

public class TestGenerator {

    DataFile<TestClass> dataFile = new DataFile<>("TestClass");

    public TestGenerator() {
    }

    public void runTests() {
        int testCount = 100;
        Random randomValue = new Random();
        Random randomForOperation = new Random();
        Random randomSeed = new Random();
        for (int j = 0; j < testCount; j++) {
            dataFile.clearData();
            System.out.println("--------------------------NEW-TEST-------------------------------------");
            int operationCount = 100;
            int addCount = 0;
            int removeCount = 0;
            int addNotPossible = 0;
            int removeNotPossible = 0;
            int randomValueSeed = randomSeed.nextInt(10000000);
            int randomForOperationSeed = randomSeed.nextInt(10000000);
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
                                    TestClass deletedClass = testClasses.remove(deleteIndex);
                                    dataFile.delete(filePositions.get(deleteIndex), deletedClass);
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
                    t.fromByteArray(dataFile.read(i, t.getSize()));
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
                    t.fromByteArray(dataFile.read(position, t.getSize()));
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

//                TestClass temp = new TestClass();
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
                TestClass testClass = new TestClass();
                dataFile.delete(0, testClass);
                dataFile.delete(45, testClass);
                dataFile.delete(90, testClass);
                this.emptyPositionsToConsole();
                this.dataInFileToConsole();

                System.out.println("delete 5");
                dataFile.delete(135, testClass);
                this.dataInFileToConsole();
                this.emptyPositionsToConsole();

                System.out.println("\nadd 6");
                dataFile.write(testNumbers[6]);
                this.dataInFileToConsole();
                this.emptyPositionsToConsole();
            }
        }
    }

    public void emptyPositionsToConsole() {
        System.out.println("----------------EMPTY-POSITIONS-----------------");
        ArrayList<EmptyPosition> emptyPositionsArrayList = dataFile.getAllEmptyPositions(new EmptyPosition());
        for (EmptyPosition t : emptyPositionsArrayList) {
            System.out.println(t);
        }
    }

    public void dataInFileToConsole() {
        System.out.println("----------------DATA-IN-FILE-----------------");
        ArrayList<TestClass> tempArrayList = dataFile.getAllData(new TestClass());
        for (TestClass t : tempArrayList) {
            System.out.println(t);
        }
    }


}
