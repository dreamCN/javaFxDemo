/**
 * @author: 鼠行止
 * @date: 2020/1/6
 */
module calculator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.wlxk.fx.calculator to javafx.fxml;
    exports com.wlxk.fx.calculator;

}