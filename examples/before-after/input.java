// Simplified case study: interpreter for arithmetic expressions
// using the canonical Visitor pattern. Three operations: print, eval, typecheck.
// Anchored to the OO baseline from the thesis (Modelo de Evaluacion §Baseline OO).

package interp;

import java.util.HashMap;
import java.util.Map;

// =========================
// AST: classic OO hierarchy
// =========================
abstract class Expr {
    abstract <R> R accept(Visitor<R> v);
}

class NumLit extends Expr {
    final int value;
    NumLit(int value) { this.value = value; }
    <R> R accept(Visitor<R> v) { return v.visitNumLit(this); }
}

class BinOp extends Expr {
    final String op; // "+", "-", "*", "/"
    final Expr left, right;
    BinOp(String op, Expr l, Expr r) { this.op = op; this.left = l; this.right = r; }
    <R> R accept(Visitor<R> v) { return v.visitBinOp(this); }
}

class Var extends Expr {
    final String name;
    Var(String name) { this.name = name; }
    <R> R accept(Visitor<R> v) { return v.visitVar(this); }
}

class IfExpr extends Expr {
    final Expr cond, thenB, elseB;
    IfExpr(Expr c, Expr t, Expr e) { this.cond = c; this.thenB = t; this.elseB = e; }
    <R> R accept(Visitor<R> v) { return v.visitIf(this); }
}

// =========================
// Visitor — 3 operations
// =========================
interface Visitor<R> {
    R visitNumLit(NumLit n);
    R visitBinOp(BinOp b);
    R visitVar(Var v);
    R visitIf(IfExpr i);
}

// Operation 1: pretty-printer
class PrettyPrinter implements Visitor<String> {
    public String visitNumLit(NumLit n) { return Integer.toString(n.value); }
    public String visitBinOp(BinOp b) {
        return "(" + b.left.accept(this) + " " + b.op + " " + b.right.accept(this) + ")";
    }
    public String visitVar(Var v) { return v.name; }
    public String visitIf(IfExpr i) {
        return "if " + i.cond.accept(this) + " then " + i.thenB.accept(this) + " else " + i.elseB.accept(this);
    }
}

// Operation 2: evaluator (mutable state: environment)
class Evaluator implements Visitor<Integer> {
    final Map<String, Integer> env;
    Evaluator(Map<String, Integer> env) { this.env = env; }
    public Integer visitNumLit(NumLit n) { return n.value; }
    public Integer visitBinOp(BinOp b) {
        int L = b.left.accept(this);
        int R = b.right.accept(this);
        // embedded business rule: integer division with minimum guard
        if (b.op.equals("/") && R == 0) throw new ArithmeticException("div by zero");
        switch (b.op) {
            case "+": return L + R;
            case "-": return L - R;
            case "*": return L * R;
            case "/": return L / R;
            default: throw new IllegalStateException("unknown op " + b.op);
        }
    }
    public Integer visitVar(Var v) {
        if (!env.containsKey(v.name)) throw new RuntimeException("unbound: " + v.name);
        return env.get(v.name);
    }
    public Integer visitIf(IfExpr i) {
        // business rule: 0 is false, non-zero is true
        return i.cond.accept(this) != 0 ? i.thenB.accept(this) : i.elseB.accept(this);
    }
}

// Operation 3: type-checker (returns "int" or throws)
class TypeChecker implements Visitor<String> {
    public String visitNumLit(NumLit n) { return "int"; }
    public String visitBinOp(BinOp b) {
        String tL = b.left.accept(this);
        String tR = b.right.accept(this);
        if (!tL.equals("int") || !tR.equals("int")) throw new RuntimeException("type error in " + b.op);
        return "int";
    }
    public String visitVar(Var v) { return "int"; } // simplification
    public String visitIf(IfExpr i) {
        String tC = i.cond.accept(this);
        String tT = i.thenB.accept(this);
        String tE = i.elseB.accept(this);
        if (!tC.equals("int")) throw new RuntimeException("cond must be int");
        if (!tT.equals(tE)) throw new RuntimeException("branch type mismatch");
        return tT;
    }
}

// =========================
// Client
// =========================
public class Main {
    public static void main(String[] args) {
        Expr e = new IfExpr(new Var("x"), new BinOp("+", new NumLit(1), new NumLit(2)), new NumLit(0));
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 1);
        System.out.println(e.accept(new PrettyPrinter()));
        System.out.println(e.accept(new Evaluator(env)));
        System.out.println(e.accept(new TypeChecker()));
    }
}
