import models.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GraphicalApp {

    private final JFrame jFrame = new JFrame("PCR Testy");
    private final JPanel jPanel = new JPanel();
    private final JTextArea jTextArea = new JTextArea();
    private final JScrollPane jScrollPane = new JScrollPane(jTextArea);

    private final Application app = new Application();
    private final int jFrameHeight = 780;
    private final int componentHeight = 40;
    private final int componentWidth = 150;
    private final int componentDistance = 12;
    private final int posun = 10;
    private final int jFrameWidth = componentWidth * 9 + 2 * posun + 9 * 2;

    private boolean novyTest = false;
    private boolean hladajPozitivneOsoby = false;
    private boolean hladajTestyPrePracovisko = false;
    private boolean zoradOkresyKraje = false;
    private boolean novaOsoba = false;

    private final String[] typy = {"Všetky uzemne jednotky", "Okres", "Kraj", "Pracovisko"};
    private final String[] pozitivne = {"Všetky vysledky testov", "Pozitivne"};
    private final String[] okresyKraje = {"Okresy", "Kraje"};
    private TypUzJednotky typUzJednotkyZorad;
    private TypUzJednotky typUzJednotkyOsoby;
    private TypUzJednotky typUzJednotkyTesty;
    private TypVysledokTestu typVysledokTestu;


    public GraphicalApp() {
        jFrame.setVisible(true);
        jFrame.setSize(jFrameWidth, jFrameHeight);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);

        jPanel.setLayout(null);
        jPanel.setBounds(0, 0, jFrameWidth, jFrameHeight);

        int width = 750;
        int y = 280;
        jTextArea.setBounds((jFrameWidth / 2) - (width / 2), y, width, jFrameHeight - y - 52);
        jTextArea.setEditable(false);


        jScrollPane.setBounds((jFrameWidth / 2) - (width / 2), y, width, jFrameHeight - y - 52);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jFrame.add(jPanel);

        //app.addRandomPersons(100);
        //app.addRandomPCRTest(100);

        addVytvorPCRTestComponents();
        addVyhladajPCRTest();
        addVypisTestovPacienta();
        addPridajOsoba();
        addVyhladajTesty();
        addVyhladajPozitivneOsoby();
        addZoradOkresyKraje();
        addVymazOsobu();
        addVymazTest();
        addSaveButton();
        addLoadButton();
        addRemoveAllData();
        addGeneruj();

        //test();

    }

    //only for testing
    private void test() {
//        ArrayList<Osoba> interval = app.getIntervalOsoba("550000123456", "6000001234");
//        for (Osoba o : interval) {
//            System.out.println(o.getKey());
//        }
//        app.printPersonTree();
//        System.out.println("size " + app.getPersonCount());
//        System.out.println("size " + interval.size());

//        ArrayList<PCRTestDate> arrayList = app.getDateIntervalTest(app.getOkres(205).getPozitivneTesty(), new Date(98, 10, 5), new Date(122, 10, 5));
//
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println("positive: " + arrayList.get(i).getData().getRodCisloPacienta());
//            System.out.println("date: " + arrayList.get(i).getData().getDatum());
//        }
//        System.out.println(app.getOkres(105).getTesty().getSize());
//        System.out.println(app.getOkres(105).getPozitivneTesty().getSize());

        //ArrayList<Osoba> arrayList2 = app.getIntervalOsoba2("0", "999999999999999");
        //ArrayList<Osoba> arrayList2 = app.getIntervalOsoba("0", "999999999999999");
        //for (int i = 0; i < arrayList2.size(); i++) {
        // System.out.println("Person: " + arrayList2.get(i).getRodCislo());
        //}
        //System.out.println(arrayList2.size());


//        ArrayList<Osoba> arrayList3 = app.getIntervalOsoba3("7805051234", "8005051234");
//        for (int i = 0; i < arrayList3.size(); i++) {
//            System.out.println("Person: " + arrayList3.get(i).getRodCislo());
//        }
//        System.out.println(arrayList3.size());
    }

    private enum TypUzJednotky {
        VSETKY,
        PRACOVISKO,
        OKRES,
        KRAJ,
    }

    private enum TypVysledokTestu {
        VSETKY,
        POZITIVNE,
    }

    private TypVysledokTestu getTypVysledkuTestu(String typ) {
        if (typ.equals(pozitivne[0]))
            return TypVysledokTestu.VSETKY;
        if (typ.equals(pozitivne[1]))
            return TypVysledokTestu.POZITIVNE;
        System.out.println("Error: getTypVysledkuTestu");
        return null;
    }

    private TypUzJednotky getTypUzJednotky(String typ) {
        if (typ.equals(typy[0]))
            return TypUzJednotky.VSETKY;
        if (typ.equals(typy[1]) || typ.equals(okresyKraje[0]))
            return TypUzJednotky.OKRES;
        if (typ.equals(typy[2]) || typ.equals(okresyKraje[1]))
            return TypUzJednotky.KRAJ;
        if (typ.equals(typy[3]))
            return TypUzJednotky.PRACOVISKO;
        System.out.println("Error: getTypUzJednotky");
        return null;
    }

    private void addGeneruj() {
        JButton jButton = new JButton("Generuj");
        jPanel.add(jButton);
        jButton.setBounds(posun + 120, jFrameHeight - posun - 30 - 46, 120, 30);

        JTextField pocetLudiText = new JTextField();
        JTextField pocetTestovText = new JTextField();

        pocetLudiText.setToolTipText("Pocet ludi");
        pocetTestovText.setToolTipText("Pocet testov");

        pocetLudiText.setText("100");
        pocetTestovText.setText("100");

        jPanel.add(pocetLudiText);
        jPanel.add(pocetTestovText);

        pocetLudiText.setBounds(posun + 120, jFrameHeight - posun - 30 * 3 - 46, 120, 30);
        pocetTestovText.setBounds(posun + 120, jFrameHeight - posun - 30 * 2 - 46, 120, 30);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pocetLudi = Integer.parseInt(pocetLudiText.getText());
                int pocetTestov = Integer.parseInt(pocetTestovText.getText());
                app.addRandomPersons(pocetLudi);
                app.addRandomPCRTest(pocetTestov);
                JOptionPane.showMessageDialog(null, "Data boli vygenerovane");
            }
        });

    }

    private void addRemoveAllData() {
        JButton jButton = new JButton("Vymaz vsetko");
        jPanel.add(jButton);
        jButton.setBounds(posun, jFrameHeight - posun - 30 - 46, 120, 30);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Naozaj chces vymazat vsetky data?") == 0) {
                    app.removeAllData();
                    JOptionPane.showMessageDialog(null, "Data boli vymazane");
                }
            }
        });
    }

    private void addSaveButton() {
//        JButton jButton = new JButton("Save");
//        jPanel.add(jButton);
//        jButton.setBounds(jFrameWidth - 120, jFrameHeight - 125, 80, 30);
//
//        jButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String nazovSuboru = JOptionPane.showInputDialog("Zadaj nazov suboru");
//                if (nazovSuboru != null) {
//                    if (app.writeToFile(nazovSuboru)) {
//                        JOptionPane.showMessageDialog(null, "Data boli ulozene do suboru: " + nazovSuboru + ".csv");
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Data sa nepodarilo ulozit do suboru: " + nazovSuboru + ".csv");
//                    }
//                }
//            }
//        });

    }

    private void addLoadButton() {
        JButton jButton = new JButton("Show");
        jPanel.add(jButton);
        jButton.setBounds(jFrameWidth - 120, jFrameHeight - 125 + 31, 80, 30);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> arrayList = app.getInfo();
                jTextArea.setText("");
                for (String s : arrayList) {
                    jTextArea.append(s);
                }
                jTextArea.setCaretPosition(0);

                jPanel.add(jScrollPane);
            }
        });
    }

    private void addVymazTest() {
        JButton jButton = new JButton("Vymaz test");
        jPanel.add(jButton);

        int posunX = 8;
        jButton.setBounds(posun + componentWidth * posunX, posun, componentWidth, componentHeight);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodTestu = JOptionPane.showInputDialog("Zadaj kod testu");
                if (kodTestu != null) {
                    PCRTest test = app.removePCRTest(kodTestu, true);
                    if (test != null) {
                        Okres okres = app.getOkres(test.getKodOkresu());
                        Pracovisko pracovisko = app.getPracovisko(test.getKodPracoviska());
                        String text = String.format("Kod testu: %s\n", test.getKodTestu());
                        Date date = test.getDatum();
                        String hours = date.getHours() < 10 ? "0" + date.getHours() : "" + date.getHours();
                        String minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
                        text += String.format("Datum testu: %s\n", date.getDate() + "." + (date.getMonth() + 1) + "." + (1900 + date.getYear()) + " " + hours + ":" + minutes);
                        text += String.format("Kod pracoviska: %s\n", test.getKodPracoviska());
                        text += String.format("Kod okresu: %s\n", test.getKodOkresu());
                        text += String.format("Kod kraja: %s\n", test.getKodKraja());
                        text += String.format("Nazov pracoviska: %s\n", pracovisko.getNazov());
                        text += String.format("Nazov okresu: %s\n", okres.getNazov());
                        text += String.format("Nazov kraja: %s\n", app.getKrajName(okres.getKodKraja()));
                        if (test.isVysledok()) {
                            text += "Vysledok: Pozitivny\n";
                        } else {
                            text += "Vysledok: Negativny\n";
                        }
                        if (test.getPoznamka() != null && !test.getPoznamka().equals("")) {
                            text += String.format("Poznamka: %s\n", test.getPoznamka());
                        }
                        text += "\nOsoba:\n";
                        text += String.format("Rodne cislo: %s\n", test.getRodCisloPacienta());
                        Osoba osoba = app.getOsoba(test.getRodCisloPacienta());
                        if (osoba != null) {
                            text += String.format("Meno: %s\n", osoba.getMeno());
                            text += String.format("Priezvisko: %s\n", osoba.getPriezvisko());
                            text += String.format("Datum Narodenia: %s\n", osoba.getDatumNarodenia().getDate() + "." + (osoba.getDatumNarodenia().getMonth() + 1) + "." + (1900 + osoba.getDatumNarodenia().getYear()));
                        } else {
                            text += "Dalsie udaje neexistuju\n";
                        }
                        //text = "Test vymazany\n" + text;
                        //jTextArea.setText(text);
                        //jTextArea.setCaretPosition(0);
                        //jPanel.add(jScrollPane);
                        JOptionPane.showMessageDialog(null, text, "Test vymazany", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String text = String.format("Test s kodom \"%s\" sa nepodarilo vymazat.\nSkontrolujte ci test existuje.", kodTestu);
                        JOptionPane.showMessageDialog(null, text);
                    }
                }
            }
        });
    }

    private void addVymazOsobu() {
        JButton jButton = new JButton("Vymaz osobu");
        jPanel.add(jButton);

        int posunX = 7;
        jButton.setBounds(posun + componentWidth * posunX, posun, componentWidth, componentHeight);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rodCislo = JOptionPane.showInputDialog("Zadaj rodne cislo");
                if (rodCislo != null) {
                    Osoba osoba = app.removeOsoba(rodCislo);
                    if (osoba != null) {
                        String text = String.format("Meno: %s\nPriezvisko: %s\nRod. cislo: %s", osoba.getMeno(), osoba.getPriezvisko(), osoba.getRodCislo());
                        JOptionPane.showMessageDialog(null, text, "Osoba vymazana", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String text = String.format("Osobu s rod. cislom \"%s\" sa nepodarilo vymazat.\nSkontrolujte ci osoba existuje.", rodCislo);
                        JOptionPane.showMessageDialog(null, text);
                    }
                }
            }
        });
    }

    private void addZoradOkresyKraje() {
        JButton jButton = new JButton("Zorad okresy/kraje");
        jPanel.add(jButton);

        int posunX = 6;

        JComboBox<String> comboBoxUzJednotky = new JComboBox<>(okresyKraje);
        comboBoxUzJednotky.setSelectedIndex(0);
        comboBoxUzJednotky.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance, componentWidth, 26);

        jButton.setBounds(posun + componentWidth * posunX, posun, componentWidth, componentHeight);

        JButton zobraz = new JButton("Zobraz");
        zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 28, componentWidth, componentHeight);

        JTextField date = new JTextField();
        JTextField x = new JTextField();

        date.setToolTipText("Zaciatocny datum");
        x.setToolTipText("Pocet dni (X)");

        Date actualDate = new Date(System.currentTimeMillis());
        date.setText(actualDate.getDate() + 1 + ".12.2021");
        x.setText("5");

        date.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance + 28, componentWidth, componentHeight);
        x.setBounds(posun + componentWidth * posunX, componentHeight * 2 + componentDistance + 28, componentWidth, componentHeight);

        typUzJednotkyZorad = getTypUzJednotky(comboBoxUzJednotky.getSelectedItem().toString());

        comboBoxUzJednotky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typUzJednotkyZorad = getTypUzJednotky(comboBoxUzJednotky.getSelectedItem().toString());
            }
        });


        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoradOkresyKraje = !zoradOkresyKraje;
                if (zoradOkresyKraje) {
                    jPanel.add(date);
                    jPanel.add(x);
                    jPanel.add(zobraz);
                    jPanel.add(comboBoxUzJednotky);
                } else {
                    jPanel.remove(date);
                    jPanel.remove(x);
                    jPanel.remove(zobraz);
                    jPanel.remove(comboBoxUzJednotky);
                }
                jPanel.repaint();

            }
        });

        zobraz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (date.getText().length() == 10) {
                    int day = Integer.parseInt(date.getText().substring(0, 2));
                    int month = Integer.parseInt(date.getText().substring(3, 5));
                    int year = Integer.parseInt(date.getText().substring(6));
                    Date endDate = new Date(year - 1900, month - 1, day);

                    int pocetDni = Integer.parseInt(x.getText());

                    if (pocetDni > 0 && pocetDni < 29) {
                        day -= pocetDni;
                        if (day < 1) {
                            month--;
                            switch (month) {
                                case 1:
                                case 3:
                                case 5:
                                case 7:
                                case 8:
                                case 10:
                                case 12:
                                    day = 31 + day;
                                    break;
                                case 4:
                                case 6:
                                case 9:
                                case 11:
                                    day = 30 + day;
                                case 2:
                                    day = 28 + day;

                            }
                            if (month < 1) {
                                year--;
                                month = 12;
                                day = 31 + day;
                            }
                        }
                        Date startDate = new Date(year - 1900, month - 1, day);

                        //TTTree<UzemnaJednotka> uzemneJednotky = new TTTree<>("okresPocetPozitivnych", new TTTreeNode<>(new OkresPocetPozitivnych()));
                        ArrayList<UzemnaJednotka> sortedUzemneJednotky = new ArrayList<>();
                        String text = "";
                        PCRTestDate t1 = new PCRTestDate(startDate);
                        PCRTestDate t2 = new PCRTestDate(endDate);
                        if (typUzJednotkyZorad == TypUzJednotky.OKRES) {
                            for (Integer okresCode : app.getOkresCodes()) {
                                Okres okres = app.getOkres(okresCode);
                                OkresPocetPozitivnych okresPocetPozitivnych = new OkresPocetPozitivnych(okres.getKod(), okres.getNazov());
                                okresPocetPozitivnych.setPocetPozitivnych(okres.getPozitivneTesty().getIntervalData(t1, t2).size());
                                sortedUzemneJednotky.add(okresPocetPozitivnych);
                            }
                            text = "Okresy\n";
                        } else if (typUzJednotkyZorad == TypUzJednotky.KRAJ) {
                            for (Integer krajeCodes : app.getKrajCodes()) {
                                Kraj kraj = app.getKraj(krajeCodes);
                                KrajPocetPozitivnych krajPocetPozitivnych = new KrajPocetPozitivnych(kraj.getKod(), kraj.getNazov());
                                krajPocetPozitivnych.setPocetPozitivnych(kraj.getPozitivneTesty().getIntervalData(t1, t2).size());
                                sortedUzemneJednotky.add(krajPocetPozitivnych);
                            }
                            text = "Kraje\n";
                        }

                        Collections.sort(sortedUzemneJednotky, (a, b) -> a.compareTo(b));

                        int index = 1;
                        if (sortedUzemneJednotky.size() == 0) {
                            text = "Ziadne uzemne jednotky";
                        }
                        for (int i = sortedUzemneJednotky.size() - 1; i >= 0; i--) {
                            UzemnaJednotka uzemnaJednotka = sortedUzemneJednotky.get(i);
                            if (typUzJednotkyZorad == TypUzJednotky.OKRES) {
                                OkresPocetPozitivnych okresPocetPozitivnych = (OkresPocetPozitivnych) uzemnaJednotka;
                                text += "----------------------------------";
                                text += String.format("\nOkres %d\n", index++);
                                text += String.format("Nazov: %s\n", okresPocetPozitivnych.getNazov());
                                text += String.format("Pocet pozitivnych: %d\n", okresPocetPozitivnych.getPocetPozitivnych());
                            } else if (typUzJednotkyZorad == TypUzJednotky.KRAJ) {
                                KrajPocetPozitivnych krajPocetPozitivnych = (KrajPocetPozitivnych) uzemnaJednotka;
                                text += "----------------------------------";
                                text += String.format("\nKraj %d\n", index++);
                                text += String.format("Nazov: %s\n", krajPocetPozitivnych.getNazov());
                                text += String.format("Pocet pozitivnych: %d\n", krajPocetPozitivnych.getPocetPozitivnych());
                            }
                        }
                        jTextArea.setText(text);
                        jTextArea.setCaretPosition(0);

                        jPanel.add(jScrollPane);
                        //JOptionPane.showMessageDialog(null, text, "Uzemne jednotky", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Pocet dni musi byt v rozmedzi 1-28");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Zadaj datum vo formate DD.MM.YYYY");
                }


            }
        });
    }

    private void addVyhladajPozitivneOsoby() {
        JButton jButton = new JButton("Vyhladaj (+) osoby");
        jPanel.add(jButton);

        int posunX = 5;

        JComboBox<String> comboBoxUzJednotky = new JComboBox<>(typy);
        comboBoxUzJednotky.setSelectedIndex(0);
        comboBoxUzJednotky.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance, componentWidth, 26);

        jButton.setBounds(posun + componentWidth * posunX, posun, componentWidth, componentHeight);

        JButton zobraz = new JButton("Zobraz");
        zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 28, componentWidth, componentHeight);

        JTextField date = new JTextField();
        JTextField x = new JTextField();
        JTextField kodUzJednotky = new JTextField();

        date.setToolTipText("Zaciatocny datum");
        x.setToolTipText("Pocet dni (X)");

        Date actualDate = new Date(System.currentTimeMillis());
        date.setText(actualDate.getDate() + 1 + ".12.2021");
        x.setText("5");
        kodUzJednotky.setText("205");

        date.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance + 28, componentWidth, componentHeight);
        x.setBounds(posun + componentWidth * posunX, componentHeight * 2 + componentDistance + 28, componentWidth, componentHeight);
        kodUzJednotky.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 28, componentWidth, componentHeight);

        typUzJednotkyOsoby = getTypUzJednotky(comboBoxUzJednotky.getSelectedItem().toString());

        comboBoxUzJednotky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typUzJednotkyOsoby = getTypUzJednotky(comboBoxUzJednotky.getSelectedItem().toString());
                if (typUzJednotkyOsoby != TypUzJednotky.VSETKY) {
                    jPanel.add(kodUzJednotky);
                    zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 4 + componentDistance + 26, componentWidth, componentHeight);
                    kodUzJednotky.setToolTipText(comboBoxUzJednotky.getSelectedItem().toString());
                } else {
                    jPanel.remove(kodUzJednotky);
                    zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 26, componentWidth, componentHeight);
                }
                jPanel.repaint();
            }
        });


        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hladajPozitivneOsoby = !hladajPozitivneOsoby;
                if (hladajPozitivneOsoby) {
                    jPanel.add(date);
                    jPanel.add(x);
                    jPanel.add(zobraz);
                    jPanel.add(comboBoxUzJednotky);
                    if (typUzJednotkyOsoby != TypUzJednotky.VSETKY) {
                        jPanel.add(kodUzJednotky);
                    }
                } else {
                    jPanel.remove(date);
                    jPanel.remove(x);
                    jPanel.remove(kodUzJednotky);
                    jPanel.remove(zobraz);
                    jPanel.remove(comboBoxUzJednotky);
                }
                jPanel.repaint();

            }
        });

        zobraz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (date.getText().length() == 10) {
                    int day = Integer.parseInt(date.getText().substring(0, 2));
                    int month = Integer.parseInt(date.getText().substring(3, 5));
                    int year = Integer.parseInt(date.getText().substring(6));
                    Date endDate = new Date(year - 1900, month - 1, day);

                    int pocetDni = Integer.parseInt(x.getText());

                    if (pocetDni > 0 && pocetDni < 29) {
                        day -= pocetDni;
                        if (day < 1) {
                            month--;
                            switch (month) {
                                case 1:
                                case 3:
                                case 5:
                                case 7:
                                case 8:
                                case 10:
                                case 12:
                                    day = 31 + day;
                                    break;
                                case 4:
                                case 6:
                                case 9:
                                case 11:
                                    day = 30 + day;
                                case 2:
                                    day = 28 + day;

                            }
                            if (month < 1) {
                                year--;
                                month = 12;
                                day = 31 + day;
                            }
                        }
                        Date startDate = new Date(year - 1900, month - 1, day);

                        ArrayList<PCRTestDate> testy = new ArrayList<>();

                        PCRTestDate t1 = new PCRTestDate(startDate);
                        PCRTestDate t2 = new PCRTestDate(endDate);
                        if (typUzJednotkyOsoby == TypUzJednotky.VSETKY) {
                            testy = app.getPcrTreePositive().getIntervalData(t1, t2);
                        } else if (typUzJednotkyOsoby == TypUzJednotky.PRACOVISKO) {
                            testy = app.getPracovisko(Integer.parseInt(kodUzJednotky.getText())).getPozitivneTesty().getIntervalData(t1, t2);
                        } else if (typUzJednotkyOsoby == TypUzJednotky.OKRES) {
                            testy = app.getOkres(Integer.parseInt(kodUzJednotky.getText())).getPozitivneTesty().getIntervalData(t1, t2);
                        } else if (typUzJednotkyOsoby == TypUzJednotky.KRAJ) {
                            testy = app.getKraj(Integer.parseInt(kodUzJednotky.getText())).getPozitivneTesty().getIntervalData(t1, t2);
                        }

                        String[] textArray = new String[testy.size()];
                        String text;
                        int index = 1;
                        for (PCRTestDate test : testy) {
                            PCRTest t = app.getPCRTest(test.getDataPosition());
                            text = "";
                            Okres okres = app.getOkres(t.getKodOkresu());
                            Pracovisko pracovisko = app.getPracovisko(t.getKodPracoviska());
                            text += "----------------------------------";
                            text += String.format("\nOsoba %d\n", index);
                            text += String.format("Rodne cislo: %s\n", t.getRodCisloPacienta());
                            Osoba osoba = app.getOsoba(t.getRodCisloPacienta());
                            if (osoba != null) {
                                text += String.format("Meno: %s\n", osoba.getMeno());
                                text += String.format("Priezvisko: %s\n", osoba.getPriezvisko());
                                text += String.format("Datum Narodenia: %s\n", osoba.getDatumNarodenia().getDate() + "." + (osoba.getDatumNarodenia().getMonth() + 1) + "." + (1900 + osoba.getDatumNarodenia().getYear()));
                            } else {
                                text += "Dalsie udaje neexistuju\n";
                            }
                            text += String.format("\nKod testu: %s\n", t.getKodTestu());
                            Date date = t.getDatum();
                            String hours = date.getHours() < 10 ? "0" + date.getHours() : "" + date.getHours();
                            String minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
                            text += String.format("Datum testu: %s\n", date.getDate() + "." + (date.getMonth() + 1) + "." + (1900 + date.getYear()) + " " + hours + ":" + minutes);
                            text += String.format("Kod pracoviska: %s\n", t.getKodPracoviska());
                            text += String.format("Kod okresu: %s\n", t.getKodOkresu());
                            text += String.format("Kod kraja: %s\n", t.getKodKraja());
                            text += String.format("Nazov pracoviska: %s\n", pracovisko.getNazov());
                            text += String.format("Nazov okresu: %s\n", okres.getNazov());
                            text += String.format("Nazov kraja: %s\n", app.getKrajName(okres.getKodKraja()));
                            if (t.isVysledok()) {
                                text += "Vysledok: Pozitivny\n";
                            } else {
                                text += "Vysledok: Negativny\n";
                            }
                            if (t.getPoznamka() != null && !t.getPoznamka().equals("")) {
                                text += String.format("Poznamka: %s\n", t.getPoznamka());
                            }
                            textArray[index - 1] = text;
                            index++;
                        }

                        jTextArea.setText("Osoby\nPocet: " + testy.size() + "\n");
                        if (testy.size() == 0) {
                            jTextArea.setText("Ziadne osoby");
                        }
                        for (String str : textArray) {
                            jTextArea.append(str);
                        }
                        jTextArea.setCaretPosition(0);
                        jPanel.add(jScrollPane);

                        //JOptionPane.showMessageDialog(null, text, "Pozitivne osoby", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Pocet dni musi byt v rozmedzi 1-28");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Zadaj datum vo formate DD.MM.YYYY");
                }


            }
        });
    }

    private void addVyhladajTesty() {
        JButton jButton = new JButton("Vyhladaj PCR testy");
        jPanel.add(jButton);

        int posunX = 4;

        JComboBox<String> comboBoxUzJednotky = new JComboBox<>(typy);
        comboBoxUzJednotky.setSelectedIndex(0);
        comboBoxUzJednotky.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance, componentWidth, 26);

        JComboBox<String> comboBoxPozitivne = new JComboBox<>(pozitivne);
        comboBoxPozitivne.setSelectedIndex(0);
        comboBoxPozitivne.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance + 26 + 1, componentWidth, 26);

        jButton.setBounds(posun + componentWidth * posunX, posun, componentWidth, componentHeight);

        JButton zobraz = new JButton("Zobraz");
        zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 26 * 2 + 2, componentWidth, componentHeight);

        JTextField startDate = new JTextField();
        JTextField endDate = new JTextField();
        JTextField kodUzJednotky = new JTextField();

        startDate.setToolTipText("Zaciatocny datum");
        endDate.setToolTipText("Koncovy datum");

        startDate.setText("01.01.2020");
        endDate.setText("01.01.2022");
        kodUzJednotky.setText("205");

        startDate.setBounds(posun + componentWidth * posunX, componentHeight + componentDistance + 26 * 2 + 2, componentWidth, componentHeight);
        endDate.setBounds(posun + componentWidth * posunX, componentHeight * 2 + componentDistance + 26 * 2 + 2, componentWidth, componentHeight);
        kodUzJednotky.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 26 * 2 + 2, componentWidth, componentHeight);

        typUzJednotkyTesty = getTypUzJednotky(comboBoxUzJednotky.getSelectedItem().toString());
        typVysledokTestu = getTypVysledkuTestu(comboBoxPozitivne.getSelectedItem().toString());

        comboBoxUzJednotky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typUzJednotkyTesty = getTypUzJednotky(comboBoxUzJednotky.getSelectedItem().toString());
                if (typUzJednotkyTesty != TypUzJednotky.VSETKY) {
                    jPanel.add(kodUzJednotky);
                    zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 4 + componentDistance + 26 * 2, componentWidth, componentHeight);
                    kodUzJednotky.setToolTipText(comboBoxUzJednotky.getSelectedItem().toString());
                } else {
                    jPanel.remove(kodUzJednotky);
                    zobraz.setBounds(posun + componentWidth * posunX, componentHeight * 3 + componentDistance + 26 * 2, componentWidth, componentHeight);
                }
                jPanel.repaint();
            }
        });

        comboBoxPozitivne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typVysledokTestu = getTypVysledkuTestu(comboBoxPozitivne.getSelectedItem().toString());
            }
        });


        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hladajTestyPrePracovisko = !hladajTestyPrePracovisko;
                if (hladajTestyPrePracovisko) {
                    jPanel.add(startDate);
                    jPanel.add(endDate);
                    jPanel.add(zobraz);
                    jPanel.add(comboBoxUzJednotky);
                    jPanel.add(comboBoxPozitivne);
                    if (typUzJednotkyTesty != TypUzJednotky.VSETKY) {
                        jPanel.add(kodUzJednotky);
                    }
                } else {
                    jPanel.remove(startDate);
                    jPanel.remove(endDate);
                    jPanel.remove(kodUzJednotky);
                    jPanel.remove(zobraz);
                    jPanel.remove(comboBoxPozitivne);
                    jPanel.remove(comboBoxUzJednotky);
                }
                jPanel.repaint();

            }
        });

        zobraz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startDate.getText().length() == 10 && endDate.getText().length() == 10) {
                    int day = Integer.parseInt(startDate.getText().substring(0, 2));
                    int month = Integer.parseInt(startDate.getText().substring(3, 5));
                    int year = Integer.parseInt(startDate.getText().substring(6));
                    Date startDate = new Date(year - 1900, month - 1, day);

                    day = Integer.parseInt(endDate.getText().substring(0, 2));
                    month = Integer.parseInt(endDate.getText().substring(3, 5));
                    year = Integer.parseInt(endDate.getText().substring(6));
                    Date endDate = new Date(year - 1900, month - 1, day);
                    PCRTestDate a = new PCRTestDate(startDate);
                    PCRTestDate b = new PCRTestDate(endDate);

                    ArrayList<PCRTestDate> testy = new ArrayList<>();

                    PCRTestDate t1 = new PCRTestDate(startDate);
                    PCRTestDate t2 = new PCRTestDate(endDate);
                    if (typVysledokTestu == TypVysledokTestu.VSETKY) {
                        if (typUzJednotkyTesty == TypUzJednotky.VSETKY) {
                            testy = app.getPcrTreeDate().getIntervalData(a, b);
                        } else if (typUzJednotkyTesty == TypUzJednotky.PRACOVISKO) {
                            testy = app.getPracovisko(Integer.parseInt(kodUzJednotky.getText())).getTesty().getIntervalData(t1, t2);
                        } else if (typUzJednotkyTesty == TypUzJednotky.OKRES) {
                            testy = app.getOkres(Integer.parseInt(kodUzJednotky.getText())).getTesty().getIntervalData(t1, t2);
                        } else if (typUzJednotkyTesty == TypUzJednotky.KRAJ) {
                            testy = app.getKraj(Integer.parseInt(kodUzJednotky.getText())).getTesty().getIntervalData(t1, t2);
                        }
                    } else if (typVysledokTestu == TypVysledokTestu.POZITIVNE) {
                        if (typUzJednotkyTesty == TypUzJednotky.VSETKY) {
                            testy = app.getPcrTreePositive().getIntervalData(t1, t2);
                        } else if (typUzJednotkyTesty == TypUzJednotky.PRACOVISKO) {
                            testy = app.getPracovisko(Integer.parseInt(kodUzJednotky.getText())).getPozitivneTesty().getIntervalData(t1, t2);
                        } else if (typUzJednotkyTesty == TypUzJednotky.OKRES) {
                            testy = app.getOkres(Integer.parseInt(kodUzJednotky.getText())).getPozitivneTesty().getIntervalData(t1, t2);
                        } else if (typUzJednotkyTesty == TypUzJednotky.KRAJ) {
                            testy = app.getKraj(Integer.parseInt(kodUzJednotky.getText())).getPozitivneTesty().getIntervalData(t1, t2);
                        }
                    }

                    String text;
                    int index = 1;
                    String[] textArray = new String[testy.size()];
                    for (PCRTestDate test : testy) {
                        PCRTest t = app.getPCRTest(test.getDataPosition());
                        text = "";
                        Okres okres = app.getOkres(t.getKodOkresu());
                        Pracovisko pracovisko = app.getPracovisko(t.getKodPracoviska());
                        text += "----------------------------------";
                        text += String.format("\nTest %d\n", index);
                        text += String.format("Kod testu: %s\n", t.getKodTestu());
                        Date date = t.getDatum();
                        String hours = date.getHours() < 10 ? "0" + date.getHours() : "" + date.getHours();
                        String minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
                        text += String.format("Datum testu: %s\n", date.getDate() + "." + (date.getMonth() + 1) + "." + (1900 + date.getYear()) + " " + hours + ":" + minutes);
                        text += String.format("Kod pracoviska: %s\n", t.getKodPracoviska());
                        text += String.format("Kod okresu: %s\n", t.getKodOkresu());
                        text += String.format("Kod kraja: %s\n", t.getKodKraja());
                        text += String.format("Nazov pracoviska: %s\n", pracovisko.getNazov());
                        text += String.format("Nazov okresu: %s\n", okres.getNazov());
                        text += String.format("Nazov kraja: %s\n", app.getKrajName(okres.getKodKraja()));
                        if (t.isVysledok()) {
                            text += "Vysledok: Pozitivny\n";
                        } else {
                            text += "Vysledok: Negativny\n";
                        }
                        if (t.getPoznamka() != null && !t.getPoznamka().equals("")) {
                            text += String.format("Poznamka: %s\n", t.getPoznamka());
                        }
                        text += "\nOsoba:\n";
                        text += String.format("Rodne cislo: %s\n", t.getRodCisloPacienta());
                        Osoba osoba = app.getOsoba(t.getRodCisloPacienta());
                        if (osoba != null) {
                            text += String.format("Meno: %s\n", osoba.getMeno());
                            text += String.format("Priezvisko: %s\n", osoba.getPriezvisko());
                            text += String.format("Datum Narodenia: %s\n", osoba.getDatumNarodenia().getDate() + "." + (osoba.getDatumNarodenia().getMonth() + 1) + "." + (1900 + osoba.getDatumNarodenia().getYear()));
                        } else {
                            text += "Dalsie udaje neexistuju\n";
                        }
                        textArray[index - 1] = text;
                        index++;
                        //System.out.println(text.length());
                    }

                    jTextArea.setText("PCR testy\nPocet: " + testy.size() + "\n");
                    for (String str : textArray) {
                        jTextArea.append(str);
                    }
                    jTextArea.setCaretPosition(0);

                    jPanel.add(jScrollPane);
                    //JOptionPane.showMessageDialog(null, text, "PCR Testy", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Zadaj datum vo formate DD.MM.YYYY");
                }


            }
        });
    }

    private void addPridajOsoba() {
        JButton jButton = new JButton("Pridaj osobu");
        jPanel.add(jButton);

        jButton.setBounds(posun + componentWidth, posun, componentWidth, componentHeight);

        JButton uloz = new JButton("Uloz");
        uloz.setBounds(posun + componentWidth, componentHeight * 4 + componentDistance, componentWidth, componentHeight);

        JTextField meno = new JTextField();
        JTextField priezvisko = new JTextField();
        JTextField rodneCislo = new JTextField();

        meno.setToolTipText("Meno");
        priezvisko.setToolTipText("Priezvisko");
        rodneCislo.setToolTipText("Rodne cislo");

        meno.setBounds(posun + componentWidth, componentHeight + componentDistance, componentWidth, componentHeight);
        priezvisko.setBounds(posun + componentWidth, componentHeight * 2 + componentDistance, componentWidth, componentHeight);
        rodneCislo.setBounds(posun + componentWidth, componentHeight * 3 + componentDistance, componentWidth, componentHeight);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novaOsoba = !novaOsoba;
                if (novaOsoba) {
                    jPanel.add(meno);
                    jPanel.add(priezvisko);
                    jPanel.add(rodneCislo);
                    jPanel.add(uloz);
                } else {
                    jPanel.remove(meno);
                    jPanel.remove(priezvisko);
                    jPanel.remove(rodneCislo);
                    jPanel.remove(uloz);
                }
                jPanel.repaint();

            }
        });

        uloz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!rodneCislo.getText().equals("")) {
                    if (rodneCislo.getText().length() == 9 || rodneCislo.getText().length() == 10) {
                        if (app.addOsoba(meno.getText(), priezvisko.getText(), rodneCislo.getText()) != null) {
                            JOptionPane.showMessageDialog(null, "Osoba bola vytvorena");
                            novaOsoba = !novaOsoba;
                            jPanel.remove(meno);
                            jPanel.remove(priezvisko);
                            jPanel.remove(rodneCislo);
                            jPanel.remove(uloz);
                            jPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(null, "Osobu sa nepodarilo vytvorit");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Zla dlzka rodneho cisla");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Zadaj rodne cislo");
                }
            }
        });
    }

    private void addVypisTestovPacienta() {
        JButton jButton = new JButton("Testy pacienta");
        jPanel.add(jButton);

        jButton.setBounds(posun + componentWidth * 3, posun, componentWidth, componentHeight);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rodCislo = JOptionPane.showInputDialog("Zadaj rodne cislo");
                if (rodCislo != null) {
                    Osoba osoba = app.getOsoba(rodCislo);
                    if (osoba != null) {
                        ArrayList<PCRTestDate> testy = osoba.getTesty().getInOrderData();
                        String text = String.format("Rodne cislo: %s\n", osoba.getRodCislo());
                        text += String.format("Meno: %s\n", osoba.getMeno());
                        text += String.format("Priezvisko: %s\n", osoba.getPriezvisko());
                        text += String.format("Datum Narodenia: %s\n", osoba.getDatumNarodenia().getDate() + "." + (osoba.getDatumNarodenia().getMonth() + 1) + "." + (1900 + osoba.getDatumNarodenia().getYear()));
                        int index = 1;
                        if (testy.size() == 0) {
                            text += "\nZiadne testy";
                        } else {
                            text += "\nPocet testov: " + testy.size() + "\n";
                        }
                        String[] textArray = new String[testy.size() == 0 ? 1 : testy.size()];
                        textArray[0] = text;
                        for (PCRTestDate t : testy) {
                            PCRTest test = app.getPCRTest(t.getDataPosition());
                            Okres okres = app.getOkres(test.getKodOkresu());
                            Pracovisko pracovisko = app.getPracovisko(test.getKodPracoviska());
                            text += "----------------------------------";
                            text += String.format("\nTest %d\n", index);
                            text += String.format("Kod testu: %s\n", test.getKodTestu());
                            Date date = test.getDatum();
                            String hours = date.getHours() < 10 ? "0" + date.getHours() : "" + date.getHours();
                            String minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
                            text += String.format("Datum testu: %s\n", date.getDate() + "." + (date.getMonth() + 1) + "." + (1900 + date.getYear()) + " " + hours + ":" + minutes);
                            text += String.format("Kod pracoviska: %s\n", test.getKodPracoviska());
                            text += String.format("Kod okresu: %s\n", test.getKodOkresu());
                            text += String.format("Kod kraja: %s\n", test.getKodKraja());
                            text += String.format("Nazov pracoviska: %s\n", pracovisko.getNazov());
                            text += String.format("Nazov okresu: %s\n", okres.getNazov());
                            text += String.format("Nazov kraja: %s\n", app.getKrajName(okres.getKodKraja()));
                            if (test.isVysledok()) {
                                text += "Vysledok: Pozitivny\n";
                            } else {
                                text += "Vysledok: Negativny\n";
                            }
                            if (test.getPoznamka() != null && !test.getPoznamka().equals("")) {
                                text += String.format("Poznamka: %s\n", test.getPoznamka());
                            }
                            textArray[index - 1] = text;
                            index++;
                            text = "";
                        }
                        jTextArea.setText("");
                        for (String str : textArray) {
                            jTextArea.append(str);
                        }
                        jTextArea.setCaretPosition(0);
                        jPanel.add(jScrollPane);
                        //JOptionPane.showMessageDialog(null, text, "PCR Testy", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Osoba neexistuje", "Osoba nenajdena", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void addVyhladajPCRTest() {
        JButton jButton = new JButton("Vyhladaj PCR test");
        jPanel.add(jButton);

        jButton.setBounds(posun + componentWidth * 2, posun, componentWidth, componentHeight);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodTestu = JOptionPane.showInputDialog("Zadaj kod PCR testu");
                if (kodTestu != null) {
                    PCRTest test = app.getPCRTest(kodTestu);
                    if (test != null) {
                        Okres okres = app.getOkres(test.getKodOkresu());
                        Pracovisko pracovisko = app.getPracovisko(test.getKodPracoviska());
                        String text = String.format("Kod testu: %s\n", test.getKodTestu());
                        Date date = test.getDatum();
                        String hours = date.getHours() < 10 ? "0" + date.getHours() : "" + date.getHours();
                        String minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
                        text += String.format("Datum testu: %s\n", date.getDate() + "." + (date.getMonth() + 1) + "." + (1900 + date.getYear()) + " " + hours + ":" + minutes);
                        text += String.format("Kod pracoviska: %s\n", test.getKodPracoviska());
                        text += String.format("Kod okresu: %s\n", test.getKodOkresu());
                        text += String.format("Kod kraja: %s\n", test.getKodKraja());
                        text += String.format("Nazov pracoviska: %s\n", pracovisko.getNazov());
                        text += String.format("Nazov okresu: %s\n", okres.getNazov());
                        text += String.format("Nazov kraja: %s\n", app.getKrajName(okres.getKodKraja()));
                        if (test.isVysledok()) {
                            text += "Vysledok: Pozitivny\n";
                        } else {
                            text += "Vysledok: Negativny\n";
                        }
                        if (test.getPoznamka() != null && !test.getPoznamka().equals("")) {
                            text += String.format("Poznamka: %s\n", test.getPoznamka());
                        }
                        text += "\nOsoba:\n";
                        text += String.format("Rodne cislo: %s\n", test.getRodCisloPacienta());
                        Osoba osoba = app.getOsoba(test.getRodCisloPacienta());
                        if (osoba != null) {
                            text += String.format("Meno: %s\n", osoba.getMeno());
                            text += String.format("Priezvisko: %s\n", osoba.getPriezvisko());
                            text += String.format("Datum Narodenia: %s\n", osoba.getDatumNarodenia().getDate() + "." + (osoba.getDatumNarodenia().getMonth() + 1) + "." + (1900 + osoba.getDatumNarodenia().getYear()));
                        } else {
                            text += "Dalsie udaje neexistuju\n";
                        }
                        JOptionPane.showMessageDialog(null, text, "PCR Test", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Test pre tento kod neexistuje", "Test nenajdeny", JOptionPane.ERROR_MESSAGE);
                    }
                }
                //System.out.println(kodTestu);
            }
        });
    }

    private void addVytvorPCRTestComponents() {
        JButton jButton = new JButton("Vytvor PCR test");
        jPanel.add(jButton);

        JButton ulozPcrTest = new JButton("Uloz");
        jButton.setBounds(posun, posun, componentWidth, componentHeight);
        ulozPcrTest.setBounds(posun, componentHeight * 7 + componentDistance, componentWidth, componentHeight);

        JTextField rodneCislo = new JTextField();
        JTextField kodPracoviska = new JTextField();
        JTextField kodOkresu = new JTextField();
        //JTextField kodKraja = new JTextField();
        JTextField poznamka = new JTextField();
        JRadioButton vysledokTrue = new JRadioButton("Positive");
        JRadioButton vysledokFalse = new JRadioButton("Negative");

        rodneCislo.setToolTipText("Rodne cislo");
        kodPracoviska.setToolTipText("Kod pracoviska");
        kodOkresu.setToolTipText("Kod okresu");
        //kodKraja.setToolTipText("Kod kraju");
        poznamka.setToolTipText("Poznamka");

        kodPracoviska.setText("203");
        kodOkresu.setText("205");


        rodneCislo.setBounds(posun, componentHeight + componentDistance, componentWidth, componentHeight);
        kodPracoviska.setBounds(posun, componentHeight * 2 + componentDistance, componentWidth, componentHeight);
        kodOkresu.setBounds(posun, componentHeight * 3 + componentDistance, componentWidth, componentHeight);
        poznamka.setBounds(posun, componentHeight * 4 + componentDistance, componentWidth, componentHeight);
        vysledokTrue.setBounds(posun, componentHeight * 5 + componentDistance, componentWidth, componentHeight);
        vysledokFalse.setBounds(posun, componentHeight * 6, componentWidth, componentHeight);

        vysledokTrue.setActionCommand("p");
        vysledokFalse.setActionCommand("n");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("p")) {
                    vysledokFalse.setSelected(false);
                } else if (e.getActionCommand().equals("n")) {
                    vysledokTrue.setSelected(false);

                }
            }
        };

        vysledokTrue.addActionListener(actionListener);
        vysledokFalse.addActionListener(actionListener);


        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novyTest = !novyTest;
                if (novyTest) {
                    jPanel.add(rodneCislo);
                    jPanel.add(kodPracoviska);
                    jPanel.add(kodOkresu);
                    jPanel.add(poznamka);
                    jPanel.add(vysledokTrue);
                    jPanel.add(vysledokFalse);
                    jPanel.add(ulozPcrTest);
                } else {
                    jPanel.remove(rodneCislo);
                    jPanel.remove(kodPracoviska);
                    jPanel.remove(kodOkresu);
                    jPanel.remove(poznamka);
                    jPanel.remove(vysledokTrue);
                    jPanel.remove(vysledokFalse);
                    jPanel.remove(ulozPcrTest);
                }
                jPanel.repaint();

            }
        });

        ulozPcrTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!rodneCislo.getText().equals("")) {
                    if (rodneCislo.getText().length() == 9 || rodneCislo.getText().length() == 10) {
                        int kodKraju = Integer.parseInt(kodOkresu.getText()) / 100;
                        boolean positive = vysledokTrue.isSelected();
                        Osoba osoba = app.getOsoba(rodneCislo.getText());
                        if (osoba == null) {
                            String meno = JOptionPane.showInputDialog(null, "Zadaj meno novej osoby");
                            String priezvisko = JOptionPane.showInputDialog(null, "Zadaj priezvisko novej osoby");
                            osoba = app.addOsoba(meno, priezvisko, rodneCislo.getText());
                        }
                        if (app.addPCRTest(null, rodneCislo.getText(), Integer.parseInt(kodPracoviska.getText()), Integer.parseInt(kodOkresu.getText()), kodKraju, positive, poznamka.getText(), osoba, null)) {
                            JOptionPane.showMessageDialog(null, "Test bol vytvoreny");
                            novyTest = !novyTest;
                            jPanel.remove(rodneCislo);
                            jPanel.remove(kodPracoviska);
                            jPanel.remove(kodOkresu);
                            jPanel.remove(poznamka);
                            jPanel.remove(vysledokTrue);
                            jPanel.remove(vysledokFalse);
                            jPanel.remove(ulozPcrTest);
                            jPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(null, "Test sa nepodarilo vytvorit");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Zla dlzka rodneho cisla");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Zadaj rodne cislo");
                }
            }
        });
    }

}
