import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class Calculator extends JFrame {
    private final JTextField display = new JTextField("0");
    private double storedValue;
    private String pendingOperation;
    private boolean startNewNumber = true;

    public Calculator() {
        super("Калькулятор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6, 6));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
        add(display, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] labels = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "C", "0", "=", "+"};
        for (String label : labels) {
            JButton button = new JButton(label);
            button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            button.addActionListener(event -> handle(label));
            buttons.add(button);
        }
        add(buttons, BorderLayout.CENTER);
    }

    private void handle(String key) {
        if (key.matches("\\d")) {
            if (startNewNumber || display.getText().equals("Ошибка")) display.setText(key);
            else display.setText(display.getText().equals("0") ? key : display.getText() + key);
            startNewNumber = false;
        } else if (key.equals("C")) {
            display.setText("0"); storedValue = 0; pendingOperation = null; startNewNumber = true;
        } else if (key.equals("=")) {
            if (pendingOperation != null) { calculate(); pendingOperation = null; startNewNumber = true; }
        } else {
            if (pendingOperation != null && !startNewNumber) calculate();
            try { storedValue = Double.parseDouble(display.getText()); pendingOperation = key; startNewNumber = true; }
            catch (NumberFormatException ignored) { }
        }
    }

    private void calculate() {
        double current;
        try { current = Double.parseDouble(display.getText()); }
        catch (NumberFormatException e) { return; }
        double result;
        switch (pendingOperation) {
            case "+": result = storedValue + current; break;
            case "-": result = storedValue - current; break;
            case "*": result = storedValue * current; break;
            case "/":
                if (current == 0) { display.setText("Ошибка"); pendingOperation = null; return; }
                result = storedValue / current; break;
            default: return;
        }
        display.setText(Double.toString(result));
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new Calculator().setVisible(true)); }
}
