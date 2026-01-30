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
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		webDriver.findElement(By.linkText("機能")).click();

		// プルダウン内の「ヘルプ」が見えるまで待機
		visibilityTimeout(By.linkText("ヘルプ"), 10);

		// 「ヘルプ」をクリック
		webDriver.findElement(By.linkText("ヘルプ")).click();

		// ヘルプ画面へ遷移したことを待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleContains("ヘルプ"));

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

		// 「よくある質問」が見えるまで待機
		visibilityTimeout(By.linkText("よくある質問"), 10);

		// 「よくある質問」をクリック
		webDriver.findElement(By.linkText("よくある質問")).click();

		// 別タブが開くまで待機
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		// 新しく開いたタブへ切り替え
		String child = webDriver.getWindowHandles().stream().reduce((a, b) -> b).get();
		webDriver.switchTo().window(child);

		// よくある質問画面へ遷移したことをタイトルで確認
		wait.until(ExpectedConditions.titleContains("よくある質問"));

		// スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		}, "faq");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		// 画面操作の待機用
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		// よくある質問画面に遷移したことを確認
		wait.until(ExpectedConditions.titleContains("よくある質問"));

		// キーワード入力欄が表示されるまで待機
		visibilityTimeout(By.cssSelector("#form"), 10);

		// キーワード入力
		String keyword = "手続き";
		webDriver.findElement(By.cssSelector("#form")).clear();
		webDriver.findElement(By.cssSelector("#form")).sendKeys(keyword);

		// 検索ボタン押下
		webDriver.findElement(By.cssSelector("input[type='submit'][value='検索']")).click();

		// 検索結果行が表示されるまで待機
		visibilityTimeout(By.cssSelector("table.sortabletable tbody tr"), 10);

		// 表示されている質問がすべてキーワードを含むことを確認
		for (WebElement dt : webDriver.findElements(By.cssSelector("table.sortabletable tbody tr dt"))) {
			assertTrue(dt.getText().contains(keyword), dt.getText());
		}

		// スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		}, "keywordSearch");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		// キーワード入力欄が表示されるまで待機
		visibilityTimeout(By.cssSelector("#form"), 10);

		// クリアボタン押下（HTMLより value="クリア"）
		webDriver.findElement(By.cssSelector("input[type='button'][value='クリア']")).click();

		// 入力欄が空になったことを確認
		assertEquals("", webDriver.findElement(By.cssSelector("#form")).getAttribute("value"));

		// スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		}, "clear");
	}
}
