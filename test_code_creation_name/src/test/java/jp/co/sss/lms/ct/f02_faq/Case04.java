package jp.co.sss.lms.ct.f02_faq;

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
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 上部メニューが見える位置へ
		scrollTo("0");

		// 「機能」メニューを押してプルダウンを開く
		webDriver.findElement(
				By.xpath("//a[contains(normalize-space(.),'機能') or .//*[contains(normalize-space(.),'機能')]]")).click();

		// プルダウン内の「ヘルプ」が見えるまで待機
		visibilityTimeout(
				By.xpath("//a[contains(normalize-space(.),'ヘルプ') or .//*[contains(normalize-space(.),'ヘルプ')]]"), 10);

		// 「ヘルプ」をクリック
		webDriver.findElement(
				By.xpath("//a[contains(normalize-space(.),'ヘルプ') or .//*[contains(normalize-space(.),'ヘルプ')]]"))
				.click();

		// スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		}, "help");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 画面操作の待機用
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		// 現在のタブ（元タブ）を保持
		String base = webDriver.getWindowHandle();

		// タブ数（クリック前）を保持
		int before = webDriver.getWindowHandles().size();

		// 「よくある質問」が見えるまで待機
		visibilityTimeout(
				By.xpath("//a[contains(normalize-space(.),'よくある質問') or .//*[contains(normalize-space(.),'よくある質問')]]"),
				10);

		// 「よくある質問」をクリック（別タブで開く）
		webDriver.findElement(
				By.xpath("//a[contains(normalize-space(.),'よくある質問') or .//*[contains(normalize-space(.),'よくある質問')]]"))
				.click();

		// 別タブが開くまで待機
		wait.until(ExpectedConditions.numberOfWindowsToBe(before + 1));

		// 新しく開いたタブへ切り替え
		for (String h : webDriver.getWindowHandles()) {
			if (!h.equals(base)) {
				webDriver.switchTo().window(h);
				break;
			}
		}

		// 別タブが開けたことを確認（タブの数）
		assertEquals(before + 1, webDriver.getWindowHandles().size());

		// 新規タブを閉じる
		webDriver.close();

		// 元タブへ戻る
		webDriver.switchTo().window(base);

		// スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		}, "faq");
	}
}
