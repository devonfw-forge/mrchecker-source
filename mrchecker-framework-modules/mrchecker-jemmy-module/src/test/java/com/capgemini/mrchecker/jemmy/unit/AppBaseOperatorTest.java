package com.capgemini.mrchecker.jemmy.unit;

import com.capgemini.mrchecker.jemmy.AppBaseOperator;
import com.capgemini.mrchecker.jemmy.tags.IntegrationTest;
import com.capgemini.mrchecker.test.core.TestExecutionObserver;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@IntegrationTest
class AppBaseOperatorTest {

	private static CalculatorPageApp sut = getSut();

	@Test
	public void shouldCreatePageInstance() {
		//TODO: jak ma byc otwierany test?
		sut.clear();
		assertThat(sut, is(notNullValue()));
	}

	@Test
	public void shouldInteractWithControls() throws InterruptedException {
		sut.clear();
		assertThat(sut.addTwoNumbers(1, 2), is(equalTo(3.0)));
	}

	public static CalculatorPageApp getSut() {
		return PageFactory.getPageInstance(CalculatorPageApp.class);
	}

	public static class CalculatorPageApp extends AppBaseOperator {

		public CalculatorPageApp() throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {
			super("com.capgemini.mrchecker.jemmy.unit.CalculatorSwingApp", "Calculator");
		}

		public double addTwoNumbers(int first, int second) {
			findJButtonByText("" + first).clickMouse();
			findJButtonByText("+").clickMouse();
			findJButtonByText("" + second).clickMouse();
			findJButtonByText("=").clickMouse();
			return Double.parseDouble(new JTextFieldOperator(getFrame(), 0).getText());
		}

		public void clear() {
			findJButtonByText("C").clickMouse();
		}

		private JButtonOperator findJButtonByText(String text) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			return new JButtonOperator(getFrame(), text);
		}
	}

	@AfterAll
	public static void cleanUpClass() {
		sut.onTestClassFinish();
		TestExecutionObserver.getInstance().removeObserver(sut);
	}
}