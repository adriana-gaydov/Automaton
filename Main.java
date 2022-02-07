package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Arrow {
    private int from, to;
    private char with;

    public Arrow(int f, int t, char w) {
        from = f;
        to = t;
        with = w;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public char getWith() {
        return with;
    }

    @Override
    public String toString() {
        return from + "--" + with + "->" + to;
    }
}

class Automaton {
    private int posCnt;
    private ArrayList<Integer> finPos = new ArrayList<>();
    private ArrayList<Arrow> arr = new ArrayList<>();

    public Automaton(String fName) throws FileNotFoundException {
        try {
            File file = new File(fName);
            Scanner inp = new Scanner(file);
            int K, M, p;
            posCnt = inp.nextInt();
            K = inp.nextInt();
            for (int i = 0; i < K; i++) {
                p = inp.nextInt();
                finPos.add(p);
            }
            M = inp.nextInt();
            int f, t;
            char w;
            for (int i = 0; i < M; i++) {
                f = inp.nextInt();
                t = inp.nextInt();
                w = inp.next().charAt(0);
                arr.add(new Arrow(f, t, w));
            }
            inp.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    public int getPosCnt() {
        return posCnt;
    }

    public int getFinPosCnt() {
        return finPos.size();
    }

    public int getArrCnt() {
        return arr.size();
    }

    public Arrow getArrow(int n) {
        return arr.get(n);
    }

    @Override
    public String toString() {
        String s = "N=" + posCnt + ", K=" + getFinPosCnt() + ":{ ";
        for (int i : finPos) s = s + i + " ";
        s = s + "}, M=" + getArrCnt() + ": { ";
        for (Arrow a : arr) s += a + " ";
        s = s + "}";
        return s;
    }

    public boolean isFinal(int p) {
        return finPos.contains(p);
    }

    public boolean isWord(String w) {
        return trace(0, w);
    }

    public boolean trace(int p, String w) {
        if (w.isEmpty()) return isFinal(p);

        for (int i = 0; i < getArrCnt(); i++)
            if (getArrow(i).getFrom() == p && getArrow(i).getWith() == w.charAt(0))
                if (trace(getArrow(i).getTo(), w.substring(1))) return true;

        return false;
    }


    public void showAllWords(int len) {
        HashSet<String> usedWords = new HashSet<String>();
        ArrayList<String> allWords = new ArrayList<String>();
        genAllWords(0, len, "", usedWords, allWords);
        System.out.println(allWords);
    }

    public void showAllWordsDivBy(int len, int div) {
        HashSet<String> usedWords = new HashSet<String>();
        ArrayList<String> allWords = new ArrayList<String>();
        genAllWords(0, len, "", usedWords, allWords);
        for (String a : allWords) {
            if (Integer.parseInt(a, 2) % div == 0) {
                System.out.print(a + ";");
            }
        }
    }


    private void genAllWords(int cur, int len, String curString, HashSet<String> usedWords, ArrayList<String> allWords) {

        if (curString.length() == len && isFinal(cur) && isWord(curString) && !usedWords.contains(curString)) {
            allWords.add(curString);
            usedWords.add(curString);
        } else if (curString.length() <= len) {
            for (int i = 0; i < arr.size(); i++) {
                genAllWords(arr.get(i).getTo(), len, curString + arr.get(i).getWith(), usedWords, allWords);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Automaton a = new Automaton("auto.txt");
            Automaton binaryAuto = new Automaton("auto2.txt");

            a.showAllWords(6);

            binaryAuto.showAllWords(6);
            binaryAuto.showAllWordsDivBy(6, 3);  // div by 3

            //System.out.println(a);
            // System.out.println(a.isWord("ABAA"));

        } catch (Exception e) {
            System.out.println("File not found");
        }


    }
}

