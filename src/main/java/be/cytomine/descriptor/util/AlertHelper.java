package be.cytomine.descriptor.util;

import javafx.scene.control.Alert;

/**
 * Date: 05-07-17
 * By  : Mormont Romain
 */
public class AlertHelper {

    /** Pop an alert */
    public static void popAlert(Alert.AlertType type, String title, String header, String content, boolean wait) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        if (wait) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

    public static void popInvalidField(String field, Exception e) {
        popInvalidField(field, e.getMessage());
    }

    public static void popInvalidField(String field, String cause) {
        AlertHelper.popAlert(
            Alert.AlertType.ERROR,
            "Erreur",
            "Champ invalide",
            "Le champ '" + field + "' est invalide: " + cause,
            true
        );
    }

    public static void popEmptyField(String field) {
        AlertHelper.popAlert(
            Alert.AlertType.ERROR,
            "Erreur",
            "Champ invalide",
            "Le champ '" + field + "' ne peut être vide.",
            true
        );
    }

    public static void popException(Exception e) {
        AlertHelper.popAlert(
                Alert.AlertType.ERROR,
                "Erreur",
                "Une exception s'est produite.",
                e.getMessage(),
                true
        );
    }

    public static void popError(String cause) {
        AlertHelper.popAlert(
                Alert.AlertType.ERROR,
                "Erreur",
                "Une erreur s'est produite.",
                cause,
                true
        );
    }
}
