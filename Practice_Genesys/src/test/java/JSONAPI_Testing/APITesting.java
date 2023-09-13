package JSONAPI_Testing;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;


public class APITesting {
	
	Properties properties = null;

    @BeforeTest
    void beforeTest(){
    	//properties = new PropReader("prop_set1").getProperties();
    }
    
	@Test
	void getAllResources() {
		Response response= given()
				.contentType("ContentType.JSON")
		.when()
			.get("https://jsonplaceholder.typicode.com/posts");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
		
		JsonPath jPath = response.jsonPath();
		//length of JSON Array
		//verifying that response body contains the 100 json records or not.
		JSONArray jo = new JSONArray(response.getBody().asString());
		//System.out.println(jo.length());
		Assert.assertEquals(jo.length(), 100);
		
		
		//verifying the 1st json object from the array
		JSONObject jo1= (JSONObject) jo.get(0);
		//System.out.println(jo1.toString());
		Assert.assertEquals(jo1.length(), 4);
		Assert.assertTrue(jo1.keySet().contains("id"));
		Assert.assertTrue(jo1.keySet().contains("title"));
		Assert.assertTrue(jo1.keySet().contains("body"));
		Assert.assertTrue(jo1.keySet().contains("userId"));
		
	
		//verfying the length of id keys in response
		ArrayList<String> idList = jPath.get("id");
		Assert.assertEquals(idList.size(), 100);
		
		//verfying the length of title keys in response
		ArrayList<String> titleList = jPath.get("title");
		Assert.assertEquals(titleList.size(), 100);
		
		//verfying the length of body keys in response
		ArrayList<String> bodyList = jPath.get("body");
		Assert.assertEquals(bodyList.size(), 100);
		
		//verfying the length of userId keys in response
		ArrayList<String> userIdList = jPath.get("userId");
		Assert.assertEquals(userIdList.size(), 100);
		
	}
	
	@Test
	@Parameters("propFName")
	void postResource(String propFName) {
		
		properties = new PropReader(propFName).getProperties();
		
		PojoRequest data=new PojoRequest();
		data.setTitle(properties.getProperty("postTitle"));
		data.setBody(properties.getProperty("postBody"));
		data.setUserId(properties.getProperty("postUserId"));
		
		Response response=given()
			.contentType("application/json")
			.body(data)
		.when()
			.post("https://jsonplaceholder.typicode.com/posts");
		
		JsonPath jPath = response.jsonPath();
		
		System.out.println(response.getBody().asString());
		
		int id = jPath.getInt("id");
		Assert.assertEquals(response.getStatusCode(), 201);
		Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
		
		Assert.assertEquals(id, 101);
		Assert.assertEquals(jPath.get("body"), data.getBody());
		Assert.assertEquals(jPath.get("title"), data.getTitle());
		Assert.assertEquals(jPath.get("userId"), data.getUserId());

		 
	}
	@Test
	@Parameters("propFName")
	void putResource(String propFName) {
		properties = new PropReader(propFName).getProperties();
		PojoRequest data=new PojoRequest();
		
		data.setTitle(properties.getProperty("putTitle"));
		data.setBody(properties.getProperty("putBody"));
		data.setUserId(properties.getProperty("putUserId"));
		
		Response response=given()
				.contentType("application/json")
				.body(data)
			.when()
				.put("https://jsonplaceholder.typicode.com/posts/"+properties.getProperty("putId"));
		
		JsonPath jPath = response.jsonPath();
		int id1 = jPath.getInt("id");
		System.out.println(response.getBody().asString());
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
		
		Assert.assertEquals(id1+"", properties.getProperty("putId"));
		
		Assert.assertEquals(jPath.get("body"), data.getBody());
		Assert.assertEquals(jPath.get("title"), data.getTitle());
		Assert.assertEquals(jPath.get("userId"), data.getUserId());
		
		
	}
	
	
	public JSONObject getDetails(String id) {
		
		Response response= given()
				.contentType("ContentType.JSON")
		.when()
			.get("https://jsonplaceholder.typicode.com/posts/"+id);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
		JSONObject obj = new JSONObject(response.getBody().asString());
		return obj;
		
	}
	
	@Test
	@Parameters("propFName")
	void patchResource(String propFName) {
		properties = new PropReader(propFName).getProperties();
		JSONObject sourceObj = this.getDetails(properties.getProperty("patchId"));
		
		JSONObject patchObj = new JSONObject();
		patchObj.put("body", properties.getProperty("patchBody"));

		Response response=given()
				.contentType("application/json")
				.body(patchObj.toString())
			.when()
				.patch("https://jsonplaceholder.typicode.com/posts/"+properties.getProperty("patchId"));
		
		JsonPath jPath = response.jsonPath();
		int id1 = jPath.getInt("id");
		System.out.println(response.getBody().asString());
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
		
		Assert.assertEquals(id1+"", properties.getProperty("patchId"));
		Assert.assertEquals(jPath.get("body"),patchObj.getString("body"));
		Assert.assertEquals(jPath.get("title"), sourceObj.getString("title"));
		Assert.assertEquals(jPath.get("userId"), sourceObj.get("userId"));
		
		
	}
	@Test
	@Parameters("propFName")
	void deleteResource(String propFName)  {
		properties = new PropReader(propFName).getProperties();
		Response response= given()
				.contentType("ContentType.JSON")
		.when()
			.delete("https://jsonplaceholder.typicode.com/posts/"+properties.getProperty("deleteId"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
	}
}
