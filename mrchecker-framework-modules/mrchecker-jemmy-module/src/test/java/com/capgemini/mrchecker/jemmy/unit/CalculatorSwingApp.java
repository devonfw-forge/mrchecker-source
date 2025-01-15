package com.capgemini.mrchecker.jemmy.unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorSwingApp extends JFrame implements ActionListener {
	private JTextField display;
	private double firstNumber = 0;
	private double secondNumber = 0;
	private String operator = "";

	public CalculatorSwingApp() {
		setTitle("Calculator");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		display = new JTextField();
		display.setEditable(false);
		display.setFont(new Font("Arial", Font.BOLD, 24));
		add(display, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 4));

		String[] buttons = {
				"7", "8", "9", "/",
				"4", "5", "6", "*",
				"1", "2", "3", "-",
				"0", "=", "C", "+"
		};

		for (String text : buttons) {
			JButton button = new JButton(text);
			button.setFont(new Font("Arial", Font.BOLD, 24));
			button.addActionListener(this);
			panel.add(button);
		}

		add(panel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.equals(".")) {
			display.setText(display.getText() + command);
		} else if (command.equals("=")) {
			secondNumber = Double.parseDouble(display.getText());
			switch (operator) {
			case "+":
				display.setText(String.valueOf(firstNumber + secondNumber));
				break;
			case "-":
				display.setText(String.valueOf(firstNumber - secondNumber));
				break;
			case "*":
				display.setText(String.valueOf(firstNumber * secondNumber));
				break;
			case "/":
				display.setText(String.valueOf(firstNumber / secondNumber));
				break;
			}
		} else if (command.equals("C")) {
			display.setText("");
		} else {
			if (!display.getText().isEmpty()) {
				firstNumber = Double.parseDouble(display.getText());
				display.setText("");
				operator = command;
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CalculatorSwingApp calculator = new CalculatorSwingApp();
			calculator.setVisible(true);
		});
	}

}

