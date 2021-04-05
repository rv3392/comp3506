import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomFactCheckerTest {
    @Test
    public void consistentGivenBothSameTime() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_TWO, "a", "b")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void consistentGivenOneAfterOther() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "a", "b")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void consistentGivenOneAfterAnotherAndSameTimeAsAnother() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "c"),
                new Fact(Fact.FactType.TYPE_ONE, "a", "d"),
                new Fact(Fact.FactType.TYPE_TWO, "b", "d")
        );

        assertTrue(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void inconsistentGivenOneAfterAnotherAndSameTimeAsAnother() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "c"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "d"),
                new Fact(Fact.FactType.TYPE_TWO, "a", "d")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void inconsistentGivenTwoAfterEachOther() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "a")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void inconsistentGivenThreeWithTwoAfterEachOther() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_ONE, "b", "c"),
                new Fact(Fact.FactType.TYPE_ONE, "c", "a")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }

    @Test
    public void inconsistentGivenTwoAtSameTimeAndAfterEachOther() {
        List<Fact> facts = List.of(
                new Fact(Fact.FactType.TYPE_ONE, "a", "b"),
                new Fact(Fact.FactType.TYPE_TWO, "a", "b")
        );

        assertFalse(FactChecker.areFactsConsistent(facts));
    }
}
