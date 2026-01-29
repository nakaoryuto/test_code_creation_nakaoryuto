package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
		pageLoadTimeout(30);
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms");

		// ログインボタンが表示されるまで待機
		visibilityTimeout(By.xpath("//input[@type='submit']"), 5);

		// タイトル確認
		assertEquals("ログイン | LMS", webDriver.getTitle());

		// ログインボタン確認
		WebElement btn = webDriver.findElement(By.xpath("//input[@type='submit']"));
		assertEquals("ログイン", btn.getAttribute("value"));

		// スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		}, "loginView");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// ログインID入力欄が表示されるまで待機
		visibilityTimeout(By.name("loginId"), 5);

		webDriver.findElement(By.name("loginId")).clear();
		webDriver.findElement(By.name("loginId")).sendKeys("StudentAA01");

		webDriver.findElement(By.name("password")).clear();
		webDriver.findElement(By.name("password")).sendKeys("StudentAA001");

		// スクリーンショットをevidenceフォルダに保存（入力後、ログイン押下前）
		getEvidence(new Object() {
		}, "beforeLogin");

		// ログイン押下
		webDriver.findElement(By.cssSelector("input[type='submit'], button[type='submit']")).click();

		// 遷移が完了してタイトルが変わるまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("ログイン | LMS")));

		// コース詳細の表示か確認
		assertTrue(webDriver.getTitle().contains("コース詳細 | LMS"), webDriver.getTitle());

		// スクリーンショットをevidenceフォルダに保存（遷移後）
		getEvidence(new Object() {
		}, "loginSuccess");
	}

}
