package PageReplacement;

import java.util.*;

class Algorithms {

    public int PageFaults;
    public int RSsize;
    public int[] ReferenceString;
    public int NumberofFrames;
    public int[] Frames;
    public String[] bits;
    public int hit = 0;
    public int next = 0;
    public int string;
    public int pointer = 0;

    public void FIFO(int no_frames, int[] refrence, int RSsize) {
        PageFaults = 0;
        System.out.println("**FIFO**");
        int[] page_frame = new int[no_frames];
        for (int i = 0; i < no_frames; i++) {
            page_frame[i] = -1;
        }
        for (int i = 0; i < RSsize; i++) {
            string = refrence[i];
            find(string, no_frames, page_frame);
        }
    }

    public void find(int string, int n, int[] page_frame) {
        for (int i = 0; i < n; i++) {
            if (page_frame[i] == string) {
                hit++;
                System.out.println("Frame: \t" + string + "\t|\t");
                return;
            }
            if (i == n - 1) {
                PageFaults++;
                page_frame[next++] = string;
                if (next == n) {
                    next = 0;
                }

                // <editor-fold desc="Print">
                System.out.print("Frame: \t" + string + "\t|\t");
                for (int j = 0; j < NumberofFrames; j++) {
                    if (page_frame[j] != -1) {
                        System.out.print(page_frame[j] + "\t");
                    }
                }
                System.out.println("");
            }
            // </editor-fold>

        }
    }

    public void LRU(int[] rs, int nof, int size) {
        System.out.println("**LRU**");
        int pointer = 0;
        PageFaults = 0;
        Boolean isFull = false;
        int buffer[];
        ArrayList<Integer> stack = new ArrayList<Integer>();
        int mem_layout[][];

        mem_layout = new int[size][nof];
        buffer = new int[nof];
        for (int j = 0; j < nof; j++) {
            buffer[j] = -1;
        }

        for (int i = 0; i < size; i++) {
            if (stack.contains(rs[i])) {
                stack.remove(stack.indexOf(rs[i]));
            }
            stack.add(rs[i]);
            int search = -1;
            for (int j = 0; j < nof; j++) {
                if (buffer[j] == rs[i]) {
                    search = j;
                    hit++;
                    break;
                }
            }
            if (search == -1) {
                if (isFull) {
                    int min_loc = size;
                    for (int j = 0; j < nof; j++) {
                        if (stack.contains(buffer[j])) {
                            int temp = stack.indexOf(buffer[j]);
                            if (temp < min_loc) {
                                min_loc = temp;
                                pointer = j;
                            }
                        }
                    }
                }
                buffer[pointer] = rs[i];
                PageFaults++;
                pointer++;
                if (pointer == nof) {
                    pointer = 0;
                    isFull = true;
                }
            }

            for (int j = 0; j < nof; j++) {
                mem_layout[i][j] = buffer[j];
            }
            // <editor-fold desc="Print">
            System.out.print("Frame: \t" + rs[i] + "\t|\t");
            for (int j = 0; j < nof; j++) {
                if (mem_layout[i][j] != -1 && search == -1) {
                    System.out.print(mem_layout[i][j] + "\t");
                }
            }
            System.out.println("");
            //</editor-fold>
        }

    }

    public void LFU(int[] rs, int nof, int size) {
        System.out.println("**LFU**");
        int num = 0, pageHit = 0, min = 0;
        int frame[] = new int[nof];
        int freq[] = new int[nof];
        int count[] = new int[100];
        boolean flag = true;
        int pointer = 0;
        boolean isFull = false;
        int page;
        PageFaults = 0;
        for (int i = 0; i < nof; i++) {
            frame[i] = -1;
            freq[i] = 0;
        }

        for (int i = 0; i < size; i++) {
            flag = true;
            page = rs[i];
            for (int j = 0; j < nof; j++) {
                if (frame[j] == page) {
                    flag = false;
                    pageHit++;
                    count[page]++;
                    break;
                }
            }
            if (flag) {
                if (isFull) {
                    for (int j = 0; j < nof; j++) {
                        num = frame[j];
                        freq[j] = count[num];
                    }
                    min = freq[0];
                    int n = 0;
                    for (int j = 0; j < nof; j++) {
                        if (freq[j] < min) {
                            min = freq[j];
                        }
                    }
                    for (int j = 0; j < nof; j++) {
                        if (freq[j] == min) {
                            n++;
                        }
                    }
                    if (freq[pointer] != min) {
                        pointer++;
                        if (pointer == nof) {
                            pointer = 0;
                            isFull = true;
                        }
                    }
                    for (int j = 0; j < nof; j++) {
                        if (freq[j] == min && n == 1) {
                            count[frame[j]] = 0;
                            frame[j] = page;
                            count[page]++;
                            break;
                        } else if (freq[j] == min && n != 1) {
                            count[frame[pointer]] = 0;
                            frame[pointer] = page;
                            count[page]++;
                            pointer++;
                            if (pointer == nof) {
                                pointer = 0;
                                isFull = true;
                            }
                            break;
                        }
                    }
                } else {
                    frame[pointer] = page;
                    pointer++;
                    if (pointer == nof) {
                        pointer = 0;
                        isFull = true;
                    }
                    count[page]++;
                }
            }

            // <editor-fold desc="Print">
            System.out.print("Frame: \t" + rs[i] + "\t|\t");
            for (int j = 0; j < nof; j++) {
                if (frame[j] != -1 && flag) {
                    System.out.print(frame[j] + "\t");
                }
            }
            System.out.println("");
            //</editor-fold>
        }
        PageFaults = size - pageHit;
    }

    public void SecondChance(int no_frames, int[] refrence, int RSsize) {
        PageFaults = 0;
        System.out.println("**Second Chance**");
        boolean second_chance[] = new boolean[no_frames];
        int[] page_frame = new int[no_frames];
        for (int i = 0; i < no_frames; i++) {
            page_frame[i] = -1;
            second_chance[i] = false;
        }

        for (int i = 0; i < RSsize; i++) {
            string = refrence[i];
            if (!find(string, page_frame, second_chance, no_frames)) {
                this.pointer = replace(string, page_frame,
                        second_chance, no_frames, pointer);
                PageFaults++;
                // <editor-fold desc="Print">
                System.out.print("Frame: \t" + string + "\t|\t");
                for (int j = 0; j < NumberofFrames; j++) {
                    if (page_frame[j] != -1) {
                        if (second_chance[j]) {
                            System.out.print(page_frame[j] + "|1|\t");
                        } else {
                            System.out.print(page_frame[j] + "|0|\t");
                        }

                    }
                }
                System.out.println("");
                //</editor-fold>
            } else {
                hit++;
                System.out.println("Frame: \t" + string + "\t|\t");
            }
        }
    }

    public boolean find(int string, int[] page_frame, boolean second_chance[], int no_of_frames) {
        for (int i = 0; i < no_of_frames; i++) {
            if (page_frame[i] == string) {
                second_chance[i] = true;
                return true;
            }
        }
        return false;
    }

    public int replace(int string, int[] page_frame, boolean second_chance[], int no_of_frames, int pointer) {
        while (true) {
            if (!second_chance[pointer]) {
                page_frame[pointer] = string;
                if (pointer < no_of_frames - 1) {
                    pointer++;
                } else {
                    pointer = 0;
                }
                return pointer;
            }
            second_chance[pointer] = false;
            if (pointer < no_of_frames - 1) {
                pointer++;
            } else {
                pointer = 0;
            }
        }
    }

    public void Optimal() {
        System.out.println("**Optimal Page Replacement Algorithm**");

        PageFaults = 0;
        Frames = new int[NumberofFrames];
        for (int i = 0; i < NumberofFrames; i++) {
            Frames[i] = -1;
        }

        int j = 0;
        while (j < RSsize) {
            if (CheckFrame(j)) {
                Frames[Furthest(j)] = ReferenceString[j];
                PageFaults++;
                // <editor-fold desc="Print">
                System.out.print("Frame: \t" + ReferenceString[j] + "\t|\t");
                for (int i = 0; i < NumberofFrames; i++) {
                    if (Frames[i] != -1) {
                        System.out.print(Frames[i] + "\t");
                    }
                }
                System.out.println("");

            } else {
                System.out.println("Frame: \t" + ReferenceString[j] + "\t|\t");
            }
            // </editor-fold>
            j++;
        }

    }

    public int Furthest(int j) {
        int[] Difference = new int[NumberofFrames];

        for (int i = 0; i < NumberofFrames; i++) {
            if (Frames[i] == -1) {
                return i;
            }
            int k;
            for (k = j + 1; k < RSsize; k++) {
                if (Frames[i] == ReferenceString[k]) {
                    Difference[i] = k - j;
                    break;
                } else if (k == RSsize - 1) {
                    Difference[i] = 1000;
                }

            }
        }

        int max = 0;
        for (int i = 0; i < NumberofFrames; i++) {
            if (Difference[i] > Difference[max]) {
                max = i;
            }
        }
        return max;
    }

    /*assumptions:
    the modify bits are set to 1 to a page then leaving 2 pages as 0 then set to the following page to 1 (0 1 0 0 1 0 0 1 0 0..)
     */
    public void EnhanceSecondChance() {
        System.out.println("**Enhanced Second Chance Algorithm**");
        PageFaults = 0;
        Frames = new int[NumberofFrames];
        bits = new String[NumberofFrames];// ("00","01","10","11")

        for (int i = 0; i < NumberofFrames; i++) {
            bits[i] = "";
            Frames[i] = 1000;
        }

        //Loop through all the reference setting one by one to te frame
        int j = 0;
        while (j < RSsize) {
            if (CheckFrame(j)) {
                BitSetting(j);
                PageFaults++;
                // <editor-fold desc="Print">
                if ((j - 1) % 3 == 0) {
                    System.out.print("Frame: \t" + ReferenceString[j] + "*\t|\t");//modified bit = 1
                } else {
                    System.out.print("Frame: \t" + ReferenceString[j] + "\t|\t");
                }

                for (int i = 0; i < NumberofFrames; i++) {
                    switch (bits[i]) {
                        case "00":
                            System.out.print(Frames[i] + "|⁰₀|\t");
                            break;
                        case "01":
                            System.out.print(Frames[i] + "|⁰₁|\t");
                            break;
                        case "10":
                            System.out.print(Frames[i] + "|¹₀|\t");
                            break;
                        case "11":
                            System.out.print(Frames[i] + "|¹₁|\t");
                            break;
                    }

                }
                System.out.println("");

            } else {
                if ((j - 1) % 3 == 0) {
                    System.out.print("Frame: \t" + ReferenceString[j] + "*\t|\t");//modified bit = 1
                } else {
                    System.out.print("Frame: \t" + ReferenceString[j] + "\t|\t");
                }
                System.out.println("");
            }
// </editor-fold>
            j++;
        }
    }

    public boolean CheckFrame(int j) {
        for (int i = 0; i < NumberofFrames; i++) {
            if (Frames[i] == ReferenceString[j]) {
                return false;
            }
        }
        return true;
    }

    public void BitSetting(int j) {

        while (true) {
            for (int i = 0; i < NumberofFrames; i++) {
                if (bits[i].equals("00") || bits[i].equals("")) {
                    Frames[i] = ReferenceString[j];
                    if ((j - 1) % 3 == 0) {
                        bits[i] = "11";
                    } else {
                        bits[i] = "10";
                    }
                    return;
                }
            }
            for (int i = 0; i < NumberofFrames; i++) {
                if (!bits[i].equals("01")) {
                    switch (bits[i]) {
                        case "10":
                            bits[i] = "00";
                            break;
                        case "11":
                            bits[i] = "01";
                            break;
                    }
                } else {
                    bits[i] = "00";

                }
            }
        }
    }
}

public class PageReplacementAlgorithms {

    /*
    assumptions:Reference String = 20 numbers 
     */
    public static void main(String[] args) {
        Algorithms a = new Algorithms();
        int n = 20;
        a.RSsize = n;
        a.ReferenceString = new int[n];
        //a.ReferenceString = new int[]{2, 3, 4, 2, 1, 3, 7, 5, 4, 3};
        //a.NumberofFrames = 3;
        a.NumberofFrames = ((int) (Math.random() * 20)) + 1;
        System.out.print("Reference String is: ( ");
        for (int i = 0; i < n; i++) {
            a.ReferenceString[i] = (int) (Math.random() * 100);
            System.out.print(a.ReferenceString[i] + " ");
        }
        System.out.println(")");

        System.out.println("Number of Frames is " + a.NumberofFrames);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        a.FIFO(a.NumberofFrames, a.ReferenceString, a.RSsize);
        System.out.println("Page Faults for FIFO:" + a.PageFaults);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        a.LRU(a.ReferenceString, a.NumberofFrames, a.RSsize);
        System.out.println("Page Faults for LRU:" + a.PageFaults);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        a.LFU(a.ReferenceString, a.NumberofFrames, a.RSsize);
        System.out.println("Page Faults for LFU:" + a.PageFaults);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        a.SecondChance(a.NumberofFrames, a.ReferenceString, a.RSsize);
        System.out.println("Page Faults for Second Chance:" + a.PageFaults);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        a.EnhanceSecondChance();
        System.out.println("Page Faults for Enhanced Second Chance:" + a.PageFaults);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        a.Optimal();
        System.out.println("Page Faults for Optimal:" + a.PageFaults);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

    }
}
