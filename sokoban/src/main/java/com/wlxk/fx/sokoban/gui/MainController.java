package com.wlxk.fx.sokoban.gui;

import com.wlxk.fx.sokoban.constant.GameMap;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author: 鼠行止
 * @date: 2020/1/10
 */
public class MainController {

    @FXML
    private GridPane gridPane;

    int level = 1;
    /**
     * temp_map为临时此位置
     */
    int[][] tempMap;
    /**
     * map为原地图上此位置
     */
    int[][] map;
    /**
     * 保存人的当前坐标     manx即为y坐标    many为x坐标
     */
    int manx, many;

    /**
     * 保存每一步操作的方向
     * LEFT = 1(推了箱子 = 1;没推箱子 = 11)
     * RIGHT = 3(推了箱子 = 3;没推箱子 = 31)
     * UP = 2(推了箱子 = 2;没推箱子 = 21)
     * DOWN = 4(推了箱子 = 4;没推箱子 = 41)
     */
    Stack<Integer> step;

    /**
     * label map
     * x_y -> label
     */
    Map<String, Label> labelMap;

    @FXML
    private void startClick(Event event) {
        this.level = 1;
        init(level);
    }

    @FXML
    private void resetClick(Event event) {
        init(level);
    }

    @FXML
    private void backClick() {
        back();
    }

    private Label getLabel(String text) {
        Label label = new Label();
        label.setMinHeight(20);
        label.setMinWidth(20);
        label.getStyleClass().add("label_" + text);
        return label;
    }

    private void init(int level) {
        step = new Stack<Integer>();
        copyData(GameMap.getByLevel(level));
        step.clear(); //初始化
        labelMap = new HashMap<>(400);
        repaint();
    }

    /**
     * 拷贝数据
     *
     * @param data 入参
     */
    void copyData(int[][] data) {
        map = data;
        tempMap = new int[map.length][map[0].length];
        manx = many = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                tempMap[i][j] = data[i][j];
                if (data[i][j] == 5) {
                    manx = i;
                    many = j;
                }
            }
        }
    }

    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            goLeft();
        } else if (event.getCode() == KeyCode.RIGHT) {
            goRight();
        } else if (event.getCode() == KeyCode.UP) {
            goUp();
        } else if (event.getCode() == KeyCode.DOWN) {
            goDown();
        }
        repaint();
        if (isWin()) {
            init(level + 1);
        }
    }

    private void repaint() {
        for (int i = 0; i < tempMap.length; i++) {
            for (int j = 0; j < tempMap[i].length; j++) {
                String mapKey = i + "_" + j;
                if (!labelMap.containsKey(mapKey)) {
                    Label label = getLabel(String.valueOf(tempMap[i][j]));
                    gridPane.add(label, j, i);
                    labelMap.put(mapKey, label);
                } else {
                    Label label = labelMap.get(mapKey);
                    ObservableList<String> styleClass = label.getStyleClass();
                    String classNum = "";
                    for (String className : styleClass) {
                        if (className.contains("_")) {
                            classNum = className.split("_")[1];
                            break;
                        }
                    }
                    if (!String.valueOf(tempMap[i][j]).equals(classNum)) {
                        label.getStyleClass().remove(1);
                        label.getStyleClass().add("label_" + tempMap[i][j]);
                    }
                }
            }
        }
    }

    public boolean isWin() {
        boolean bok = true;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if ((map[i][j] == 4 || map[i][j] == 9) && tempMap[i][j] != 9) {
                    bok = false;
                }
            }
        }
        return bok;
    }

    /**
     * 判断人原来的地方是草地或放箱子的地方
     */
    public void manOldPos() {
        if (map[manx][many] == 4 || map[manx][many] == 9) {
            tempMap[manx][many] = 4;
        } else {
            tempMap[manx][many] = 2;
        }
    }

    public void goLeft() {
        //左边有箱子
        if (tempMap[manx][many - 1] == 3 || tempMap[manx][many - 1] == 9) {
            //左边第二个位置没有箱子
            if (tempMap[manx][many - 2] == 2) {
                tempMap[manx][many - 2] = 3;
                tempMap[manx][many - 1] = 6;
                manOldPos();
                many--;
                //左走
                step.push(1);
            } else if (tempMap[manx][many - 2] == 4) {
                tempMap[manx][many - 2] = 9;
                tempMap[manx][many - 1] = 6;
                manOldPos();
                many--;
                //左走
                step.push(1);
            }
        } else if (tempMap[manx][many - 1] == 2 || tempMap[manx][many - 1] == 4) {
            //左边是草地或者目标位置
            tempMap[manx][many - 1] = 6;
            manOldPos();
            many--;
            //左走
            step.push(11);
        }
    }

    public void goRight() {
        //右边有箱子
        if (tempMap[manx][many + 1] == 3 || tempMap[manx][many + 1] == 9) {
            //右边第二个位置没有箱子
            if (tempMap[manx][many + 2] == 2) {
                tempMap[manx][many + 2] = 3;
                tempMap[manx][many + 1] = 7;
                manOldPos();
                many++;
                //右走
                step.push(3);
            } else if (tempMap[manx][many + 2] == 4) {
                tempMap[manx][many + 2] = 9;
                tempMap[manx][many + 1] = 7;
                manOldPos();
                many++;
                //右走
                step.push(3);
            }
        } else if (tempMap[manx][many + 1] == 2 || tempMap[manx][many + 1] == 4) {
            //右边是草地
            tempMap[manx][many + 1] = 7;
            manOldPos();
            many++;
            //右走
            step.push(31);
        }
    }

    public void goUp() {
        //上边有箱子
        if (tempMap[manx - 1][many] == 3 || tempMap[manx - 1][many] == 9) {
            //上边第二个位置没有箱子
            if (tempMap[manx - 2][many] == 2) {
                tempMap[manx - 2][many] = 3;
                tempMap[manx - 1][many] = 8;
                manOldPos();
                manx--;
                //上走
                step.push(2);
            } else if (tempMap[manx - 2][many] == 4) {
                tempMap[manx - 2][many] = 9;
                tempMap[manx - 1][many] = 8;
                manOldPos();
                manx--;
                //上走
                step.push(2);
            }
        } else if (tempMap[manx - 1][many] == 2 || tempMap[manx - 1][many] == 4) {
            //上边是草地
            tempMap[manx - 1][many] = 8;
            manOldPos();
            manx--;
            //上走
            step.push(21);
        }
    }

    public void goDown() {
        //上边有箱子
        if (tempMap[manx + 1][many] == 3 || tempMap[manx + 1][many] == 9) {
            //上边第二个位置没有箱子
            if (tempMap[manx + 2][many] == 2) {
                tempMap[manx + 2][many] = 3;
                tempMap[manx + 1][many] = 5;
                manOldPos();
                manx++;
                //下走
                step.push(4);
            } else if (tempMap[manx + 2][many] == 4) {
                tempMap[manx + 2][many] = 9;
                tempMap[manx + 1][many] = 5;
                manOldPos();
                manx++;
                //下走
                step.push(4);
            }
        } else if (tempMap[manx + 1][many] == 2 || tempMap[manx + 1][many] == 4) {
            //上边是草地
            tempMap[manx + 1][many] = 5;
            manOldPos();
            manx++;
            //下走
            step.push(41);
        }
    }

    /**
     * 往左悔一步
     *
     * @param direction 入参
     */
    public void backLeft(int direction) {
        //人的后面能否走
        if (tempMap[manx][many + 1] == 2 || tempMap[manx][many + 1] == 4) {
            //人的前面是否有箱子
            if (tempMap[manx][many - 1] == 3 || tempMap[manx][many - 1] == 9) {
                tempMap[manx][many + 1] = 6;
                if (direction == 11) {
                    //没推箱子
                    if (map[manx][many] == 4) {
                        tempMap[manx][many] = 4;
                    } else {
                        tempMap[manx][many] = 2;
                    }
                } else {
                    //推了箱子
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        tempMap[manx][many] = 9;
                    } else {
                        tempMap[manx][many] = 3;
                    }
                    if (map[manx][many - 1] == 4 || map[manx][many - 1] == 9) {
                        tempMap[manx][many - 1] = 4;
                    } else {
                        tempMap[manx][many - 1] = 2;
                    }
                }
                many++;
            } else {
                tempMap[manx][many + 1] = 6;
                if (map[manx][many] == 4) {
                    tempMap[manx][many] = 4;
                } else {
                    tempMap[manx][many] = 2;
                }
                many++;
            }
        }
    }

    /**
     * 往右悔一步
     *
     * @param direction 入参
     */
    public void backRight(int direction) {
        //人的后面能否走
        if (tempMap[manx][many - 1] == 2 || tempMap[manx][many - 1] == 4) {
            //人的前面是否有箱子
            if (tempMap[manx][many + 1] == 3 || tempMap[manx][many + 1] == 9) {
                tempMap[manx][many - 1] = 7;
                if (direction == 31) {
                    //没推箱子
                    if (map[manx][many] == 4) {
                        tempMap[manx][many] = 4;
                    } else {
                        tempMap[manx][many] = 2;
                    }
                } else {
                    //推了箱子
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        tempMap[manx][many] = 9;
                    } else {
                        tempMap[manx][many] = 3;
                    }
                    if (map[manx][many + 1] == 4 || map[manx][many + 1] == 9) {
                        tempMap[manx][many + 1] = 4;
                    } else {
                        tempMap[manx][many + 1] = 2;
                    }
                }
                many--;
            } else {
                tempMap[manx][many - 1] = 7;
                if (map[manx][many] == 4) {
                    tempMap[manx][many] = 4;
                } else {
                    tempMap[manx][many] = 2;
                }
                many--;
            }
        }
    }

    /**
     * 往上悔一步
     *
     * @param direction 入参
     */
    public void backUp(int direction) {
        //人的后面能否走
        if (tempMap[manx + 1][many] == 2 || tempMap[manx + 1][many] == 4) {
            //人的前面是否有箱子
            if (tempMap[manx - 1][many] == 3 || tempMap[manx - 1][many] == 9) {
                tempMap[manx + 1][many] = 8;
                if (direction == 21) {
                    //没推箱子
                    if (map[manx][many] == 4) {
                        tempMap[manx][many] = 4;
                    } else {
                        tempMap[manx][many] = 2;
                    }
                } else {
                    //推了箱子
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        tempMap[manx][many] = 9;
                    } else {
                        tempMap[manx][many] = 3;
                    }
                    if (map[manx - 1][many] == 4 || map[manx - 1][many] == 9) {
                        tempMap[manx - 1][many] = 4;
                    } else {
                        tempMap[manx - 1][many] = 2;
                    }
                }
                manx++;
            } else {
                tempMap[manx + 1][many] = 8;
                if (map[manx][many] == 4) {
                    tempMap[manx][many] = 4;
                } else {
                    tempMap[manx][many] = 2;
                }
                manx++;
            }
        }
    }

    /**
     * 往下悔一步
     *
     * @param direction 入参
     */
    public void backDown(int direction) {
        //人的后面能否走
        if (tempMap[manx - 1][many] == 2 || tempMap[manx - 1][many] == 4) {
            //人的前面是否有箱子
            if (tempMap[manx + 1][many] == 3 || tempMap[manx + 1][many] == 9) {
                tempMap[manx - 1][many] = 5;
                if (direction == 41) {
                    //没推箱子
                    if (map[manx][many] == 4) {
                        tempMap[manx][many] = 4;
                    } else {
                        tempMap[manx][many] = 2;
                    }
                } else {
                    //推了箱子
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        tempMap[manx][many] = 9;
                    } else {
                        tempMap[manx][many] = 3;
                    }
                    if (map[manx + 1][many] == 4 || map[manx + 1][many] == 9) {
                        tempMap[manx + 1][many] = 4;
                    } else {
                        tempMap[manx + 1][many] = 2;
                    }
                }
                manx--;
            } else {
                tempMap[manx - 1][many] = 5;
                if (map[manx][many] == 4) {
                    tempMap[manx][many] = 4;
                } else {
                    tempMap[manx][many] = 2;
                }
                manx--;
            }
        }
    }

    /**
     * //悔一步
     */
    public void back() {
        if (step.isEmpty()) {
            return;
        }
        int st = step.pop();
        switch (st) {
            //左
            case 1:
            case 11:
                backLeft(st);
                break;
            //右
            case 3:
            case 31:
                backRight(st);
                break;
            //上
            case 2:
            case 21:
                backUp(st);
                break;
            //下
            case 4:
            case 41:
                backDown(st);
                break;
            default:
                break;
        }
        repaint();
    }
}
