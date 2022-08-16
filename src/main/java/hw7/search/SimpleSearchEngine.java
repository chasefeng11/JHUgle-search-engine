package hw7.search;

import hw7.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class SimpleSearchEngine implements SearchEngine {
    private Map<String, Set<String>> a;

    public SimpleSearchEngine(Map<String, Set<String>> var1) {
        this.a = var1;
    }

    public void buildSearchEngine(File var1) {
        try {
            Scanner var2 = new Scanner(new FileReader(var1));

            try {
                while(var2.hasNext()) {
                    String var3 = var2.nextLine();
                    String[] var4;
                    int var5 = (var4 = var2.nextLine().split("\\s+")).length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        String var7 = var4[var6];
                        Object var10 = new HashSet();
                        if (this.a.has(var7)) {
                            var10 = (Set)this.a.get(var7);
                        } else {
                            this.a.insert(var7, (Set) var10);
                        }

                        ((Set)var10).add(var3);
                    }
                }
            } catch (Throwable var12) {
                try {
                    var2.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }

                throw var12;
            }

            var2.close();
        } catch (FileNotFoundException var13) {
            System.err.println("can't open " + var1.getAbsolutePath());
        }

    }

    public void runSearchEngine() {
        System.out.println("\n=== JHUgle Search Engine Running! ===\n>>> Add your query below >>> ");
        Scanner var1 = new Scanner(System.in);
        Stack var2 = new Stack();

        while(true) {
            System.out.print("> ");
            String var3 = var1.next();
            if ("!".equals(var3)) {
                var1.close();
                return;
            }

            if ("?".equals(var3)) {
                a(var2);
            } else if (!a(var3)) {
                this.a(var2, var3);
            } else {
                this.b(var2, var3);
            }
        }
    }

    public List<String> queryWithoutRunning(String var1) {
        Stack var2 = new Stack();
        ArrayList var3 = new ArrayList();
        String[] var8;
        int var4 = (var8 = var1.split("\\s+")).length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var8[var5];
            if ("!".equals(var6)) {
                break;
            }

            if ("?".equals(var6)) {
                if (var2.size() > 0) {
                    Iterator var9 = ((Set)var2.peek()).iterator();

                    while(var9.hasNext()) {
                        String var7 = (String)var9.next();
                        var3.add(var7);
                    }
                }
            } else if (!a(var6)) {
                this.a(var2, var6);
            } else {
                this.b(var2, var6);
            }
        }

        return var3;
    }

    private void a(Stack<Set<String>> var1, String var2) {
        Object var3 = new HashSet();
        if (this.a.has(var2)) {
            var3 = (Set)this.a.get(var2);
        }

        var1.add((Set) var3);
    }

    private static void a(Stack<Set<String>> var0) {
        if (var0.size() > 0) {
            Iterator var2 = ((Set)var0.peek()).iterator();

            while(var2.hasNext()) {
                String var1 = (String)var2.next();
                System.out.println(var1);
            }
        }

    }

    private static boolean a(String var0) {
        return "||".equals(var0) || "&&".equals(var0);
    }

    private static void b(Stack<Set<String>> var0) {
        Set var1 = (Set)var0.pop();
        Set var2 = (Set)var0.pop();
        HashSet var3;
        (var3 = new HashSet(var1)).retainAll(var2);
        var0.push(var3);
    }

    private static void c(Stack<Set<String>> var0) {
        Set var1 = (Set)var0.pop();
        Set var2 = (Set)var0.pop();
        HashSet var3;
        (var3 = new HashSet(var1)).addAll(var2);
        var0.push(var3);
    }

    private void b(Stack<Set<String>> var1, String var2) {
        if (var1.size() < 2) {
            System.err.println("You need at least two keywords!");
        } else if ("&&".equals(var2)) {
            b(var1);
        } else if ("||".equals(var2)) {
            c(var1);
        } else {
            System.err.println("Unsupported operation!");
        }
    }
}
