package com.atds.project;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//Caller for the fastapi to call and format the json response
public class ATDSCaller {

    //Final vars for the atds caller
    private static final String URL = "http://127.0.0.1:8000/api/atds"; 
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public List<Map<String, Object>> callAPI() {
        List<Map<String, Object>> asteroidList = new ArrayList<>(); //Create a list for asteroid data

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .GET()
            .build();
        
        try {
            //Try to send request and store the response in the list
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            if (status != 200) {
                //Handle a failed connection
                System.out.println("There was an error connecting to the fastapi service!");
                System.out.println("STATUS CODE: " + status);
                return asteroidList;
            }

            //Grab the root object of the json
            JsonObject root = GSON.fromJson(response.body(), JsonObject.class);
            JsonObject nearEarthObjects = root.getAsJsonObject("near_earth_objects");

            if (nearEarthObjects == null) return asteroidList; //If no nearby objects return the empty list

            //Loop through json to gather all asteroids not just one
            for (Map.Entry<String, JsonElement> dateEntry : nearEarthObjects.entrySet()) {
                JsonArray asteroidsArray = dateEntry.getValue().getAsJsonArray();

                for (JsonElement asteroidElement : asteroidsArray) {
                    JsonObject asteroid = asteroidElement.getAsJsonObject();
                    Map<String, Object> asteroidMap = new HashMap<>();
                
                    //Grab the name and if it is considered dangerous and store it into the map
                    asteroidMap.put("name", asteroid.get("name").getAsString());
                    asteroidMap.put("isDangerous", asteroid.get("is_potentially_hazardous_asteroid").getAsBoolean());

                    //Loop through the approach data
                    JsonArray closeApproachData = asteroid.getAsJsonArray("close_approach_data");
                    if (closeApproachData != null && closeApproachData.size() > 0) {
                        JsonObject firstApproach = closeApproachData.get(0).getAsJsonObject();
                    
                        double missDistance = firstApproach.getAsJsonObject("miss_distance")
                                .get("kilometers").getAsDouble();
                        double speed = firstApproach.getAsJsonObject("relative_velocity")
                                .get("kilometers_per_hour").getAsDouble();

                        asteroidMap.put("missDistanceKm", missDistance);
                        asteroidMap.put("speedKmH", speed);
                    } else {
                        asteroidMap.put("missDistanceKm", 0.0);
                        asteroidMap.put("speedKmH", 0.0);
                    }

                    asteroidList.add(asteroidMap);
                }
            }

        } catch (IOException | InterruptedException e) {
            //Handle the exceptions
            System.out.println("Error sending request!");
            e.printStackTrace();
        }

        return asteroidList;
    }
}
