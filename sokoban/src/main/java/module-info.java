/**
 * @author: 鼠行止
 * @date: 2020/1/10
 */
module sokoban {

    //================导出================//
    opens com.wlxk.fx.sokoban.gui to javafx.fxml;
    exports com.wlxk.fx.sokoban;
    exports com.wlxk.fx.sokoban.gui;

    //================JavaFX================//
    requires transitive javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

}