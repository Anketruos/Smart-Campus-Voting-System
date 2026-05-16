package util;

import javafx.scene.Scene;

import java.net.URL;

public final class SceneUtil {

    private SceneUtil() {
    }

    public static void applyStyles(Scene scene) {
        URL stylesheet = SceneUtil.class.getResource("/styles.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
    }
}
