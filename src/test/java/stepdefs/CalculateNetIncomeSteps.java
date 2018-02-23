package stepdefs;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.paulhammant.ngwebdriver.ByAngular;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pojos.Expense;
import pojos.Income;
import utils.ConfigUtil;
import utils.SeleniumUtil;

public class CalculateNetIncomeSteps {
	private WebDriver driver = null;

	// PicoContainer injects ServiceHook class
	public CalculateNetIncomeSteps(ServiceHook serviceHook) {
		this.driver = serviceHook.getDriver();
	}

	@Given("^I Am At The Home Page$")
	public void I_Am_At_The_Home_Page() throws Throwable {
		driver.get(ConfigUtil.readKey("base_url"));
		driver.manage().window().maximize();
		SeleniumUtil.captureScreen(driver);
	}

	@When("^I Enter My Start Balance: (.*)$")
	public void I_Enter_My_Start_Balance(String startBalance) throws Throwable {
		By startBalanceBy = ByAngular.model("startBalance");
		SeleniumUtil.enterInField(driver, startBalanceBy, startBalance);
		SeleniumUtil.captureScreen(driver);
	}

	@When("^I Click Add Regular Income: (\\d+)$")
	public void I_Click_Add_Regular_Income(int incomeCount) throws Throwable {
		if (0 >= incomeCount) {
			incomeCount = 1;
		}

		By addIncomeBy = By.cssSelector("button[ng-click='addIncome();']");
		for (int ctr = 0; ctr < incomeCount - 1; ctr++) {
			SeleniumUtil.clickElement(driver, addIncomeBy);
		}

		SeleniumUtil.captureScreen(driver);
	}

	@When("^I Enter My Regular Income Sources$")
	public void I_Enter_My_Regular_Income_Sources(DataTable dataTable) throws Throwable {
		List<Income> incomeList = dataTable.asList(Income.class);

		By nameFieldBy = ByAngular.model("income.name");
		List<WebElement> nameFields = driver.findElements(nameFieldBy);

		By amountFieldBy = ByAngular.model("income.amount");
		List<WebElement> amountFields = driver.findElements(amountFieldBy);

		By freqDropMenuBy = ByAngular.model("income.frequency");
		List<Select> freqDropMenus = SeleniumUtil.getSelectElements(driver, freqDropMenuBy);

		for (int ctr = 0; ctr < incomeList.size(); ctr++) {
			SeleniumUtil.enterInField(driver, nameFields.get(ctr), incomeList.get(ctr).getName());
			SeleniumUtil.enterInField(driver, amountFields.get(ctr), incomeList.get(ctr).getAmount());
			SeleniumUtil.selectFromDropMenuByText(driver, freqDropMenus.get(ctr), incomeList.get(ctr).getFrequency());
		}

		SeleniumUtil.captureScreen(driver);
	}

	@When("^I Click Add Regular Expenses: (\\d+)$")
	public void I_Click_Add_Regular_Expenses(int expenseCount) throws Throwable {
		if (0 >= expenseCount) {
			expenseCount = 1;
		}

		By addExpenseBy = By.cssSelector("button[ng-click='addExpense();']");
		for (int ctr = 0; ctr < expenseCount - 1; ctr++) {
			SeleniumUtil.clickElement(driver, addExpenseBy);
		}

		SeleniumUtil.captureScreen(driver);
	}

	@When("^I Enter My Regular Expenses$")
	public void I_Enter_My_Regular_Expenses(DataTable dataTable) throws Throwable {
		List<Expense> expenseList = dataTable.asList(Expense.class);

		By nameFieldBy = ByAngular.model("expense.name");
		List<WebElement> nameFields = driver.findElements(nameFieldBy);

		By amountFieldBy = ByAngular.model("expense.amount");
		List<WebElement> amountFields = driver.findElements(amountFieldBy);

		By freqDropMenuBy = ByAngular.model("expense.frequency");
		List<Select> freqDropMenus = SeleniumUtil.getSelectElements(driver, freqDropMenuBy);

		for (int ctr = 0; ctr < expenseList.size(); ctr++) {
			SeleniumUtil.enterInField(driver, nameFields.get(ctr), expenseList.get(ctr).getName());
			SeleniumUtil.enterInField(driver, amountFields.get(ctr), expenseList.get(ctr).getAmount());
			SeleniumUtil.selectFromDropMenuByText(driver, freqDropMenus.get(ctr), expenseList.get(ctr).getFrequency());
		}

		SeleniumUtil.captureScreen(driver);
	}

	@Then("^I Should See Net Income Per Month: (.*)$")
	public void I_Should_See_Net_Income_Per_Month(String netIncomePerMonth) throws Throwable {
		By netIncomePerMonthBy = ByAngular.binding("roundDown(monthlyNet())");
		String netIncomePerMonthText = driver.findElement(netIncomePerMonthBy).getText();
		Assert.assertEquals(netIncomePerMonth, netIncomePerMonthText);
	}

	@Then("^I Should See Net Income Per Year: (.*)$")
	public void I_Should_See_Net_Income_Per_Year(String netIncomePerYear) throws Throwable {
		By netIncomePerYearBy = ByAngular.binding("roundDown(monthlyNet()*12)+tallyTransactions()");
		String netIncomePerYearText = driver.findElement(netIncomePerYearBy).getText();
		Assert.assertEquals(netIncomePerYear, netIncomePerYearText);
		SeleniumUtil.scrollVertical(driver, 500);
		SeleniumUtil.captureScreen(driver);
	}

}
