package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class StatisticsModeTest {

    // ---- fromUserInput: valid inputs ----

    @Test
    public void fromUserInput_fullNameTag_returnsTag() {
        assertEquals(Optional.of(StatisticsMode.TAG), StatisticsMode.fromUserInput("tag"));
    }

    @Test
    public void fromUserInput_shorthandT_returnsTag() {
        assertEquals(Optional.of(StatisticsMode.TAG), StatisticsMode.fromUserInput("t"));
    }

    @Test
    public void fromUserInput_fullNameDepartment_returnsDepartment() {
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), StatisticsMode.fromUserInput("department"));
    }

    @Test
    public void fromUserInput_shorthandD_returnsDepartment() {
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), StatisticsMode.fromUserInput("d"));
    }

    @Test
    public void fromUserInput_aliasDepth_returnsDepartment() {
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), StatisticsMode.fromUserInput("dept"));
    }

    // ---- fromUserInput: case insensitivity ----

    @Test
    public void fromUserInput_uppercaseTag_returnsTag() {
        assertEquals(Optional.of(StatisticsMode.TAG), StatisticsMode.fromUserInput("TAG"));
    }

    @Test
    public void fromUserInput_mixedCaseDepartment_returnsDepartment() {
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), StatisticsMode.fromUserInput("Department"));
    }

    @Test
    public void fromUserInput_upperCaseDept_returnsDepartment() {
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), StatisticsMode.fromUserInput("DEPT"));
    }

    // ---- fromUserInput: whitespace trimming ----

    @Test
    public void fromUserInput_leadingTrailingWhitespace_returnsTag() {
        assertEquals(Optional.of(StatisticsMode.TAG), StatisticsMode.fromUserInput("  tag  "));
    }

    @Test
    public void fromUserInput_whitespaceDept_returnsDepartment() {
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), StatisticsMode.fromUserInput("  dept  "));
    }

    // ---- fromUserInput: invalid / null inputs ----

    @Test
    public void fromUserInput_null_returnsEmptyOptional() {
        assertEquals(Optional.empty(), StatisticsMode.fromUserInput(null));
    }

    @Test
    public void fromUserInput_emptyString_returnsEmptyOptional() {
        assertEquals(Optional.empty(), StatisticsMode.fromUserInput(""));
    }

    @Test
    public void fromUserInput_unknownString_returnsEmptyOptional() {
        assertEquals(Optional.empty(), StatisticsMode.fromUserInput("role"));
    }

    @Test
    public void fromUserInput_blankString_returnsEmptyOptional() {
        assertEquals(Optional.empty(), StatisticsMode.fromUserInput("   "));
    }

    // ---- getFullName ----

    @Test
    public void getFullName_tag_returnsTag() {
        assertEquals("tag", StatisticsMode.TAG.getFullName());
    }

    @Test
    public void getFullName_department_returnsDepartment() {
        assertEquals("department", StatisticsMode.DEPARTMENT.getFullName());
    }

    // ---- enum basics ----

    @Test
    public void values_containsTagAndDepartment() {
        StatisticsMode[] modes = StatisticsMode.values();
        assertEquals(2, modes.length);
        assertTrue(java.util.Arrays.asList(modes).contains(StatisticsMode.TAG));
        assertTrue(java.util.Arrays.asList(modes).contains(StatisticsMode.DEPARTMENT));
    }

    @Test
    public void fromUserInput_validReturnIsPresent() {
        assertTrue(StatisticsMode.fromUserInput("t").isPresent());
        assertFalse(StatisticsMode.fromUserInput("xyz").isPresent());
    }
}

