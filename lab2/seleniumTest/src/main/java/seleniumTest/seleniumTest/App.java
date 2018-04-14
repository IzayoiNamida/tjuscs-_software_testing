package seleniumTest.seleniumTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
/*
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
*/

public class App {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://psych.liebes.top/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testSelenium() throws Exception {
	  
	  Workbook wb =null;
      Sheet sheet = null;
      Row row = null;
      List<Map<String,String>> list = null;
      String cellData = null;
      String filePath = "/Users/izayoi/Desktop/input.xlsx";
      String columns[] = {"name","git"};
      wb = readExcel(filePath);
      if(wb != null){
          //用来存放表中数据
          list = new ArrayList<Map<String,String>>();
          //获取第一个sheet
          sheet = wb.getSheetAt(0);
          //获取最大行数
          int rownum = sheet.getPhysicalNumberOfRows();
          //获取第一行
          row = sheet.getRow(0);
          //获取最大列数
          int colnum = row.getPhysicalNumberOfCells();
          for (int i = 1; i<rownum; i++) {
              Map<String,String> map = new LinkedHashMap<String,String>();
              row = sheet.getRow(i);
              if(row !=null){
                  for (int j=0;j<colnum;j++){
                      cellData = getStringVal(row.getCell(j));
                      //System.out.println(cellData.trim());
                      map.put(columns[j], cellData.trim());
                  }
              }else{
                  break;
              }
              list.add(map);
          }
      }
      //遍历解析出来的list
      for (Map<String,String> map : list) {
    	  String username = "";
    	  String giturl = "";
    	  String password = "";
    	  int flag = 0;
          for (Entry<String,String> entry : map.entrySet()) {
        	  if(flag == 0)
        	  {
        		  username = entry.getValue();
        		  flag++;
        	  }
        	  else
        	  {
        		  giturl = entry.getValue();
        		  System.out.println(giturl);
        		  flag--;
        	  }
              
          }
          password = username.substring(4);
          System.out.print(username + " " + password);
          driver.get(baseUrl + "/st");
          driver.findElement(By.id("username")).clear();
          driver.findElement(By.id("username")).sendKeys(username);
          driver.findElement(By.id("password")).clear();
          driver.findElement(By.id("password")).sendKeys(password);
          driver.findElement(By.id("submitButton")).click();
          try {
              assertEquals(giturl, driver.findElement(By.xpath("//p")).getText());
        	  System.out.println(giturl);
            } catch (Error e) {
              verificationErrors.append(e.toString());
            }
          driver.findElement(By.cssSelector("p.login-box-msg")).click();
          System.out.println();
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

  public static Workbook readExcel(String filePath){
      Workbook wb = null;
      if(filePath==null){
          return null;
      }
      String extString = filePath.substring(filePath.lastIndexOf("."));
      InputStream is = null;
      try {
          is = new FileInputStream(filePath);
          if(".xls".equals(extString)){
              return wb = new HSSFWorkbook(is);
          }else if(".xlsx".equals(extString)){
              return wb = new XSSFWorkbook(is);
          }else{
              return wb = null;
          }
          
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
      return wb;
  }
  public static Object getCellFormatValue(Cell cell){
      Object cellValue = null;
      if(cell!=null){
          //判断cell类型
          switch(cell.getCellType()){
          case Cell.CELL_TYPE_NUMERIC:{
              cellValue = String.valueOf(cell.getNumericCellValue());
              break;
          }
          case Cell.CELL_TYPE_FORMULA:{
              //判断cell是否为日期格式
              if(DateUtil.isCellDateFormatted(cell)){
                  //转换为日期格式YYYY-mm-dd
                  cellValue = cell.getDateCellValue();
              }else{
                  //数字
                  cellValue = String.valueOf(cell.getNumericCellValue());
              }
              break;
          }
          case Cell.CELL_TYPE_STRING:{
              cellValue = cell.getRichStringCellValue().getString();
              break;
          }
          default:
              cellValue = "";
          }
      }else{
          cellValue = "";
      }
      return cellValue;
  }
  
  private static String getStringVal(Cell cell) {
      switch (cell.getCellType()) {
          case Cell.CELL_TYPE_BOOLEAN:
              return cell.getBooleanCellValue() ? "true" : "false";
          case Cell.CELL_TYPE_FORMULA:
              return cell.getCellFormula();
          case Cell.CELL_TYPE_NUMERIC:
              cell.setCellType(Cell.CELL_TYPE_STRING);
              return cell.getStringCellValue();
          case Cell.CELL_TYPE_STRING:
              return cell.getStringCellValue();
          default:
              return "";
      }
  }
  
}
