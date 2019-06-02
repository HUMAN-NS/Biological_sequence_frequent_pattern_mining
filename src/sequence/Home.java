/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequence;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author User
 */
public class Home extends javax.swing.JFrame {

    ArrayList<JTextField> TFList;
    ArrayList<ArrayList> Table;
    ArrayList<String> MergedTable;
    static int id;
    Trees T;
    ArrayList<String> Freq_Set;

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        TFList = new ArrayList<JTextField>();
        MergedTable = new ArrayList<String>();
        id = 0;
        Freq_Set = new ArrayList<String>();
        info.setTabSize(12);
    }

    void GenerateTables() {

        Table = new ArrayList<ArrayList>();
        // info.setText("");
        for (JTextField jt : TFList) {

            // info.append("\n\nNew Table\n---------------------------------\n\n");
            ArrayList<String> pattern = new ArrayList<String>();

            String s = jt.getText().trim();
            System.err.println(" txt: " + s);
            char chr[] = s.toCharArray();
            int len = chr.length;
            HashSet<Character> ch = new HashSet<Character>();
            for (char c : chr) {
                ch.add(c);
            }
            info.append("\nWords: " + ch + "\n");

            // System.err.println(" hashset: " + ch);
            String str = "";
            for (char x : ch) {
                // System.err.println("\tx:" + x);
                boolean on = false;
                int pos = 0;
                for (int i = 0; i < len; i++) {

                    //  System.err.println("chr[" + i + "]:" + chr[i]);
                    if (!on) {
                        System.err.println("!on");
                        if (chr[i] == x) {
                            System.err.println("=x");
                            str = "" + chr[i];
                            pos = i + 1;
                            on = true;
                            if ((i + 1 == len)) {
                                pattern.add(str + "#" + pos);

                            }


                        }
                    } else {
                        System.err.println("on");
                        if (chr[i] == x) {
                            System.err.println("=x");
                            pattern.add(str + "#" + pos);
                            // info.append("\n" + str + "\t\t" + pos);
                            str = "" + x;
                            pos = i + 1;
                            if ((i + 1 == len)) {
                                pattern.add(str + "#" + pos);
                                // info.append("\n" + str + "\t\t" + pos);

                            }

                        } else if ((i + 1 == len)) {
                            str += chr[i];
                            pattern.add(str + "#" + pos);
                            //info.append("\n" + str + "\t\t" + pos);

                        } else {
                            System.err.println("!=x");
                            str += chr[i];
                        }
                    }
                }
                System.out.println(pattern);
            }
            ArrayList<String> arrlist = new ArrayList<>();
            for (String sp : pattern) {
                boolean flag = false;
                for (int i = 0; i < arrlist.size(); i++) {
                    if (arrlist.get(i).startsWith(sp.split("#")[0] + "#")) {
                        //int j = arrlist.indexOf(s);
                        String data = arrlist.get(i);
                        arrlist.remove(i);
                        String da = data.split("#")[0];
                        String db = data.split("#")[1];
                        db = db + "," + sp.split("#")[1];
                        arrlist.add(da + "#" + db);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    arrlist.add(sp);
                }

            }

            pattern.clear();
            pattern.addAll(arrlist);
            Collections.sort(pattern); //sorting...

            // info.append("\n\n "+pattern+"\n\n");
            Table.add(pattern);
        }

        for (ArrayList<String> s : Table) {
            info.append("\n\nNew Table\n-----------------\n\n");
            for (String str : s) {
                info.append("\n" + str.split("#")[0] + "\t\t" + str.split("#")[1]);
            }
        }
    }

    void MergeTables() {


        int min_sup = (int) minSupport.getValue();
        int sup_cnt = 0;
        MergedTable.clear();
        ArrayList<String> s = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        HashSet<String> hs = new HashSet<String>();
        hs.clear();

        for (ArrayList<String> ti : Table) {
            for (String pattern : ti) {

                String p = pattern.split("#")[0];
                //String loc=pattern.split("#")[1];

                hs.add(p);
            }
        }
        temp.clear();
        temp.addAll(hs);
        Collections.sort(temp);
        boolean ok;
        for (String str : temp) {
            String p_locset = str + "#";
            sup_cnt = 0;
            for (ArrayList<String> ti : Table) {

                ok = false;
                boolean hasSupport = false;

                p_locset += "{";
                for (String pattern : ti) {


                    String p = pattern.split("#")[0];
                    String loc = pattern.split("#")[1];
                    if (p.startsWith(str)) {
                        hasSupport = true;
                        p_locset += loc + ",";
                        ok = true;

                    }
                }

                if (hasSupport) {
                    sup_cnt++;
                }

                if (!ok) {
                    p_locset += "\u00F8,";
                }
                p_locset += "$";
                p_locset = p_locset.replace(",$", "");
                p_locset += "},";
            }
            p_locset += "$";
            p_locset = p_locset.replace(",$", "");

            if (sup_cnt >= min_sup) {
                MergedTable.add(p_locset);
            }
        }
        info.append("\n\nMerged table\n----------------------------------------------------\np\t\tloc_set\n----------------------------------------------------\n");
        for (String ss : MergedTable) {
            info.append("\n" + ss.split("#")[0] + "\t\t" + ss.split("#")[1]);
        }
    }

    void Node_Extend(ArrayList<String> Pr, Trees.Node d, boolean containsLeaf, String path, String prevEdge) {


        if (Pr.size() == 0) {
            //javax.swing.JOptionPane.showMessageDialog(null, "" + prevEdge);

            char ar[] = prevEdge.toCharArray();
            String st = "";
            String Ndata = "";
            int n = 0;
            boolean flag = false;
            int jj = 0;
            for (int i = 0; i < ar.length; i++) {
                if ((ar[i] + "").matches("[0-9]")) {
                    n++;
                    st += ar[i];
                    flag = true;

                } else {
                    if (flag) {
                        flag = false;
                        int k = Integer.parseInt(st);
                        k++;
                        k = (k > getSize(Table.get(jj))) ? 0 : k;
                        String ss = (k == 0) ? "\u00F8" : k + "";
                        //String ss = k + "";

                        for (int j = 0; j < n&&j<ss.length(); j++) {
                            System.out.println("ss.charAt("+j+"):"+ss.charAt(j));
                            ar[i - n + j] = ss.charAt(j);

                        }

                        n = 0;
                        st = "";
                    }
                }
                if (ar[i] == '}') {
                    jj++;
                }
            }
            Trees.Node<String> di = new Trees.Node<String>(id++, d);
            di.edgeValue = "$";
            String t = new String(ar);
            t = t.replaceAll(",\u00F8", "");
            t = t.replaceAll("\u00F8,", "");
            di.data = t;
            d.children.add(di);
            return;

        }
        HashSet<Character> StartChars = new HashSet<Character>();
        for (String s : Pr) {
            System.out.println("320\n " + s + " \t\t");
            StartChars.add(s.split("#")[0].charAt(0));

        }
        System.err.println("420characters: " + StartChars);
        ArrayList<Character> temp = new ArrayList<Character>();
        temp.addAll(StartChars);
        Collections.sort(temp);
        ArrayList<String> P = new ArrayList<String>();


        for (char x : temp) {
            P.clear();
            for (String s : Pr) {
                if (s.startsWith(x + "")) {
                    P.add(s);
                }
            }

            ArrayList<String> P_ = new ArrayList<String>();
            P_.clear();
            boolean hasLeaf = false;
            for (String p : P) {
                String pa = p.split("#")[0];
//                if ((p.trim()).startsWith(x + "") && (!(pa.trim()).equals(""))) {
                System.out.println("520 " + p);
                p = "-" + p;
                p = p.replaceAll("-" + x, "");
                System.err.print("\t 620" + p);
                String pb = p.split("#")[1];
                //##################--Modify starting Position--#######################################
                char ar[] = pb.toCharArray();
                String st = "";
                int n = 0;
                boolean flag = false;
                for (int i = 0; i < ar.length; i++) {


                    if ((ar[i] + "").matches("[0-9]")) {
                        n++;
                        st += ar[i];
                        flag = true;

                    } else {
                        if (flag) {
                            flag = false;
                            int k = Integer.parseInt(st);
                            k++;
                            String ss = k + "";

                            for (int j = 0; j < n&&j<ss.length(); j++) {
                                ar[i - n + j] = ss.charAt(j);
                            }

                            n = 0;
                            st = "";
                        }
                    }

                }
                
                
                //#########################################################################################
                pa = p.split("#")[0];
                System.err.println(" 720 " + pa + " ");
                if (!(pa.trim()).equals("")) {
                    String newPb = new String(ar);
                    P_.add(pa + "#" + newPb);
                } else {
                    hasLeaf = true;
                }

            }

            System.out.println(" 820 " + P_.size() + "   " + P_);
            if (P_.size() >= 0) {

                Trees.Node<String> di = new Trees.Node<String>(id++, d);
                di.edgeValue = x + "";
                String tmp = checkIntables(path + "" + x);
                tmp=tmp.replaceAll("\u00F8,", "");
                tmp=tmp.replaceAll(",\u00F8", "");
                di.data = "" + tmp;
                
                // prevEdge=tmp;
                d.children.add(di);
                Node_Extend(P_, di, hasLeaf, path + "" + x, tmp);
            }
        }

        if (containsLeaf) {

            char ar[] = prevEdge.toCharArray();
            String st = "";
            int n = 0;
            boolean flag = false;
            int jj = 0;
            for (int i = 0; i < ar.length; i++) {


                if ((ar[i] + "").matches("[0-9]+")) {
                    n++;
                    st += ar[i];
                    flag = true;

                } else {
                    if (flag) {
                        flag = false;
                        int k = Integer.parseInt(st);
                        k++;
                        k = (k > getSize(Table.get(jj))) ? 0 : k;
                        String ss = (k == 0) ? "\u00F8" : k + "";


                        for (int j = 0; j < n&&j<ss.length(); j++) {
                            ar[i - n + j] = ss.charAt(j);
                        }

                        n = 0;
                        st = "";
                    }
                }

                if (ar[i] == '}') {
                    jj++;
                }

            }
            String t = new String(ar);
            t = t.replaceAll(",\u00F8", "");
            t = t.replaceAll("\u00F8,", "");

            Trees.Node<String> d2 = new Trees.Node<String>(id++, d);
            d2.edgeValue = "$";
            d2.data = t;
            d.children.add(d2);
        }
    }

    int getSize(ArrayList<String> a) {
        int aSize = a.size();
        for (String s : a) {
            String nstr = s.split("#")[1];
            String split[] = nstr.split(Pattern.quote(","));
            int slen = split.length;
            aSize = (slen > 1) ? aSize + (slen - 1) : aSize;
        }
        return aSize;
    }

    String checkIntables(String b) {

        String str = "{";

        for (ArrayList<String> a : Table) {
            str += "{";
            boolean isnull = true;
            int aSize = getSize(a);
            for (String s : a) {

                if (s.startsWith(b.trim())) {
                    int l = b.length();
                    String nstr = s.split("#")[1];
                    String split[] = nstr.split(Pattern.quote(","));
                    String ss = "";
                    for (String t : split) {
                        if ((t.trim()).matches("[0-9]+")) {
                            int n = Integer.parseInt(t);
                            n = (n + (l - 1) > aSize) ? 0 : n + (l - 1);
                            ss += (n == 0) ? "\u00F8" : n;
                            isnull = false;
                        }
                        ss += ",";
                    }

                    str += ss;
                }

            }
            if (isnull) {
                str += "\u00F8";
            } else {
                str += "-";
                str = str.replaceAll(",-", "");
            }
            str += "},";
        }

        str += "}";
        str = str.replaceAll(Pattern.quote("},}"), "}}");
        str = str.replaceAll(Pattern.quote(",\u00F8"), "");
        str = str.replaceAll(Pattern.quote("\u00F8,"), "");
        str = str.replaceAll(Pattern.quote("{-}"), "{\u00F8}");
        str = str.replaceAll(Pattern.quote("{}"), "{\u00F8}");

        return str;
    }

    void Mspan(String s, ArrayList<HashSet> Ei, Trees.Node a) {

        //R(T.root);
        ArrayList<Trees.Node> al = a.children;
        ArrayList<HashSet> E = new ArrayList<>();
       // E.addAll(Ei);
        for (Trees.Node b : al) {
           E.clear();
           E.addAll(Ei);
            try {
                 ArrayList<HashSet<String>> Tb = String2ArrayList(b.data + "");
//                 System.err.println("^^data: "+Tb+" "+b.data);

               
//                }
//                catch(Exception ee){javax.swing.JOptionPane.showMessageDialog(null, "\n\n "+b.data+"Exception "+ee);}

                int num = 0;
                // System.out.println("" + Tb);
                String sup="";
                for (int i = 0; i < Tb.size(); i++) {

                    //System.out.println(Tb.get(i)+" ## "+E.get(i));
                    HashSet<String> tmp =new HashSet();
                    tmp.addAll(trimCollection(Tb.get(i)));
                    tmp.retainAll(trimCollection(E.get(i)));
                    tmp.remove("\u00F8");
                  //  System.out.println(Tb+" $$ "+E+" "+Tb.get(i)+" & "+ E.get(i)+"  tmp "+tmp);
                    if (tmp.size() > 0) {
                        num++;
                        sup+="S"+(i+1)+",";
                    } else {
                        tmp.add("\u00F8");
                    }

                    E.set(i, add(tmp, 1, i));
                }
                String x = b.edgeValue + "";
                if (num >= (int) minSupport.getValue()) {
                    
//                    System.out.println(" hasMin: "+Tb+" $ "+Ei );
                    
                     if(!(s+""+x).endsWith("$")){
                    Freq_Set.add(s + "" + x+"#"+num+"#"+sup+"#"+isPrimaryPattern(s+""+x));
                     }
                    boolean isLeaf = false;
//                    if (b.edgeValue.equals("$")) {
//                        isLeaf = true;
//                    }
                if (b.children.size() == 1) {
                    Trees.Node<String> nt = (Trees.Node) b.children.get(0);
                    if ((nt.edgeValue).trim().equals("$")) {

                        isLeaf = true;
                    }

                }
                    if (!isLeaf) {
                        //System.out.println("Not leaf "+b.data+" ###Edge: "+E );
                        Mspan(s + "" + x, E, b);
                    } else {
                         System.out.println("is leaf "+T.root+" ###Edge: "+E);
                        // if(b.parent.children.size()>1){}else{
                        Mspan(s+"" + x, E, T.root);//String2ArrayList(b.data + "") 
                    }
                }
                else{
                   // System.err.println(" NoMin: "+Tb+" $ "+Ei+" :: "+num );
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "error 224 " + e);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
    
    boolean isPrimaryPattern(String s){
        
        for(ArrayList<String> L:Table){
            
            for(String st:L){
                if(st.startsWith(s+"#")){
                    return true;
                }
            }
            
        }
        return false;
    }
    
   ArrayList<String> trimCollection(HashSet<String> c){
       
       ArrayList<String> al=new ArrayList<>();
       al.addAll(c);       
       for(int i=0;i<c.size();i++){
       String s=al.get(i);
       al.set(i, s.trim());
       }
       return al; 
   }

   int getSequnceSize(int ti){
       int len=0;
       JTextField tx=TFList.get(ti);
          len= tx.getText().trim().length();
       return len;
   }
   
   
    HashSet add(HashSet<String> hs, int i, int ti) {

        ArrayList<String> as = new ArrayList<>();
        as.addAll(hs);
        int len=getSequnceSize(ti);
        for (int j = 0; j < as.size(); j++) {

            if (as.get(j).matches("[0-9]+")) {
                int v = Integer.parseInt(as.get(j));
                v = v + i;
                if (v >len/*getSize(Table.get(ti))*/) {

                    as.set(j, "\u00F8");
                } else {
                    as.set(j, v + "");
                }
            }

        }
        HashSet<String> h = new HashSet<>();
        h.addAll(as);
        return (h);
    }
//    void R(Trees.Node n) {
//
//        ArrayList<Trees.Node> child = n.children;
//        System.out.println(n.data + " " + String2ArrayList("" + n.data));
//        for (Trees.Node cn : child) {
//
//            R(cn);
//        }
//
//    }

    ArrayList String2ArrayList(String pattern) {

        //System.err.println("__________________________________" + pattern);
        ArrayList<HashSet<String>> list = new ArrayList<>();
        HashSet<String> hs = new HashSet<>();
        char ch[] = pattern.toCharArray();
        int j = 0;
        String st = "";
        for (int i = 0; i < ch.length; i++) {
            try {
//             System.out.println("\nch " + ch[i]);

                if ((ch[i] + "").matches("\u00F8")) {
                    //  System.out.println("\n550 ");
                    hs.add(ch[i] + "");
                } else if ((ch[i] + "").matches("[0-9]")) {
                    //  System.out.println("\n220 ");
                    if ((ch[i + 1] + "").matches("[0-9]")) {
                        //    System.out.println("\n330 ");
                        st += ch[i];
                    } else if ((ch[i + 1] + "").matches("~")) {
                        int f = Integer.parseInt(ch[i] + "");
                        int s = Integer.parseInt((ch[i + 2] + ""));
                        for (int c = f; c <= s; c++) {
                            hs.add(c + "");
                        }
                        i = i + 2;

                    } else {
                        st += ch[i];
                        //  System.out.println("\n440 ");
                        hs.add(st);
                        st = "";
                    }
                }


                if (ch[i] == '}') {
                    // System.out.println("\n660 ");

                    if (hs.size() > 0) {
                        //  System.out.println("\n770 " + hs);
                        list.add(hs);
                    }

                    hs = new HashSet<>();
                }

            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, pattern + " :pattern " + e);
            }

        }


//        System.out.println(" " + list);
//
//        for (HashSet s : list) {
//            System.out.println(" size " + s.size());
//        }

        return list;
    }
    
    
    // Modification Part -Two way
    
    void scan_twoway() {
        info.append("\n\nSequence\tPattern\tTwo_way"); // \tEnhanced TW");
        for (JTextField jt : TFList) {
            for (String p : Freq_Set) {
                try {
                    
                    String Seq = jt.getText().trim();
                    String pat = p.split("#")[0].trim();
                    ArrayList<ArrayList<Integer>> res = scanner_tw(Seq, pat);
                    String r = scan_enhancedtw(res, pat, Seq);
                    info.append("\n\n"+Seq+"\t"+pat+"\t"+res+"\t"+r);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static ArrayList scanner_tw(String seq, String pat) {

        seq = seq.trim();
        pat = pat.trim();
        char T[] = seq.toCharArray();
        char P[] = pat.toCharArray();
        ArrayList<Integer> POS = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> TOTAL = new ArrayList<ArrayList<Integer>>();
        for (int Ti = 0; Ti < T.length; Ti++) {
            int first = 0, last = T.length - 1;
            boolean left = true;
            int pi = 0;
            int pm = P.length - 1;
            int x = 0;
            int y = T.length - 1;
            ArrayList<Integer> Res = new ArrayList<Integer>();
            boolean Noexit = true;
            while ((pi <= pm) && (x <= y)) {
                if (left) {
                    for (int i = first; i <= last; i++) {
                        x++;
                        if (!POS.contains(i)) {
                            if (T[i] == P[pi]) {
                                POS.add(i);
                                first = i + 1;
                                System.out.println(P[pi] + " :>" + (i + 1));
                                Res.add((i + 1));
                                pi++;
                                left = false;
                                break;
                            }
                        }
//                    if(i==last){Noexit=false;}
                    }

                } else {
                    for (int i = last; i >= first; i--) {

                        y--;
                        if (!POS.contains(i)) {
                            if (T[i] == P[pm]) {
                                POS.add(i);
                                last = i - 1;
                                System.out.println(P[pm] + " :< " + (i + 1));
                                Res.add((i + 1));
                                pm--;
                                left = true;
                                break;
                            }
                        }
//                    if(i==first){Noexit=false;}
                    }
                }
            }

            if (Res.size() != P.length) {
                break;
            }
            Collections.sort(Res);
            TOTAL.add(Res);
            System.out.println("\n " + Res);
            System.out.println("\n:-----------------------------------:\n");
        }

        return TOTAL;
    }

    static String scan_enhancedtw(ArrayList<ArrayList<Integer>> TOTAL, String Pattern, String Sequence) {

        System.out.println("----Enhanced Two way------\n");
        ArrayList<Integer> pos = new ArrayList<Integer>();
        ArrayList<Integer> other = new ArrayList<Integer>();

        char seq[] = Sequence.trim().toCharArray();
        char pat[] = Pattern.trim().toCharArray();

        for (ArrayList<Integer> el : TOTAL) {
            for (int val : el) {

                pos.add((val - 1));
            }
        }
        Collections.sort(pos);
        String str = "";
        for (int i = 0; i < seq.length; i++) {

            if (!pos.contains(i)) {
                other.add(i);
                str += seq[i];
            }

        }

        System.out.println(" BALANCE STRING: " + str + other);

        char oth[] = str.trim().toCharArray();
        ArrayList<Integer> nid = new ArrayList<Integer>();
        Integer pp[] = new Integer[pat.length];
        for (int k = 0; k < pp.length; k++) {
            pp[k] = 9999;
        }
        ArrayList<Integer> pid = new ArrayList<Integer>();
        for (int cd : other) {
            for (int i = 0; i < pat.length; i++) {
                if ((seq[cd] == pat[i]) && (!pid.contains((i)))) {
                    pp[i] = cd + 1;
                    pid.add(i);
                    break;
                }
            }
        }

        boolean flag = true;
        for (int k = 0; k < pp.length; k++) {
            if (pp[k] == 9999) {
                flag = false;
            }
        }

        if (flag) {
            nid.clear();
            nid.addAll(Arrays.asList(pp));
           System.out.println("\n Enhanced Two-Way Scan Result: " + nid);
        //return "";
              return nid.toString();
        } else {
          System.out.println("\nEnhanced Two-Way Scan Result: No Results");
         
         // return "";
          return "No Enhanced Results";
        }

    }
    
    
    
   // End of modification 
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        CountSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        minSupport = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        vFS = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        leftPanelTop = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        info = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 51, 51));

        CountSpinner.setFont(new java.awt.Font("Tahoma", 0, 24));
        CountSpinner.setModel(new javax.swing.SpinnerNumberModel(4, 0, 18, 1));
        CountSpinner.setValue(4);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText("No. of Sequences");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton1.setText("1. OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton2.setText("2. Generate Table");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton3.setText("3. Merge Tables");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        minSupport.setFont(new java.awt.Font("Tahoma", 0, 24));
        minSupport.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));
        minSupport.setValue(2);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText("Min. support");

        jButton4.setBackground(new java.awt.Color(153, 153, 153));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("4. Prefix Tree");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 0), 1, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ClassDiagramIcon.gif"))); // NOI18N
        jButton5.setText(" 4. Node_Extend");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton6.setText("5. MSpan");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        vFS.setFont(new java.awt.Font("Tahoma", 1, 12));
        vFS.setText("6. View Freq_Set");
        vFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vFSActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton7.setText("Scan");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton8.setText("Two Way Scan");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton9.setText("console fullscreen");
        jButton9.setContentAreaFilled(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setOpaque(true);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(CountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vFS))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(632, 632, 632)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minSupport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jButton9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(CountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(0, 3, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(vFS, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(minSupport, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addContainerGap(22, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        leftPanelTop.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter protein Sequences Here"));

        leftPanel.setBackground(new java.awt.Color(204, 204, 204));
        leftPanel.setLayout(new java.awt.GridLayout(10, 1, 0, 5));
        leftPanelTop.add(leftPanel);

        jPanel2.add(leftPanelTop, java.awt.BorderLayout.LINE_START);

        info.setBackground(new java.awt.Color(0, 0, 0));
        info.setColumns(20);
        info.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        info.setForeground(new java.awt.Color(255, 255, 255));
        info.setRows(5);
        jScrollPane1.setViewportView(info);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 630, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     CountSpinner.hide();
        int j = JOptionPane.showConfirmDialog(this, " Are you sure, Reload Input Area?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (j == 0) {
             ArrayList<String> proSeq=new ArrayList<>();
            try {
                File f = new File("D:\\testp.txt");
//                FileInputStream fi=new FileInputStream(f);
//                int c;
//                while ((c=fi.read())!=-1) {                    
//                    System.out.println((char)c);
//                }
               
                BufferedReader br=new BufferedReader(new FileReader(f));
                String s;
                while ((s=br.readLine())!=null) {
                    String ar[]=s.split(" ");
                    System.out.println("len.."+ar.length);
                    System.out.println(ar[ar.length-1]);
                    proSeq.add(ar[ar.length-1]);
                }
                
            } catch (Exception e) {
                System.out.println("error in getting Protien Sequnece..."+e);
            }


//          int n = (int) CountSpinner.getValue();
           int n=proSeq.size();
            leftPanel.removeAll();
          leftPanel.setLayout(new GridLayout(n, 1, 0, 5));
            TFList.clear();
            for (int i = 0; i < n; i++) {

                JTextField tf = new JTextField(30);
        tf.setText(proSeq.get(i));
                TFList.add(tf);
                tf.setFont(new Font("verdana", 1, 20));

                leftPanel.add(tf);
            }

            leftPanel.revalidate();
        }    // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            GenerateTables();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Exception  " + e);
        }

        // TODO add your handling code here:    
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        MergeTables();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        T.init();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        T = new Trees("root");
        String tmp = "{";
        for (ArrayList s : Table) {
            tmp += "{1~" + s.size() + "},";
        }
        tmp += "-";
        tmp = tmp.replaceAll(Pattern.quote(",-"), "}");
        T.root.data = tmp;

        Node_Extend(MergedTable, T.root, false, "", "");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        T.init();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        Freq_Set.clear();
        HashSet<String> hs = new HashSet<>();

        Mspan("", String2ArrayList(T.root.data + ""), T.root);

        System.out.println("Freq_set\n__________________________________________________\n " + Freq_Set);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void vFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vFSActionPerformed

        
        info.append("\n\nFrequency Set\n__________________________________________________________________________________________________________\n");
        info.append("\nPattern\tSupport\tSequences\t\tPrimary Pattern");
        info.append("\n__________________________________________________________________________________________________________\n");
        for(String s:Freq_Set){
            String arr[]=s.split("#");
            arr[3]=(arr[3].equalsIgnoreCase("true"))?"Yes":"No";
            arr[2]=arr[2]+"$";
            arr[2]=arr[2].replaceAll(Pattern.quote(",$"),"");
            
            info.append("\n"+arr[0]+"\t"+arr[1]+"\t"+arr[2]+"\t\t"+arr[3]);
         
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_vFSActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
          Scan_init();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        scan_twoway();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
  new console();          // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    
     String Scan_oneway(String P, String T) {

        int index = 0, num_occu = 0;
        char Parr[] = P.toCharArray();
        char Tarr[] = T.toCharArray();
        ArrayList<Integer> mark = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        String sum = "";
        int c = 0;
        int p = -1;
        while ((c + 1) < Tarr.length) {

            for (int i = 0; i < Tarr.length; i++) {
                c = i;
                System.out.println("Ti:" + Tarr[i] + " Pi:" + Parr[index]);
                if (Tarr[i] == Parr[index] && (!mark.contains(i))) {
                    p = i;
                    mark.add(i);
                    if (sum.equals("")) {
                        sum += (i + 1);
                    } else {
                        sum += "#" + (i + 1);
                    }
                    System.out.println(" == " + index + " Sum:" + sum + " i:" + i);
                    index++;
                    if (index >= Parr.length) {
                        num_occu++;
                        index = 0;
                        list.add(sum);
                        System.out.println(" list: " + list);
                        sum = "";
                        break;
                    }
                }

            }
        }
        ArrayList<ArrayList> Last = new ArrayList<>();

        for (String i : list) {
            System.out.println(" hh " + (i));

            String s[] = i.split("#");
            int SUM = 0;
            boolean flag = true;
            ArrayList<Integer> Sm=new ArrayList();
            for (int j = (s.length) - 1; j > -1; j--) {
                
                if(j>0){
                    int k=Integer.parseInt(s[j])-Integer.parseInt(s[j-1]);
                    Sm.add(0,k-1);
                    
                }
                
//                if (flag) {
//                    flag = false;
//                    SUM += Integer.parseInt(s[j]);
//
//                } else {
//                    SUM -= Integer.parseInt(s[j]);
//                }
            }

            Last.add(Sm);
        }

        System.out.println(" Last: " + Last);


        int sup1 = num_occu;
        String P1 = P + " \t" + Last;

        System.out.println("\n\n return :  " + sup1 + ", " + P1);

        return sup1 + ", pattern " + P1;


    }





void Scan_init() {

        for (JTextField jt : TFList) {
            info.append(" \n\n------------------Sequence : " + jt.getText() + "\n");
            for (String p : Freq_Set) {
                try {
                      String arr[]=p.split("#");
                      int l=arr[0].length();
                    if(l>1)
                    {
                        info.append("\n***Pattern :" + arr[0] + "\n");
                    String res = Scan_oneway(p.split("#")[0].trim(), jt.getText().trim());
                    
                    info.append("\n Occurance " + res);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }



    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
//              
                HashSet<String> s=new HashSet();
                HashSet<String> t=new HashSet();
                s.add("2");
                s.add("\u00F8 ");
                
                t.add("\u00F8 ");
                s.removeAll(t);
                System.out.println(" t "+t);
                
                
                
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner CountSpinner;
    static javax.swing.JTextArea info;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel leftPanelTop;
    private javax.swing.JSpinner minSupport;
    private javax.swing.JButton vFS;
    // End of variables declaration//GEN-END:variables
}
