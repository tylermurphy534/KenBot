package net.tylermurphy.apis.twitch;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

	@RequestMapping(value="/webhooks/subscription")
	   public String callback(@RequestBody String body, @RequestHeader Map<String, String> headers) {
			if(body == null) return "Error: no body was sent";
			try {
				JSONObject json = new JSONObject(body);
				JSONObject subscription = json.getJSONObject("subscription");
				String status = subscription.getString("status");
				if(status.equals("webhook_callback_verification_pending")) {
					return json.getString("challenge");
				}
				return "false";
			} catch(JSONException e) {
				return e.getMessage();
			}
	   }
	
}
