package randoop.sequence;

import org.junit.Test;

import randoop.types.ClassOrInterfaceType;
import randoop.types.GenericClassType;
import randoop.types.InstantiatedType;
import randoop.types.JDKTypes;
import randoop.types.JavaTypes;
import randoop.types.ParameterizedType;
import randoop.types.ReferenceType;
import randoop.types.Substitution;
import randoop.types.Type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VariableNamerTest {

  @Test
  public void testNamesEndingInDigit() {
    ClassOrInterfaceType nonParamType =
        ClassOrInterfaceType.forClass(NonparameterizedTypeWithDigit19.class);
    String name = VariableRenamer.getVariableName(nonParamType);
    assertFalse("last character not a digit: " + name, lastCharIsDigit(name));

    GenericClassType genericType = ParameterizedType.forClass(GenericTypeWithDigit2.class);
    InstantiatedType type;
    Substitution<ReferenceType> substitution =
        Substitution.forArgs(
            genericType.getTypeParameters(), (ReferenceType) JavaTypes.STRING_TYPE);
    type = genericType.apply(substitution);
    name = VariableRenamer.getVariableName(type);
    assertFalse("last character not a digit: " + name, lastCharIsDigit(name));

    genericType = JDKTypes.LIST_TYPE;
    substitution =
        Substitution.forArgs(genericType.getTypeParameters(), (ReferenceType) nonParamType);
    InstantiatedType listType = genericType.apply(substitution);
    name = VariableRenamer.getVariableName(listType);
    assertFalse("last character not a digit: " + name, lastCharIsDigit(name));
  }

  private boolean lastCharIsDigit(String name) {
    return Character.isDigit(name.charAt(name.length() - 1));
  }
}
