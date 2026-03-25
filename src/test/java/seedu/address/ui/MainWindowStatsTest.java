package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class MainWindowStatsTest {

    @Test
    public void statsPanelPlaceholder_fieldExists() {
        // Test that the field exists via reflection (covers the field declaration)
        try {
            Class<?> mainWindowClass = MainWindow.class;
            java.lang.reflect.Field field = mainWindowClass.getDeclaredField("statsPanelPlaceholder");
            assertNotNull(field);
        } catch (NoSuchFieldException e) {
            org.junit.jupiter.api.Assertions.fail("statsPanelPlaceholder field not found");
        }
    }
}
