package de.rose53.watchdogstatus;

import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
import static java.awt.Color.GREEN;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WatchdogStatusResource {

	private static final Color[][] okSmiley = new Color[][] {
		new Color[]{BLACK,BLACK, BLACK,BLACK,BLACK,BLACK,BLACK, BLACK},
		new Color[]{BLACK,GREEN, GREEN,BLACK,BLACK,GREEN,GREEN, BLACK},
		new Color[]{BLACK,GREEN, GREEN,BLACK,BLACK,GREEN,GREEN, BLACK},
		new Color[]{BLACK,BLACK, BLACK,GREEN,GREEN,BLACK,BLACK, BLACK},
		new Color[]{BLACK,BLACK, BLACK,GREEN,GREEN,BLACK,BLACK, BLACK},
		new Color[]{GREEN,BLACK, BLACK,BLACK,BLACK,BLACK,BLACK, GREEN},
		new Color[]{BLACK,GREEN, BLACK,BLACK,BLACK,BLACK,GREEN, BLACK},
		new Color[]{BLACK,BLACK, GREEN,GREEN,GREEN,GREEN,BLACK, BLACK}
	};

	private static final Color[][] errorSmiley = new Color[][] {
		new Color[]{BLACK,BLACK, BLACK,BLACK,BLACK,BLACK,BLACK, BLACK},
		new Color[]{BLACK,RED, RED,BLACK,BLACK,RED,RED, BLACK},
		new Color[]{BLACK,RED, RED,BLACK,BLACK,RED,RED, BLACK},
		new Color[]{BLACK,BLACK, BLACK,RED,RED,BLACK,BLACK, BLACK},
		new Color[]{BLACK,BLACK, BLACK,RED,RED,BLACK,BLACK, BLACK},
		new Color[]{BLACK,BLACK, BLACK,BLACK,BLACK,BLACK,BLACK, BLACK},
		new Color[]{BLACK,BLACK, RED,RED,RED,RED,BLACK, BLACK},
		new Color[]{BLACK,RED, BLACK,BLACK,BLACK,BLACK,RED, BLACK}
	};

    @PUT
    public Response status(String jsonStatus) {

    	JsonObject status = Json.createReader(new StringReader(jsonStatus)).readObject();

    	if (status != null) {
    		try {
	    		String s = status.getString("status");
	    		if ("OK".equals(s)) {
					WS2812B.instance().setLED(new WS2812B.LEDField(okSmiley));
	    		} else if ("ERROR".equals(s)) {
	    			WS2812B.instance().setLED(new WS2812B.LEDField(errorSmiley));
	    		} else {
	    			WS2812B.instance().clear();
	    		}
			} catch (IOException e) {
				return Response.serverError().build();
			}
    	}

    	return Response.noContent().build();
    }

    @GET
    public String test() {

    	try {
			WS2812B.instance().setLED(null, null);
		} catch (IOException e) {
			//Response.serverError().build();
		}

    	return "Yes";
    }
}