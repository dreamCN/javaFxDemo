package com.wlxk.fx.calculator;

import com.wlxk.fx.calculator.constant.ButtonEnum;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author: 鼠行止
 * @date: 2020/1/6
 */
public class PrimaryController {

    @FXML
    private Label showText;
    @FXML
    private Label formulaText;

    private String formulaStr = "";

    private String text = "0";

    private ButtonEnum preButton;

    private BigDecimal total = new BigDecimal(0);

    @FXML
    private void buttonOnClick(Event event) throws IOException {
        Button target = (Button) event.getTarget();
        ButtonEnum buttonEnum = ButtonEnum.getEnumById(target.getId());
        if (null == buttonEnum) {
            return;
        }
        try {
            switch (buttonEnum) {
                case C:
                case CE:
                    formulaStr = "";
                    text = "0";
                    total = new BigDecimal(0);
                    showText.setText(text);
                    formulaText.setText(formulaStr);
                    break;
                case ZERO:
                case ONE:
                case TWO:
                case THREE:
                case FOUR:
                case FIVE:
                case SIX:
                case SEVEN:
                case EIGHT:
                case NINE:
                    numButtonClick(buttonEnum.getValue());
                    break;
                case DOT:
                    dotButtonClick(buttonEnum.getValue());
                    break;
                case ADD:
                case SUBTRACT:
                case MULTIPLY:
                case DIVIDE:
                    calculateButtonClick(buttonEnum.getValue(), buttonEnum);
                    break;
                case EQUAL:
                    equalButtonClick();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            formulaStr = "";
            text = "0";
            preButton = null;
            total = new BigDecimal(0);
            showText.setText(e.getMessage());
            formulaText.setText("");
        }

    }

    private void numButtonClick(String str) {
        if ("0".equals(text)) {
            text = str;
        } else {
            text += str;
        }
        showText.setText(text);
    }

    private void dotButtonClick(String str) {
        if (text.contains(".")) {
            return;
        }
        text += str;
        showText.setText(text);
    }

    private void calculateButtonClick(String str, ButtonEnum buttonEnum) {
        if (null != preButton) {
            preButtonCalculate();
            showText.setText(total.toString());
        } else {
            total = new BigDecimal(text);
        }
        formulaStr = formulaStr + text + str;
        preButton = buttonEnum;
        text = "0";
        formulaText.setText(formulaStr);
    }

    private void equalButtonClick() {
        if (null != preButton) {
            preButtonCalculate();
        }
        showText.setText(total.toString());
        formulaStr = "";
        text = "0";
        formulaText.setText(formulaStr);
    }

    private void preButtonCalculate() {
        switch (preButton) {
            case ADD:
                total = total.add(new BigDecimal(text));
                break;
            case SUBTRACT:
                total = total.subtract(new BigDecimal(text));
                break;
            case DIVIDE:
                if ("0".equals(text)) {
                    throw new RuntimeException("除数不能为零");
                }
                total = total.divide(new BigDecimal(text));
                break;
            case MULTIPLY:
                total = total.multiply(new BigDecimal(text));
                break;
            default:
                break;
        }
        preButton = null;
    }
}
