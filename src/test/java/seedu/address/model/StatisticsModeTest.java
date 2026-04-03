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

    @Test
    public void fromUserInput_uppercaseRole_returnsRole() {
        assertEquals(Optional.of(StatisticsMode.ROLE), StatisticsMode.fromUserInput("ROLE"));
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

    @Test
    public void fromUserInput_whitespaceRole_returnsRole() {
        assertEquals(Optional.of(StatisticsMode.ROLE), StatisticsMode.fromUserInput("  role  "));
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
        assertEquals(Optional.empty(), StatisticsMode.fromUserInput("invalid"));
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

    @Test
    public void getFullName_role_returnsRole() {
        assertEquals("role", StatisticsMode.ROLE.getFullName());
    }

    // ---- enum basics ----

    @Test
    public void values_containsTagDepartmentAndRole() {
        StatisticsMode[] modes = StatisticsMode.values();
        assertEquals(3, modes.length);
        assertTrue(java.util.Arrays.asList(modes).contains(StatisticsMode.TAG));
        assertTrue(java.util.Arrays.asList(modes).contains(StatisticsMode.DEPARTMENT));
        assertTrue(java.util.Arrays.asList(modes).contains(StatisticsMode.ROLE));
    }

    @Test
    public void fromUserInput_validReturnIsPresent() {
        assertTrue(StatisticsMode.fromUserInput("t").isPresent());
        assertTrue(StatisticsMode.fromUserInput("r").isPresent());
        assertFalse(StatisticsMode.fromUserInput("xyz").isPresent());
    }
}

