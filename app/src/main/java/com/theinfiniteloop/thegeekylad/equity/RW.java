package com.theinfiniteloop.thegeekylad.equity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Rahul Pillai on 20-Jul-17.
 */

public class RW {

    public void write(File file, String s) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException io) {}
    }

    public String read(File file) {
        String s="";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String t;
            while ((t = bufferedReader.readLine()) != null)
                s+=t+"\n";
        } catch (IOException io) {}
        return s;
    }

    public String formatEmail(String s) {

        String str = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.')
                str += "^";
            else
                str += s.charAt(i);
        }

        return str;

    }

    public String formatDate(String date) {

        Scanner scanner = new Scanner(date);
        scanner.useDelimiter("_");

        String s = scanner.next();

        switch (scanner.nextInt()) {
            case 1:
                s+="-JAN";
                break;
            case 2:
                s+="-FEB";
                break;
            case 3:
                s+="-MAR";
                break;
            case 4:
                s+="-APR";
                break;
            case 5:
                s+="-MAY";
                break;
            case 6:
                s+="-JUN";
                break;
            case 7:
                s+="-JUL";
                break;
            case 8:
                s+="-AUG";
                break;
            case 9:
                s+="-SEP";
                break;
            case 10:
                s+="-OCT";
                break;
            case 11:
                s+="-NOV";
                break;
            case 12:
                s+="-DEC";
                break;
        }

        s+="-"+scanner.next();

        return s;

    }

    public String formatTime(String time) {

        int t,u;
        Scanner scanner = new Scanner(time);
        scanner.useDelimiter("_");

        return (t = scanner.nextInt())%12+":"+(((u = scanner.nextInt())%100==0)?"0"+u:u+"")+((t>12)?" PM":" AM");

    }

}
