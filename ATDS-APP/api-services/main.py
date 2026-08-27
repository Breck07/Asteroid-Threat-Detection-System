from fastapi import FastAPI, HTTPException
import os
import requests
from dotenv import load_dotenv
from datetime import date, timedelta

#Grab the current date and then grab the day before so the api only sends current asteroids
CURRENT_DATE = date.today()
YESTERDAY_DATE = CURRENT_DATE - timedelta(days=1)

#Create fastapi var and call the dotenv method
load_dotenv()
app = FastAPI()

#Declare nasa's api end point for nearby earth objects, and load in the api key
API_KEY = os.getenv("API_KEY")
NASA_URL = "https://api.nasa.gov/neo/rest/v1/feed"


#Establish api end point and function to call the nasa api
@app.get("/api/atds")
def get_data():

    #Params for the url
    query_params = {
        "start_date": YESTERDAY_DATE.isoformat(),
        "end_date": CURRENT_DATE.isoformat(),
        "api_key" : API_KEY 
    }

    #Try to send the request and return response json
    try:
        response = requests.get(NASA_URL, params=query_params)
        response.raise_for_status()

        json_results = response.json()
        return json_results
    
    #Handle exceptions
    except requests.RequestException as error:
        raise HTTPException(
            status_code=502,
            detail=f"There was an error connecting to the NASA API: {error}", #Display the error message and status code
        ) from error