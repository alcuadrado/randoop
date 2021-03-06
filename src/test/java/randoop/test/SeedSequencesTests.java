package randoop.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import randoop.TestValue;
import randoop.generation.SeedSequences;
import randoop.operation.TypedOperation;
import randoop.reflection.PackageVisibilityPredicate;
import randoop.reflection.ReflectionManager;
import randoop.reflection.TestValueExtractor;
import randoop.sequence.Sequence;
import randoop.sequence.Variable;
import randoop.types.JavaTypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SeedSequencesTests {

  @Test
  public void testGetSeedsFromAnnotatedFields() {
    Package thisPackage = this.getClass().getPackage();

    Set<Sequence> annotatedTestValues = new LinkedHashSet<>();
    ReflectionManager manager = new ReflectionManager(new PackageVisibilityPredicate(thisPackage));
    manager.add(new TestValueExtractor(annotatedTestValues));

    try {
      manager.apply(MissingPublicMod.class);
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage(),
          e.getMessage().contains("public")); // message should at least mention static modifier.
    }
    assertTrue(
        "shouldn't get anything but have " + annotatedTestValues.size() + " value(s)",
        annotatedTestValues.isEmpty());

    try {
      manager.apply(MissingStaticMod.class);
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage(),
          e.getMessage().contains("static")); // message should at least mention static modifier.
    }
    assertTrue("didn't get anything ", annotatedTestValues.isEmpty());

    try {
      manager.apply(BadType0.class);
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage(),
          e.getMessage().contains("type")); // message should at least mention type problem.
    }
    assertTrue("got nothing ", annotatedTestValues.isEmpty());

    try {
      manager.apply(BadType1.class);
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage(),
          e.getMessage().contains("type")); // message should at least mention type problem.
    }
    assertTrue("got nothing ", annotatedTestValues.isEmpty());

    try {
      manager.apply(BadType2.class);
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage(),
          e.getMessage().contains("type")); // message should at least mention type problem.
    }
    assertTrue("and still nothing... ", annotatedTestValues.isEmpty());

    Set<Sequence> s4 = new LinkedHashSet<>();
    ReflectionManager managerS4 =
        new ReflectionManager(new PackageVisibilityPredicate(thisPackage));
    managerS4.add(new TestValueExtractor(s4));

    managerS4.apply(TestValueExamples.class);
    Set<Sequence> expected =
        SeedSequences.objectsToSeeds(
            Arrays.asList(
                new Object[] {0, 1, 2, 3, "hi", false, (byte) 3, 'c', 3L, (float) 1.3, 1.4}));
    expected.add(
        new Sequence()
            .extend(
                TypedOperation.createNullOrZeroInitializationForType(JavaTypes.STRING_TYPE),
                new ArrayList<Variable>()));
    assertEquals(expected, s4);
  }
}

@SuppressWarnings("unused")
class TestValueExamples {
  @TestValue public static int x1 = 0;
  @TestValue public static boolean b = false;
  @TestValue public static byte by = 3;
  @TestValue public static char c = 'c';
  @TestValue public static long l = 3L;
  @TestValue public static float f = (float) 1.3;
  @TestValue public static double d = 1.4;
  @TestValue public static String s1 = null;
  @TestValue public static String s2 = "hi";
  @TestValue public static int[] a1 = new int[] {1, 2, 3};
  @TestValue public static int[] a2 = new int[] {};
}

@SuppressWarnings("unused")
class MissingPublicMod {
  @TestValue private static int x1 = 0;
}

@SuppressWarnings("unused")
class MissingStaticMod {
  @TestValue public int x1 = 0;
}

@SuppressWarnings("unused")
class BadType0 {
  // not static
  @TestValue static public Integer x1 = 0;
}

@SuppressWarnings("unused")
class BadType1 {
  @TestValue public static Object o1 = (int) 1;
}

@SuppressWarnings("unused")
class BadType2 {
  @TestValue public static Object o1 = (int) 1;
}
