import tests.TestGenerator;

public class Main {

    public static void main(String[] args) {

        TestGenerator testGenerator = new TestGenerator();
        testGenerator.runTests();

        new GraphicalApp();

    }

}
