import data.DataFile;
import data.EmptyPosition;
import models.*;
import tests.TestClassIndex;
import twoThreeTree.TTTree;
import twoThreeTree.TTTreeNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class Application {

    private final TTTree<OsobaIndex> personTree = new TTTree<>("personTree", new TTTreeNode<>(new OsobaIndex()));
    private final TTTree<PCRTestCode> pcrTreeCode = new TTTree<>("pcrTreeCode", new TTTreeNode<>(new PCRTestCode()));
    private final TTTree<PCRTestDate> pcrTreeDate = new TTTree<>("pcrTreeDate", new TTTreeNode<>(new PCRTestDate()));
    private final TTTree<PCRTestDate> pcrTreePositive = new TTTree<>("pcrTreePositive", new TTTreeNode<>( new PCRTestDate()));
    private final TTTree<UzemnaJednotka> okresTree = new TTTree<>("okresTree", new TTTreeNode<>(new Okres()));
    private final TTTree<UzemnaJednotka> pracoviskoTree = new TTTree<>("pracoviskoTree", new TTTreeNode<>(new Pracovisko()));
    private final ArrayList<Osoba> randomOsoby = new ArrayList<>();

    private final DataFile<PCRTest> testDataFile = new DataFile<>("testDataFile", new PCRTest());
    private final DataFile<Osoba> osobaDataFile = new DataFile<>("0osobaDataFile", new Osoba());

    private final String[] names = {"Alexander", "Jett", "Griffin", "Tyson", "Donavan", "Remington", "German", "Frederick", "Camden", "Peter", "Gunnar", "Joey", "Leroy", "Braylon", "Darius", "Simeon", "Parker", "Colten", "Finnegan", "Esteban", "Nathaniel", "Marshall", "Hamza", "Mohammed", "Alden", "Kadin", "Will", "Orlando", "Lyric", "Shawn", "Ronald", "Brycen", "Kaleb", "Jaylan", "Keenan", "Bryant", "Eden", "Zaire", "Jarrett", "Gunner", "Dante", "Adonis", "Rex", "Giancarlo", "Grayson", "Alex", "Alfredo", "Ariel", "Cade", "Frank", "Craig", "Avery", "Rolando", "Daniel", "Nigel", "Malcolm", "Carmelo", "John", "Octavio", "Adrian", "Kash", "Shamar", "Alessandro", "Oliver", "Deangelo", "Kade", "Todd", "Spencer", "Trevin", "Jessie", "Santiago", "Theodore", "Damien", "Cayden", "Kelton", "Chance", "Jacob", "Jayvon", "Cael", "Zackary", "Javon", "Chandler", "Bentley", "Bronson", "Mekhi", "Emilio", "Hassan", "Micah", "Ronin", "Dennis", "Coby", "Rigoberto", "Morgan", "Oswaldo", "Brogan", "Wyatt", "Seamus", "Darian", "Titus", "Heath", "Marlene", "Miriam", "Kaylah", "Mylie", "Jaelyn", "Angeline", "Georgia", "Mia", "Rachael", "Zoie", "Emely", "Keira", "Cynthia", "Alena", "Mercedes", "Amara", "Carla", "Daisy", "Angie", "Lexie", "Reese", "Christine", "June", "Lila", "Angelina", "Sylvia", "Jacey", "Adalynn", "Alaina", "Dominique", "Rory", "Payten", "Casey", "Kira", "Noemi", "Fatima", "Alexandria", "Renee", "Jazmine", "Olive", "Cailyn", "Myah", "Evie", "Andrea", "Elizabeth", "Mariana", "Erika", "Kiersten", "Trinity", "Carissa", "Abagail", "Nicole", "Marlie", "Jewel", "Jasmine", "Kaia", "Mikayla", "Elise", "Leila", "Alia", "Alisha", "Kyla", "Juliet", "Paityn", "Isla", "Mareli", "Cristina", "Belinda", "Heidi", "Chana", "Shaniya", "Tania", "Isabela", "Avery", "Siena", "Amiya", "Madalyn", "Ryleigh", "Tara", "Jaylene", "Emery", "Jenna", "Jazlyn", "Shannon", "Brielle", "Reagan", "Averi", "Caitlin", "Abbey", "Aisha", "Cecelia", "Jocelynn", "Jordin", "Janiah", "Karissa", "Chelsea", "Kylie", "Eliana", "Sarah", "Kendall"};
    private final String[] lastNames = {"Giles", "Todd", "Wilkerson", "Meyer", "Padilla", "Buchanan", "Dillon", "Joseph", "Mullen", "Moss", "Finley", "Compton", "Fritz", "Freeman", "Solis", "Browning", "Ball", "Adkins", "Nunez", "Travis", "Gilmore", "Santiago", "Mayo", "Carson", "Bauer", "Brandt", "Yu", "Fleming", "Paul", "English", "Douglas", "Pacheco", "Carlson", "Mcintyre", "Sampson", "Oliver", "Tapia", "Galloway", "Bautista", "Mccormick", "Singleton", "Newman", "Gordon", "Davila", "Ramsey", "Brooks", "Colon", "Donaldson", "Farmer", "Mcguire", "Garza", "Villarreal", "Alexander", "Kennedy", "Cross", "Hall", "Charles", "Mcconnell", "Bass", "Daniels", "Bishop", "Odonnell", "Salinas", "Wise", "Ayala", "Koch", "Kirk", "Schwartz", "Lindsey", "Leon", "Werner", "Bowers", "Carr", "Mooney", "Norton", "Beck", "Mcknight", "Phelps", "Valencia", "Richards", "Gallegos", "Potter", "Brewer", "Martin", "Garrison", "Meza", "Herring", "Harding", "Wong", "Mata", "Booth", "Mason", "Frazier", "Hughes", "Montgomery", "Robertson", "Zamora", "Ryan", "Shields", "Maynard"};
    private final String[] okresNames = {"Bratislava I", "Bratislava II", "Bratislava III", "Bratislava IV", "Bratislava V", "Malacky", "Pezinok", "Senec", "Dunajská Streda", "Galanta", "Hlohovec", "Piešťany", "Senica", "Skalica", "Trnava", "Bánovce nad Bebravou", "Ilava", "Myjava", "Nové Mesto nad Váhom", "Partizánske", "Považská Bystrica", "Prievidza", "Púchov", "Trenčín", "Komárno", "Levice", "Nitra", "Nové Zámky", "Šaľa", "Topoľčany", "Zlaté Moravce", "Bytča", "Čadca", "Dolný Kubín", "Kysucké Nové Mesto", "Liptovský Mikuláš", "Martin", "Námestovo", "Ružomberok", "Turčianske Teplice", "Tvrdošín", "Žilina", "Banská Bystrica", "Banská Štiavnica", "Brezno", "Detva", "Krupina", "Lučenec", "Poltár", "Revúca", "Rimavská Sobota", "Veľký Krtíš", "Zvolen", "Žarnovica", "Žiar nad Hronom", "Bardejov", "Humenné", "Kežmarok", "Levoča", "Medzilaborce", "Poprad", "Prešov", "Sabinov", "Snina", "Stará Ľubovňa", "Stropkov", "Svidník", "Vranov nad Topľov", "Gelnica", "Košice I", "Košice II", "Košice III", "Košice IV", "Košice - okolie", "Michalovce", "Rožňava", "Sobrance", "Spišská Nová Ves", "Trebišov"};
    private final Integer[] okresCodes = {101, 102, 103, 104, 105, 106, 107, 108, 201, 202, 203, 204, 205, 206, 207, 301, 302, 303, 304, 305, 306, 307, 308, 309, 401, 402, 403, 404, 405, 406, 407, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 612, 613, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811};
    private final String[] krajNames = {"Bratislavský", "Trnavský", "Trenčiansky", "Nitriansky", "Žilinský", "Banskobystrický", "Prešovský", "Košický"};
    private final Integer[] krajCodes = {1, 2, 3, 4, 5, 6, 7, 8};
    private final String[] pracoviskaNames = {"Nemocnica s poliklinikou Milosrdní bratia, spol. s r. o.", "UNB – Nemocnica Ružinov, Ružinov", "Univerzitná nemocnica Bratislava  - Nemocnica akad. L. Dérera", "Univerzitná nemocnica Bratislava - Nemocnica sv. Cyrila a Metoda", "Nemocnica Malacky", "Nemocnica s Poliklinikou Dunajská Streda", "Nemocnica s Poliklinikou Sv. Lukáša Galanta", "Nemocnica A. Wintera Piešťany", "Fakultná Nemocnica s Poliklinikou Skalica", "Fakultná Nemocnica Trnava", "Nemocnica Bánovce nad Bebravou", "Nemocnica s Poliklinikou Ilava", "Nemocnica s Poliklinikou Myjava", "Nemocnica s Poliklinikou Nové Mesto nad Váhom", "Nemocnica na okraji mesta Partizánske", "Nemocnica s Poliklinikou Považská Bystrica", "Nemocnica s Poliklinikou Prievidza, Bojnice", "Nemocnica Handlová", "Fakultná nemocnica Trenčín", "Nemocnica  Agel Komárno s.r.o.", "Nemocnica Levice", "Hospitale Šahy", "Fakultná Nemocnica Nitra", "Fakultná Nemocnica s Poliklinikou Nové Zámky", "Svet zdravia Nemocnica Topoľčany", "Nemocnica Zlaté Moravce", "Kysucká Nemocnica s Poliklinikou Čadca", "Dolnooravská Nemocnica s Poliklinikou MUDr. L.N.J", "Liptovská Nemocnica s Poliklinikou MUDr. I.Stodolu", "Univerzitná Nemocnica Martin", "Hornooravská Nemocnica s Poliklinikou Trstená", "Fakultná Nemocnica s Poliklinikou Žilina", "Oravská poliklinika Námestovo", "Fakultná nemocnica s poliklinikou  F. D. Roosevelta Banská Bystrica", "Nemocnica s Poliklinikou Brezno", "Nemocnica s Poliklinikou Krupina", "Všeobecná Nemocnica s Poliklinikou Lučenec", "Nemocnica s Poliklinikou Revúca", "Gemerclinic Hnúšťa", "Svet zdravia, a. s., Všeobecná nemocnica Rimavská Sobota", "Všeobecná Nemocnica s Poliklinikou Veľký Krtíš", "Nemocnica AGEL Zvolen a.s.", "Nemocnica s Poliklinikou Mediform Nová Baňa", "Svet zdravia, a.s.,  Všeobecná nemocnica Žiar nad Hronom", "PRO VITAE n. o., Gelnica , Nemocnica s poliklinikou", "Železničné zdravotníctvo Košice s.r.o.", "Nemocnica AGEL Košice-Šaca a.s.", "Univerzitná nemocnica L. Pasteura Košice", "Univerzitná nemocnica L. Pasteura Košice 2", "Nemocnica s Poliklinikou Štefana Kukuru Michalovce, a.s.", "Nemocnica s Poliklinikou sv. Barbory Rožňava", "Regionálna nemocnica Sobrance, n.o.", "Nemocnica Krompachy", "Nemocnica s Polklinikou Kráľovský Chlmec", "Nemocnica s Poliklinikou Trebišov", "Nemocnica s Poliklinikou Spišská Nová Ves, a.s.", "Nemocnica s Poliklinikou Sv. Jakuba Bardejov", "Nemocnica A. Leňa Humenné", "Nemocnica Kežmarok", "Nemocnica Levoča", "Nemocnica Poprad", "Fakultná Nemocnica s Poliklinikou Prešov", "Poliklinika Sabinov, n.o.", "Nemocnica Snina, s.r.o.", "Ľubovnianska nemocnica", "Nemocnica Stropkov", "Nemocnica gen. L. Svobodu Svidník", "Vranovská nemocnica, a.s."};
    private final Integer[] pracoviskaCodes = {101, 102, 103, 104, 105, 201, 202, 203, 204, 205, 301, 302, 303, 304, 305, 306, 307, 308, 309, 401, 402, 403, 404, 405, 406, 407, 501, 502, 503, 504, 505, 506, 507, 601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812};

    private final Kraj[] kraje = new Kraj[krajCodes.length];

    Random random = new Random();

    public Application() {
        //random.setSeed(60); //TODO: remove it
        if (okresCodes.length != okresNames.length) {
            System.out.println("Error: okresCodes.length != okresNames.length");
        }
        if (krajCodes.length != krajNames.length) {
            System.out.println("Error: krajCodes.length != krajNames.length");
        }
        if (pracoviskaNames.length != pracoviskaCodes.length) {
            System.out.println("Error: pracoviskaNames.length != pracoviskaCodes.length");
        }
        for (int i = 0; i < okresCodes.length; i++) {
            Okres okres = new Okres(okresCodes[i], okresCodes[i] / 100, okresNames[i]);
            okresTree.add(okres);
            //System.out.println(okresTree.getRoot());
            //System.out.println(okres);
        }
        //System.out.println(okresTree.getSize());
        //treeToConsole(okresTree);
        for (int i = 0; i < krajCodes.length; i++) {
            Kraj kraj = new Kraj(krajCodes[i], krajNames[i]);
            kraje[i] = kraj;
        }
        for (int i = 0; i < pracoviskaCodes.length; i++) {
            Pracovisko pracovisko = new Pracovisko(pracoviskaCodes[i], pracoviskaCodes[i] / 100, pracoviskaNames[i]);
            pracoviskoTree.add(pracovisko);
            //System.out.println(pracovisko);
        }
        //treeToConsole(pracoviskoTree);
    }

    public void treeToConsole(TTTree<?> tree) {
        System.out.println("----------------TREE-FILE------------------");
        tree.printInfo();
        ArrayList<? extends TTTreeNode<?>> arrayList = tree.getAllData();
        for (TTTreeNode<?> node : arrayList) {
            System.out.println(node);
        }
    }

    public ArrayList<String> getInfo() {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<TTTreeNode<PCRTestCode>> arrayList2 = pcrTreeCode.getAllData();
        arrayList.add("Strom vsetkych testov: \n");
        arrayList.add(pcrTreeCode.getInfo() + "\n");
        int i = 0;
        for (TTTreeNode<PCRTestCode> node : arrayList2) {
            if (node.hasDataL()) {
                i++;
            }
            if (node.hasDataR()) {
                i++;
            }
        }
        arrayList.add("Pocet dat v subore: " + i + "\n");
        for (TTTreeNode<PCRTestCode> node : arrayList2) {
            arrayList.add(node.toString() + "\n");
        }
        ArrayList<EmptyPosition> emptyPositionArrayList = pcrTreeCode.getAllEmptyPositions();
        arrayList.add("Empty Positions: \n");
        for (EmptyPosition emptyPosition : emptyPositionArrayList) {
            arrayList.add(emptyPosition.toString() + "\n");
        }
        arrayList.add("\nData testov: \n");
        ArrayList<PCRTest> arrayList3 = testDataFile.getAllData();
        for (PCRTest test : arrayList3) {
            arrayList.add(test.toString() + "\n");
        }
        emptyPositionArrayList = testDataFile.getAllEmptyPositions();
        arrayList.add("Empty Positions: \n");
        for (EmptyPosition emptyPosition : emptyPositionArrayList) {
            arrayList.add(emptyPosition.toString() + "\n");
        }

        arrayList.add("\nStrom osob: \n");
        arrayList.add(personTree.getInfo() + "\n");
        ArrayList<TTTreeNode<OsobaIndex>> arrayList4 = personTree.getAllData();
        i = 0;
        for (TTTreeNode<OsobaIndex> node : arrayList4) {
            if (node.hasDataL()) {
                i++;
            }
            if (node.hasDataR()) {
                i++;
            }
        }
        arrayList.add("Pocet dat v subore: " + i + "\n");
        for (TTTreeNode<OsobaIndex> node : arrayList4) {
            arrayList.add(node.toString() + "\n");
        }
        emptyPositionArrayList = personTree.getAllEmptyPositions();
        arrayList.add("Empty Positions: \n");
        for (EmptyPosition emptyPosition : emptyPositionArrayList) {
            arrayList.add(emptyPosition.toString() + "\n");
        }
        arrayList.add("\nData osob: \n");
        ArrayList<Osoba> arrayList5 = osobaDataFile.getAllData();
        for (Osoba osoba : arrayList5) {
            arrayList.add(osoba.toString() + "\n");
        }
        emptyPositionArrayList = osobaDataFile.getAllEmptyPositions();
        arrayList.add("Empty Positions: \n");
        for (EmptyPosition emptyPosition : emptyPositionArrayList) {
            arrayList.add(emptyPosition.toString() + "\n");
        }
        return arrayList;
    }

    public void printPersonTree() {
        personTree.preorder(personTree.getRoot());
    }

    public void printPcrTree() {
        pcrTreeCode.preorder(pcrTreeCode.getRoot());
    }

    public void printOkresTree() {
        okresTree.preorder(okresTree.getRoot());
    }

    public Integer[] getOkresCodes() {
        return okresCodes;
    }

    public Integer[] getKrajCodes() {
        return krajCodes;
    }

    public TTTree<PCRTestDate> getPcrTreeDate() {
        return pcrTreeDate;
    }

    public TTTree<PCRTestDate> getPcrTreePositive() {
        return pcrTreePositive;
    }

    public Okres getOkres(int kodOkresu) {
        Okres okres = new Okres(kodOkresu, -1, "");
        return (Okres) okresTree.search(okres);
    }

    public int getPersonCount() {
        return personTree.getSize();
    }

    public String getKrajName(int kodKraja) {
        if (kodKraja >= krajNames.length) {
            return "Neznamy kraj " + kodKraja;
        }
        return krajNames[kodKraja];
    }

    public PCRTest getPCRTest(String kodTestu) {
        PCRTestCode temp = new PCRTestCode(kodTestu);
        PCRTestCode test = pcrTreeCode.search(temp);
        if (test == null) {
            return null;
        }
        PCRTest p = new PCRTest();
        p.fromByteArray(testDataFile.read(test.getDataPosition()));
        return p;
    }

    public PCRTest getPCRTest(long position) {
        PCRTest p = new PCRTest();
        p.fromByteArray(testDataFile.read(position));
        return p;
    }

    public Osoba getOsoba(String rodCislo) {
        OsobaIndex temp = new OsobaIndex(rodCislo);
        OsobaIndex osobaIndex = personTree.search(temp);
        if (osobaIndex == null)
            return null;
        Osoba osoba = new Osoba();
        osoba.setMyPosition(osobaIndex.getDataPosition());
        osoba.fromByteArray(osobaDataFile.read(osobaIndex.getDataPosition()));
        return osoba;
    }

    public Osoba getOsoba(long position) {
        Osoba osoba = new Osoba();
        osoba.setMyPosition(position);
        osoba.fromByteArray(osobaDataFile.read(position));
        return osoba;
    }

    public Pracovisko getPracovisko(int kodPracoviska) {
        Pracovisko p = new Pracovisko(kodPracoviska);
        return (Pracovisko) pracoviskoTree.search(p);
    }

    public Kraj getKraj(int kodKraja) {
        if (kodKraja > kraje.length) {
            return null;
        }
        return kraje[kodKraja - 1];
    }

    public ArrayList<OsobaIndex> getVsetkyOsoba() {
        System.out.println(personTree.getSize());
        return personTree.getInOrderData();
    }

//    public ArrayList<Osoba> getIntervalOsoba(String startRodCislo, String endRodCislo) {
//        System.out.println(personTree.getSize());
//        return personTree.getIntervalData(startRodCislo, endRodCislo);
//    }

    public boolean addPCRTest(String kodTestu, String rodCislo, int kodPracoviska, int kodOkresu, int kodKraju, boolean vysledok, String poznamka, Osoba osoba, Date datum) {
        //Date date = new Date(rok - 1900, mesiac - 1, den);
        //Osoba osoba = getOsoba(rodCislo);
        if (kodTestu == null) {
            kodTestu = UUID.randomUUID().toString();
        }
        PCRTest pcrTest = new PCRTest(kodTestu, rodCislo, kodPracoviska, kodOkresu, kodKraju, vysledok, poznamka, datum, osoba.getMyPosition());
        long testPosition = testDataFile.write(pcrTest);
        PCRTestCode pcrTestCode = new PCRTestCode(pcrTest.getKodTestu(), testPosition);
        PCRTestDate pcrTestDate = new PCRTestDate(pcrTest.getDatum(), pcrTest.getKodTestu(), testPosition);
        boolean[] isTestAdd = new boolean[9];
        for (int i = 0; i < isTestAdd.length; i++) {
            isTestAdd[i] = true;
        }
        isTestAdd[0] = pcrTreeCode.add(pcrTestCode);
        if (!isTestAdd[0]) {
            return false;
        }
        isTestAdd[1] = getPracovisko(kodPracoviska).getTesty().add(pcrTestDate);
        isTestAdd[2] = getKraj(kodKraju).getTesty().add(pcrTestDate);
        isTestAdd[3] = getOkres(kodOkresu).getTesty().add(pcrTestDate);
        if (vysledok) {
            isTestAdd[4] = getOkres(kodOkresu).getPozitivneTesty().add(pcrTestDate);
            isTestAdd[5] = getKraj(kodKraju).getPozitivneTesty().add(pcrTestDate);
            isTestAdd[6] = getPracovisko(kodPracoviska).getPozitivneTesty().add(pcrTestDate);
            isTestAdd[7] = pcrTreePositive.add(pcrTestDate);
        }
        isTestAdd[8] = pcrTreeDate.add(pcrTestDate);
        for (boolean pom : isTestAdd) {
            if (!pom) {
                return false;
            }
        }
        osoba.getTesty().add(pcrTestDate);
        return true;
    }

    public void removeAllData() {
        ArrayList<OsobaIndex> osoby = this.personTree.getInOrderData();
        for (OsobaIndex osoba : osoby) {
            this.removeOsoba(osoba.getRodCislo());
        }
    }

    public Osoba removeOsoba(String rodCislo) {
        OsobaIndex osobaIndex = new OsobaIndex(rodCislo);
        osobaIndex = personTree.remove(osobaIndex);
        if (osobaIndex == null)
            return null;
        Osoba osoba = getOsoba(osobaIndex.getDataPosition());
        osobaDataFile.delete(osobaIndex.getDataPosition());
        if (osoba != null) {
            ArrayList<PCRTestDate> testy = osoba.getTesty().getInOrderData();
            for (int i = 0; i < testy.size(); i++) {
                PCRTestDate test = testy.get(i);
                if (this.removePCRTest(test.getTestCode(), false) == null) {
                    return null;
                }
            }
            return osoba;
        }
        return null;
    }

    public PCRTest removePCRTest(String kodTestu, boolean onlyTest) {
        PCRTestCode p = new PCRTestCode(kodTestu);
        PCRTestCode t = pcrTreeCode.remove(p);
        if (t == null) {
            return null;
        }
        PCRTest test = getPCRTest(t.getDataPosition());
        testDataFile.delete(t.getDataPosition());
        PCRTestDate testDate = new PCRTestDate(test.getDatum(), test.getKodTestu());
        if (test.isVysledok()) {
            getPracovisko(test.getKodPracoviska()).getPozitivneTesty().remove(testDate);
            getOkres(test.getKodOkresu()).getPozitivneTesty().remove(testDate);
            getKraj(test.getKodKraja()).getPozitivneTesty().remove(testDate);
            pcrTreePositive.remove(testDate);
        }
        getPracovisko(test.getKodPracoviska()).getTesty().remove(testDate);
        getOkres(test.getKodOkresu()).getTesty().remove(testDate);
        getKraj(test.getKodKraja()).getTesty().remove(testDate);
        pcrTreeDate.remove(testDate);
        if (onlyTest) {
            Osoba osoba = getOsoba(test.getOsobaPosition());
            osoba.getTesty().remove(testDate);
        }
        return test;
    }

    public Osoba addOsoba(String meno, String priezvisko, String rodCislo) {
        Date actualDate = new Date(System.currentTimeMillis());
        int year = Integer.parseInt(rodCislo.substring(0, 2));
        int month = Integer.parseInt(rodCislo.substring(2, 4));
        int day = Integer.parseInt(rodCislo.substring(4, 6));
        Date date = new Date(year < actualDate.getYear() - 100 ? year + 100 : year, month - 1, day);
        Osoba osoba = new Osoba(meno, priezvisko, date, rodCislo);
        long personPosition = osobaDataFile.write(osoba);
        OsobaIndex osobaIndex = new OsobaIndex(osoba.getRodCislo(), personPosition);
        osoba.setMyPosition(personPosition);
        if (personTree.add(osobaIndex)) {
            return osoba;
        }
        return null;
    }

    public void addRandomPCRTest(int count) {
        Date actualDate = new Date(System.currentTimeMillis());
        for (int i = 0; i < count; i++) {
            String rodCislo = this.getRandomRodCislo();
            int kodPracoviska = pracoviskaCodes[random.nextInt(pracoviskaCodes.length)];
            int kodOkresu = okresCodes[random.nextInt(okresCodes.length)];
            int kodKraju = kodOkresu / 100;
            boolean vysledok = random.nextInt(2) == 1;
            Osoba osoba = getOsoba(rodCislo);
            if (osoba == null) {
                if (randomOsoby.size() > 0) {
                    osoba = randomOsoby.get(random.nextInt(randomOsoby.size()));
                } else {
                    System.out.println("Musi sa vygenerovat aspon 1 nahodna osoba.");
                    return;
                }
            }
            String kodTestu = "" + random.nextInt(10000);
            kodTestu = null;

            int year = random.nextInt(actualDate.getYear() - 121 + 1) + 121;
            int month = random.nextInt(12) + 1;
            int day = random.nextInt(31) + 1;
            if (month == 2 && day > 28) {
                day = 28;
            } else if (day == 31) {
                switch (month) {
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        day = 30;
                }
            }
            int hour = random.nextInt(24);
            int minutes = random.nextInt(60);
            Date date = new Date(year, month - 1, day, hour, minutes);
            //date = null;
            if (osoba != null)
                this.addPCRTest(kodTestu, osoba.getRodCislo(), kodPracoviska, kodOkresu, kodKraju, vysledok, null, osoba, date);
        }
        this.randomOsoby.clear();
    }

    public void addRandomPersons(int count) {
        for (int i = 0; i < count; i++) {
            String randomName = names[random.nextInt(this.names.length)];
            String randomLastName = lastNames[random.nextInt(this.lastNames.length)];
            Osoba osoba = this.addOsoba(randomName, randomLastName, getRandomRodCislo());
            if (osoba != null) {
                randomOsoby.add(osoba);
            }
        }
    }

    private String getRandomRodCislo() {
        Date actualDate = new Date(System.currentTimeMillis());
        int randomCode;
        int randomYear = random.nextInt(100);
        String randomCodeStr;
        if (randomYear < 54 && randomYear > actualDate.getYear() - 100) {
            randomCode = random.nextInt(1000);
            if (randomCode < 10) {
                randomCodeStr = "00" + randomCode;
            } else if (randomCode < 100) {
                randomCodeStr = "0" + randomCode;
            } else {
                randomCodeStr = "" + randomCode;
            }
        } else {
            randomCode = random.nextInt(10000);
            if (randomCode < 10) {
                randomCodeStr = "000" + randomCode;
            } else if (randomCode < 100) {
                randomCodeStr = "00" + randomCode;
            } else if (randomCode < 1000) {
                randomCodeStr = "0" + randomCode;
            } else {
                randomCodeStr = "" + randomCode;
            }
        }
        int randomMonth = random.nextInt(12) + 1;
        int randomDay = random.nextInt(30) + 1;
        if (randomMonth == 2 && randomDay > 28) {
            randomDay = 28;
        }
        String randomRodCislo = "" + (randomYear < 10 ? ("0" + randomYear) : randomYear) + (randomMonth < 10 ? ("0" + randomMonth) : randomMonth) + (randomDay < 10 ? ("0" + randomDay) : randomDay) + randomCodeStr;
        //System.out.println(randomRodCislo);
        return randomRodCislo;
    }


}
