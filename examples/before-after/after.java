// Multiparadigm refactoring of the OO baseline in input.java.
// Target language: Java 21 (same language as the source — default mode of /multiparadigm).
// Applies mechanisms M1 (Object Algebra) and M3 (ADT + Pattern Matching) from the
// catalog using multiparadigm features Java has had since version 21:
// sealed interfaces, records, switch expressions over patterns, lambdas and HOFs.
// UML stereotypes are annotated as comments on the new declarations.

package interp;

import java.util.Map;

// =========================
// AST as a closed ADT — <<algebra>> base (M3: ADT + sealed types)
// The OO hierarchy with accept() is replaced by a sealed interface
// with records. The compiler guarantees exhaustiveness in switch.
// =========================
sealed interface Expr permits NumLit, BinOp, Var, IfExpr {}
record NumLit(int value)                          implements Expr {}
record BinOp(Op op, Expr left, Expr right)        implements Expr {}
record Var(String name)                           implements Expr {}
record IfExpr(Expr cond, Expr thenB, Expr elseB)  implements Expr {}

// The operator is no longer a String — closed enum, type-safe.
enum Op { ADD, SUB, MUL, DIV }

// =========================
// <<algebra>> of interpretations (Object Algebra / Tagless Final, M1)
// An interpretation is a VALUE (anonymous instance or lambda group),
// not a heavy class. Closes the constructors; opens the interpretations.
// =========================
interface ExprAlg<A> {
    A num(int v);
    A bin(Op op, A l, A r);
    A variable(String name);
    A ifExpr(A c, A t, A e);
}

// Folder ADT → algebra. Exhaustive pattern matching over the sealed interface.
final class Fold {
    private Fold() {}
    public static <A> A fold(Expr e, ExprAlg<A> alg) {
        return switch (e) {
            case NumLit(int v)                   -> alg.num(v);
            case BinOp(Op op, Expr l, Expr r)    -> alg.bin(op, fold(l, alg), fold(r, alg));
            case Var(String name)                -> alg.variable(name);
            case IfExpr(Expr c, Expr t, Expr el) -> alg.ifExpr(fold(c, alg), fold(t, alg), fold(el, alg));
        };
    }
}

// =========================
// Business rules extracted to pure functions (M4: HOF + FP composition)
// They used to live inside the visitors; now they are testable in isolation.
// =========================
final class Rules {
    private Rules() {}
    // Rule: 0 is false, non-zero is true
    public static boolean isTruthy(int v) { return v != 0; }
    // Rule: division with guard
    public static int safeDiv(int l, int r) {
        if (r == 0) throw new ArithmeticException("div by zero");
        return l / r;
    }
}

// =========================
// Interpretation 1 — Pretty printer (pure FP, no state)
// =========================
final class Interpretations {
    private Interpretations() {}

    public static final ExprAlg<String> PRETTY = new ExprAlg<>() {
        public String num(int v)                                  { return Integer.toString(v); }
        public String bin(Op op, String l, String r)              { return "(" + l + " " + opSym(op) + " " + r + ")"; }
        public String variable(String name)                       { return name; }
        public String ifExpr(String c, String t, String e)        { return "if " + c + " then " + t + " else " + e; }
    };

    private static String opSym(Op op) {
        return switch (op) {
            case ADD -> "+"; case SUB -> "-"; case MUL -> "*"; case DIV -> "/";
        };
    }

    // =========================
    // Interpretation 2 — Evaluator
    // The environment is modeled as an immutable Map (caller builds it once).
    // No encapsulated mutable state; if a REPL with persistent bindings is later
    // introduced, that is where a <<var>> Env wrapper would come in.
    // =========================
    public static ExprAlg<Integer> evalIn(Map<String, Integer> env) {
        return new ExprAlg<>() {
            public Integer num(int v) { return v; }
            public Integer bin(Op op, Integer l, Integer r) {
                return switch (op) {
                    case ADD -> l + r;
                    case SUB -> l - r;
                    case MUL -> l * r;
                    case DIV -> Rules.safeDiv(l, r);
                };
            }
            public Integer variable(String name) {
                Integer v = env.get(name);
                if (v == null) throw new RuntimeException("unbound: " + name);
                return v;
            }
            public Integer ifExpr(Integer c, Integer t, Integer e) {
                return Rules.isTruthy(c) ? t : e;
            }
        };
    }

    // =========================
    // Interpretation 3 — Type checker
    // =========================
    public enum Ty { INT, BOOL }

    public static final ExprAlg<Ty> TYPECHECK = new ExprAlg<>() {
        public Ty num(int v) { return Ty.INT; }
        public Ty bin(Op op, Ty l, Ty r) {
            if (l == Ty.INT && r == Ty.INT) return Ty.INT;
            throw new RuntimeException("type error in " + op);
        }
        public Ty variable(String name) { return Ty.INT; }
        public Ty ifExpr(Ty c, Ty t, Ty e) {
            if (c != Ty.INT) throw new RuntimeException("cond must be int");
            if (t != e)      throw new RuntimeException("branch type mismatch");
            return t;
        }
    };
}

// =========================
// <<typeclass>>-like — external openness via functional interface (M2)
// Without native type classes, Java uses functional interfaces as a proxy.
// Anyone can register a Show<T> without touching the core.
// =========================
@FunctionalInterface
interface Show<A> { String show(A a); }

// =========================
// Client
// =========================
public class Main {
    public static void main(String[] args) {
        Expr e = new IfExpr(new Var("x"),
                            new BinOp(Op.ADD, new NumLit(1), new NumLit(2)),
                            new NumLit(0));
        Map<String, Integer> env = Map.of("x", 1); // immutable

        System.out.println(Fold.fold(e, Interpretations.PRETTY));            // (1 + 2)
        System.out.println(Fold.fold(e, Interpretations.evalIn(env)));       // 3
        System.out.println(Fold.fold(e, Interpretations.TYPECHECK));         // INT

        // <<typeclass>>: register a new way to print without touching the core
        Show<Integer> hex = i -> "0x" + Integer.toHexString(i);
        System.out.println(hex.show(Fold.fold(e, Interpretations.evalIn(env)))); // 0x3
    }
}
