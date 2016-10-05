package laurentesp.calculator;

/**
 * Created by SOEOSSA on 04/10/2016.
 */

public class Calculator {

    // The 2 attributes valOperandX are used to keep the value of the operands in case of a change in an activity
    private static StringBuffer valOperand1 = new StringBuffer("0");
    private static StringBuffer valOperand2 = new StringBuffer("0");

    // The attribute operator is used to keep the value of the operator in case of a change in a activity
    private static StringBuffer operator = new StringBuffer("");

    // The attribute curOperand is used to determine during the calculation if the user is entering the first or the second operand
    private static int curOperand = 0;

    // This attribute is used to clear the StringBuffer for the display after the choice of an operator as soon as a new operand is written
    private static boolean takeStringIn = true;

    // This attribute is used to determine if an operator has already been chosen
    private static boolean operatorJustChosen = false;

    // Attribute used to clear the calculation after an equals if no operator chosen just after the equals
    private static boolean equalsWaitingForNewOperator = false;

    private static final String errorString = "Error can't divide by zero";

    static String getValOutToShowtoUser(String stringIn, String stringInTextView) {
        StringBuffer stringBufOut = new StringBuffer("");
        switch (stringIn) {
            // The "C" button is used to clear the 2 operands and the operator
            case "C":
                clearOperator();
                stringBufOut.setLength(0);
                stringBufOut.append("0");
                break;

            case "+":
                stringBufOut = prepareOperator(stringInTextView, "addFunction");
                break;

            case "-":
                stringBufOut = prepareOperator(stringInTextView, "minusFunction");
                break;

            case "x":
                stringBufOut = prepareOperator(stringInTextView, "multFunction");
                break;

            case "/":
                stringBufOut = prepareOperator(stringInTextView, "divFunction");
                break;

            case "=":
                curOperand = 0;
                stringBufOut.setLength(0);
                if (valOperand2.length() == 0) {
                    //In case of the user clicks on an operator and clicks on equal just after, we have to clone the first operand
                    valOperand2 = valOperand1;
                }
                stringBufOut.append(getResultFromOperatorOnOperands(valOperand1.toString(), valOperand2.toString(), operator.toString()));
                valOperand1 = stringBufOut;
                equalsWaitingForNewOperator = true;
                break;

            case ".":
                // If there is no character already typed before a point we have to put a 0 before the point
                if ((stringInTextView.equals("0")) || (equalsWaitingForNewOperator)) {
                    stringBufOut.append("0");
                    stringBufOut.append(stringIn);
                    equalsWaitingForNewOperator = false;
                } else {
                    // There can't be two points in a Double
                    if (!(stringInTextView.contains("."))) {
                        stringBufOut.append(stringInTextView);
                        stringBufOut.append(stringIn);
                    } else {
                        stringBufOut.append(stringInTextView);
                    }
                }
                break;

            default:
                // The default case is for 0..9 buttons
                // Requirement, if a 0 has been entered previously we won't show it
                // If flag takeStringIn is false, this means that we should not show the stringIn
                if ((stringInTextView.equals("0")) || (!(takeStringIn)) || (equalsWaitingForNewOperator)) {
                    stringBufOut.append(stringIn);
                    equalsWaitingForNewOperator = false;
                } else {
                    stringBufOut.append(stringInTextView);
                    stringBufOut.append(stringIn);
                }

                takeStringIn = true;
                operatorJustChosen = false;

                if (curOperand == 0) {
                    valOperand1 = stringBufOut;
                } else {
                    valOperand2 = stringBufOut;
                }
        }

        return stringBufOut.toString();
    }

    static String getLastOperand() {
        StringBuffer stringBufOut = new StringBuffer("");
        if (curOperand == 0) {
            stringBufOut = valOperand1;
        } else {
            stringBufOut = valOperand2;
        }

        return stringBufOut.toString();
    }

    static String getResultFromOperatorOnOperands(String operand1, String operand2, String operator) {
        double valOperand1;
        double valOperand2;
        double valOut = 0;
        boolean errorOnOperation = false;
        String stringOut;

        if ((operand1.equals(errorString)) || (operand2.equals(errorString))) {
            errorOnOperation = true;
        } else {

            if (operand1.length() == 0) {
                valOperand1 = 0;
            } else {
                valOperand1 = Double.valueOf(operand1);
            }

            if (operand2.length() == 0) {
                valOperand2 = 0;
            } else {
                valOperand2 = Double.valueOf(operand2);
            }

            switch (operator) {
                case "addFunction":
                    valOut = valOperand1 + valOperand2;
                    break;
                case "minusFunction":
                    valOut = valOperand1 - valOperand2;
                    break;
                case "multFunction":
                    valOut = valOperand1 * valOperand2;
                    break;
                case "divFunction":
                    if (valOperand2 == 0) {
                        errorOnOperation = true;
                    } else {
                        valOut = valOperand1 / valOperand2;
                    }
                    break;
                default:
                    valOut = valOperand1;
            }
        }

        if (errorOnOperation) {
            stringOut = errorString;
        } else {
            stringOut = removeFractionalPartFromDoubleIfNotNecessary(valOut);
        }
        return stringOut;
    }

    static String removeFractionalPartFromDoubleIfNotNecessary(double valIn) {
        String valOut;
        valOut = Double.toString(valIn).replaceAll("\\.0*$", "");
        return valOut;
    }


    private static StringBuffer prepareOperator(String stringInTextView, String functionName) {
        StringBuffer stringBufOut = new StringBuffer("");
        // Test if we are already entered the second operand, in this case we have to show the result of the first operation
        if ((curOperand == 1) && (!(operatorJustChosen))) {
            stringBufOut.append(getResultFromOperatorOnOperands(valOperand1.toString(), valOperand2.toString(), operator.toString()));
            valOperand1 = stringBufOut;
        } else {
            stringBufOut.append(stringInTextView);
        }

        // Because we have entered an operator this means that we are entering the second operand
        curOperand = 1;

        operator.setLength(0);
        operator.append(functionName);
        operatorJustChosen = true;

        // Put this flag to false will make a reset on the display when the user will enter the next operand
        takeStringIn = false;
        return stringBufOut;
    }

    private static void clearOperator() {
        valOperand1.setLength(0);
        valOperand2.setLength(0);
        curOperand = 0; // Go Back to first operand
    }


}
