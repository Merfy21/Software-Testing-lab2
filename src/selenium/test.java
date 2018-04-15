package selenium;

import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class test {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  
 
  @Before
  public void setUp() throws Exception {
	
	  
	//System.setProperty("webdriver.firefox.bin", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
    driver = new FirefoxDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.get("https://psych.liebes.top/st"); 
 }
  
  
  public Map<String,String> readExcel() throws Exception
  {
	  Map<String,String> content = new HashMap<String,String>();
      FileInputStream fis = new FileInputStream(new File("E:\\2018\\软件测试\\input.xlsx"));
      XSSFWorkbook workbook = new XSSFWorkbook(fis);
      XSSFSheet sheet = workbook.getSheet("Sheet1");
      
      for(int i=sheet.getFirstRowNum();i<=sheet.getLastRowNum();i++){
          XSSFRow row = sheet.getRow(i);
          String cellValue[] = new String[row.getLastCellNum()];
          for(int j=row.getFirstCellNum();j<row.getLastCellNum();j++){
              XSSFCell cell = row.getCell(j);
              switch (cell.getCellType()) 
              {  
              case Cell.CELL_TYPE_STRING:  
                   cellValue[j] = cell.getStringCellValue().trim();
                   break;  
              case Cell.CELL_TYPE_NUMERIC:  
                   if(DateUtil.isCellDateFormatted(cell)) {  
                       cellValue[j] = cell.getDateCellValue().toString().trim();  
                   }else
                   {  
                      BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                      cellValue[j] = bd.toPlainString().trim();  
                   }  
                   break;  
              }
          }
          content.put(cellValue[0],cellValue[1]);
      }
      workbook.close();
      fis.close();
      return content;
  }
  
  
  @Test
  public void testUntitledTestCase() throws Exception {
	  Map<String,String> content = readExcel();
      Iterator<Entry<String, String>> iterator = content.entrySet().iterator();  
      //遍历
      while (iterator.hasNext()) {  
    	  
          Entry<String, String> entry = iterator.next();  
          String username = entry.getKey();  
          String password = username.substring(4);
          String url = entry.getValue();
          
          if(url==null) 
          {
        	  continue;
          }
            driver.get("https://psych.liebes.top/st");
            
            driver.findElement(By.id("username")).click();
            driver.findElement(By.id("username")).clear();
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).click();
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("submitButton")).click();
            
            String geturl = driver.findElement(By.xpath("//p")).getText();//xpath找元素
            System.out.println(username+" "+password+" "+url+" "+geturl);
            
            if(url.charAt(url.length()-1)=='/') url = url.substring(0, url.length()-1);
            if(geturl.charAt(geturl.length()-1)=='/') geturl = geturl.substring(0, geturl.length()-1);
            assertEquals(url, geturl);
            
      }
	}

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
