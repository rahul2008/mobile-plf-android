package com.philips.cl.di.ews.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.By;
import com.jayway.android.robotium.solo.Solo;
import com.philips.cl.di.ews.sample.EwsSampleActivity;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@CucumberOptions(features = "features")
public class UserActivitySteps extends ActivityInstrumentationTestCase2<EwsSampleActivity> {

    
	private String mToken;
	HttpClient mHttpclient;
	JSONObject mUserProfile;
	private Solo solo;
	
	public UserActivitySteps(SomeDependency dependency) {
        super(EwsSampleActivity.class);
        assertNotNull(dependency);
        mHttpclient = new DefaultHttpClient();
        
    }
	@Override
	public void setUp() throws Exception {
		//setUp() is run before a test case is started. 
		//This is where the solo object is created.
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		//tearDown() is run after a test case has finished. 
		//finishOpenedActivities() will finish all the activities that have been opened during the test execution.
		solo.finishOpenedActivities();
	}
     @Given("^my token is \"([^\"]*)\"$")
     public void my_token_is(String token) throws Throwable {
    	 mToken=token;
     }
     @When("^I retrieve data from the server$")
     public void I_retrieve_data_from_the_server() throws Throwable {
       	 try{
             HttpResponse response = mHttpclient.execute(new HttpPost("http://di03.ehv.campus.philips.com/users/?token="+mToken));
              if (response.getStatusLine().getStatusCode() == 200) {
                 Log.e("STEP response",response.getStatusLine().toString());
            	 String result = EntityUtils.toString(response.getEntity());
                 Log.e("STEP result", result);
                 mUserProfile = new JSONObject(result);
                 Log.e("STEP json", mUserProfile.toString());
             }
       	 }catch(Exception e){
			 Log.e("STEP", "Failed");
			 e.printStackTrace();
		 }
     }
     @Then("^my \"([^\"]*)\" is (still )?\"([^\"]*)\"$")
     public void My_hair_color_is(String name, String x, String value) throws Throwable {
    	 try{
    		 assertEquals(mUserProfile.getString(name) , value );
    	 }catch(Exception e){
			 Log.e("STEP", mUserProfile.toString());
			 e.printStackTrace();
		 }
     }
      @When("^I close my app$")
      public void I_close_my_app() throws Throwable {
    	  getActivity().finish();
      }
      @When("^open my app again$")
      public void open_my_app_again() throws Throwable {
    	  Intent intent = new Intent(getActivity(), EwsSampleActivity.class);
    	  getActivity().startActivity(intent);
    	  solo = new Solo(getInstrumentation(), getActivity());

      }
       @Given("^I am logged in to Facebook, Google and Twitter$")
       public void I_am_logged_in_to_Facebook_Google_and_Twitter() throws Throwable {
           // Express the Regexp above with the code you wish you had
           //TODO: throw new PendingException();
       }
       @When("^I press \"([^\"]*)\"$")
       public void I_press(String viewName) throws Throwable {
   		   solo.clickOnText(viewName); 
       }
       
       @Then("^I should see \"([^\"]*)\"$")
       public void I_should_see(String text) throws Throwable {
    	   assertTrue(solo.searchText(text)); 
       }
       @When("^I enter \"([^\"]*)\" as \"([^\"]*)\"$")
       public void I_enter_as(String value, String name) throws Throwable {
    	   solo.typeTextInWebElement(By.cssSelector("#"+name),  value);
       }
 	   //int id = getActivity().getResources().getIdentifier(name, "id", getActivity().getPackageName());
	   //EditText editText =(EditText) getActivity().findViewById(id);
       @When("^I press \"([^\"]*)\" on the webpage$")
       public void I_press_on_the_webpage(String viewName) throws Throwable {
   		   solo.clickOnWebElement(By.cssSelector("#"+viewName)); 
       }
 
       @Then("^I should see \"([^\"]*)\" on the webpage$")
       public void I_should_see_on_the_webpage(String text) throws Throwable {
    	   assertTrue(solo.searchText(text));
       }


       @When("^I wait for (\\d+) second$")
       public void I_wait_for_second(int sec) throws Throwable {
    	   Thread.sleep(sec * 1000);
       }
       @Then("^I should have a apk file$")
       public void I_should_have_a_apk_file() throws Throwable {
       }


}
