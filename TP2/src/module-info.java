/**
 * Defines the Foundations26 module and its JavaFX dependencies.
 */

module FoundationsF26 {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	opens entityClasses to javafx.base;
	opens applicationMain to javafx.graphics, javafx.fxml;
}
