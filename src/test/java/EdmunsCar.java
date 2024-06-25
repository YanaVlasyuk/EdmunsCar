
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;


public class EdmunsCar {
    @Test
    public void test() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        //n Navigate to
        driver.get("https://www.edmunds.com/");

        //Click on Shop Used
        driver.findElement(By.linkText("Shop Used")).click();

        //3. In the next page, clear the zipcode field and enter 22031
        driver.findElement(By.name("zip")).sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.BACK_SPACE);
        driver.findElement(By.name("zip")).sendKeys("22031", Keys.ENTER);
        Thread.sleep(5000);

        //4. Check the following checkbox
        WebElement checkbox = driver.findElement(By.xpath("//label[@data-tracking-value='deliveryType~local~Only show local listings']//span[@class='checkbox checkbox-icon size-18 icon-checkbox-unchecked3 text-gray-form-controls']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", checkbox);
        Thread.sleep(5000);

        //5. Choose Tesla for a make
        Select makeDropdown = new Select(driver.findElement(By.id("usurp-make-select")));
        makeDropdown.selectByVisibleText("Tesla");
        Thread.sleep(3000);

        //6. Verify that the default selected option in Models dropdown is Any Model for Model
        //dropdown. And the default years are 2012 and 2023 in year input fields.
        Select modelDropDown = new Select(driver.findElement(By.id("usurp-model-select")));
        Assert.assertEquals(modelDropDown.getFirstSelectedOption().getText(), "Add Model");
        WebElement minYear = driver.findElement(By.id("min-value-input-Year"));
        Assert.assertEquals(minYear.getAttribute("value"), "2011");
        WebElement maxYear = driver.findElement(By.id("max-value-input-Year"));
        Assert.assertEquals(maxYear.getAttribute("value"), "2024");

        // Verify that Model dropdown options are [Any Model, Model 3, Model S, Model X,
        //Model Y, Cybertruck, Roadster]
      /*((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500)");
      Thread.sleep(5000);
      List<String> expectedModels = List.of("Model 3", "Model S", "Model X", "\n" +
              "Model Y", "Cybertruck", "Roadster");
      List<WebElement> options = new Select(driver.findElement(By.id("usurp-model-select"))).getOptions();
      Thread.sleep(3000);
      List<String> actualModels = new ArrayList<>();
      for (WebElement option : options) {
       actualModels.add(option.getText().trim());
       Assert.assertEquals(actualModels, expectedModels);*/

        //8. In Models dropdown, choose Model 3
        modelDropDown.selectByVisibleText("Model 3");
        Thread.sleep(4000);

        // 9. Enter 2020 for year min field and hit enter (clear the existing year first)
        WebElement minYear1 = driver.findElement(By.id("min-value-input-Year"));
        minYear1.sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.BACK_SPACE);
        minYear1.sendKeys("2020", Keys.ENTER);
        Thread.sleep(2000);

        //10. In the results page, verify that there are 21 search results, excluding the sponsored
        //result/s. And verify that each search result title contains ‘Tesla Model 3’
        //To isolate the 21 results, excluding the sponsored one/s, use a custom xpath which uses
        //the common class for the search results that you need.
        List<WebElement> results = driver.findElements(By.xpath("//div[contains(text(), 'Tesla Model 3')]"));
        Assert.assertEquals(results.size(), 21);
        for (WebElement result : results) {
            Assert.assertTrue(result.getText().contains("Tesla Model 3"));
        }
        //11.Extract the year from each search result title and verify that each year is within the
        // selected range (2020-2023)
        List<String> resultsText = new ArrayList<>();
        results.forEach(s -> resultsText.add(s.getText()));
        System.out.println(resultsText);
        List<Integer> listYears = new ArrayList<>();
        resultsText.forEach((s -> listYears.add(Integer.valueOf(s.replace(" Tesla Model 3", "")))));
        listYears.sort(Comparator.reverseOrder());
        System.out.println(listYears);
        for (Integer listYear : listYears) {
            Assert.assertTrue(listYear >= 2020 && listYear <= 2023);
        }
        //.12 From the dropdown on the right corner choose “Price: Low to High” option and verify
        //that the results are sorted from lowest to highest price.
        Select sortBy = new Select(driver.findElement(By.id("sort-by")));
        sortBy.selectByVisibleText(("Price: Low to High"));
        Thread.sleep(3000);
        List<WebElement> prices = driver.findElements(By.xpath("//span[class='heading-3']"));

        List<Integer> realPrices = new ArrayList<>();
        for (WebElement price : prices) {
            realPrices.add(Integer.valueOf(price.getText().replaceAll("[$,]", "")));
            Thread.sleep(2000);

            List<Integer> expectedPrices = new ArrayList<>(realPrices);
            expectedPrices.sort(Comparator.naturalOrder());
            Assert.assertEquals(realPrices, expectedPrices);
        }
        //13. From the dropdown menu, choose “Price: High to Low” option and verify that the
        //results are sorted from highest to lowest price.
        Select sortByButton = new Select(driver.findElement(By.id("sort-by")));//locating the button
        sortByButton.selectByVisibleText(("Price: High to Low"));//click on

        List<WebElement> prices2 = driver.findElements(By.xpath("//span[class='heading-3']"));//locating the list of prices popped-up
        List<Integer> resultPrices = new ArrayList<>();
        for (WebElement price2 : prices2) {
            resultPrices.add(Integer.valueOf(price2.getText().replaceAll("[$,]", "")));//deleting special symbols for future comparing and converting to Int

            List<Integer> expectedPrices2 = new ArrayList<>(resultPrices);
            expectedPrices2.sort(Comparator.reverseOrder());//sorting integers
            Assert.assertEquals(resultPrices, expectedPrices2);

            //14 From the dropdown menu, choose “Mileage: Low to High” option and verify that the
            //results are sorted from highest to lowest mileage.
            Select sortByButton2 = new Select(driver.findElement(By.id("sort-by")));
            sortByButton2.selectByVisibleText("“Mileage: Low to High");

            List<WebElement> millages = driver.findElements(By.xpath("//span[@class='text-cool-gray-30']"));
            List<Integer> resultdMillages = new ArrayList<>();
            for (WebElement millage : millages) {
                resultdMillages.add(Integer.valueOf(millage.getText().replaceAll("[,miles]", "")));
                List<Integer> expectedMillages = new ArrayList<>(resultdMillages);
                expectedMillages.sort(Comparator.naturalOrder());
                Assert.assertEquals(resultdMillages, expectedMillages);
            }
            // 15 Find the last result and store its title, price and mileage (get the last result dynamically,
            //i.e., your code should click on the last result regardless of how many results are there).
            //Click on it to open the details about the result.
            List<WebElement> titleElements = driver.findElements(By.xpath("//div[@class='size-16 text-cool-gray-10 font-weight-bold mb-0_5']"));
            String lastTitle = titleElements.get(titleElements.size() - 1).getText();
            System.out.println("Last Title: " + lastTitle);

            List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='heading-3']"));
            String lastPrice = priceElements.get(priceElements.size() - 1).getText();
            System.out.println("Last Price: " + lastPrice);

            List<WebElement> mileageElements = driver.findElements(By.xpath("//span[contains(text(), 'miles')][@class='text-cool-gray-30']"));
            String lastMileage = mileageElements.get(mileageElements.size() - 1).getText().replace(" miles", "");
            System.out.println("Last Mileage: " + lastMileage);

            //List<WebElement> elements = driver.findElements(By.xpath("//div[class='d-flex flex-column usurp-inventory-card overflow-hidden pos-r h-100 w-100 rounded-12 bg-white']"));
            //elements.get(elements.size() - 1).click();


            List<WebElement> elements = driver.findElements(By.xpath("//div[@class='vehicle-info d-flex flex-column pos-r p-1']"));
            elements.get(elements.size() - 1).click();//CLICKING DOES NOT WORK!!!!
            Thread.sleep(4000);

            //List<WebElement> elements = driver.findElements(By.xpath("//li[ends-with(@class, 'slide next')]"));
            // elements.get(elements.size() - 1).click();

            //16. Verify the title price and mileage matches the info from the previous step.
            String expectedTitle = driver.findElement(By.xpath("//h1[.='2023 Tesla Model 3']")).getText();
            String expectedPrice = driver.findElement(By.xpath("//span[@data-testid='vdp-price-row']")).getText();
            String expectedMileage = driver.findElement(By.xpath("//div[.='Mileage']/following-sibling::div")).getText();
            Assert.assertEquals(lastTitle, expectedTitle);
            Assert.assertEquals(lastPrice, expectedPrice);
            Assert.assertEquals(lastMileage, expectedMileage);
//17. Go back to the results page and verify that the clicked result has “Viewed” element on it.
            Thread.sleep(6000);
            driver.navigate().back();
            Thread.sleep(6000);

            elements = driver.findElements(By.xpath("//li[@class='d-flex mb-0_75 mb-md-1_5 col-12 col-md-6']"));
            String lastElementText=elements.get(elements.size()-1).getText();
            Assert.assertEquals("Viewed", lastElementText);
            Thread.sleep(3000);
            driver.quit();
}
}
}



