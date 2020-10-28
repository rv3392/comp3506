import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyFactCheckerTest {
    @Test
    public void testConsistent1() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "Anna")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }
    @Test
    public void testInConsistent2() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_ONE, "Katya", "Anna")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }
    @Test
    public void testInConsistent3() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_ONE, "Matt", "A"),
                new Fact(Fact.FactType.TYPE_ONE, "A", "B"),
                new Fact(Fact.FactType.TYPE_TWO, "Sanni", "B")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }
    @Test
    public void testInConsistent4() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_ONE, "Matt", "A"),
                new Fact(Fact.FactType.TYPE_TWO, "A", "B"),
                new Fact(Fact.FactType.TYPE_ONE, "B", "Sanni")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }
    @Test
    public void testInConsistent5() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_ONE, "Matt", "Katya")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }
    @Test
    public void testConsistent6() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void testConsistent7() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Sanni")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "A", "B"),
                new Fact(Fact.FactType.TYPE_ONE, "C", "D"),
                new Fact(Fact.FactType.TYPE_TWO, "A", "D"),
                new Fact(Fact.FactType.TYPE_TWO, "C", "B")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test2() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_TWO, "A", "B"),
                new Fact(Fact.FactType.TYPE_TWO, "C", "B"),
                new Fact(Fact.FactType.TYPE_ONE, "C", "D"),
                new Fact(Fact.FactType.TYPE_ONE, "D", "B")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test3() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "Sanni")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test4() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "a"),
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Kenton")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test5() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "a"),
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Katya"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Kenton")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test6() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "a"),
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Kenton"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Anna")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test7() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "a"),
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Kenton"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Anna"),
                new Fact(Fact.FactType.TYPE_TWO, "Anna", "b")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void test8() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "Anna", "Kenton"),
                new Fact(Fact.FactType.TYPE_TWO, "Kenton", "Katya"),
                new Fact(Fact.FactType.TYPE_TWO, "Katya", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Matt"),
                new Fact(Fact.FactType.TYPE_TWO, "Matt", "Max"),
                new Fact(Fact.FactType.TYPE_TWO, "Max", "Sanni"),
                new Fact(Fact.FactType.TYPE_ONE, "Max", "a"),
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Kenton"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "Anna"),
                new Fact(Fact.FactType.TYPE_ONE, "Sanni", "Kenton")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }
}
