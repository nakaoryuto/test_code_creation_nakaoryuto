package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
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
		// TODO ここに追加
		goTo("http://localhost:8080/lms");

		// ログインボタンが表示されるまで待機
		By loginButton = By.xpath("//button[normalize-space()='ログイン'] | //input[@type='submit' and @value='ログイン']");
		visibilityTimeout(loginButton, 5);

		// タイトル（ブラウザのタイトル）確認後ログイン
		assertEquals("ログイン", webDriver.getTitle());

		// ログインボタン文字確認後ログイン
		WebElement btn = webDriver.findElement(loginButton);
		String btnText = btn.getText();
		if (btnText == null || btnText.isBlank()) {
			btnText = btn.getAttribute("value");
		}
		assertEquals("ログイン", btnText.trim());

		// スクリーンショットをEvidenceファイルに保存
		getEvidence(new Object() {
		}, "loginView");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms");

		// 入力欄（画面設計書のパラメータ名に従い name を使用）
		By loginId = By.name("loginId");
		By password = By.name("password");
		visibilityTimeout(loginId, 5);

		webDriver.findElement(loginId).clear();
		webDriver.findElement(loginId).sendKeys("StudentZZ99");

		webDriver.findElement(password).clear();
		webDriver.findElement(password).sendKeys("StudentZZ99");

		// ログインボタン押下
		By loginButton = By.xpath("//button[normalize-space()='ログイン'] | //input[@type='submit' and @value='ログイン']");
		webDriver.findElement(loginButton).click();

		// エラーメッセージ表示確認
		By errorMsg = By.xpath("//*[contains(normalize-space(),'ログインに失敗しました')]");
		visibilityTimeout(errorMsg, 5);
		assertTrue(webDriver.findElement(errorMsg).isDisplayed());

		// スクリーンショットをEvidenceファイルに保存
		getEvidence(new Object() {
		}, "authFail");
	}

}