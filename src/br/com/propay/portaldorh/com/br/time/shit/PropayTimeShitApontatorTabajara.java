package br.com.propay.portaldorh.com.br.time.shit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class PropayTimeShitApontatorTabajara {

	private static WebDriver driver;
	private static final String PROPAY_PORTAL_RH = "https://propay.portaldorh.com.br/portal_taco/";
	private static final String LOGIN = "00********";	
	private static final String SENHA = "*********";	
	private static String HORA_ENTRADA = "08";
	private static String HORA_SAIDA = "17";

	public static void main(String[] args) throws InterruptedException {

		String currentDir = System.getProperty("user.dir");
		System.out.println("Current dir using System:" + currentDir);

		System.out.println("Setting webdriver for Google Chrome, find file chromedriver.exe in directory" + currentDir);
		System.setProperty("webdriver.chrome.driver", currentDir.concat("\\chromedriver.exe"));

		driver = new ChromeDriver();

		// Main Page
		System.out.println("Login in main page");
		loginMainPage(
				"Produtos/SAAA/Principal2.aspx?amb_selecionado=12&abrir_nova_janela=N&eh_mdesigner=N&nome_portal=4C47434C69444747792F4646553874357A356A4176513D3D");

		// Meus apontamentos
		System.out.println("Meus apontamentos");
		goToUrl("meus_apontamentos_3.aspx");

		// Justificativa do usuário
		System.out.println("Justificativa do usuario");
		goToUrl("Produtos/NorberMyWay2010/NorberRedirecionamento.aspx?paginaAsp=just_user/justuser.asp&dv=webponto12");
		moveToLastWindowsHandle();
		driver.switchTo().frame(0);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//a[contains(@href,'IncluirMarcacao')]")).click();
		moveToLastWindowsHandle();

		// Preenche Time Sheet
		System.out.println("Preenche o apontamento na data de hoje");
		fillTimeShit();

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
		driver.quit();
	}

	/**
	 * Login in main page
	 * 
	 * @param url
	 */
	public static void loginMainPage(String url) {
		driver.get(PROPAY_PORTAL_RH.concat(url));
		driver.findElement(By.name("txtLogin")).sendKeys(LOGIN);
		driver.findElement(By.name("txtSenha")).sendKeys(SENHA);
		driver.findElement(By.name("btnEntrar")).click();
	}

	/**
	 * Navigate to URL informed in String URL
	 * 
	 * @param url
	 */
	private static void goToUrl(String url) {
		driver.navigate().to(PROPAY_PORTAL_RH.concat(url));
	}

	/**
	 * Fill time sheet based Hora de entrada = horaEntrada Hora de saide =
	 */
	public static void fillTimeShit() {
		selectComboValue("Data", getCurrentDate());

		String randomMinute = StringUtils.leftPad(Integer.toString(generateMinuteRandom()), 2, "0");

		driver.findElement(By.name("Horario1")).sendKeys(HORA_ENTRADA.concat(randomMinute));
		driver.findElement(By.name("Horario2")).sendKeys(HORA_SAIDA.concat(randomMinute));
		selectComboValue("cmbMotivo", "Funcionário Marca Ponto Manual");
		driver.findElement(By.className("BotaoAchatado")).click();
	}

	/**
	 * Get current date in format dd/MM/YYYY
	 * 
	 * @return Date in format dd=Day, MM=Month and yyyy=Year
	 */
	public static String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	/**
	 * Get last window opened
	 */
	public static void moveToLastWindowsHandle() {
		driver.switchTo().window(
				driver.getWindowHandles().stream().skip(driver.getWindowHandles().size() - 1).findFirst().get());
	}

	/**
	 * //Select and Set combobox by name
	 * 
	 * @param elementId
	 *            combobox go get
	 * @param value
	 *            to set in combobox
	 */
	public static void selectComboValue(final String elementId, final String value) {
		final Select selectBox = new Select(driver.findElement(By.name(elementId)));
		selectBox.selectByValue(value);
	}

	public static int generateMinuteRandom() {
		return ThreadLocalRandom.current().nextInt(1, 32);
	}

}
