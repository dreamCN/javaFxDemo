package com.wlxk.fx.sokoban.gui;

import com.wlxk.fx.sokoban.constant.GameMap;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    int level;
    /**
     * temp_map为临时此位置     map为原地图上此位置
     */
    int temp_map[][], map[][];
    /**
     * 保存人的当前坐标     manx即为y坐标    many为x坐标
     */
    int manx, many;
    public final static int MAXLEVEL = 5;
    Stack<Integer> step;
    //保存每一步操作的方向
    /* LEFT = 1(推了箱子 = 1;没推箱子 = 11)
     * RIGHT = 3(推了箱子 = 3;没推箱子 = 31)
     * UP = 2(推了箱子 = 2;没推箱子 = 21)
     * DOWN = 4(推了箱子 = 4;没推箱子 = 41)
     *
     */
    Map<String, Label> labelMap;

    @FXML
    private void testClick(Event event) {
        step = new Stack<Integer>();
        init(1);
    }

    @FXML
    private void resetClick(Event event) {
        step = new Stack<Integer>();
        init(level);
    }

    private Label getLabel(String text) {
        Label label = new Label();
        label.setMinHeight(20);
        label.setMinWidth(20);
//        label.setText(text);
        label.getStyleClass().add("label_" + text);
        return label;
    }

    private void init(int level) {
        this.level = level;
        switch (level) {
            case 1:
                copydata(GameMap.LEAVE_1);
                break;
            default:
                copydata(GameMap.LEAVE_1);
                break;
        }
        step.clear(); //初始化
        labelMap = new HashMap<>();
        repaint();
    }

    //拷贝数据
    void copydata(int data[][]) {
        map = data;
        temp_map = new int[20][20];
        manx = many = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                temp_map[i][j] = data[i][j];
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

        }
        /*if(isWin()) {
            if(level==MAXLEVEL)
            {
                JOptionPane.showMessageDialog(null, "！！！祝贺您通过最后一关！！！");
                return;
            }
            else
            {
                int choice=0;
                choice=JOptionPane.showConfirmDialog(null,"恭喜您通过第"+level+"关!\n是否要进入下一关？","过关",JOptionPane.YES_NO_OPTION);
                if(choice==0)
                {
                    Sel("Next"); //进入下一关
                }
                if(choice==1);
                {
                    return;
                }
            }
        }*/
    }

    private void repaint() {
        for (int i = 0; i < temp_map.length; i++) {
            for (int j = 0; j < temp_map[i].length; j++) {
                String mapKey = i + "_" + j;
                if (!labelMap.containsKey(mapKey)) {
                    Label label = getLabel(String.valueOf(temp_map[i][j]));
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
                    if (!String.valueOf(temp_map[i][j]).equals(classNum)) {
                        label.getStyleClass().remove(1);
                        label.getStyleClass().add("label_" + temp_map[i][j]);
//                        label.setText(String.valueOf(temp_map[i][j]));
                    }
                }
            }
        }
    }

    public boolean isWin() {
        boolean bok = true;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((map[i][j] == 4 || map[i][j] == 9) && temp_map[i][j] != 9) {
                    bok = false;
                }
            }
        }
        return bok;
    }

    //判断人原来的地方是草地或放箱子的地方
    public void manoldPos() {
        if (map[manx][many] == 4 || map[manx][many] == 9) {
            temp_map[manx][many] = 4;
        } else {
            temp_map[manx][many] = 2;
        }
    }

    public void goLeft() {
        //左边有箱子
        if (temp_map[manx][many - 1] == 3 || temp_map[manx][many - 1] == 9) {
            //左边第二个位置没有箱子
            if (temp_map[manx][many - 2] == 2) {
                temp_map[manx][many - 2] = 3;
                temp_map[manx][many - 1] = 6;
                manoldPos();
                many--;
                step.push(1);  //左走
            } else if (temp_map[manx][many - 2] == 4) {
                temp_map[manx][many - 2] = 9;
                temp_map[manx][many - 1] = 6;
                manoldPos();
                many--;
                step.push(1);  //左走
            }
        } else if (temp_map[manx][many - 1] == 2 || temp_map[manx][many - 1] == 4)//左边是草地或者目标位置
        {
            temp_map[manx][many - 1] = 6;
            manoldPos();
            many--;
            step.push(11);  //左走
        }
    }

    public void goRight() {
        //右边有箱子
        if (temp_map[manx][many + 1] == 3 || temp_map[manx][many + 1] == 9) {
            //右边第二个位置没有箱子
            if (temp_map[manx][many + 2] == 2) {
                temp_map[manx][many + 2] = 3;
                temp_map[manx][many + 1] = 7;
                manoldPos();
                many++;
                step.push(3);  //右走
            } else if (temp_map[manx][many + 2] == 4) {
                temp_map[manx][many + 2] = 9;
                temp_map[manx][many + 1] = 7;
                manoldPos();
                many++;
                step.push(3);  //右走
            }
        } else if (temp_map[manx][many + 1] == 2 || temp_map[manx][many + 1] == 4)//右边是草地
        {
            temp_map[manx][many + 1] = 7;
            manoldPos();
            many++;
            step.push(31);  //右走
        }
    }

    public void goUp() {
        //上边有箱子
        if (temp_map[manx - 1][many] == 3 || temp_map[manx - 1][many] == 9) {
            //上边第二个位置没有箱子
            if (temp_map[manx - 2][many] == 2) {
                temp_map[manx - 2][many] = 3;
                temp_map[manx - 1][many] = 8;
                manoldPos();
                manx--;
                step.push(2);  //上走
            } else if (temp_map[manx - 2][many] == 4) {
                temp_map[manx - 2][many] = 9;
                temp_map[manx - 1][many] = 8;
                manoldPos();
                manx--;
                step.push(2);  //上走
            }
        } else if (temp_map[manx - 1][many] == 2 || temp_map[manx - 1][many] == 4)//上边是草地
        {
            temp_map[manx - 1][many] = 8;
            manoldPos();
            manx--;
            step.push(21);  //上走
        }
    }

    public void goDown() {
        //上边有箱子
        if (temp_map[manx + 1][many] == 3 || temp_map[manx + 1][many] == 9) {
            //上边第二个位置没有箱子
            if (temp_map[manx + 2][many] == 2) {
                temp_map[manx + 2][many] = 3;
                temp_map[manx + 1][many] = 5;
                manoldPos();
                manx++;
                step.push(4);  //下走
            } else if (temp_map[manx + 2][many] == 4) {
                temp_map[manx + 2][many] = 9;
                temp_map[manx + 1][many] = 5;
                manoldPos();
                manx++;
                step.push(4);  //下走
            }
        } else if (temp_map[manx + 1][many] == 2 || temp_map[manx + 1][many] == 4)//上边是草地
        {
            temp_map[manx + 1][many] = 5;
            manoldPos();
            manx++;
            step.push(41);  //下走
        }
    }

    //往左悔一步
    public void backLeft(int direction) {
        //人的后面能否走
        if (temp_map[manx][many + 1] == 2 || temp_map[manx][many + 1] == 4) {
            //人的前面是否有箱子
            if (temp_map[manx][many - 1] == 3 || temp_map[manx][many - 1] == 9) {
                temp_map[manx][many + 1] = 6;
                if (direction == 11)  //没推箱子
                {
                    if (map[manx][many] == 4) {
                        temp_map[manx][many] = 4;
                    } else {
                        temp_map[manx][many] = 2;
                    }
                } else  //推了箱子
                {
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        temp_map[manx][many] = 9;
                    } else {
                        temp_map[manx][many] = 3;
                    }
                    if (map[manx][many - 1] == 4 || map[manx][many - 1] == 9) {
                        temp_map[manx][many - 1] = 4;
                    } else {
                        temp_map[manx][many - 1] = 2;
                    }
                }
                many++;
            } else {
                temp_map[manx][many + 1] = 6;
                if (map[manx][many] == 4) {
                    temp_map[manx][many] = 4;
                } else {
                    temp_map[manx][many] = 2;
                }
                many++;
            }
        }
    }

    //往右悔一步
    public void backRight(int direction) {
        //人的后面能否走
        if (temp_map[manx][many - 1] == 2 || temp_map[manx][many - 1] == 4) {
            //人的前面是否有箱子
            if (temp_map[manx][many + 1] == 3 || temp_map[manx][many + 1] == 9) {
                temp_map[manx][many - 1] = 7;
                if (direction == 31)  //没推箱子
                {
                    if (map[manx][many] == 4) {
                        temp_map[manx][many] = 4;
                    } else {
                        temp_map[manx][many] = 2;
                    }
                } else  //推了箱子
                {
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        temp_map[manx][many] = 9;
                    } else {
                        temp_map[manx][many] = 3;
                    }
                    if (map[manx][many + 1] == 4 || map[manx][many + 1] == 9) {
                        temp_map[manx][many + 1] = 4;
                    } else {
                        temp_map[manx][many + 1] = 2;
                    }
                }
                many--;
            } else {
                temp_map[manx][many - 1] = 7;
                if (map[manx][many] == 4) {
                    temp_map[manx][many] = 4;
                } else {
                    temp_map[manx][many] = 2;
                }
                many--;
            }
        }
    }

    //往上悔一步
    public void backUp(int direction) {
        //人的后面能否走
        if (temp_map[manx + 1][many] == 2 || temp_map[manx + 1][many] == 4) {
            //人的前面是否有箱子
            if (temp_map[manx - 1][many] == 3 || temp_map[manx - 1][many] == 9) {
                temp_map[manx + 1][many] = 8;
                if (direction == 21)  //没推箱子
                {
                    if (map[manx][many] == 4) {
                        temp_map[manx][many] = 4;
                    } else {
                        temp_map[manx][many] = 2;
                    }
                } else  //推了箱子
                {
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        temp_map[manx][many] = 9;
                    } else {
                        temp_map[manx][many] = 3;
                    }
                    if (map[manx - 1][many] == 4 || map[manx - 1][many] == 9) {
                        temp_map[manx - 1][many] = 4;
                    } else {
                        temp_map[manx - 1][many] = 2;
                    }
                }
                manx++;
            } else {
                temp_map[manx + 1][many] = 8;
                if (map[manx][many] == 4) {
                    temp_map[manx][many] = 4;
                } else {
                    temp_map[manx][many] = 2;
                }
                manx++;
            }
        }
    }

    //往下悔一步
    public void backDown(int direction) {
        //人的后面能否走
        if (temp_map[manx - 1][many] == 2 || temp_map[manx - 1][many] == 4) {
            //人的前面是否有箱子
            if (temp_map[manx + 1][many] == 3 || temp_map[manx + 1][many] == 9) {
                temp_map[manx - 1][many] = 5;
                if (direction == 41)  //没推箱子
                {
                    if (map[manx][many] == 4) {
                        temp_map[manx][many] = 4;
                    } else {
                        temp_map[manx][many] = 2;
                    }
                } else  //推了箱子
                {
                    if (map[manx][many] == 4 || map[manx][many] == 9) {
                        temp_map[manx][many] = 9;
                    } else {
                        temp_map[manx][many] = 3;
                    }
                    if (map[manx + 1][many] == 4 || map[manx + 1][many] == 9) {
                        temp_map[manx + 1][many] = 4;
                    } else {
                        temp_map[manx + 1][many] = 2;
                    }
                }
                manx--;
            } else {
                temp_map[manx - 1][many] = 5;
                if (map[manx][many] == 4) {
                    temp_map[manx][many] = 4;
                } else {
                    temp_map[manx][many] = 2;
                }
                manx--;
            }
        }
    }

    public void back()  //悔一步
    {
        if (step.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "您还没有进行移动，不能悔一步!","提示", JOptionPane.WARNING_MESSAGE);
//            requestFocus();
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
